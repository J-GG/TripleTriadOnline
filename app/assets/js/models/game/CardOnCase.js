'use strict';

/**
 * A card placed on a case.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.31
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
            this.flippedByCardRef = cardOnCase.flippedByCardRef;
            this.flippedByRule = cardOnCase.flippedByRule;
            this.flippingStep = cardOnCase.flippingStep;
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

        /**
         * Get the identifier of the card by which this card has been flipped.
         * @returns the identifier of the card by which this card has been flipped or null if it's not flipped
         * @since 17.12.28
         */
        getFlippedByCardRef() {
            return this.flippedByCardRef;
        }

        /**
         * Get the rule which has flipped this card.
         * @returns the rule which has flipped this card or null if it's not flipped
         * @since 17.12.28
         */
        getFlippedByRule() {
            return this.flippedByRule;
        }

        /**
         * Get the step in the flipping chain.
         * @returns the the step in the flipping chain or null if it's not flipped
         * @since 17.12.28
         */
        getFlippingStep() {
            return this.flippingStep;
        }

        /**
         * Get whether the card has just been flipped or not.
         * @returns {boolean} true if it has just been flipped
         * @since 17.12.28
         */
        isFlipped() {
            return this.flippedByCardRef !== null;
        }
    }
});