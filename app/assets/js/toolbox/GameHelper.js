'use strict';

/**
 * A helper to manage the game more easily.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.26
 */
define([cardGame.gamePath + "js/models/game/Game.js"], function (Game) {

    return class GameHelper {

        /**
         * Get the player of the current member.
         * @param game the game
         * @returns {*} the player of the current member
         * @since 17.12.26
         */
        static getPlayerOfMember(game) {
            if (typeof game !== "object" || !(game instanceof Game)) {
                logger.warning("Expected Game type");
            }

            for (let i = game.getPlayers().length - 1; i >= 0; i--) {
                if (game.getPlayer(i).getMemberRef() === cardGame.member.getMemberRef()) {
                    return game.getPlayer(i);
                }
            }
            logger.debug("The member's player couldn't be found");
            return undefined;
        }

        /**
         * Get the index of the player to whom it's the turn. The first player is 1 (and not 0).
         * @param game the game
         * @returns {*} the index of the player
         * @since 17.12.26
         */
        static getIndexPlayerTurn(game) {
            if (typeof game !== "object" || !(game instanceof Game)) {
                logger.warning("Expected Game type");
            }

            for (let i = game.getPlayers().length - 1; i >= 0; i--) {
                if (game.getPlayer(i).getPlayerRef() === game.getPlayerTurnRef()) {
                    return i + 1;
                }
            }
            logger.debug("The player to whom it's the turn to play couldn't be found");
            return undefined;
        }
    }
});
