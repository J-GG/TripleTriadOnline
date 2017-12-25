package toolbox;

import models.membership.MemberModel;
import play.mvc.Http;
import play.mvc.Security;

/**
 * Authenticator.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.11
 * @since 17.12.11
 */
public class Authenticator extends Security.Authenticator {
    @Override
    public String getUsername(final Http.Context ctx) {
        final String memberUid = ctx.session().get("memberUid");

        if (memberUid != null) {
            final MemberModel member = MemberModel.find.query()
                    .where()
                    .eq("uid", memberUid)
                    .findUnique();

            if (member != null) {
                ctx.args.put("member", member);
                return member.getUsername();
            }
        }

        ctx.session().clear();
        return null;
    }
}
