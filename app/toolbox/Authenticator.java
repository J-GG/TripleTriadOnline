package toolbox;

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
        return "true";
    }
}
