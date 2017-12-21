'use strict';

/**
 * Manage the main menu.
 * @author Jean-Gabriel Genest
 * @since 17.10.30
 * @version 17.12.19
 */
define([cardGame.gamePath + "js/views/common/Common.js",
    cardGame.gamePath + "js/toolbox/Key.js",
    cardGame.gamePath + "js/views/common/Sound.js",
    cardGame.gamePath + "js/views/common/Forms.js",
    cardGame.gamePath + "js/models/Member.js"], function (Common, Key, Sound, Forms, Member) {

    /**
     * Manage the main menu for authenticated users.
     * @since 17.12.11
     */
    function showMenuAuthenticated() {
        cardGame.$container.find(".splash-screen__menu").removeClass("splash-screen__menu--hidden");
        Common.linearChoice({}, function (e) {
            switch (e.key) {
                case Key.ENTER:
                    Sound.play(Sound.getKeys().SELECT);
                    switch (e.choice) {
                        case 1:
                            Routes.get(Routes.getKeys().PLAY)(true);
                            break;
                        case 2:
                            Routes.get(Routes.getKeys().PLAY)();
                            break;
                        case 3:
                            Routes.get(Routes.getKeys().SETTINGS)();
                            break;
                        default:
                            $.get({
                                url: "/logout",
                                dataType: "json"
                            }).done(function (response) {
                                cardGame.member = new Member(response.member);
                                Routes.get(Routes.getKeys().SPLASH_SCREEN)();
                            });
                            break;
                    }
                    break;
            }
        });
    }

    /**
     * Manage the main menu for non authenticated users.
     * @since 17.122.17
     */
    function showMenuNonAuthenticated() {
        cardGame.$container.find(".splash-screen__menu").removeClass("splash-screen__menu--hidden");
        Common.linearChoice({}, function (e) {
            switch (e.key) {
                case Key.ENTER:
                    Sound.play(Sound.getKeys().SELECT);
                    cardGame.$container.find(".cursor").remove();
                    switch (e.choice) {
                        case 1:
                            Routes.get(Routes.getKeys().LOGIN_FORM)();
                            break;
                        default:
                            Routes.get(Routes.getKeys().SIGNUP_FORM)();
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
            showMenuNonAuthenticated();
        }
    }

    /**
     * Manage the login and sign up forms.
     * @param url url for the AJAX request
     * @since 17.12.17
     */
    function manageLoginSignupForm(url) {
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
                            let data = {};
                            let $username = $("#login-username").find("input");
                            let $password = $("#login-password").find("input");
                            data[$username.attr("name")] = $username.val();
                            data[$password.attr("name")] = $password.val();
                            let $form = $("#login-signup-form");
                            Forms.clearErrorMessages($form);
                            $.post({
                                url: url,
                                data: data,
                                dataType: "json"
                            }).done(function (response) {
                                if (response.member !== undefined) {
                                    cardGame.member = new Member(response.member);
                                    Routes.get(Routes.getKeys().SPLASH_SCREEN)();
                                } else {
                                    Forms.showErrorMessages($form, response.errors);
                                }
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

    return {
        /**
         * Manage the main menu.
         * @param authenticated true if the user is authenticated
         * @since 17.12.11
         */
        showMenu(authenticated) {
            setTimeout(function () {
                if (authenticated) {
                    showMenuAuthenticated();
                } else {
                    showMenuNonAuthenticated();
                }
            }, 1500);
        },

        /**
         * Manage the login form.
         * @since 17.12.17
         */
        showLoginForm() {
            manageLoginSignupForm("/login");
        },

        /**
         * Manage the sign up form.
         * @since 17.12.17
         */
        showSignupForm() {
            manageLoginSignupForm("/signup");
        }
    }
});