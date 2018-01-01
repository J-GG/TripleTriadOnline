package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entity.game.GameEntity;
import models.game.*;
import models.membership.MemberModel;
import play.Logger;
import play.libs.Json;
import toolbox.CardHelper;

import java.util.*;

/**
 * GameActor.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.31
 * @since 17.12.25
 */
public class GameActor extends UntypedAbstractActor {

    /**
     * Reference of the current actor.
     *
     * @since 17.12.25
     */
    private final ActorRef out;

    /**
     * Identifier of the member.
     *
     * @since 17.12.26
     */
    private final UUID memberUid;

    /**
     * Association between each member and their actor.
     *
     * @since 17.12.26
     */
    public static Map<UUID, ActorRef> connections = new HashMap<>();

    /**
     * Return the configuration to create this actor.
     *
     * @param out       the reference of the actor
     * @param memberUid the identifier of the member
     * @return the configuration object
     * @since 17.12.25
     */
    public static Props props(final ActorRef out, final UUID memberUid) {
        return Props.create(GameActor.class, out, memberUid);
    }

    /**
     * Create a new actor.
     *
     * @param out       the reference of the actor
     * @param memberUid the identifier of the member
     * @since 17.12.26
     */
    public GameActor(final ActorRef out, final UUID memberUid) {
        this.out = out;
        this.memberUid = memberUid;
    }

    @Override
    public void onReceive(final Object message) throws Throwable {
        final JsonNode jsonNode = Json.parse(message.toString());
        JsonNode result = null;
        if (jsonNode.has("step") && jsonNode.has("data")) {
            final JsonNode jsonData = jsonNode.get("data");
            switch (jsonNode.get("step").asText()) {
                case "start":
                    result = start(jsonData);
                    break;
                case "playerTurn":
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
     * Start or resume a game.
     *
     * @param jsonData the data from the client
     * @return a Json with the game. The format is: {gameRef: ###} to resume a game or {member2Ref: ###} to start a new pvp game or empty to start a new game against the AI
     * @since 17.12.26
     */
    private JsonNode start(final JsonNode jsonData) {
        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("uid", this.memberUid)
                .findUnique();

        if (member == null) {
            Logger.error("Couldn't find the member [{}]", this.memberUid);
            throw new RuntimeException("404");
        }

        final GameModel game;
        if (jsonData.has("gameRef")) {
            game = GameModel.find.query()
                    .where()
                    .eq("uid", jsonData.get("gameRef").asText())
                    .findUnique();

            if (game == null) {
                Logger.error("Couldn't find the game [{}]", jsonData.get("gameRef").asText());
                throw new RuntimeException("404");
            }
        } else {
            MemberModel member2 = null;
            if (jsonData.has("member2Ref")) {
                final JsonNode member2Ref = jsonData.get("member2Ref");
                if (member2Ref != null && !member2Ref.textValue().isEmpty()) {
                    if (member2Ref.asText().equals(member.getUid().toString())) {
                        Logger.error("Member [{}] is trying to play against him/herself", member.getUid());
                        throw new RuntimeException("404");
                    }

                    member2 = MemberModel.find.query()
                            .where()
                            .eq("uid", member2Ref.asText())
                            .findUnique();
                }
                Logger.info("Member [{}] started a new game against [{}]", this.memberUid, member2 != null ? member2.getUid() : "AI");
            }

            final PlayerModel player1 = new PlayerModel(member);
            final PlayerModel player2 = new PlayerModel(member2);
            player1.setDeck(CardHelper.getRandomCards(5));
            player2.setDeck(CardHelper.getRandomCards(5));
            game = new GameModel();
            game.addPlayer(player1);
            game.addPlayer(player2);
            game.setDifficulty(member.getMemberSettings().getDefaultGameSettings().getDifficulty());
            game.setEnabledRules(member.getMemberSettings().getDefaultGameSettings().getEnabledRules());

            final Random randomGenerator = new Random();
            final int randomNumber = randomGenerator.nextInt(game.getPlayers().size());
            game.setPlayerTurn(game.getPlayers().get(randomNumber));
            game.save();
        }

        final ObjectNode result = Json.newObject();
        result.set("game", Json.toJson(new GameEntity(game)));

        return result;
    }

    /**
     * Play a card.
     * If the player is a human, play the card he/she has chosen. If the the player is an AI, a card is chosen.
     *
     * @param jsonData the data from the client
     * @return a Json with the game and the card played
     * @since 17.12.31
     */
    private JsonNode playerTurn(final JsonNode jsonData) {
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

        if (!member.getPlayers().contains(game.getPlayers().get(0)) && !member.getPlayers().contains(game.getPlayers().get(1))) {
            Logger.error("The member [{}] is not a player of the game [{}]", member.getUid(), game.getUid());
            throw new RuntimeException("403");
        }

        final int row;
        final int col;
        final CardInDeckModel cardInDeck;
        final PlayerModel player;
        if (jsonData.has("cardPlayedRef") && jsonData.has("row") && jsonData.has("col")) { //Human
            cardInDeck = CardInDeckModel.find.query()
                    .where()
                    .eq("uid", jsonData.get("cardPlayedRef").asText())
                    .findUnique();

            row = jsonData.get("row").asInt();
            col = jsonData.get("col").asInt();

            if (cardInDeck == null) {
                Logger.error("The card in deck [{}] couldn't be found ", jsonData.get("cardPlayerRef").asText());
                throw new RuntimeException("404");
            }

            if (row < 0 || row >= game.getBoard().getNbRows() || col < 0 || col >= game.getBoard().getNbCols()) {
                Logger.error("Coordinates ({}, {}) out of range ({}, {})", row, col, game.getBoard().getNbRows(), game.getBoard().getNbCols());
                throw new RuntimeException("404");
            }

            if (game.getBoard().getCase(row, col).getCardOnCase() != null) {
                Logger.error("The case ({}, {}) in the game [{}] is not empty", row, col, game.getUid());
                throw new RuntimeException("403");
            }

            player = cardInDeck.getPlayer();
            if (!game.getPlayers().contains(player) || !member.getPlayers().contains(player)) {
                Logger.error("The player [{}] doesn't match belong to the game [{}] nor the member [{}]", player.getUid(), game.getUid(), member.getUid());
                throw new RuntimeException("403");
            }

            if (!game.getPlayerTurn().getUid().equals(player.getUid())) {
                Logger.error("It's not the player's [{}] turn in the game [{}]", player.getUid(), game.getUid());
                throw new RuntimeException("403");
            }

        } else { //AI
            player = game.getPlayerTurn();
            if (player.getMember() != null) {
                Logger.error("The player [{}] is not an AI", player.getUid());
                throw new RuntimeException("403");
            }

            final Random randomGenerator = new Random();

            final int selectedCardIndex = randomGenerator.nextInt(player.getDeck().size());
            cardInDeck = player.getDeck().get(selectedCardIndex);

            final List<CaseModel> emptyCases = new ArrayList<>();
            for (final CaseModel caseModel : game.getBoard().getCases()) {
                if (caseModel.getCardOnCase() == null) {
                    emptyCases.add(caseModel);
                }
            }
            final int selectedCaseIndex = randomGenerator.nextInt(emptyCases.size());
            final CaseModel selectedCase = emptyCases.get(selectedCaseIndex);
            row = selectedCase.getRow();
            col = selectedCase.getCol();
        }

        final int cardInDeckIndex = player.removeCard(cardInDeck);
        cardInDeck.delete();

        game.getBoard().getCase(row, col).setCardOnCase(new CardOnCaseModel(cardInDeck));
        game.setPlayerTurn(game.getNextPlayer(player));

        final boolean gameIsOver = game.getBoard().getCases().stream().noneMatch(caseModel -> caseModel.getCardOnCase() == null);
        game.setGameOver(gameIsOver);

        game.save();

        final ObjectNode result = Json.newObject();
        result.set("game", Json.toJson(new GameEntity(game)));
        final ObjectNode cardPlayedNode = Json.newObject();
        cardPlayedNode.put("cardPlayedIndex", cardInDeckIndex);
        cardPlayedNode.put("playerRef", player.getUid().toString());
        cardPlayedNode.put("row", row);
        cardPlayedNode.put("col", col);
        result.set("cardPlayed", cardPlayedNode);

        return result;
    }
}