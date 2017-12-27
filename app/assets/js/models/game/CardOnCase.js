'use strict';

/**
 * A card placed on a case.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.26
 */
define([], function () {
    return class CardOnCase {

        /**
         * Create a new card on case based on the parameters.
         * @param cardOnCase the card
         * @since 17.12.26
         */
        constructor(cardOnCase) {
            this.name = cardOnCase.name;
            this.cardOnCaseRef = cardOnCase.cardOnCaseRef;
            this.playerRef = cardOnCase.playerRef;
        }

        /**
         * Get the name of the card.
         * @returns {*} the name of the card
         * @since 17.12.26
         */
        getName() {
            return this.name;
        }

        /**
         * Get the identifier of the card on case.
         * @returns {*} the identifier of the card on case
         * @since 17.12.26
         */
        getCardOnCaseRef() {
            return this.cardOnCaseRef;
        }

        /**
         * Get the identifier of the player owning the card.
         * @returns {*} the identifier of the player owning the card
         * @since 17.12.26
         */
        getPlayerRef() {
            return this.playerRef;
        }
    }
});