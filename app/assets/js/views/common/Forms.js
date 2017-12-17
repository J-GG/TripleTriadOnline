'use strict';

/**
 * Helper for the forms.
 * @author Jean-Gabriel Genest
 * @since 17.12.13
 * @version 17.12.13
 */
define([], function () {
    return (function () {

        return {
            showErrorMessages($form, errors){
                jQuery.each(errors, function (field, errorsField) {
                    jQuery.each(errorsField, function (index, errorField) {
                        $form.find(".error-" + field).append("<span>" + errorField + "</span>");
                    });
                });
                $(window).trigger("resize");
            },

            clearErrorMessages($form){
                $form.find(".form__error-message").text("");
                $(window).trigger("resize");
            }
        }
    })();
});