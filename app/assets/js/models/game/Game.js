'use strict';

/**
 * A Game.
 * @author Jean-Gabriel Genest
 * @since 17.12.26
 * @version 17.12.31
 */
define([cardGame.gamePath + "js/models/game/Board.js",
    cardGame.gamePath + "js/models/game/Player.js"], function (Board, Player) {
    return class Game {

        /**
         * Create a new game based on the parameters.
         * @param game the game
         * @since 17.12.26
         */
        constructor(game) {
            this.gameRef = game.gameRef;
            this.playerTurnRef = game.playerTurnRef;
            this.board = new Board(game.board);
            this.players = [];
            for (let i in game.players) {
                this.players.push(new Player(game.players[i]));
            }
            this.enabledRules = game.enabledRules;
            this.gameOver = game.gameOver;
            this.winnersRef = game.winnersRef;
        }

        /**
         * Get the unique identifier of the game.
         * @returns {*} the unique identifier of the game
         * @since 17.12.26
         */
        getGameRef() {
            return this.gameRef;
        }

        /**
         * Get the identifier of the player to whom it's the turn.
         * @return the identifier of the player to whom it's the turn
         * @since 17.12.26
         */
        getPlayerTurnRef() {
            return this.playerTurnRef;
        }

        /**
         * Get the board.
         * @return the board
         * @since 17.12.26
         */
        getBoard() {
            return this.board;
        }

        /**
         * Get the list of players.
         * @return {Array} the list of players
         * @since 17.12.26
         */
        getPlayers() {
            return this.players;
        }

        /**
         * Get a player.
         * @param index the index of the player
         * @return the player
         * @since 17.12.26
         */
        getPlayer(index) {
            return this.players[index];
        }

        /**
         * Get the list of enabled rules.
         * @return the list of enabled rules
         * @since 17.12.26
         */
        getEnabledRules() {
            return this.enabledRules;
        }

        /**
         * Get whether the given rule is enabled.
         * @param rule the rule to check
         * @returns {boolean} true if the rule is enabled
         * @since 17.12.26
         */
        isRuleEnabled(rule) {
            return this.enabledRules.includes(rule);
        }

        /**
         * Get whether the game is over or not.
         * @return true if the game is over
         * @since 17.12.31
         */
        isGameOver() {
            return this.gameOver;
        }

        /**
         * Get the list of winners' identifiers
         * @return {*} the list of winners' identifiers if the game is over
         * @since 18.01.01
         */
        getWinnersRef() {
            return this.gameOver ? this.winnersRef : undefined;
        }
    }
});