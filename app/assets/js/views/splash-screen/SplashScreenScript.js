'use strict';

/**
 * Manage the main menu.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.11
 */
define([cardGame.gamePath + "js/views/common/Common.js",
    cardGame.gamePath + "js/toolbox/Key.js",
    cardGame.gamePath + "js/views/common/Sound.js"], function (Common, Key, Sound) {

    /**
     * Manage the main menu.
     * @param authenticated true if the user is authenticated
     * @since 17.12.11
     */
    function showMenu(authenticated) {
        cardGame.$container.find(".splash-screen__menu").removeClass("splash-screen__menu--hidden");
        Common.linearChoice({}, function (e) {
            switch (e.key) {
                case Key.ENTER:
                    switch (e.choice) {
                        case 1:
                            Sound.play(Sound.getKeys().SELECT);
                            if (authenticated) {
                                Routes.get(Routes.getKeys().PLAY)(true);
                            } else {
                                cardGame.$container.find(".cursor").remove();
                                Routes.get(Routes.getKeys().LOGIN_FORM)();
                            }
                            break;
                        case 2:
                            Sound.play(Sound.getKeys().SELECT);
                            if (authenticated) {
                                Routes.get(Routes.getKeys().PLAY)();
                            } else {
                                cardGame.$container.find(".cursor").remove();
                                Routes.get(Routes.getKeys().SIGNUP_FORM)();
                            }
                            break;
                        default:
                            Sound.play(Sound.getKeys().SELECT);
                            Routes.get(Routes.getKeys().SETTINGS)();
                            break;
                    }
                    break;
            }
        });
    }

    /**
     * Close the login or sign up form
     * @param choiceEvent the event object
     * @param authenticated true if the user is authenticated
     * @since 17.12.11
     */
    function closeLoginSignupForm(choiceEvent, authenticated) {
        choiceEvent.unbind();
        $("#message-login").remove();
        cardGame.$container.find(".cursor").remove();
        if (authenticated) {
            Routes.get(Routes.getKeys().SPLASH_SCREEN)();
        } else {
            showMenu(false);
        }
    }

    return {
        /**
         * Manage the main menu.
         * @param authenticated true if the user is authenticated
         * @since 17.12.11
         */
        showMenu(authenticated) {
            setTimeout(function () {
                showMenu(authenticated);
            }, 1500);
        },

        /**
         * Manage the login or sign up form.
         * @param loginForm true is the login form is shown
         * @since 17.12.11
         */
        showLoginSignupForm(loginForm){
            Common.linearChoice({selector: "#login-choices", unbindOnEnter: false}, function (e) {
                switch (e.key) {
                    case Key.ENTER:
                        switch (e.choice) {
                            case 1:
                                Sound.play(Sound.getKeys().SELECT);
                                cardGame.$container.find("#login-username input").focus();
                                break;
                            case 2:
                                Sound.play(Sound.getKeys().SELECT);
                                cardGame.$container.find("#login-password input").focus();
                                break;
                            case 3:
                                Sound.play(Sound.getKeys().SELECT);

                                $.post({
                                    url: "/login",
                                    contentType: "application/json",
                                    data: JSON.stringify({
                                        username: $("#login-username").find("input").val(),
                                        password: $("#login-password").find("input").val()
                                    }),
                                    dataType: "json"
                                }).done(function (data) {
                                    closeLoginSignupForm(e, data.authenticated);
                                }).fail(function () {
                                    logger.warn("An error occurred while trying to log in");
                                });
                                break;
                            case 4:
                                Sound.play(Sound.getKeys().CANCEL);
                                closeLoginSignupForm(e, false);
                                break;
                        }
                        break;

                    case Key.ESCAPE:
                        Sound.play(Sound.getKeys().CANCEL);
                        closeLoginSignupForm(e, false);
                        break;

                    case Key.DOWN:
                    case Key.UP:
                        $("input").blur();
                        cardGame.$container.find(".board").focus();
                }
            });
        }
    }
});