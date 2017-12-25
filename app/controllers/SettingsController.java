package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import forms.SettingsForm;
import models.entity.membership.MemberEntity;
import models.membership.MemberModel;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import toolbox.Authenticator;
import toolbox.SessionHelper;

import javax.inject.Inject;

/**
 * SettingsController.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.21
 * @since 17.12.20
 */
public class SettingsController extends Controller {

    /**
     * A helper to deal with forms.
     *
     * @since 17.12.16
     */
    @Inject
    private FormFactory formFactory;

    /**
     * Update the member's settings.
     *
     * @return a JSON containing the member
     * @since 17.12.20
     */
    @Security.Authenticated(Authenticator.class)
    public Result POST_Settings() {
        final MemberModel member = SessionHelper.getMember();

        final Form<SettingsForm> bindedForm = this.formFactory.form(SettingsForm.class).bindFromRequest();

        if (!bindedForm.hasErrors()) {
            final SettingsForm form = bindedForm.get();
            if (form.password != null && !form.password.isEmpty()) {
                member.setPassword(BCrypt.hashpw(form.password, BCrypt.gensalt()));
            }
            member.getMemberSettings().setLanguage(form.language);
            member.getMemberSettings().setAudioEnabled(form.audioEnabled);
            member.getMemberSettings().getDefaultGameSettings().setDifficulty(form.difficulty);
            member.getMemberSettings().getDefaultGameSettings().setEnabledRules(form.enabledRules);
            changeLang(form.language);
            member.save();
            Logger.info("Member [{}] updated his/her settings", member.getUid());
        }

        final ObjectNode result = Json.newObject();
        result.set("member", Json.toJson(new MemberEntity(member)));

        return ok(result).as(Http.MimeTypes.JSON);
    }

}
