package models;

import io.ebean.annotation.DbArray;
import models.enumeration.GameDifficultyEnum;
import models.enumeration.RuleEnum;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * GameSettingsModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
@MappedSuperclass
public class GameSettingsModel extends BaseModel {

    /**
     * The difficulty of the game.
     *
     * @since 17.12.17
     */
    @Column(nullable = false)
    private GameDifficultyEnum difficulty;

    /**
     * The list of enabled rules as a List of String to be stored in a DB.
     *
     * @since 17.12.17
     */
    @DbArray
    @Column(name = "enabled_rules")
    public List<String> storableEnabledRules;

    /**
     * The list of enabled rules.
     *
     * @since 17.12.17
     */
    @Transient
    private List<RuleEnum> enabledRules;

    /**
     * Create new settings with default values.
     *
     * @since 17.12.19
     */
    public GameSettingsModel() {
        this.enabledRules = new ArrayList<>();
        this.storableEnabledRules = new ArrayList<>();
        this.difficulty = GameDifficultyEnum.NORMAL;
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
        this.enabledRules = new ArrayList<>();
        for (final String enabledRule : this.storableEnabledRules) {
            this.enabledRules.add(RuleEnum.valueOf(enabledRule));
        }
        return this.enabledRules;
    }

    /**
     * Set the list of enabled rules
     *
     * @param enabledRules the list of enabled rules
     * @since 17.12.17
     */
    public void setEnabledRules(final List<RuleEnum> enabledRules) {
        this.enabledRules = enabledRules;
        this.storableEnabledRules = new ArrayList<>();
        for (final RuleEnum enabledRule : enabledRules) {
            this.storableEnabledRules.add(enabledRule.toString());
        }
    }
}
