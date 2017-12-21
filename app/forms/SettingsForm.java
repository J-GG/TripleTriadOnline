package forms;

import models.enumeration.GameDifficultyEnum;
import models.enumeration.RuleEnum;
import play.api.i18n.Lang;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import service.LanguagesService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SettingsForm.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.21
 * @since 17.12.20
 */
@Constraints.Validate
public class SettingsForm implements Constraints.Validatable<List<ValidationError>> {

    /**
     * The member's new password.
     *
     * @since 17.12.21
     */
    public String password;

    /**
     * The language.
     *
     * @since 17.12.20
     */
    @Constraints.Required
    public String language;

    /**
     * Whether audio is enabled.
     *
     * @since 17.12.20
     */
    @Constraints.Required
    public boolean audioEnabled;

    /**
     * The game difficulty.
     *
     * @since 17.12.21
     */
    @Constraints.Required
    public GameDifficultyEnum difficulty;

    /**
     * The list of enabled rules.
     *
     * @since 17.12.20
     */
    public List<RuleEnum> enabledRules;

    @Override
    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<>();
        if (!LanguagesService.langs.availables().stream().map(Lang::code).collect(Collectors.toList()).contains(this.language)) {
            errors.add(new ValidationError("language", "SETTINGS.LANGUAGE.INVALID"));
        }

        return errors.isEmpty() ? null : errors;
    }

    public List<RuleEnum> getEnabledRules() {
        return this.enabledRules != null ? this.enabledRules : new ArrayList<>();
    }
}
