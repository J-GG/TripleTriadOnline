package toolbox;

import models.MemberModel;
import play.mvc.Http;

/**
 * SessionHelper.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
public class SessionHelper {

    /**
     * Return the MemberModel of the member if they are connected.
     *
     * @return the member if he/she is logged in or null
     * @since 17.12.17
     */
    public static MemberModel getMember() {
        try {
            MemberModel member = (MemberModel) Http.Context.current().args.getOrDefault("member", null);
            if (member == null) {
                final String memberUid = Http.Context.current().session().get("memberUid");
                if (memberUid != null) {
                    member = MemberModel.find.query()
                            .where()
                            .eq("uid", memberUid)
                            .findUnique();
                    Http.Context.current().args.put("member", member);
                }
            }
            return member;
        } catch (final RuntimeException ignore) {
            return null;
        }
    }
}
