package controllers;

import actors.GameActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.WebSocket;
import toolbox.Authenticator;
import toolbox.SessionHelper;

import javax.inject.Inject;
import java.util.UUID;

/**
 * GameController.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.27
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
}
