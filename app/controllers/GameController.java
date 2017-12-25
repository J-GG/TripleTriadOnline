package controllers;

import actors.GameActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entity.game.GameEntity;
import models.game.GameModel;
import models.game.PlayerModel;
import models.membership.MemberModel;
import play.Logger;
import play.libs.Json;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import toolbox.Authenticator;
import toolbox.CardHelper;
import toolbox.SessionHelper;

import javax.inject.Inject;
import java.util.Random;
import java.util.UUID;

/**
 * GameController.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.25
 * @since 17.12.21
 */
public class GameController extends Controller {

    /**
     * Akka actor system.
     *
     * @since 17.12.25
     */
    private final ActorSystem actorSystem;

    /**
     * Akka materialize.
     *
     * @since 17.12.25
     */
    private final Materializer materializer;

    @Inject
    public GameController(final ActorSystem actorSystem, final Materializer materializer) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }

    /**
     * Initialize the websocket.
     *
     * @return a websocket
     * @since 17.12.25
     */
    @Security.Authenticated(Authenticator.class)
    public WebSocket WS_Play() {
        final UUID uid = SessionHelper.getMember().getUid();
        return WebSocket.Text.accept(request -> ActorFlow.actorRef(actorRef -> GameActor.props(actorRef, uid), this.actorSystem, this.materializer));
    }

    /**
     * Initialize a new game.
     *
     * @return a JSON containing the member
     * @since 17.12.21
     */
    @Security.Authenticated(Authenticator.class)
    public Result POST_InitGame() {
        final MemberModel member = SessionHelper.getMember();
        final JsonNode jsonNode = request().body().asJson();

        if (jsonNode == null) {
            return badRequest();
        }

        final String player2Uid = jsonNode.get("opponent").asText();
        MemberModel opponent = null;
        if (!player2Uid.isEmpty()) {
            opponent = MemberModel.find.query()
                    .where()
                    .eq("uid", player2Uid)
                    .findUnique();
        }
        Logger.info("Member [{}] started a new game against [{}]", member.getUid(), opponent != null ? opponent : "AI");

        final PlayerModel player1 = new PlayerModel(member);
        final PlayerModel player2 = new PlayerModel(opponent);
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

        final GameEntity gameEntity = new GameEntity(game);
        final ObjectNode result = Json.newObject();
        result.set("game", Json.toJson(gameEntity));

        return ok(result).as(Http.MimeTypes.JSON);
    }

}
