package forms;

import models.membership.MemberModel;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * SignupForm.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.21
 * @since 17.12.17
 */
@Constraints.Validate
public class SignupForm implements Constraints.Validatable<List<ValidationError>> {

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

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<>();

        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("username", this.username)
                .findUnique();

        if (member != null) {
            errors.add(new ValidationError("username", "SIGNUP.DUPLICATE"));
        }

        return errors.isEmpty() ? null : errors;
    }
}
