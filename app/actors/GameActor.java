package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entity.game.GameEntity;
import models.game.CardInDeckModel;
import models.game.CardOnCaseModel;
import models.game.GameModel;
import models.game.PlayerModel;
import models.membership.MemberModel;
import play.Logger;
import play.libs.Json;
import toolbox.CardHelper;

import java.util.*;

/**
 * GameActor.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.27
 * @since 17.12.25
 */
public class GameActor extends UntypedAbstractActor {

    private final ActorRef out;
    private final UUID memberUid;

    public static Map<UUID, ActorRef> connections = new HashMap<>();


    public GameActor(final ActorRef out, final UUID memberUid) {
        this.out = out;
        this.memberUid = memberUid;
    }

    public static Props props(final ActorRef out, final UUID memberUid) {
        return Props.create(GameActor.class, out, memberUid);
    }

    @Override
    public void onReceive(final Object message) throws Throwable {
        final JsonNode jsonNode = Json.parse(message.toString());
        JsonNode result = null;
        if (jsonNode.has("step") && jsonNode.has("data")) {
            final JsonNode jsonData = jsonNode.get("data");
            switch (jsonNode.get("step").asText()) {
                case "init":
                    result = init(jsonData);
                    break;
                case "AITurn":
                    result = AITurn(jsonData);
                    break;
                case "PlayerTurn":
                    result = playerTurn(jsonData);
                    break;
            }
        }

        if (result == null) {
            Logger.error("The actor couldn't find what to do");
            throw new Exception("404");
        }

        final GameModel game = GameModel.find.query()
                .where()
                .eq("uid", result.get("game").get("gameRef").asText())
                .findUnique();

        connections.put(this.memberUid, this.out);
        final List<PlayerModel> players = game.getPlayers();
        for (final PlayerModel player : players) {
            if (player.getMember() != null && connections.containsKey(player.getMember().getUid())) {
                connections.get(player.getMember().getUid()).tell(result.toString(), self());
            }
        }
    }

    @Override
    public void postStop() throws Exception {
        connections.remove(this.memberUid);
        super.postStop();
    }

    /**
     * Initialize a new game.
     *
     * @param jsonData the data from the client
     * @return a Json with the game
     * @since 17.12.26
     */
    private JsonNode init(final JsonNode jsonData) {
        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("uid", this.memberUid)
                .findUnique();

        final JsonNode player2Ref = jsonData.get("player2Ref");
        MemberModel member2 = null;
        if (player2Ref != null && !player2Ref.textValue().isEmpty()) {
            member2 = MemberModel.find.query()
                    .where()
                    .eq("uid", player2Ref.asText())
                    .findUnique();
        }
        Logger.info("Member [{}] started a new game against [{}]", this.memberUid, member2 != null ? member2 : "AI");

        final PlayerModel player1 = new PlayerModel(member);
        final PlayerModel player2 = new PlayerModel(member2);
        player1.setDeck(CardHelper.getRandomCards(5));
        player2.setDeck(CardHelper.getRandomCards(5));

        final GameModel game = new GameModel();
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setDifficulty(member.getMemberSettings().getDefaultGameSettings().getDifficulty());
        game.setEnabledRules(member.getMemberSettings().getDefaultGameSettings().getEnabledRules());

        final Random randomGenerator = new Random();
        final int randomNumber = randomGenerator.nextInt(game.getPlayers().size());
        game.setPlayerTurn(game.getPlayers().get(randomNumber));
        game.save();

        final ObjectNode result = Json.newObject();
        result.set("game", Json.toJson(new GameEntity(game)));

        return result;
    }

    /**
     * Make the AI choose a card to play from its deck and place it on the board.
     *
     * @param jsonData the data from the client
     * @return a Json with the game and the card played
     * @since 17.12.27
     */
    private JsonNode AITurn(final JsonNode jsonData) {
        if (!jsonData.has("gameRef")) {
            Logger.error("GameRef is missing");
            throw new RuntimeException("404");
        }

        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("uid", this.memberUid)
                .findUnique();

        final GameModel game = GameModel.find.query()
                .where()
                .eq("uid", jsonData.get("gameRef").asText())
                .findUnique();

        if (member == null || game == null) {
            Logger.error("Couldn't find the member [{}] or the game [{}]", this.memberUid, jsonData.get("gameRef").asText());
            throw new RuntimeException("404");
        }

        if (member.getPlayers().stream().noneMatch(playerModel -> playerModel.getUid().equals(game.getPlayers().get(0).getUid()))) {
            Logger.error("The member [{}] is not a player of the game [{}]", member.getUid(), game.getUid());
            throw new RuntimeException("403");
        }

        final PlayerModel AIPlayer = game.getPlayer(1);
        if (!game.getPlayerTurn().getUid().equals(AIPlayer.getUid())) {
            Logger.error("It's not the AI's [{}] turn in the game [{}]", AIPlayer.getUid(), game.getUid());
            throw new RuntimeException("403");
        }

        final Random randomGenerator = new Random();

        final int selectedCardIndex = randomGenerator.nextInt(AIPlayer.getDeck().size());
        final CardInDeckModel selectedCard = AIPlayer.removeCard(selectedCardIndex);
        selectedCard.delete();

        final int selectedCaseIndex = randomGenerator.nextInt(game.getBoard().getCases().size());
        game.getBoard().getCases().get(selectedCaseIndex).setCardOnCase(new CardOnCaseModel(selectedCard));
        game.save();

        final ObjectNode result = Json.newObject();
        result.set("game", Json.toJson(new GameEntity(game)));
        final ObjectNode cardPlayedNode = Json.newObject();
        cardPlayedNode.put("cardPlayedIndex", selectedCardIndex);
        cardPlayedNode.put("row", game.getBoard().getCases().get(selectedCaseIndex).getRow());
        cardPlayedNode.put("col", game.getBoard().getCases().get(selectedCaseIndex).getCol());
        result.set("cardPlayed", cardPlayedNode);

        return result;
    }

    /**
     * Play the card chosen by the player.
     *
     * @param jsonData the data from the client
     * @return a Json with the game and the card played
     * @since 17.12.27
     */
    private JsonNode playerTurn(final JsonNode jsonData) {
        if (!jsonData.has("gameRef") || !jsonData.has("cardPlayedRef") || !jsonData.has("row") || !jsonData.has("col")) {
            Logger.error("Data is missing");
            throw new RuntimeException("404");
        }

        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("uid", this.memberUid)
                .findUnique();

        final GameModel game = GameModel.find.query()
                .where()
                .eq("uid", jsonData.get("gameRef").asText())
                .findUnique();

        final CardInDeckModel cardInDeck = CardInDeckModel.find.query()
                .where()
                .eq("uid", jsonData.get("cardPlayedRef").asText())
                .findUnique();

        final int row = jsonData.get("row").asInt();
        final int col = jsonData.get("col").asInt();

        if (member == null || game == null || cardInDeck == null) {
            Logger.error("Couldn't find the member [{}], the game [{}] or the card played [{}]", this.memberUid, jsonData.get("gameRef").asText(), jsonData.get("cardPlayedRef").asText());
            throw new RuntimeException("404");
        }

        if (row < 0 || row >= game.getBoard().getNbRows() || col < 0 || col >= game.getBoard().getNbCols()) {
            Logger.error("Coordinates ({}, {}) out of range ({}, {})", row, col, game.getBoard().getNbRows(), game.getBoard().getNbCols());
            throw new RuntimeException("404");
        }

        final PlayerModel player = cardInDeck.getPlayer();
        if (!game.getPlayers().contains(player) || !member.getPlayers().contains(player)) {
            Logger.error("The player [{}] doesn't match belong to the game [{}] nor the member [{}]", player.getUid(), game.getUid(), member.getUid());
            throw new RuntimeException("403");
        }

        if (!game.getPlayerTurn().getUid().equals(player.getUid())) {
            Logger.error("It's not the player's [{}] turn in the game [{}]", player.getUid(), game.getUid());
            throw new RuntimeException("403");
        }

        if (game.getBoard().getCase(row, col).getCardOnCase() != null) {
            Logger.error("The case ({}, {}) in the game [{}] is not empty", row, col, game.getUid());
            throw new RuntimeException("403");
        }

        final int cardInDeckIndex = player.removeCard(cardInDeck);
        cardInDeck.delete();

        game.getBoard().getCase(row, col).setCardOnCase(new CardOnCaseModel(cardInDeck));
        game.save();

        final ObjectNode result = Json.newObject();
        result.set("game", Json.toJson(new GameEntity(game)));
        final ObjectNode cardPlayedNode = Json.newObject();
        cardPlayedNode.put("cardPlayedIndex", cardInDeckIndex);
        cardPlayedNode.put("row", row);
        cardPlayedNode.put("col", col);
        result.set("cardPlayed", cardPlayedNode);

        return result;
    }
}