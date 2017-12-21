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
 * @version 17.12.21
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
    public String username;

    /**
     * The member's password.
     *
     * @since 17.12.17
     */
    @Constraints.Required
    public String password;

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
     * Return the MemberModel of the authenticated user.
     *
     * @return the memberModel of the authenticated user.
     * @since 17.12.17
     */
    public MemberModel getMember() {
        return this._member;
    }
}
