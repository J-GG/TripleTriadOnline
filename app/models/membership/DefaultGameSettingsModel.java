package models.membership;

import io.ebean.Finder;
import io.ebean.annotation.DbJson;
import models.BaseModel;
import models.enumeration.GameDifficultyEnum;
import models.enumeration.RuleEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DefaultGameSettingsModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.18
 */
@Entity
@Table(name = "default_game_settings")
public class DefaultGameSettingsModel extends BaseModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.17
     */
    public static final Finder<Long, DefaultGameSettingsModel> find = new Finder<>(DefaultGameSettingsModel.class);

    /**
     * The difficulty of the game.
     *
     * @since 17.12.17
     */
    @Column(nullable = false)
    private GameDifficultyEnum difficulty;

    /**
     * The list of enabled rules.
     *
     * @since 17.12.17
     */
    @DbJson
    @Column(nullable = false)
    private List<RuleEnum> enabledRules;

    /**
     * The memberSettings to whom the settings are applied.
     *
     * @since 17.12.17
     */
    @OneToOne(targetEntity = MemberSettingsModel.class, mappedBy = "defaultGameSettings", optional = false, cascade = CascadeType.REMOVE)
    private MemberSettingsModel memberSettings;

    /**
     * Create new settings for games with default values.
     *
     * @since 17.12.23
     */
    public DefaultGameSettingsModel() {
        this.difficulty = GameDifficultyEnum.NORMAL;
        this.enabledRules = new ArrayList<>();
    }

    /**
     * Return the member's settings to which the game settings are attached.
     *
     * @return the member's settings to which the game settings are attached.
     * @since 17.12.17
     */
    public MemberSettingsModel getMemberSettings() {
        return this.memberSettings;
    }

    /**
     * Set the member's settings to which the game settings are attached.
     *
     * @param memberSettings the member's settings to which the game settings are attached.
     * @since 17.12.17
     */
    public void setMemberSettings(final MemberSettingsModel memberSettings) {
        this.memberSettings = memberSettings;
    }

    /**
     * Return the difficulty of the game.
     *
     * @return the difficulty of the game
     * @since 17.12.17
     */
    public GameDifficultyEnum getDifficulty() {
        return this.difficulty;
    }

    /**
     * Set the difficulty of the game.
     *
     * @param difficulty the difficulty of the game
     * @since 17.12.17
     */
    public void setDifficulty(final GameDifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Return the list of enabled rules.
     *
     * @return the list of enabled rules.
     * @since 17.12.17
     */
    public List<RuleEnum> getEnabledRules() {
        return this.enabledRules;
    }

    /**
     * Set the list of enabled rules
     *
     * @param enabledRules the list of enabled rules
     * @since 17.12.17
     */
    public void setEnabledRules(final List<RuleEnum> enabledRules) {
        this.enabledRules = enabledRules == null ? new ArrayList<>() : enabledRules;
    }
}
