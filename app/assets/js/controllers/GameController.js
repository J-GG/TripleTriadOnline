'use strict';

/**
 * Controller for the game.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.27
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

        /**
         * Wait for the event from the server regarding the next card to place.
         * @since 17.12.27
         */
        function receiveCardPlayedEvent() {
            cardGame.webSocketGame.onmessage = function (event) {
                console.log(JSON.parse(event.data));
                cardGame.webSocketGame.onmessage = undefined;
                let data = JSON.parse(event.data);
                let cardPlayed = data.cardPlayed;
                let game = new Game(data.game);
                GameScript.playCard(game, cardPlayed.cardPlayedIndex, cardPlayed.row, cardPlayed.col);
            }
        }

        return {

            /**
             * Load, display the template and the script and start the game.
             * @param player2Uid The second player's identifier
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
                    cardGame.webSocketGame.onmessage = undefined;
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
             * @param gameRef the identifier of the game
             * @param card The card played
             * @param row The row of the case
             * @param col The column of the case
             * @since 17.11.04
             */
            playerPlaysCard(gameRef, card, row, col) {
                cardGame.webSocketGame.send(JSON.stringify({
                    step: "PlayerTurn",
                    data: {
                        gameRef: gameRef,
                        cardPlayedRef: card.getCardInDeckRef(),
                        row: row,
                        col: col
                    }
                }));
                receiveCardPlayedEvent();
            },

            /**
             * Make the AI plays a card.
             * @param gameRef the identifier of the game
             * @since 17.11.11
             */
            AITurn(gameRef) {
                cardGame.webSocketGame.send(JSON.stringify({step: "AITurn", data: {gameRef: gameRef}}));
                receiveCardPlayedEvent();
            },

            /**
             * Wait for the other player's next move
             * @since 17.12.27
             */
            waitPlayer() {
                receiveCardPlayedEvent();
            },

            /**
             * End the player's turn. If the game is not over, a new turn starts. Otherwise, the game ends.
             * @since 17.11.04
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