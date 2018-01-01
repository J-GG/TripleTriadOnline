'use strict';

/**
 * A helper to manage the game more easily.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.31
 */
define([cardGame.gamePath + "js/models/game/Game.js",
    cardGame.gamePath + "js/models/game/CardOnCase.js"], function (Game, CardOnCase) {

    return class GameHelper {

        /**
         * Enumeration to indicate the position of a card relative to another one.
         * @returns {{TOP: number, TOP_RIGHT: number, RIGHT: number, BOTTOM_RIGHT: number, BOTTOM: number, BOTTOM_LEFT: number, LEFT: number, TOP_LEFT: number}}
         * @since 17.12.28
         */
        static getCardPositions() {
            return {
                TOP: 0,
                TOP_RIGHT: 1,
                RIGHT: 2,
                BOTTOM_RIGHT: 3,
                BOTTOM: 4,
                BOTTOM_LEFT: 5,
                LEFT: 6,
                TOP_LEFT: 7
            };
        }

        /**
         * Get the player of the current member.
         * @param game the game
         * @returns {Player} the player of the current member
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
         * Get the player from their identifier.
         * @param game the game
         * @param playerRef the identifier of the player
         * @returns {Player} the player
         * @since 17.12.27
         */
        static getPlayerFromRef(game, playerRef) {
            if (typeof game !== "object" || !(game instanceof Game)) {
                logger.warning("Expected Game type");
            }

            return game.getPlayer(this.getPlayerIndexFromRef(game, playerRef));
        }

        /**
         * Return the index of the player based on their identifier.
         * @param game the game
         * @param playerRef the player unique identifier
         * @return {Number} the index of the player
         * @since 17.12.34
         */
        static getPlayerIndexFromRef(game, playerRef) {
            if (typeof game !== "object" || !(game instanceof Game)) {
                logger.warning("Expected Game type");
            }

            let players = game.getPlayers();
            for (let i = players.length - 1; i >= 0; i--) {
                if (game.getPlayer(i).getPlayerRef() === playerRef) {
                    return i;
                }
            }
        }

        /**
         * Get the coordinate of the card.
         * @param game The game
         * @param card CardOnBoard to look for
         * @returns {*} A object {row: row, col: col} containing the position of the card or undefined if it couldn't be found
         * @since 17.12.28
         */
        static getCardCoordinate(game, card) {
            if (typeof card !== "object" || !(card instanceof CardOnCase)) {
                logger.warning("Expected CardOnCase type");
            }

            for (let i = 0; i < game.getBoard().getNbRows(); i++) {
                for (let j = 0; j < game.getBoard().getNbCols(); j++) {
                    if (game.getBoard().getCase(i, j) === card) {
                        return {row: i, col: j};
                    }
                }
            }

            return undefined;
        }

        /**
         * Returns the relative position of card1 to card2.
         * @param game The game
         * @param card1 The card the position is being looked for
         * @param card2 The card of reference
         * @returns {number} A value among the position enumeration
         * @since 17.12.28
         */
        static getRelativePositionOf(game, card1, card2) {
            if (typeof card1 !== "object" || !(card1 instanceof CardOnCase)) {
                logger.warning("Expected CardOnCase type");
            }
            if (typeof card2 !== "object" || !(card1 instanceof CardOnCase)) {
                logger.warning("Expected CardOnCase type but");
            }

            let card1Pos = this.getCardCoordinate(game, card1);
            let card2Pos = this.getCardCoordinate(game, card2);

            if (card1Pos === undefined || card2Pos === undefined) {
                logger.warning("[card1: " + card1.getCard().getName() + "] and/or [card2: " + card2.getCard().getName() + "] are not on the board");
                return;
            }

            let position;
            if (card1Pos.col === card2Pos.col && card1Pos.row < card2Pos.row) {
                position = this.getCardPositions().TOP;
            } else if (card1Pos.col > card2Pos.col && card1Pos.row < card2Pos.row) {
                position = this.getCardPositions().TOP_RIGHT;
            } else if (card1Pos.col > card2Pos.col && card1Pos.row === card2Pos.row) {
                position = this.getCardPositions().RIGHT;
            } else if (card1Pos.col === card2Pos.col && card1Pos.row > card2Pos.row) {
                position = this.getCardPositions().BOTTOM;
            } else if (card1Pos.col < card2Pos.col && card1Pos.row > card2Pos.row) {
                position = this.getCardPositions().BOTTOM_LEFT;
            } else if (card1Pos.col < card2Pos.col && card1Pos.row === card2Pos.row) {
                position = this.getCardPositions().LEFT;
            } else if (card1Pos.col < card2Pos.col && card1Pos.row < card2Pos.row) {
                position = this.getCardPositions().TOP_LEFT;
            }

            logger.debug("[card1: " + card1.getCard().getName() + "; row: " + card1Pos.row + "; col: " + card1Pos.col + "]'s relative position to "
                + "[card2: " + card2.getCard().getName() + "; row: " + card2Pos.row + "; col: " + card2Pos.col + "] is "
                + Object.keys(this.getCardPositions())[position]);

            return position;
        }
    }
});
