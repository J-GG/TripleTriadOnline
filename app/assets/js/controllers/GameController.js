'use strict';

/**
 * Controller for the game.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.25
 */
define([cardGame.gamePath + "js/views/game/GameScript.js",
    cardGame.gamePath + "js/models/Settings.js",
    cardGame.gamePath + "js/models/GameEngine.js",
    cardGame.gamePath + "js/models/game/Game.js"], function (GameScript, Settings, GameEngine, Game) {
    return (function () {

        /**
         * URL of the template.
         * @type {string}
         * @since 17.10.30
         */
        let TEMPLATE = cardGame.gamePath + 'js/views/game/game.html';

        /**
         * The game engine.
         * @type {GameEngine}
         * @since 17.11.04
         */
        let gameEngine;

        return {

            /**
             * Load, display the template and the script and start the game.
             * @since 17.10.30
             */
            startGame(player2Uid) {
                let loc = window.location, webSocketUrl;
                if (loc.protocol === "https:") {
                    webSocketUrl = "wss:";
                } else {
                    webSocketUrl = "ws:";
                }
                webSocketUrl += "//" + loc.host;

                cardGame.webSocketGame = new WebSocket(webSocketUrl + "/game/play");

                cardGame.webSocketGame.onopen = function () {
                    cardGame.webSocketGame.send(JSON.stringify({
                        step: "init",
                        data: {player2Ref: player2Uid === undefined ? "" : player2Uid}
                    }));
                };

                cardGame.webSocketGame.onmessage = function (event) {
                    let game = new Game(JSON.parse(event.data).game);
                    let player2IsAI = game.getPlayer(1).isAnAI();

                    let data = {
                        player1: game.getPlayers()[0].getUsername(),
                        player2: player2IsAI ? undefined : game.getPlayers()[1].getUsername(),
                        onePlayer: player2IsAI,
                        gamePath: cardGame.gamePath
                    };

                    $.get(TEMPLATE, function (source) {
                        let template = Handlebars.compile(source);
                        cardGame.$container.find(".board__game-area").html(template(data));
                        GameScript.startGame(game);
                    });
                };
            },

            /**
             * A player plays the card at the coordinates on the board.
             * @param card The card played
             * @param coordinates The coordinate of the card
             * @version 17.11.04
             */
            playerPlaysCard(card, ...coordinates) {
                let params = gameEngine.playerPlaysCard(card, ...coordinates);
                GameScript.playCard(...params);
            },

            /**
             * Make the AI play a card.
             * @version 17.11.11
             */
            AIPlaysCard() {
                let params = gameEngine.AIPlaysCard();
                GameScript.playCard(...params);
            },

            /**
             * End the player's turn. If the game is not over, a new turn starts. Otherwise, the game ends.
             * @version 17.11.04
             */
            endTurn() {
                let gameState = gameEngine.endTurn();
                if (gameState.isGameOver()) {
                    GameScript.gameOver(gameState);
                } else {
                    GameScript.newTurn(gameState);
                }
            }
        }
    })();
});