'use strict';

/**
 * The object defining the routing system. Each route is defined by a key and a method.
 * When a route is invoked, the corresponding method to the key is returned.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.19
 */
define([cardGame.gamePath + "js/controllers/SplashScreenController.js",
        cardGame.gamePath + "js/controllers/SettingsController.js",
        cardGame.gamePath + "js/controllers/GameController.js",
        cardGame.gamePath + "js/controllers/FinalScreenController.js"],
    function (SplashScreenController, SettingsController, GameController, FinalScreenController) {
        return (function () {

            /**
             * The list of keys.
             * @since 17.11.10
             */
            let keys = {
                DEFAULT: "DEFAULT",
                SPLASH_SCREEN: "SPLASH_SCREEN",
                LOGIN_FORM: "LOGIN_FORM",
                SIGNUP_FORM: "SIGNUP_FORM",
                SETTINGS: "SETTINGS",
                START_GAME: "START_GAME",
                PLAYER_PLAYS_CARD: "PLAYER_PLAYS_CARD",
                END_TURN: "END_TURN",
                FINAL_SCREEN: "FINAL_SCREEN"
            };

            /**
             * The list of routes.
             * @since 17.10.30
             */
            let routes = {
                [keys.DEFAULT]: SplashScreenController.splashScreen,
                [keys.SPLASH_SCREEN]: SplashScreenController.splashScreen,
                [keys.LOGIN_FORM]: SplashScreenController.loginForm,
                [keys.SIGNUP_FORM]: SplashScreenController.signupForm,
                [keys.SETTINGS]: SettingsController.settings,
                [keys.START_GAME]: GameController.startGame,
                [keys.PLAYER_PLAYS_CARD]: GameController.playerPlaysCard,
                [keys.END_TURN]: GameController.endTurn,
                [keys.FINAL_SCREEN]: FinalScreenController.finalScreen
            };

            return {

                /**
                 * Return the list of keys.
                 * @returns {*} The list of keys
                 * @since 17.11.10
                 */
                getKeys() {
                    return keys;
                },

                /**
                 * Return the method corresponding to the given key.
                 * @param key The key of the route
                 * @returns {*} The method corresponding to the key
                 * @since 17.10.30
                 */
                get(key) {
                    let method = routes[key] ? routes[key] : routes[keys.DEFAULT];
                    logger.debug("Routing [key: " + key + "] to [method: " + method.name + "]");

                    return method;
                }
            }
        })();
    });