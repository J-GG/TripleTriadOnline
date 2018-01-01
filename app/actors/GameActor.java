package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
 * @version 18.01.01
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
     * The member.
     *
     * @since 18.01.01
     */
    private final MemberModel member;

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
        this.member = MemberModel.find.query()
                .where()
                .eq("uid", memberUid)
                .findUnique();

        if (this.member == null) {
            Logger.error("Couldn't find the member [{}]", memberUid);
            throw new RuntimeException("404");
        }
    }

    @Override
    public void onReceive(final Object message) throws Throwable {
        final JsonNode jsonNode = Json.parse(message.toString());
        if (jsonNode.has("message") && jsonNode.has("data")) {
            final JsonNode jsonData = jsonNode.get("data");
            switch (jsonNode.get("message").asText()) {
                case "start":
                    start(jsonData);
                    break;
                case "playerTurn":
                    playerTurn(jsonData);
                    break;
            }
        }
    }

    @Override
    public void postStop() throws Exception {
        connections.remove(this.member.getUid());
        super.postStop();
    }

    /**
     * Start or resume a game.
     *
     * @param jsonData the data from the client
     * @since 17.12.26
     */
    private void start(final JsonNode jsonData) {
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

            if (game.getPlayers().stream().filter(playerModel -> playerModel.getMember() != null).noneMatch(playerModel -> this.member.getUid().equals(playerModel.getMember().getUid()))) {
                Logger.error("The member [{}] is not a player of the game [{}]", this.member.getUid(), game.getUid());
                throw new RuntimeException("403");
            }
        } else {
            MemberModel member2 = null;
            if (jsonData.has("member2Ref")) {
                final JsonNode member2Ref = jsonData.get("member2Ref");
                if (member2Ref != null && !member2Ref.textValue().isEmpty()) {
                    if (member2Ref.asText().equals(this.member.getUid().toString())) {
                        Logger.error("Member [{}] is trying to play against him/herself", this.member.getUid());
                        throw new RuntimeException("404");
                    }

                    member2 = MemberModel.find.query()
                            .where()
                            .eq("uid", member2Ref.asText())
                            .findUnique();
                }
                Logger.info("Member [{}] started a new game against [{}]", this.member.getUid(), member2 != null ? member2.getUid() : "AI");
            }

            final PlayerModel player1 = new PlayerModel(this.member);
            final PlayerModel player2 = new PlayerModel(member2);
            player1.setDeck(CardHelper.getRandomCards(5));
            player2.setDeck(CardHelper.getRandomCards(5));
            game = new GameModel();
            game.addPlayer(player1);
            game.addPlayer(player2);
            game.setDifficulty(this.member.getMemberSettings().getDefaultGameSettings().getDifficulty());
            game.setEnabledRules(this.member.getMemberSettings().getDefaultGameSettings().getEnabledRules());

            final Random randomGenerator = new Random();
            final int randomNumber = randomGenerator.nextInt(game.getPlayers().size());
            game.setPlayerTurn(game.getPlayers().get(randomNumber));
            game.save();
        }

        final ObjectNode startResult = Json.newObject();
        final ObjectNode data = Json.newObject();
        final ArrayNode loggedInPlayers = Json.newArray();
        connections.keySet().forEach(uuid -> loggedInPlayers.add(uuid.toString()));
        data.set("game", Json.toJson(new GameEntity(game)));
        data.set("loggedInPlayersRef", loggedInPlayers);
        startResult.set("data", data);
        startResult.put("message", "start");

        this.out.tell(startResult.toString(), self());

        final ObjectNode loginResult = Json.newObject();
        final PlayerModel playerLoggedIn = game.getPlayers().stream()
                .filter(playerModel -> playerModel.getMember() != null && playerModel.getMember().getUid().equals(this.member.getUid()))
                .findFirst()
                .orElseGet(null);
        data.put("playerRef", playerLoggedIn.getUid().toString());
        loginResult.set("data", data);
        loginResult.put("message", "login");

        final List<PlayerModel> players = game.getPlayers();
        for (final PlayerModel player : players) {
            if (player.getMember() != null && connections.containsKey(player.getMember().getUid())) {
                connections.get(player.getMember().getUid()).tell(loginResult.toString(), self());
            }
        }

        connections.put(this.member.getUid(), this.out);
    }

    /**
     * Play a card.
     * If the player is a human, play the card he/she has chosen. If the the player is an AI, a card is chosen.
     *
     * @param jsonData the data from the client
     * @since 17.12.31
     */
    private void playerTurn(final JsonNode jsonData) {
        if (!jsonData.has("gameRef")) {
            Logger.error("GameRef is missing");
            throw new RuntimeException("404");
        }

        final GameModel game = GameModel.find.query()
                .where()
                .eq("uid", jsonData.get("gameRef").asText())
                .findUnique();

        if (game == null) {
            Logger.error("Couldn't find the game [{}]", jsonData.get("gameRef").asText());
            throw new RuntimeException("404");
        }

        if (game.getPlayers().stream().noneMatch(playerModel -> this.member.getPlayers().contains(playerModel))) {
            Logger.error("The member [{}] is not a player of the game [{}]", this.member.getUid(), game.getUid());
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
            if (!game.getPlayers().contains(player) || !this.member.getPlayers().contains(player)) {
                Logger.error("The player [{}] doesn't match belong to the game [{}] nor the member [{}]", player.getUid(), game.getUid(), this.member.getUid());
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
        final ObjectNode data = Json.newObject();
        data.set("game", Json.toJson(new GameEntity(game)));
        final ObjectNode cardPlayedNode = Json.newObject();
        cardPlayedNode.put("cardPlayedIndex", cardInDeckIndex);
        cardPlayedNode.put("playerRef", player.getUid().toString());
        cardPlayedNode.put("row", row);
        cardPlayedNode.put("col", col);
        data.set("cardPlayed", cardPlayedNode);
        result.set("data", data);
        result.put("message", "playerTurn");

        final List<PlayerModel> players = game.getPlayers();
        for (final PlayerModel playerModel : players) {
            if (player.getMember() != null && connections.containsKey(playerModel.getMember().getUid())) {
                connections.get(playerModel.getMember().getUid()).tell(result.toString(), self());
            }
        }
    }
}