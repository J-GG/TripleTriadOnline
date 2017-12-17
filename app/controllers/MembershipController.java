package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import forms.LoginForm;
import forms.SignupForm;
import models.MemberModel;
import org.mindrot.jbcrypt.BCrypt;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * MembershipController.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
public class MembershipController extends Controller {

    /**
     * A helper to deal with forms.
     *
     * @since 17.12.16
     */
    @Inject
    private FormFactory formFactory;

    /**
     * Try to log in the user.
     *
     * @return a JSON containing the member's username of the errors.
     * @since 17.12.17
     */
    public Result POST_Login() {
        final Form<LoginForm> bindedForm = this.formFactory.form(LoginForm.class).bindFromRequest();

        if (bindedForm.hasErrors()) {
            final ObjectNode result = Json.newObject();
            result.set("errors", bindedForm.errorsAsJson());
            return ok(result).as(Http.MimeTypes.JSON);
        }
        final LoginForm form = bindedForm.get();

        if (form.get_member() == null) {
            throw new RuntimeException("404");
        }

        ctx().session().put("memberUid", form.get_member().getUid().toString());

        final ObjectNode result = Json.newObject();
        result.put("success", form.get_member().getUsername());

        return ok(result).as(Http.MimeTypes.JSON);
    }

    /**
     * Try to sign up the user.
     *
     * @return a JSON containing the member's username or the errors.
     * @since 17.12.17
     */
    @Transactional
    public Result POST_Signup() {
        final Form<SignupForm> bindedForm = this.formFactory.form(SignupForm.class).bindFromRequest();

        if (bindedForm.hasErrors()) {
            final ObjectNode result = Json.newObject();
            result.set("errors", bindedForm.errorsAsJson());
            return ok(result).as(Http.MimeTypes.JSON);
        }
        final SignupForm form = bindedForm.get();

        final MemberModel member = new MemberModel();
        member.setUsername(form.getUsername());
        member.setPassword(BCrypt.hashpw(form.getPassword(), BCrypt.gensalt()));

        member.save();

        ctx().session().put("memberUid", member.getUid().toString());

        final ObjectNode result = Json.newObject();
        result.put("success", member.getUsername());

        return ok(result).as(Http.MimeTypes.JSON);
    }

    /**
     * Log out the member.
     *
     * @return an empty response
     * @since 17.12.17
     */
    public Result GET_Logout() {
        ctx().session().clear();

        return ok();
    }

    /**
     * Return whether the user is authenticated or not.
     *
     * @return a JSON containing true or false whether the user is authenticated
     * @since 17.12.17
     */
    public Result GET_IsAuthenticated() {
        final ObjectNode result = Json.newObject();
        final boolean authenticated = ctx().session().get("memberUid") != null;
        result.put("authenticated", authenticated);

        return ok(result).as(Http.MimeTypes.JSON);
    }

}
