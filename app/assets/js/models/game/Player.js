'use strict';

/**
 * A Player.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.26
 */
define([cardGame.gamePath + "js/models/game/CardInDeck.js"], function (CardInDeck) {
    return class Player {

        /**
         * Create a new player based on the parameters.
         * @param player the player
         * @since 17.12.26
         */
        constructor(player) {
            this.playerRef = player.playerRef;
            this.memberRef = player.memberRef;
            this.username = player.username;
            this.deck = [];
            for (let i in player.deck) {
                this.deck.push(new CardInDeck(player.deck[i]));
            }
            this.score = player.score;
        }

        /**
         * Get the identifier of the player.
         * @returns {*} the identifier of the player
         * @since 17.12.26
         */
        getPlayerRef() {
            return this.playerRef;
        }

        /**
         * Get the identifier of the member.
         * @returns {*} the identifier of the member
         * @since 17.12.26
         */
        getMemberRef() {
            return this.memberRef;
        }

        /**
         * Get the member's username.
         * @returns {*} the member's username
         * @since 17.12.26
         */
        getUsername() {
            return this.username;
        }

        /**
         * Get the player's deck.
         * @returns {*} the player's deck
         * @since 17.12.26
         */
        getDeck() {
            return this.deck;
        }

        /**
         * Get a card in the player's deck.
         * @param index The index of the card
         * @returns {*} the card
         * @since 17.12.27
         */
        getCard(index) {
            return this.deck[index];
        }

        /**
         * Get the player's score.
         * @returns {*} the player's score
         */
        getScore() {
            return this.score;
        }

        /**
         * Get whether the player is an AI.
         * @return {boolean} true if the player is an AI
         * @since 17.12.26
         */
        isAnAI() {
            return this.memberRef === null;
        }
    }
});