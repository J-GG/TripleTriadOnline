'use strict';

/**
 * Controller for the splash screen.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.21
 */
define([cardGame.gamePath + "js/views/splash-screen/SplashScreenScript.js"], function (splashScreenScript) {
    return (function () {

        /**
         * URL of the template
         * @type {string}
         * @since 17.10.30
         */
        let TEMPLATE = cardGame.gamePath + 'js/views/splash-screen/splash-screen.html';

        /**
         * URL of the login template
         * @type {string}
         * @since 17.12.11
         */
        let TEMPLATE_LOGIN_SIGNUP = cardGame.gamePath + 'js/views/splash-screen/loginSignup.html';


        return {

            /**
             * Load and display the template and the script.
             * @since 17.10.30
             */
            splashScreen() {
                let data = {
                    i18n: cardGame.i18n,
                    authenticated: cardGame.member.username !== null
                };

                $.get(TEMPLATE, function (source) {
                    let template = Handlebars.compile(source);
                    cardGame.$container.find(".board__game-area").html(template(data));
                    splashScreenScript.showMenu(cardGame.member.username !== null);
                });
            },

            /**
             * Load and display the login form.
             * @since 17.12.11
             */
            loginForm() {
                let data = {
                    i18n: cardGame.i18n,
                    loginForm: true
                };

                $.get(TEMPLATE_LOGIN_SIGNUP, function (source) {
                    let template = Handlebars.compile(source);
                    cardGame.$container.find(".board__background").append(template(data));
                    splashScreenScript.showLoginForm();
                });
            },


            /**
             * Display the sign up form.
             * @since 17.12.11
             */
            signupForm() {
                let data = {
                    i18n: cardGame.i18n,
                    loginForm: false
                };

                $.get(TEMPLATE_LOGIN_SIGNUP, function (source) {
                    let template = Handlebars.compile(source);
                    cardGame.$container.find(".board__background").append(template(data));
                    splashScreenScript.showSignupForm();
                });
            }
        }
    })();
});