'use strict';

/**
 * Controller for the splash screen.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.12
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

        /**
         * Load and display the login or sign up form.
         * @param loginForm true if it should display the login form.
         * @since 17.12.11
         */
        function loginSignupForm(loginForm) {
            let data = {
                i18n: cardGame.i18n,
                loginForm: loginForm === true
            };

            $.get(TEMPLATE_LOGIN_SIGNUP, function (source) {
                let template = Handlebars.compile(source);
                cardGame.$container.find(".board__background").append(template(data));
                splashScreenScript.showLoginSignupForm(loginForm);
            });
        }

        return {

            /**
             * Load and display the template and the script.
             * @since 17.10.30
             */
            splashScreen() {
                let authenticated = false;
                let data = {
                    i18n: cardGame.i18n,
                    authenticated: authenticated
                };

                $.get(TEMPLATE, function (source) {
                    let template = Handlebars.compile(source);
                    cardGame.$container.find(".board__game-area").html(template(data));
                    splashScreenScript.showMenu(authenticated);
                });
            },

            /**
             * Load and display the login form.
             * @since 17.12.11
             */
            loginForm(){
                loginSignupForm(true);
            },


            /**
             * Display the sign up form.
             * @since 17.12.11
             */
            signupForm(){
                loginSignupForm(false);
            }
        }
    })();
});