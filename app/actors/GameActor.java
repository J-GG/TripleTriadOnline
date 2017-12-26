package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entity.game.GameEntity;
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
 * @version 17.12.25
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
        GameModel game = null;
        if (jsonNode.has("step") && jsonNode.has("data")) {
            final JsonNode jsonData = jsonNode.get("data");
            switch (jsonNode.get("step").asText()) {
                case "init":
                    game = init(jsonData);
                    break;
                case "AITurn":
                    game = AITurn(jsonData);
                    break;
            }
        }

        if (game == null) {
            Logger.error("There is no game");
            throw new RuntimeException("404");
        }

        final GameEntity gameEntity = new GameEntity(game);
        final ObjectNode result = Json.newObject();
        result.set("game", Json.toJson(gameEntity));

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
     * @param initData the data from the client
     * @return a Json with the game information
     * @since 17.12.26
     */
    private GameModel init(final JsonNode initData) {
        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("uid", this.memberUid)
                .findUnique();

        final JsonNode player2Ref = initData.get("player2Ref");
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
        game.setPlayerTurn(game.getPlayers().get(1));
        game.save();

        return game;
    }

    private GameModel AITurn(final JsonNode jsonData) {
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
            Logger.error("Couldn't find the member or the game");
            throw new RuntimeException("404");
        }

        if (!member.getPlayers().stream().anyMatch(playerModel -> playerModel.getUid() == (game.getPlayers().get(0).getUid()))) {
            Logger.error("The member is not a player of the game");
            throw new RuntimeException("403");
        }

        return null;
    }
}