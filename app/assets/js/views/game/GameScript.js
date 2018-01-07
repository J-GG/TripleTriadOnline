'use strict';

/**
 * Show and manage the game.
 * @author Jean-Gabriel Genest
 * @since 17.11.02
 * @version 17.12.31
 */
define([cardGame.gamePath + "js/toolbox/Key.js",
        cardGame.gamePath + "js/models/Settings.js",
        cardGame.gamePath + "js/models/Rules.js",
        cardGame.gamePath + "js/models/Board.js",
        cardGame.gamePath + "js/views/common/Sound.js",
        cardGame.gamePath + "js/toolbox/GameHelper.js"],
    function (Key, Settings, Rules, Board, Sound, GameHelper) {

        /**
         * Start or resume a game.
         * @param game The game
         * @since 17.12.31
         */
        function startGame(game) {
            Sound.play(Sound.getKeys().GAME);

            let newGame = true;
            for (let i = game.getBoard().getNbRows() - 1; i >= 0; i--) {
                for (let j = game.getBoard().getNbCols() - 1; j >= 0; j--) {
                    if (game.getBoard().getCase(i, j).getCardOnCase()) {
                        newGame = false;
                        break;
                    }
                }
                if (!newGame) {
                    break;
                }
            }

            showBoard(game);
            let animationDecksDelay = showDecks(game);
            setTimeout(function () {
                let animationDrawPlayerDelay = 0;
                showScores(game);
                if (newGame) {
                    animationDrawPlayerDelay = drawFirstPlayerPlaying(game);
                }

                setTimeout(() => startNewTurn(game), (animationDrawPlayerDelay + .2) * 1000);
            }, animationDecksDelay * 1000);
        }

        /**
         * Show the cards already placed on the board if the game is resumed.
         * @param game The game
         * @since 17.12.31
         */
        function showBoard(game) {
            let nbCardOnBoardPerPlayer = [];
            for (let i = game.getBoard().getNbRows() - 1; i >= 0; i--) {
                for (let j = game.getBoard().getNbCols() - 1; j >= 0; j--) {
                    let cardOnCase = game.getBoard().getCase(i, j).getCardOnCase();
                    if (cardOnCase) {
                        let playerIndex = GameHelper.getPlayerIndexFromRef(game, cardOnCase.getPlayerRef());
                        nbCardOnBoardPerPlayer[playerIndex] = nbCardOnBoardPerPlayer[playerIndex] !== undefined ? (nbCardOnBoardPerPlayer[playerIndex] + 1) : 0;
                        cardGame.$container.find(".card.card--player-" + (playerIndex + 1)).eq(nbCardOnBoardPerPlayer[playerIndex])
                            .addClass("card--row-" + i + " card--col-" + j)
                            .removeClass("card-back")
                            .css({
                                "background-image": "url('" + cardGame.gamePath + "img/cards/" + cardOnCase.getName().replace(/ /g, '').toLowerCase() + ".jpg')"
                            });

                    }
                }
            }
        }

        /**
         * Draw the players' cards. If open is disabled, show only the back of the opponent's cards.
         * @param game The game
         * @return {number} the length of the animation in seconds
         * @since 17.12.31
         */
        function showDecks(game) {
            /* Show player's cards */
            for (let i = game.getPlayers().length - 1; i >= 0; i--) {
                let deck = game.getPlayer(i).getDeck();

                for (let j = deck.length - 1; j >= 0; j--) {
                    cardGame.$container.find(".card--player-" + (i + 1)).eq($(this).length - 1 - j)
                        .addClass("card--out-board card--deck-player-" + (i + 1) + " card--player-" + (i + 1) + "-appearance-deck-" + j)
                        .css({
                            "background-image": "url('" + cardGame.gamePath + "img/cards/" + deck[j].getName().replace(/ /g, '').toLowerCase() + ".jpg')"
                        });

                    /* Hide the cards and show the back if open is disabled */
                    if (!game.isRuleEnabled(Rules.getRules().OPEN)) {
                        /* Hide the cards of the player who is not the member */
                        if (game.getPlayer(i) !== GameHelper.getPlayerOfMember(game)) {
                            cardGame.$container.find(".card--player-" + (i + 1)).eq($(this).length - 1 - j).each(function () {
                                $(this).data("background", $(this).css("background-image"))
                                    .css("background-image", "")
                                    .addClass("card--back")
                                    .removeClass("card--player-" + (i + 1));
                            });
                        }
                    }
                }
            }

            /* After the animation of the cards*/
            let $cardAnimation = cardGame.$container.find(".card--player-2-appearance-deck-0");
            return parseFloat($cardAnimation.css("animation-duration")) + parseFloat($cardAnimation.css("animation-delay"));
        }

        /**
         * Select the first player who is going to play.
         * @param game The game
         * @return {number} the length of the animation in seconds
         * @since 17.11.05
         */
        function drawFirstPlayerPlaying(game) {
            let playerPlaying = GameHelper.getPlayerIndexFromRef(game, game.getPlayerTurnRef()) + 1;

            /* Select the player */
            Sound.play(Sound.getKeys().SELECTOR);
            cardGame.$container.find(".player-selector")
                .removeClass("player-selector--hide")
                .addClass("player-selector--draw-player-" + playerPlaying);

            /* After the animation of the selector*/
            return parseFloat(cardGame.$container.find(".player-selector--draw-player-" + playerPlaying).css("animation-duration"));
        }

        /**
         * Show the scores and place definitively cards in decks.
         * @param game the game
         * @since 17.12.31
         */
        function showScores(game) {
            for (let i = game.getPlayers().length - 1; i >= 0; i--) {
                //Show scores
                cardGame.$container.find(".score--player-" + (i + 1)).text(game.getPlayer(i).getScore());
                /* Remove the classes for the animation and add the classes for the position */
                for (let j = game.getPlayer(i).getDeck().length; j >= 0; j--) {
                    cardGame.$container.find(".card--player-" + (i + 1) + "-appearance-deck-" + j)
                        .addClass("card--deck-" + j)
                        .removeClass("card--out-board card--player-" + (i + 1) + "-appearance-deck-" + j);
                }
            }
        }

        /**
         * Start a new turn.
         * @param game The game
         * @since 17.11.05
         */
        function startNewTurn(game) {
            let playerPlaying = GameHelper.getPlayerIndexFromRef(game, game.getPlayerTurnRef()) + 1;

            //Move the selector to the player currently playing
            cardGame.$container.find(".player-selector")
                .removeClass()
                .addClass("player-selector player-selector--turn player-selector--turn-player-" + playerPlaying);

            //If the member has to play, we ask him/her to choose a card
            if (GameHelper.getPlayerOfMember(game).getPlayerRef() === game.getPlayerTurnRef()) {
                chooseCardToPlay(game);
            } else {
                //If the 2nd player is an AI, we ask the server to choose a card
                if (game.getPlayer(1).isAnAI() && playerPlaying === 2) {
                    Routes.get(Routes.getKeys().PLAYER_PLAYS_CARD)();
                } else {
                    //If the player is a human, we wait for the next move
                    Routes.get(Routes.getKeys().PLAYER_WAIT)();
                }
            }
        }

        /**
         * Let the player selects a card in their deck.
         * @param game The game
         * @param selectedCard Index of the currently selected card
         * @since 17.11.05
         */
        function chooseCardToPlay(game, selectedCard) {
            let playerPlaying = GameHelper.getPlayerIndexFromRef(game, game.getPlayerTurnRef()) + 1;

            /* The default card is the top one */
            if (selectedCard === undefined) {
                selectedCard = GameHelper.getPlayerFromRef(game, game.getPlayerTurnRef()).getDeck().length - 1;
            }

            //Specify the cards in the deck of the playing player
            cardGame.$container.find(".card--deck-player-" + playerPlaying).addClass("card--deck-player-playing");

            updateSelectedCard(game, selectedCard);

            cardGame.$container.keydown(function (e) {
                switch (e.which) {
                    case Key.UP:
                        if (selectedCard + 1 < GameHelper.getPlayerFromRef(game, game.getPlayerTurnRef()).getDeck().length) {
                            selectedCard++;
                            Sound.play(Sound.getKeys().SELECT);
                        }
                        updateSelectedCard(game, selectedCard);
                        break;

                    case Key.DOWN:
                        if (selectedCard - 1 >= 0) {
                            selectedCard--;
                            Sound.play(Sound.getKeys().SELECT);
                        }
                        updateSelectedCard(game, selectedCard);
                        break;

                    case Key.ENTER:
                        cardGame.$container.find(".cursor")
                            .removeClass("cursor--player-" + playerPlaying + " cursor--card-" + selectedCard);
                        cardGame.$container.off("keydown");
                        Sound.play(Sound.getKeys().SELECT);
                        chooseCase(game, selectedCard);
                        break;

                    default:
                        return;
                }
            });

            cardGame.$container.find(".card--deck-player-" + playerPlaying).off("click"); //Remove the previous events if the player changed their card
            cardGame.$container.find(".card--deck-player-" + playerPlaying).on("click", function () {
                //Get the id of the clicked card
                let $selectedCard = $(this);
                cardGame.$container.find(".card--deck-player-" + playerPlaying).each(function (index) {
                    if ($selectedCard.get(0) === $(this).get(0)) {
                        selectedCard = cardGame.$container.find(".card--deck-player-" + playerPlaying).length - 1 - index;

                        cardGame.$container.find(".cursor")
                            .removeClass("cursor--player-" + playerPlaying + " cursor--card-" + selectedCard);

                        updateSelectedCard(game, selectedCard);
                        cardGame.$container.off("keydown");

                        Sound.play(Sound.getKeys().SELECT);
                        chooseCase(game, selectedCard);
                    }
                })
            });
        }

        /**
         * Move the cursor to the selected card, shift the latter and show the card name.
         * @param game The game
         * @param selectedCard Index of the currently selected card
         * @since 17.11.05
         */
        function updateSelectedCard(game, selectedCard) {
            let playerPlaying = GameHelper.getPlayerIndexFromRef(game, game.getPlayerTurnRef()) + 1;

            //Move the cursor
            cardGame.$container.find(".cursor")
                .removeClass()
                .addClass("cursor cursor--player-" + playerPlaying + " cursor--card-" + selectedCard);

            //Shift the cards
            cardGame.$container.find(".card--selected-player-" + playerPlaying)
                .removeClass("card--selected-player-" + playerPlaying);

            cardGame.$container.find(".card--deck-player-" + playerPlaying + ".card--deck-" + selectedCard)
                .addClass("card--selected-player-" + playerPlaying);

            //Show the name of the card
            showCardNameMessage(GameHelper.getPlayerFromRef(game, game.getPlayerTurnRef()).getCard(selectedCard));
        }

        /**
         * Show the name of the card.
         * @param card Card whose the name must be shown
         * @since 17.11.05
         */
        function showCardNameMessage(card) {
            cardGame.$container.find("#card-name-message").removeClass("message--hidden").text(card.getName());
        }

        /**
         * Let the player choose the case where to place the card.
         * @param game The game
         * @param selectedCard Index of the currently selected card
         * @since 17.11.05
         */
        function chooseCase(game, selectedCard) {
            let playerPlaying = GameHelper.getPlayerIndexFromRef(game, game.getPlayerTurnRef()) + 1;
            let currentRow = 1, currentCol = 1;
            let $cursor = cardGame.$container.find(".cursor");

            //Move the cursor to the board
            $cursor.addClass("cursor--row-" + currentRow + " cursor--col-" + currentCol);
            $cursor.removeClass("cursor--player-" + playerPlaying);

            /* Show the name of the card under the cursor or hide it */
            showCardNameUnderCursor(game, currentRow, currentCol);

            /* Case selection */
            cardGame.$container.keydown(function (e) {
                let previousRow = currentRow;
                let previousCol = currentCol;

                switch (e.which) {
                    case Key.LEFT:
                        currentCol - 1 >= 0 ? currentCol-- : currentCol;
                        break;

                    case Key.UP:
                        currentRow - 1 >= 0 ? currentRow-- : currentRow;
                        break;

                    case Key.RIGHT:
                        currentCol + 1 < 3 ? currentCol++ : currentCol;
                        break;

                    case Key.DOWN:
                        currentRow + 1 < 3 ? currentRow++ : currentRow;
                        break;

                    case Key.ENTER:
                        if (!game.getBoard().getCase(currentRow, currentCol).getCardOnCase()) {
                            //Unbind the click on the deck
                            cardGame.$container.find(".card").removeClass("card--deck-player-playing");
                            cardGame.$container.find(".card--deck-player-" + playerPlaying).off("click");

                            //Unbind events on the cases
                            cardGame.$container.find(".board__grid").removeClass("board__grid--pointer");
                            $board__grid.off("click");
                            cardGame.$container.off("keydown");

                            Sound.play(Sound.getKeys().SELECT);
                            Routes.get(Routes.getKeys().PLAYER_PLAYS_CARD)(GameHelper.getPlayerFromRef(game, game.getPlayerTurnRef()).getCard(selectedCard).getCardInDeckRef(),
                                currentRow,
                                currentCol);
                        }
                        return;
                        break;

                    case Key.ESCAPE:
                        cardGame.$container.find(".board__grid").removeClass("board__grid--pointer");
                        $board__grid.off("click");
                        cardGame.$container.off("keydown");
                        Sound.play(Sound.getKeys().CANCEL);
                        chooseCardToPlay(game, selectedCard);
                        return;
                        break;

                    default:
                        return;
                }

                if (previousCol !== currentCol || previousRow !== currentRow) {
                    Sound.play(Sound.getKeys().SELECT);
                }

                //Move the cursor
                $cursor.removeClass("cursor--row-" + previousRow + " cursor--col-" + previousCol)
                    .addClass("cursor--row-" + currentRow + " cursor--col-" + currentCol);

                showCardNameUnderCursor(game, currentRow, currentCol);
            });

            let $board__grid = $(".board__grid");
            $board__grid.addClass("board__grid--pointer");
            $board__grid.off("click"); //Remove the previous events if the player changed their card
            $board__grid.on("click", function (e) {
                let x = e.clientX - $board__grid.offset().left;
                let y = e.clientY - $board__grid.offset().top;
                let row = 0;
                let col = 0;
                if (y > 1 / 3 * $board__grid.height() && y <= 2 / 3 * $board__grid.height()) {
                    row = 1;
                }
                else if (y > 2 / 3 * $board__grid.height()) {
                    row = 2;
                }

                if (x > 1 / 3 * $board__grid.width() && x <= 2 / 3 * $board__grid.width()) {
                    col = 1;
                }
                else if (x > 2 / 3 * $board__grid.width()) {
                    col = 2;
                }

                if (!game.getBoard().getCase(row, col).getCardOnCase()) {
                    //Unbind the click on the deck
                    cardGame.$container.find(".card").removeClass("card--deck-player-playing");
                    cardGame.$container.find(".card--deck-player-" + playerPlaying).off("click");

                    //Unbind events on the cases
                    cardGame.$container.find(".board__grid").removeClass("board__grid--pointer");
                    $board__grid.off("click");
                    cardGame.$container.off("keydown");

                    Sound.play(Sound.getKeys().SELECT);
                    Routes.get(Routes.getKeys().PLAYER_PLAYS_CARD)(GameHelper.getPlayerFromRef(game, game.getPlayerTurnRef()).getCard(selectedCard).getCardInDeckRef(),
                        row,
                        col);
                }
            })
        }

        /**
         * Show the name of the card under the cursor if there is any.
         * @param game The game
         * @param row Row where the cursor is
         * @param col Column where the cursor is
         * @since 17.11.05
         */
        function showCardNameUnderCursor(game, row, col) {
            if (game.getBoard().getCase(row, col).getCardOnCase()) {
                showCardNameMessage(game.getBoard().getCase(row, col).getCardOnCase());
            } else {
                cardGame.$container.find("#card-name-message").addClass("message--hidden").text();
            }
        }

        /**
         * Remove the card from the player's deck and lower the other cards.
         * @param game the game
         * @param playerRef Identifier of the player who played the card
         * @param indexCardPlayed Index of the card played
         * @param row Row on the board where the card is played
         * @param col Column on the board where the card is played
         * @since 17.11.05
         */
        function removeCardFromDeck(game, playerRef, indexCardPlayed, row, col) {
            let playerPlaying = GameHelper.getPlayerIndexFromRef(game, playerRef) + 1;

            //Remove the card from the deck
            cardGame.$container.find(".card--deck-player-" + playerPlaying + ".card--deck-" + indexCardPlayed).addClass("card--disappearance-deck-" + indexCardPlayed);
            Sound.play(Sound.getKeys().MOVE_CARD);

            //Remove the classes positioning the cursor on the board
            cardGame.$container.find(".cursor").addClass("cursor--hide");

            //Remove the name of the card under the cursor if there is any
            cardGame.$container.find("#card-name-message").addClass("message--hidden");

            let animationDisappearanceDelay = parseFloat(cardGame.$container.find(".card--disappearance-deck-" + indexCardPlayed).css("animation-duration"));

            /* Lower the position of the cards above the one which has just been removed from the deck */
            for (let i = indexCardPlayed + 1; i < game.getPlayer(GameHelper.getPlayerIndexFromRef(game, playerRef)).getDeck().length + 1; i++) {
                cardGame.$container.find(".card--deck-player-" + playerPlaying + ".card--deck-" + i)
                    .addClass("card--deck-lower-" + (i - 1))
                    .removeClass("card--deck-" + i);

                let animationLowerDelay = parseFloat(cardGame.$container.find(".card--deck-lower-" + (i - 1)).css("animation-duration"));

                (function (playerPlaying, i) {
                    setTimeout(function () {
                        cardGame.$container.find(".card--deck-player-" + playerPlaying + ".card--deck-lower-" + (i - 1))
                            .addClass("card--deck-" + (i - 1))
                            .removeClass("card--deck-lower-" + (i - 1));
                    }, animationLowerDelay * 1000);
                })(playerPlaying, i);
            }

            setTimeout(() => placeCardOnBoard(game, playerRef, indexCardPlayed, row, col), animationDisappearanceDelay * 1000);
        }

        /**
         * Position the card on the board.
         * @param game The game
         * @param playerRef Identifier of the player who played the card
         * @param indexCardPlayed Index of the card played
         * @param row Row on the board where the card is played
         * @param col Column on the board where the card is played
         * @since 17.11.05
         */
        function placeCardOnBoard(game, playerRef, indexCardPlayed, row, col) {
            let playerPlaying = GameHelper.getPlayerIndexFromRef(game, playerRef) + 1;

            //If the rule open is disabled, it's time to reveal the card
            if (!game.isRuleEnabled(Rules.getRules().OPEN)) {
                cardGame.$container.find(".card.card--disappearance-deck-" + indexCardPlayed).each(function () {
                    $(this).css("background-image", $(this).data("background"))
                        .addClass("card--player-" + playerPlaying)
                        .removeClass("card--back");
                });
            }

            //Move the card to the board
            cardGame.$container.find(".card--deck-player-" + playerPlaying + ".card--disappearance-deck-" + indexCardPlayed)
                .addClass("card--appearance-row-" + row + " card--col-" + col)
                .removeClass("card--disappearance-deck-" + indexCardPlayed
                    + " card--deck-player-" + playerPlaying
                    + " card--deck-" + indexCardPlayed
                    + " card--selected-player-" + playerPlaying);

            let animationAppearanceDelay = parseFloat(cardGame.$container.find(".card--appearance-row-" + row).css("animation-duration"));

            setTimeout(function () {
                //Position the card on its case
                cardGame.$container.find(".card--appearance-row-" + row)
                    .addClass("card--row-" + row)
                    .removeClass("card--appearance-row-" + row);

                //Flip the nearby cards if necessary
                let animationFlipDelay = flipCards(game);

                setTimeout(function () {
                    //Update scores
                    for (let i = 0; i < game.getPlayers().length; i++) {
                        cardGame.$container.find(".score--player-" + (i + 1)).text(game.getPlayer(i).getScore());
                    }

                    if (game.isGameOver()) {
                        gameIsOver(game);
                    } else {
                        startNewTurn(game)
                    }
                }, animationFlipDelay * 1000);

            }, animationAppearanceDelay * 1000);
        }

        /**
         * Flip the cards which need to be flipped.
         * @param game The game
         * @returns {number} the time in seconds that the animation will take
         * @since 17.11.05
         */
        function flipCards(game) {
            /* Determine what rules will apply at each step */
            let steps = 0, nbRulesDisplayed = 0, rules = {};
            for (let i = game.getBoard().getNbRows() - 1; i >= 0; i--) {
                for (let j = game.getBoard().getNbCols() - 1; j >= 0; j--) {
                    let cardOnCase = game.getBoard().getCase(i, j).getCardOnCase();
                    if (cardOnCase !== undefined && cardOnCase.isFlipped()) {
                        //Find the last step
                        if (cardOnCase.getFlippingStep() > steps) {
                            steps = cardOnCase.getFlippingStep();
                        }
                        //Count the number of rules applied without counting the simple one and by counting each step only once
                        if (cardOnCase.getFlippedByRule() !== Rules.getRules().SIMPLE && rules[cardOnCase.getFlippingStep()] === undefined) {
                            nbRulesDisplayed++;
                        }
                        //Associate the step to the rule
                        rules[cardOnCase.getFlippingStep()] = cardOnCase.getFlippedByRule();
                    }
                }
            }

            //Flip the cards
            function flipCard(step) {
                //Show the rule applying
                let delayed = 0;
                if (rules[step] !== Rules.getRules().SIMPLE) {
                    cardGame.$container.find(".board__background").append($("<div>", {
                        class: "text-title text-title--slide",
                        text: rules[step]
                    }));
                    delayed = 1.5;
                }

                setTimeout(function () {
                    //Remove the text
                    cardGame.$container.find(".text-title").remove();

                    Sound.play(Sound.getKeys().FLIP_CARD);

                    for (let i = game.getBoard().getNbRows() - 1; i >= 0; i--) {
                        for (let j = game.getBoard().getNbCols() - 1; j >= 0; j--) {
                            let cardOnCase = game.getBoard().getCase(i, j).getCardOnCase();
                            if (cardOnCase !== undefined && cardOnCase.isFlipped() && cardOnCase.getFlippingStep() === step) {
                                //X or Y rotation
                                let position = GameHelper.getRelativePositionOf(game, cardOnCase.getCardOnCaseRef(), cardOnCase.getFlippedByCardRef());
                                let rotation = "Y";
                                if (position === GameHelper.getCardPositions().BOTTOM || position === GameHelper.getCardPositions().TOP) {
                                    rotation = "X";
                                }

                                //Add a back to the card
                                cardGame.$container.find(".card.card--row-" + i + ".card--col-" + j)
                                    .addClass("card--front card--front-" + rotation + "-row-" + i + "-col-" + j);
                                cardGame.$container.find(".board__background").append($("<div>", {
                                    class: "card card--back card--back-" + rotation + "-row-" + i + "-col-" + j + " card--row-" + i + " card--col-" + j
                                }));

                                let animationFlipDelay = parseFloat(cardGame.$container.find(".card--back-" + rotation + "-row-" + i + "-col-" + j).css("animation-duration"));

                                //Change the color of the card
                                (function (i, j) {
                                    setTimeout(function () {
                                        cardGame.$container.find(".card.card--front.card--row-" + i + ".card--col-" + j)
                                            .removeClass("card--player-1 card--player-2")
                                            .addClass("card--player-" + (GameHelper.getPlayerIndexFromRef(game, cardOnCase.getPlayerRef()) + 1));
                                    }, animationFlipDelay / 2 * 1000);
                                })(i, j);

                                (function (i, j) {
                                    //Remove the back
                                    setTimeout(function () {
                                        cardGame.$container.find(".card.card--front.card--row-" + i + ".card--col-" + j)
                                            .removeClass("card--front card--front-" + rotation + "-row-" + i + "-col-" + j);
                                        cardGame.$container.find(".card.card--back.card--row-" + i + ".card--col-" + j).remove();

                                        if (step < steps) {
                                            flipCard(step + 1);
                                        }

                                    }, animationFlipDelay * 1000);
                                })(i, j);
                            }
                        }
                    }
                }, delayed * 1000);
            }

            if (steps >= 1) {
                flipCard(1);
            }

            return (steps * .5) + (nbRulesDisplayed * 1.5);
        }

        /**
         * Show the game over screen with the name of the winner or a draw message.
         * Start the victory music if there is a winner.
         * @param game The game
         * @since 17.11.05
         */
        function gameIsOver(game) {

            let text = "";
            if (game.getWinnersRef().length > 1) {
                text = cardGame.i18n.DRAW;
            } else {
                if (game.getWinnersRef()[0] === GameHelper.getPlayerOfMember(game).getPlayerRef()) {
                    text = cardGame.i18n.WIN;
                    Sound.stopAllAndPlay(Sound.getKeys().VICTORY);
                } else {
                    text = cardGame.i18n.LOSE;
                }
            }
            cardGame.$container.find(".board__background").append($("<div>", {
                class: "text-title",
                text: text
            }));

            cardGame.$container.keydown(function (e) {
                switch (e.which) {
                    case Key.ENTER:
                        gameDisappear(game);
                        break;

                    default:
                        return;
                }
            });

            cardGame.$container.find(".board__background").addClass("board__background--pointer");
            cardGame.$container.find(".board__background").on("click", function () {
                gameDisappear(game);
            })
        }

        /**
         * Make the game view disappear to show the final screen.
         * @param game the game
         * @since 17.11.20
         */
        function gameDisappear(game) {
            cardGame.$container.off("click");
            cardGame.$container.find(".board__background").removeClass("board__background--pointer");
            cardGame.$container.off("keydown");

            cardGame.$container.find(".board__background").fadeOut("slow", () => Routes.get(Routes.getKeys().END_GAME)(game));
        }

        /**
         * Notify that the given player joined the game.
         * @param game the game
         * @param playerRef the player who joined the game
         * @since 18.01.01
         */
        function playerJoinedGame(game, playerRef) {
            let playerIndex = GameHelper.getPlayerIndexFromRef(game, playerRef);

            cardGame.$container.find(".player-name__avatar--player-" + (playerIndex + 1))
                .removeClass("player-name__avatar--logged-out")
                .addClass("player-name__avatar--logged-in");
        }

        /**
         * Notify that the given player left the game.
         * @param game the game
         * @param playerRef the player who left the game
         * @since 18.01.01
         */
        function playerLeftGame(game, playerRef) {
            let playerIndex = GameHelper.getPlayerIndexFromRef(game, playerRef);

            cardGame.$container.find(".player-name__avatar--player-" + (playerIndex + 1))
                .removeClass("player-name__avatar--logged-in")
                .addClass("player-name__avatar--logged-out");
        }

        return {
            /**
             * Start the game (draw cards and the first player playing) and let the first player chooses a card to play.
             * @param game the game
             * @since 17.11.02
             */
            startGame(game) {
                startGame(game);
            },

            /**
             * Play the card on the board.
             * @param game The game
             * @param playerRef Identifier of the player who played the card
             * @param indexCard Index of the card in the deck
             * @param row Row of the case where the card is played
             * @param col Column of the case where the card is played
             * @since 17.11.05
             */
            playCard(game, playerRef, indexCard, row, col) {
                removeCardFromDeck(game, playerRef, indexCard, row, col);
            },

            /**
             * Notify that the given player joined the game.
             * @param game the game
             * @param playerRef the player who joined the game
             * @since 18.01.01
             */
            playerJoinedGame(game, playerRef) {
                playerJoinedGame(game, playerRef);
            },

            /**
             * Notify that the given player left the game.
             * @param game the game
             * @param playerRef the player who left the game
             * @since 18.01.01
             */
            playerLeftGame(game, playerRef) {
                playerLeftGame(game, playerRef);
            }
        };
    });