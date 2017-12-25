package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import forms.LoginForm;
import forms.SignupForm;
import models.entity.membership.MemberEntity;
import models.membership.MemberModel;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import toolbox.Authenticator;
import toolbox.SessionHelper;

import javax.inject.Inject;

/**
 * MembershipController.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.20
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

        if (form.getMember() == null) {
            throw new RuntimeException("404");
        }
        Logger.info("Member [uid: {}] is now logged in", form.getMember().getUid());

        ctx().session().put("memberUid", form.getMember().getUid().toString());

        final ObjectNode result = Json.newObject();
        result.set("member", Json.toJson(new MemberEntity(form.getMember())));

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
        member.setUsername(form.username);
        member.setPassword(BCrypt.hashpw(form.password, BCrypt.gensalt()));
        member.getMemberSettings().setLanguage(lang().language());

        member.save();
        Logger.info("Member [uid: {}] just signed up and is now logged in", member.getUid());

        ctx().session().put("memberUid", member.getUid().toString());

        final ObjectNode result = Json.newObject();
        result.set("member", Json.toJson(new MemberEntity(member)));

        return ok(result).as(Http.MimeTypes.JSON);
    }

    /**
     * Log out the member.
     *
     * @return an empty response
     * @since 17.12.17
     */
    @Security.Authenticated(Authenticator.class)
    public Result GET_Logout() {
        Logger.info("Member [uid: {}] has logged out", SessionHelper.getMember().getUid());

        ctx().session().clear();

        final MemberEntity memberEntity = new MemberEntity();
        memberEntity.getMemberSettings().setAudioEnabled(true);
        memberEntity.getMemberSettings().setLanguage(lang().language());
        final ObjectNode result = Json.newObject();
        result.set("member", Json.toJson(memberEntity));

        return ok(result).as(Http.MimeTypes.JSON);
    }

    /**
     * Return whether the user is authenticated or not.
     *
     * @return a JSON containing true or false whether the user is authenticated
     * @since 17.12.17
     */
    public Result GET_IsAuthenticated() {
        final boolean authenticated = SessionHelper.getMember() != null;

        final ObjectNode result = Json.newObject();
        result.put("authenticated", authenticated);

        return ok(result).as(Http.MimeTypes.JSON);
    }

    /**
     * Return the member if he/she is logged in. The default settings if not.
     *
     * @return a JSON containing either the member or the default settings
     * @since 17.12.18
     */
    public Result GET_Member() {
        final MemberModel member = SessionHelper.getMember();
        final MemberEntity memberEntity;
        if (member == null) {
            memberEntity = new MemberEntity();
            memberEntity.getMemberSettings().setAudioEnabled(true);
            memberEntity.getMemberSettings().setLanguage(lang().language());
        } else {
            memberEntity = new MemberEntity(member);
        }

        final ObjectNode result = Json.newObject();
        result.set("member", Json.toJson(memberEntity));

        return ok(result).as(Http.MimeTypes.JSON);
    }
}
