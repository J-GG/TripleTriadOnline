package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class Membership extends Controller {

    public Result POST_AjaxLogin() {
        final JsonNode jsonNode = request().body().asJson();
        final ObjectNode jsonResult = Json.newObject();
        if (jsonNode == null) {
            jsonResult.put("authenticated", false);
            return ok(jsonResult).as(Http.MimeTypes.JSON);
        }

        final String username = jsonNode.get("username").asText();
        final String password = jsonNode.get("password").asText();
        jsonResult.put("authenticated", true);

        return ok(jsonResult).as(Http.MimeTypes.JSON);
    }

}
