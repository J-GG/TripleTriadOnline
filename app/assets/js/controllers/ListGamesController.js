'use strict';

/**
 * List the games the player is player and the list of members to start a new one.
 * @author Jean-Gabriel Genest
 * @since 18.01.13
 * @version 18.01.13
 */
define([cardGame.gamePath + "js/views/list-games/ListGamesScript.js"], function (ListGamesScript) {
    return (function () {

        /**
         * URL of the template.
         * @type {string}
         * @since 18.01.13
         */
        let TEMPLATE = cardGame.gamePath + 'js/views/list-games/list-games.html';

        return {

            /**
             * Load and display the template and the script.
             * @since 17.10.30
             */
            listGames() {
                $.get(TEMPLATE, function (source) {
                    $.get({
                        url: "/game/list-games",
                        dataType: "json"
                    }).done(function (response) {
                        let template = Handlebars.compile(source);
                        let data = {
                            i18n: cardGame.i18n,
                            members: response.members,
                            games: response.games
                        };

                        cardGame.$container.find(".board__game-area").html(template(data));
                        ListGamesScript.manageListGames();
                    });
                });
            }
        }
    })();
});