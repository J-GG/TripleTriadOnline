package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * ApplicationController.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
public class ApplicationController extends Controller {

    /**
     * Return the page containing the game.
     *
     * @return the page containing the game
     * @since 17.12.17
     */
    public Result GET_Index() {
        return ok(index.render("Triple Triad Online"));
    }

}
