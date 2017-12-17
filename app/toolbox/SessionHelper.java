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
            return (MemberModel) Http.Context.current().args.getOrDefault("member", null);
        } catch (final RuntimeException ignore) {
            return null;
        }
    }
}
