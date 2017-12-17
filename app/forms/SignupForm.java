package forms;

import models.MemberModel;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.i18n.Messages;

import java.util.ArrayList;
import java.util.List;

@Constraints.Validate
public class SignupForm implements Constraints.Validatable<List<ValidationError>> {

    @Constraints.Required
    @Constraints.MinLength(3)
    @Constraints.MaxLength(10)
    private String username;

    @Constraints.Required
    private String password;

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<>();

        final MemberModel member = MemberModel.find.query()
                .where()
                .eq("username", username)
                .findUnique();

        if (member != null) {
            errors.add(new ValidationError("username", "SIGNUP.DUPLICATE"));
        }

        return errors.isEmpty() ? null : errors;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
