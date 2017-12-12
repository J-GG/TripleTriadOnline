package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import toolbox.Authenticator;
import views.html.index;

public class Application extends Controller {

    @Security.Authenticated(Authenticator.class)
    public Result index() {
        return ok(index.render("Triple Triad Online"));
    }

}
