'use strict';

/**
 * Entry point of the card game.
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.10.30
 */
define([], function () {

    /**
     * URL of the base template
     * @type {string}
     * @since 17.10.30
     */
    let TEMPLATE = 'js/views/base/base.html';

    /**
     * URL of the loader template
     * @type {string}
     * @since 17.11.19
     */
    let TEMPLATE_LOADER = 'js/views/base/loader.html';

    return {
        /**
         * Start the game with customized options:
         *  - container: Id of the container where the game will be displayed
         *  - nodePath: Path to the node_modules folder containing the dependencies
         *  - gamePath: Path to the card game folder
         * @param options Options to start the game as a literal object
         * @since 17.10.30
         */
        start(options) {
            //Game options
            window.cardGame = {};
            window.cardGame.nodePath = "./";
            window.cardGame.gamePath = "./";

            if (options !== undefined) {
                if (options.nodePath !== undefined) {
                    window.cardGame.nodePath = options.nodePath;
                }
                if (options.gamePath !== undefined) {
                    window.cardGame.gamePath = options.gamePath;
                }
            }

            require([cardGame.nodePath + "js/plugins/js-logging.browser.min.js",
                cardGame.gamePath + "js/models/membership/Member.js"], function (logging, Member) {

                window.cardGame.$container = $("#card-game");
                if (options !== undefined) {
                    if (options.container !== undefined) {
                        let $tmpContainer = $("#" + options.container);
                        if ($tmpContainer.length > 0) {
                            cardGame.$container = $tmpContainer;
                        } else {
                            throw "Container [options.container: " + cardGame.$container + "] can't be found";
                        }
                    }
                }

                window.logger = logging.colorConsole();
                logger.setLevel("debug");
                Handlebars.registerHelper('ifEquals', function (arg1, arg2, options) {
                    return (arg1 === arg2) ? options.fn(this) : options.inverse(this);
                });
                Handlebars.registerHelper('ifIsCurrentMember', function (arg1, options) {
                    return (cardGame.member.getMemberRef() !== arg1) ? options.fn(this) : options.inverse(this);
                });
                Handlebars.registerHelper('toLocalDateTime', function (arg1, options) {
                    let date = new Date(arg1);
                    let dateOptions = {
                        year: 'numeric',
                        month: '2-digit',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    };

                    let format = cardGame.member !== undefined ? cardGame.member.getMemberSettings().getLanguage() : "en";

                    return date.toLocaleDateString(format, dateOptions);
                });

                //Loader
                $.get(cardGame.gamePath + TEMPLATE_LOADER, function (source) {
                    let template = Handlebars.compile(source);
                    let data = {
                        gamePath: cardGame.gamePath
                    };
                    cardGame.$container.html(template(data));
                });

                $.get({
                    url: "/get-member",
                    dataType: "json"
                }).done(function (data) {
                    window.cardGame.member = new Member(data.member);

                    //Launch the game
                    logger.debug("Game launching in [container: " + cardGame.$container[0].id + "]");
                    require([cardGame.gamePath + "js/views/base/Base.js",
                        cardGame.gamePath + "js/toolbox/Routes.js"], function (baseScript, Routes) {
                        require([cardGame.gamePath + "js/lang/i18n_" + cardGame.member.getMemberSettings().getLanguage() + ".js"], function (i18n) {
                            window.cardGame.i18n = i18n;
                            window.Routes = Routes;

                            //Load the minimal view
                            $.get(cardGame.gamePath + TEMPLATE, function (source) {
                                let template = Handlebars.compile(source);
                                let data = {
                                    i18n: cardGame.i18n,
                                    gamePath: cardGame.gamePath
                                };

                                cardGame.$container.html(template(data));
                                baseScript.initViews();

                                Routes.get(Routes.getKeys().SPLASH_SCREEN)()
                            });
                        });
                    });
                });
            });
        }
    }
});
