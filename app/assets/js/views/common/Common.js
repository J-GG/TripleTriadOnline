'use strict';

/**
 * Common functions used in the views.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.11.21
 */
define([cardGame.gamePath + "js/toolbox/Key.js",
    cardGame.gamePath + "js/views/common/Sound.js"], function (Key, Sound) {
    return {

        /**
         * Manage a list of choices offered to the user.
         * Show a cursor and deals with the up, down and enter keys.
         * The callback function is called whenever a key is down.
         * Options are:
         *  - selector: {@type string} Id or class added to the select-choices class (default: "")
         *  - defaultChoice: {@type int between 1 and the number of choices} set the option selected by default (default: 1)
         *  - unbindOnEnter: {@type boolean} whether the function should unbind the event when the enter key is pressed (default: true)
         * @param options Options as a literal object
         * @param callback Function to call
         * @since 17.10.30
         */
        linearChoice(options, callback) {
            let choice = 1;
            let unbindOnEnter = true;
            let selector = "";
            let $selectChoicesChoice = cardGame.$container.find(".select-choices").find(".select-choices__choice");
            let maxChoice = $selectChoicesChoice.length;

            //Options
            if (typeof options === "object") {
                selector = options.selector !== undefined ? options.selector : selector;
                $selectChoicesChoice = cardGame.$container.find(".select-choices" + selector).find(".select-choices__choice");
                maxChoice = $selectChoicesChoice.length;
                choice = options.defaultChoice !== undefined
                && Number.isInteger(options.defaultChoice)
                && options.defaultChoice > 0
                && options.defaultChoice <= tmpMaxChoice
                    ? options.defaultChoice : choice;
                unbindOnEnter = options.unbindOnEnter === false ? false : unbindOnEnter;
            }


            //Add a cursor at the default position
            let $cursor = $("<div>", {class: "cursor"});
            cardGame.$container.find(".board__background").append($cursor);

            updateCursorPosition();

            cardGame.$container.keydown(function (e) {
                if (cardGame.$container.find($(document.activeElement)).length > 0) {
                    switch (e.which) {
                        case Key.UP:
                            if (choice - 1 > 0) {
                                choice--;
                                Sound.play(Sound.getKeys().SELECT);
                            }
                            break;

                        case Key.DOWN:
                            if (choice + 1 <= maxChoice) {
                                choice++;
                                Sound.play(Sound.getKeys().SELECT);
                            }
                            break;

                        case Key.ENTER:
                            if (unbindOnEnter) {
                                unbind();
                            }
                            break;
                        default:
                            break;
                    }
                    callback({unbind: unbind, key: e.which, choice: choice});
                    updateCursorPosition();
                }
            });

            $selectChoicesChoice.on("click", function () {
                let $clickedChoice = $(this);
                $selectChoicesChoice.each(function (index) {
                    if ($clickedChoice.get(0) === $(this).get(0)) {
                        choice = index + 1;
                        if (unbindOnEnter) {
                            unbind();
                        }
                        updateCursorPosition();
                        callback({unbind: unbind, key: Key.ENTER, choice: choice});
                    }
                })
            });

            //update the cursor at the correct position if the windows is resized
            $(window).resize(updateCursorPosition);

            /**
             * Update the position of the cursor based on the selected choice.
             * @since 17.10.30
             */
            function updateCursorPosition() {
                if ($selectChoicesChoice.length > 0) {
                    let $tag = $($selectChoicesChoice[choice - 1]);
                    let tagHeight = $tag.offset().top + $tag.height() / 2;

                    //Add a left margin to select choices corresponding to the size of the cursor
                    if ($cursor.width() > 0) {
                        $selectChoicesChoice.css({
                            marginLeft: ($cursor.width() * 1.2) + "px"
                        });
                        $cursor.css({
                            marginLeft: "-" + ($cursor.width() * 1.2) + "px",
                            top: tagHeight - $cursor.height() / 2 - cardGame.$container.find(".board__game-area").offset().top,
                            left: $tag.offset().left - cardGame.$container.find(".board__game-area").offset().left
                        });
                    }
                }
            }

            /**
             * Unbind the events linked to this function
             * @since 17.11.11
             */
            function unbind() {
                cardGame.$container.off("keydown");
                $selectChoicesChoice.off("click");
                $(window).off("resize", updateCursorPosition);
            }
        }
    }
});