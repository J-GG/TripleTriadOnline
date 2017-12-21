'use strict';

/**
 * Controller for the settings.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.19
 */
define([cardGame.gamePath + "js/views/settings-screen/SettingsScreenScript.js",
    cardGame.gamePath + "js/models/Rules.js"], function (SettingsScreenScript, Rules) {
    return (function () {

        /**
         * URL of the template.
         * @type {string}
         * @since 17.10.30
         */
        let TEMPLATE = cardGame.gamePath + 'js/views/settings-screen/settings-screen.html';

        return {

            /**
             * Load and display the template and the script.
             * @since 17.10.30
             */
            settings() {
                $.get(TEMPLATE, function (source) {
                    let template = Handlebars.compile(source);
                    let memberSettings = cardGame.member.getMemberSettings();
                    let defaultGameSettings = memberSettings.getDefaultGameSettings();
                    let data = {
                        i18n: cardGame.i18n,
                        username: cardGame.member.getUsername(),
                        audio: memberSettings.isAudioEnabled(),
                        lang: memberSettings.getLanguage(),
                        difficulty: defaultGameSettings.getDifficulty(),
                        open: defaultGameSettings.isRuleEnabled(Rules.getRules().OPEN),
                        war: defaultGameSettings.isRuleEnabled(Rules.getRules().WAR),
                        same: defaultGameSettings.isRuleEnabled(Rules.getRules().SAME),
                        plus: defaultGameSettings.isRuleEnabled(Rules.getRules().PLUS),
                        combo: defaultGameSettings.isRuleEnabled(Rules.getRules().COMBO)
                    };

                    cardGame.$container.find(" .board__game-area").html(template(data));
                    SettingsScreenScript.manageSettings();
                });
            }
        }
    })();
});