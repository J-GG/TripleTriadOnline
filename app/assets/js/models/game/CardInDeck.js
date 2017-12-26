'use strict';

/**
 * A card in a player's deck.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.26
 */
define([], function () {
    return class CardInDeck {

        /**
         * Create a new card in deck based on the parameters.
         * @param cardInDeck the card
         * @since 17.12.26
         */
        constructor(cardInDeck) {
            this.name = cardInDeck.name;
            this.cardInDeckRef = cardInDeck.cardInDeckRef;
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
         * Get the identifier of the card in deck.
         * @returns {*} the identifier of the card in deck
         * @since 17.12.26
         */
        getCardInDeckRef() {
            return this.cardInDeckRef;
        }

    }
});