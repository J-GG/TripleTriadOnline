/**
 * A member. It is also used to communicate with the backend.
 * @author Jean-Gabriel Genest
 * @since 17.12.19
 * @version 17.12.19
 */
define([cardGame.gamePath + "js/models/MemberSettings.js"], function (MemberSettings) {
    return class Member {

        /**
         * Create a new member based on the parameters.
         * @param member the member's information
         * @since 17.12.19
         */
        constructor(member) {
            this.username = member.username;
            this.memberSettings = new MemberSettings(member.memberSettings);
        }

        /**
         * Get the member's username.
         * @returns {*} the member's username
         * @since 17.12.19
         */
        getUsername() {
            return this.username;
        }

        /**
         * Set the member's username.
         * @param username the member's username
         * @since 17.12.19
         */
        setUsername(username) {
            this.username = username;
        }

        /**
         * Get the member's settings.
         * @returns {*} the member's settings
         * @since 17.12.19
         */
        getMemberSettings() {
            return this.memberSettings;
        }

        /**
         * Set the member's settings
         * @param memberSettings the member's settings
         * @since 17.12.19
         */
        setMemberSettings(memberSettings) {
            this.memberSettings = memberSettings;
        }
    }
});