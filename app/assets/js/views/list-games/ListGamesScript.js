'use strict';

/**
 * Manage the list of games.
 * @author Jean-Gabriel Genest
 * @since 18.01.14
 * @version 18.01.14
 */
define([cardGame.gamePath + "js/views/common/Common.js",
        cardGame.gamePath + "js/toolbox/Key.js",
        cardGame.gamePath + "js/views/common/Sound.js"],
    function (Common, Key, Sound) {
        return {
            manageListGames() {
                Common.linearChoice({}, function (e) {
                    switch (e.key) {
                        case Key.ENTER:
                        case Key.ESCAPE:
                            e.unbind();
                            Sound.play(Sound.getKeys().CANCEL);
                            Routes.get(Routes.getKeys().SPLASH_SCREEN)();
                            break;
                    }
                });

                cardGame.$container.find("[data-member-ref]").on("click", function () {
                    Routes.get(Routes.getKeys().START_GAME)({member2Ref: $(this).data("memberRef")});
                });
                cardGame.$container.find("[data-game-ref]").on("click", function () {
                    Routes.get(Routes.getKeys().START_GAME)({gameRef: $(this).data("gameRef")});
                });
            }
        }
    });