package controllers;

import actors.GameActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entity.game.GameEntity;
import models.game.GameModel;
import models.membership.MemberModel;
import play.libs.Json;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import toolbox.Authenticator;
import toolbox.SessionHelper;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * GameController.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.13
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
     * Return the list of the current games and the members.
     *
     * @return a json containing the list of members and games
     * @since 18.01.13
     */
    @Security.Authenticated(Authenticator.class)
    public Result GET_ListGames() {
        final MemberModel member = SessionHelper.getMember();
        final ObjectNode result = Json.newObject();

        final List<MemberModel> members = MemberModel.find.query()
                .where()
                .not().eq("uid", member.getUid())
                .order("username ASC")
                .findList();
        final ArrayNode memberEntities = Json.newArray();
        for (final MemberModel memberModel : members) {
            final ObjectNode memberInformation = Json.newObject();
            memberInformation.put("memberRef", memberModel.getUid().toString());
            memberInformation.put("username", memberModel.getUsername());
            memberEntities.add(Json.toJson(memberInformation));
        }

        List<GameModel> games = GameModel.find.query().where()
                .eq("gameOver", false)
                .eq("players.member", member)
                .findList();
        games = games.stream().filter(gameModel -> gameModel.getPlayers().stream().noneMatch(playerModel -> playerModel.getMember() == null)).collect(Collectors.toList());
        final ArrayNode gameEntities = Json.newArray();
        for (final GameModel gameModel : games) {
            final GameEntity gameEntity = new GameEntity(gameModel);
            gameEntities.add(Json.toJson(gameEntity));
        }

        result.set("members", memberEntities);
        result.set("games", gameEntities);
        return ok(result);
    }
}
