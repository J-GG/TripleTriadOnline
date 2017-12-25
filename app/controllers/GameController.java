package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.entity.game.GameEntity;
import models.game.GameModel;
import models.game.PlayerModel;
import models.membership.MemberModel;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import toolbox.Authenticator;
import toolbox.CardHelper;
import toolbox.SessionHelper;

import java.util.Random;

/**
 * GameController.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.21
 * @since 17.12.21
 */
public class GameController extends Controller {

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
