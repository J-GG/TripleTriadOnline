'use strict';

/**
 * Controller for the game.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.31
 */
define([cardGame.gamePath + "js/views/game/GameScript.js",
    cardGame.gamePath + "js/models/Settings.js",
    cardGame.gamePath + "js/models/game/Game.js"], function (GameScript, Settings, Game) {
    return (function () {

        /**
         * URL of the template.
         * @type {string}
         * @since 17.10.30
         */
        let TEMPLATE = cardGame.gamePath + 'js/views/game/game.html';

        /**
         * Wait for the event from the server regarding the next card to place.
         * @since 17.12.27
         */
        function receiveCardPlayedEvent() {
            cardGame.webSocketGame.onmessage = function (event) {
                let data = JSON.parse(event.data);
                if (data.cardPlayed !== undefined) {
                    cardGame.webSocketGame.onmessage = undefined;
                    let cardPlayed = data.cardPlayed;
                    let game = new Game(data.game);
                    GameScript.playCard(game, cardPlayed.playerRef, cardPlayed.cardPlayedIndex, cardPlayed.row, cardPlayed.col);
                }
            }
        }

        return {

            /**
             * Load, display the template and the script and start the game.
             * @param json Json data to send to start the game ({gameRef: ###} to resume a game or {member2Ref: ###} to start a new pvp game or empty to start a new game against the AI)
             * @since 17.10.30
             */
            startGame(json) {
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
                        step: "start",
                        data: json === undefined ? "" : json
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
             * @param card The identifier of the card
             * @param row The row of the case
             * @param col The column of the case
             * @since 17.11.04
             */
            playerPlaysCard(gameRef, card, row, col) {
                cardGame.webSocketGame.send(JSON.stringify({
                    step: "playerTurn",
                    data: {
                        gameRef: gameRef,
                        cardPlayedRef: card,
                        row: row,
                        col: col
                    }
                }));
                receiveCardPlayedEvent();
            },

            /**
             * Wait for the other player's next move
             * @since 17.12.27
             */
            playerWait() {
                receiveCardPlayedEvent();
            },

            /**
             * End the game.
             * @since 18.01.01
             */
            endGame() {
                cardGame.webSocketGame.close();
                Routes.get(Routes.getKeys().FINAL_SCREEN)(true)
            }
        }
    })();
});