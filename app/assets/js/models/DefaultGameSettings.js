/**
 * A member's default settings for games. It is also used to communicate with the backend.
 * @author Jean-Gabriel Genest
 * @since 17.12.19
 * @version 17.12.19
 */
define([], function () {
    return class DefaultGameSettings {

        /**
         * Create new member's settings for the games based on the parameters.
         * @param defaultGameSettings the member's information
         * @since 17.12.19
         */
        constructor(defaultGameSettings) {
            this.difficulty = defaultGameSettings.difficulty;
            this.enabledRules = defaultGameSettings.enabledRules;
        }

        /**
         * Get the difficulty of the games.
         * @returns {*} the difficulty of the games
         * @since 17.12.19
         */
        getDifficulty() {
            return this.difficulty;
        }

        /**
         * Set the difficulty of the games.
         * @param difficulty the difficulty of the games.
         * @since 17.12.19
         */
        setDifficulty(difficulty) {
            this.difficulty = difficulty;
        }

        /**
         * Get the list of enabled rules.
         * @returns {*} the list of enabled rules
         * @since 17.12.19
         */
        getEnabledRules() {
            return this.enabledRules;
        }

        /**
         * Get whether the given rule is enabled.
         * @param rule the rule to check
         * @returns {boolean} true if the rule is enabled
         * @since 17.12.19
         */
        isRuleEnabled(rule) {
            return this.enabledRules.includes(rule);
        }

        /**
         * Set the list of enabled rules.
         * @param enabledRules the list of enabled rules
         * @since 17.12.19
         */
        setEnabledRules(enabledRules) {
            this.enabledRules = enabledRules;
        }

    }
});