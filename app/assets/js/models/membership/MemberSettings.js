'use strict';

/**
 * A member's settings. It is also used to communicate with the backend.
 * @author Jean-Gabriel Genest
 * @since 17.12.19
 * @version 17.12.19
 */
define([cardGame.gamePath + "js/models/membership/DefaultGameSettings.js"], function (DefaultGameSettings) {
    return class MemberSettings {

        /**
         * Create new member's settings based on the parameters.
         * @param memberSettings the member's information
         * @since 17.12.19
         */
        constructor(memberSettings) {
            this.audioEnabled = memberSettings.audioEnabled;
            this.language = memberSettings.language;
            this.defaultGameSettings = new DefaultGameSettings(memberSettings.defaultGameSettings);
        }

        /**
         * Get whether audio is enabled.
         * @returns {boolean} true if audio is enabled
         * @since 17.12.19
         */
        isAudioEnabled() {
            return this.audioEnabled;
        }

        /**
         * Set whether audio is enabled.
         * @param audioEnabled true if audio is enabled
         * @since 17.12.19
         */
        setAudioEnabled(audioEnabled) {
            this.audioEnabled = audioEnabled;
        }

        /**
         * Get the language of the interface.
         * @returns {*} the language of the interface
         * @since 17.12.19
         */
        getLanguage() {
            return this.language;
        }

        /**
         * Set the language of the interface.
         * @param language the language of the interface
         * @since 17.12.19
         */
        setLanguage(language) {
            this.language = language;
        }

        /**
         * Get the default settings for the games.
         * @returns {*} the default settings for the games
         * @since 17.12.19
         */
        getDefaultGameSettings() {
            return this.defaultGameSettings;
        }

        /**
         * Set the default settings for the games.
         * @param defaultGameSettings the default settings for the game
         * @since 17.12.19
         */
        setDefaultGameSettings(defaultGameSettings) {
            this.defaultGameSettings = defaultGameSettings;
        }
    }
});