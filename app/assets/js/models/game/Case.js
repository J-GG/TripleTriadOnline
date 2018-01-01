'use strict';

/**
 * A case on the board.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.26
 */
define([cardGame.gamePath + "js/models/game/CardOnCase.js"], function (CardOnCase) {
    return class Case {

        /**
         * Create a new case based on the parameters.
         * @param caseJson the case
         * @since 17.12.26
         */
        constructor(caseJson) {
            if (caseJson !== null && caseJson.cardOnCase !== null) {
                this.cardOnCase = new CardOnCase(caseJson.cardOnCase);
            }
        }

        /**
         * Get the card placed on this case.
         * @returns {*} the card placed on this case
         * @since 17.12.26
         */
        getCardOnCase() {
            return this.cardOnCase;
        }
    }
});