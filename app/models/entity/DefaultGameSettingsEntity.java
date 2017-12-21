package models.entity;

import java.util.List;

/**
 * DefaultGameSettingsEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.19
 * @since 17.12.19
 */
public class DefaultGameSettingsEntity {

    /**
     * The difficulty of the game.
     *
     * @since 17.12.17
     */
    private String difficulty;

    /**
     * The list of enabled rules.
     *
     * @since 17.12.17
     */
    private List<String> enabledRules;

    /**
     * Return the difficulty of the game.
     *
     * @return the difficulty of the game
     * @since 17.12.17
     */
    public String getDifficulty() {
        return this.difficulty;
    }

    /**
     * Set the difficulty of the game.
     *
     * @param difficulty the difficulty of the game
     * @since 17.12.17
     */
    public void setDifficulty(final String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Return the list of enabled rules.
     *
     * @return the list of enabled rules.
     * @since 17.12.17
     */
    public List<String> getEnabledRules() {
        return this.enabledRules;
    }

    /**
     * Set the list of enabled rules
     *
     * @param enabledRules the list of enabled rules
     * @since 17.12.17
     */
    public void setEnabledRules(final List<String> enabledRules) {
        this.enabledRules = enabledRules;
    }
}
