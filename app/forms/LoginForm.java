package forms;

import models.MemberModel;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * LoginForm.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
@Constraints.Validate
public class LoginForm implements Constraints.Validatable<List<ValidationError>> {

    /**
     * The member's username.
     *
     * @since 17.12.17
     */
    @Constraints.Required
    @Constraints.MinLength(3)
    @Constraints.MaxLength(10)
    private String username;

    /**
     * The member's password.
     *
     * @since 17.12.17
     */
    @Constraints.Required
    private String password;

    /**
     * The MemberModel if the user could be authenticated.
     *
     * @since 17.12.17
     */
    private MemberModel _member;

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<>();

        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("username", this.username)
                .findUnique();

        if (member == null || !BCrypt.checkpw(this.password, member.getPassword())) {
            errors.add(new ValidationError("", "LOGIN.FAILURE"));
        }
        this._member = member;

        return errors.isEmpty() ? null : errors;
    }

    /**
     * Return the member's username.
     *
     * @return the member's username
     * @since 17.12.17
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the member's password.
     *
     * @param username the member's password
     * @since 17.12.17
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Return the member's password.
     *
     * @return the member's password
     * @since 17.12.17
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the member's password.
     *
     * @param password the member's password
     * @since 17.12.17
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Return the MemberModel of the authenticated user.
     *
     * @return the memberModel of the authenticated user.
     * @since 17.12.17
     */
    public MemberModel get_member() {
        return this._member;
    }
}
