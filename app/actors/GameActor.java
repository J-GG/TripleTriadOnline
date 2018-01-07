package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ebean.Query;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import models.entity.game.GameEntity;
import models.game.*;
import models.membership.MemberModel;
import play.Logger;
import play.libs.Json;
import toolbox.CardHelper;
import toolbox.rules.RulesFactory;

import java.util.*;
import java.util.stream.Collectors;

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
     * The player.
     *
     * @since 18.01.01
     */
    private PlayerModel player;

    /**
     * The game.
     *
     * @since 18.01.01
     */
    private GameModel game;

    /**
     * Association between each player and their actor.
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

        final ObjectNode logoutResult = Json.newObject();
        final ObjectNode logoutData = Json.newObject();
        logoutData.set("game", Json.toJson(new GameEntity(this.game)));
        logoutData.put("playerRef", this.player.getUid().toString());
        logoutResult.set("data", logoutData);
        logoutResult.put("message", "left");

        final List<PlayerModel> players = this.game.getPlayers();
        for (final PlayerModel playerModel : players) {
            if (playerModel.getMember() != null && !playerModel.getUid().equals(this.player.getUid()) && connections.containsKey(playerModel.getUid())) {
                connections.get(playerModel.getUid()).tell(logoutResult.toString(), ActorRef.noSender());
            }
        }

        connections.remove(this.player.getUid());

        super.postStop();
    }

    /**
     * Start or resume a game.
     *
     * @param jsonData the data from the client
     * @since 17.12.26
     */
    private void start(final JsonNode jsonData) {
        if (jsonData.has("gameRef")) {
            this.game = GameModel.find.query()
                    .where()
                    .eq("uid", jsonData.get("gameRef").asText())
                    .findUnique();

            if (this.game == null) {
                Logger.error("Couldn't find the game [{}]", jsonData.get("gameRef").asText());
                throw new RuntimeException("404");
            }

            this.player = this.game.getPlayers().stream().filter(playerModel -> playerModel.getMember() != null && playerModel.getMember().getUid().equals(this.member.getUid())).findFirst().orElse(null);
            if (this.player == null) {
                Logger.error("The member [{}] is not a player of the game [{}]", this.member.getUid(), this.game.getUid());
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

            String sql = "SELECT game.uid FROM game "
                    + "INNER JOIN player as p1 ON game.uid = p1.game_uid "
                    + "INNER JOIN player as p2 ON game.uid = p2.game_uid "
                    + "WHERE game_over = false "
                    + "AND p1.member_uid = :member ";
            if (member2 != null) {
                sql += "AND p2.member_uid= :member2";
            } else {
                sql += "AND p2.member_uid IS NULL";
            }

            final RawSql rawSql = RawSqlBuilder
                    .parse(sql)
                    .columnMapping("game.uid", "uid")
                    .create();

            final Query<GameModel> query = GameModel.find.query()
                    .setRawSql(rawSql)
                    .setParameter("member", this.member.getUid());
            if (member2 != null) {
                query.setParameter("member2", member2.getUid());
            }
            final GameModel existingGame = query.findUnique();

            if (existingGame != null) {
                this.game = existingGame;
                this.player = this.game.getPlayers().stream()
                        .filter(playerModel -> playerModel.getMember() != null && this.member.getUid().equals(playerModel.getMember().getUid()))
                        .findFirst()
                        .orElse(null);
            } else {
                this.player = new PlayerModel(this.member);
                final PlayerModel player2 = new PlayerModel(member2);
                this.player.setDeck(CardHelper.getRandomCards(5));
                player2.setDeck(CardHelper.getRandomCards(5));
                this.game = new GameModel();
                this.game.addPlayer(this.player);
                this.game.addPlayer(player2);
                this.game.setDifficulty(this.member.getMemberSettings().getDefaultGameSettings().getDifficulty());
                this.game.setEnabledRules(this.member.getMemberSettings().getDefaultGameSettings().getEnabledRules());

                final Random randomGenerator = new Random();
                final int randomNumber = randomGenerator.nextInt(this.game.getPlayers().size());
                this.game.setPlayerTurn(this.game.getPlayers().get(randomNumber));
                this.game.save();
            }
        }
        connections.put(this.player.getUid(), this.out);

        final ObjectNode startResult = Json.newObject();
        final ObjectNode startData = Json.newObject();
        final ArrayNode loggedInPlayers = Json.newArray();
        connections.keySet().stream()
                .filter(uuid ->
                        this.game.getPlayers().stream()
                                .map(PlayerModel::getUid)
                                .collect(Collectors.toList())
                                .contains(uuid))
                .forEach(uuid ->
                        loggedInPlayers.add(uuid.toString()));
        final JsonNode gameNode = Json.toJson(new GameEntity(this.game));
        startData.set("game", gameNode);
        startData.set("playersPlaying", loggedInPlayers);
        startResult.set("data", startData);
        startResult.put("message", "start");

        this.out.tell(startResult.toString(), ActorRef.noSender());

        final ObjectNode joinedResult = Json.newObject();
        final ObjectNode joinedData = Json.newObject();
        joinedData.set("game", gameNode);
        joinedData.put("playerRef", this.player.getUid().toString());
        joinedResult.set("data", joinedData);
        joinedResult.put("message", "joined");

        final List<PlayerModel> players = this.game.getPlayers();
        for (final PlayerModel playerModel : players) {
            if (playerModel.getMember() != null && !playerModel.getUid().equals(this.player.getUid()) && connections.containsKey(playerModel.getUid())) {
                connections.get(playerModel.getUid()).tell(joinedResult.toString(), ActorRef.noSender());
            }
        }
    }

    /**
     * Play a card.
     * If the player is a human, play the card he/she has chosen. If the the player is an AI, a card is chosen.
     *
     * @param jsonData the data from the client
     * @since 17.12.31
     */
    private void playerTurn(final JsonNode jsonData) {
        if (this.game == null || this.player == null) {
            Logger.error("No player or game");
            throw new RuntimeException("404");
        }
        this.game = GameModel.find.query()
                .where()
                .eq("uid", this.game.getUid())
                .findUnique();
        this.player = PlayerModel.find.query()
                .where()
                .eq("uid", this.player.getUid())
                .findUnique();
        final int row;
        final int col;
        final CardInDeckModel cardInDeck;
        final PlayerModel playerTurn;

        if (jsonData.has("cardPlayedRef") && jsonData.has("row") && jsonData.has("col")) { //Human
            cardInDeck = CardInDeckModel.find.query()
                    .where()
                    .eq("uid", jsonData.get("cardPlayedRef").asText())
                    .findUnique();

            row = jsonData.get("row").asInt();
            col = jsonData.get("col").asInt();

            if (cardInDeck == null || !this.player.getDeck().contains(cardInDeck)) {
                Logger.error("The card in deck [{}] couldn't be found in the player's [{}] deck ", jsonData.get("cardPlayerRef").asText(), this.player.getUid());
                throw new RuntimeException("404");
            }

            if (row < 0 || row >= this.game.getBoard().getNbRows() || col < 0 || col >= this.game.getBoard().getNbCols()) {
                Logger.error("Coordinates ({}, {}) out of range ({}, {})", row, col, this.game.getBoard().getNbRows(), this.game.getBoard().getNbCols());
                throw new RuntimeException("404");
            }

            if (this.game.getBoard().getCase(row, col).getCardOnCase() != null) {
                Logger.error("The case ({}, {}) in the game [{}] is not empty", row, col, this.game.getUid());
                throw new RuntimeException("403");
            }

            if (!this.game.getPlayerTurn().getUid().equals(this.player.getUid())) {
                Logger.error("It's not the player's [{}] turn in the game [{}]", this.player.getUid(), this.game.getUid());
                throw new RuntimeException("403");
            }
            playerTurn = this.player;
        } else { //AI
            playerTurn = this.game.getPlayerTurn();
            if (playerTurn.getMember() != null) {
                Logger.error("The player [{}] is not an AI", playerTurn.getUid());
                throw new RuntimeException("403");
            }

            final Random randomGenerator = new Random();

            final int selectedCardIndex = randomGenerator.nextInt(playerTurn.getDeck().size());
            cardInDeck = playerTurn.getDeck().get(selectedCardIndex);

            final List<CaseModel> emptyCases = new ArrayList<>();
            for (final CaseModel caseModel : this.game.getBoard().getCases()) {
                if (caseModel.getCardOnCase() == null) {
                    emptyCases.add(caseModel);
                }
            }
            final int selectedCaseIndex = randomGenerator.nextInt(emptyCases.size());
            final CaseModel selectedCase = emptyCases.get(selectedCaseIndex);
            row = selectedCase.getRow();
            col = selectedCase.getCol();
        }

        final int cardInDeckIndex = playerTurn.removeCard(cardInDeck);
        cardInDeck.delete();

        this.game.getBoard().getCase(row, col).setCardOnCase(new CardOnCaseModel(cardInDeck));
        RulesFactory.applyRules(this.game, this.game.getBoard().getCase(row, col));
        this.game.setPlayerTurn(this.game.getNextPlayer(playerTurn));

        final boolean gameIsOver = this.game.getBoard().getCases().stream().noneMatch(caseModel -> caseModel.getCardOnCase() == null);
        this.game.setGameOver(gameIsOver);

        this.game.save();

        final ObjectNode result = Json.newObject();
        final ObjectNode data = Json.newObject();
        data.set("game", Json.toJson(new GameEntity(this.game)));
        final ObjectNode cardPlayedNode = Json.newObject();
        cardPlayedNode.put("cardPlayedIndex", cardInDeckIndex);
        cardPlayedNode.put("playerRef", playerTurn.getUid().toString());
        cardPlayedNode.put("row", row);
        cardPlayedNode.put("col", col);
        data.set("cardPlayed", cardPlayedNode);
        result.set("data", data);
        result.put("message", "playerTurn");

        final List<PlayerModel> players = this.game.getPlayers();
        for (final PlayerModel playerModel : players) {
            if (playerModel.getMember() != null && connections.containsKey(playerModel.getUid())) {
                connections.get(playerModel.getUid()).tell(result.toString(), ActorRef.noSender());
            }
        }
        this.game.getBoard().unflipCards();
        this.game.save();
    }
}