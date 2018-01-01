'use strict';

/**
 * Controller for the game.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.31
 */
define([cardGame.gamePath + "js/views/game/GameScript.js",
    cardGame.gamePath + "js/toolbox/GameHelper.js",
    cardGame.gamePath + "js/models/game/Game.js"], function (GameScript, GameHelper, Game) {
    return (function () {

        /**
         * URL of the template.
         * @type {string}
         * @since 17.10.30
         */
        let TEMPLATE = cardGame.gamePath + 'js/views/game/game.html';

        /**
         * Identifier of the timer.
         * @type {number}
         * @since 18.01.01
         */
        let timerID;

        /**
         * The time before sending a message again.
         * @type {number}
         * @since 18.01.01
         */
        let TIMEOUT = 60000;

        /**
         * Wait for the event from the server regarding the next card to place.
         * @since 17.12.27
         */
        function onMessage(messageToListen) {
            cardGame.webSocketGame.onmessage = function (event) {
                let result = JSON.parse(event.data);
                if (messageToListen === "start" && result.message === "start") {
                    let game = new Game(result.data.game);
                    let player2IsAI = game.getPlayer(1).isAnAI();
                    let data = {
                        player1: game.getPlayers()[0].getUsername(),
                        player2: player2IsAI ? undefined : game.getPlayers()[1].getUsername(),
                        onePlayer: player2IsAI,
                        gamePath: cardGame.gamePath
                    };
                    for (let i = 0; i < result.data.playersPlaying.length; i++) {
                        data["player" + (GameHelper.getPlayerIndexFromRef(game, result.data.playersPlaying[i]) + 1) + "LoggedIn"] = true;
                    }

                    $.get(TEMPLATE, function (source) {
                        let template = Handlebars.compile(source);
                        cardGame.$container.find(".board__game-area").html(template(data));
                        GameScript.startGame(game,);
                    });
                } else if (messageToListen === "playerTurn" && result.message === "playerTurn") {
                    let cardPlayed = result.data.cardPlayed;
                    GameScript.playCard(new Game(result.data.game), cardPlayed.playerRef, cardPlayed.cardPlayedIndex, cardPlayed.row, cardPlayed.col);
                } else if (result.message === "joined") {
                    GameScript.playerJoinedGame(new Game(result.data.game), result.data.playerRef);
                } else if (result.message === "left") {
                    GameScript.playerLeftGame(new Game(result.data.game), result.data.playerRef);
                }
            }
        }

        /**
         * Send empty messages through the websocket to keep it alive.
         * @since 18.01.01
         */
        function keepAlive() {
            if (cardGame.webSocketGame.readyState === cardGame.webSocketGame.OPEN) {
                cardGame.webSocketGame.send("{}");
            }
            timerID = setTimeout(keepAlive, TIMEOUT);
        }

        /**
         * Cancel the keepAlive function to stop sending messages.
         * @since 18.01.01
         */
        function cancelKeepAlive() {
            if (timerID) {
                clearTimeout(timerID);
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
                        message: "start",
                        data: json === undefined ? "" : json
                    }));

                    keepAlive();
                };

                cardGame.webSocketGame.onclose = function () {
                    cancelKeepAlive();
                };

                onMessage("start");
            },

            /**
             * A player plays the card at the coordinates on the board.
             * @param card The identifier of the card
             * @param row The row of the case
             * @param col The column of the case
             * @since 17.11.04
             */
            playerPlaysCard(card, row, col) {
                cardGame.webSocketGame.send(JSON.stringify({
                    message: "playerTurn",
                    data: {
                        cardPlayedRef: card,
                        row: row,
                        col: col
                    }
                }));
                onMessage("playerTurn");
            },

            /**
             * Wait for the other player's next move
             * @since 17.12.27
             */
            playerWait() {
                onMessage("playerTurn");
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