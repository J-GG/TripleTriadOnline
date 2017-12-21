/*
 * Copyright (C) 2014 - 2017 PayinTech, SAS - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package service;

import play.i18n.Langs;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Expose the languages of the application.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.21
 * @since 17.12.21
 */
@Singleton
public final class LanguagesService {

    /**
     * Instance of the languages of the application.
     *
     * @since 17.12.21
     */
    public static Langs langs;

    /**
     * Build instance.
     *
     * @param langs the object to manage langs in the app
     * @since 17.12.21
     */
    @Inject
    private LanguagesService(final Langs langs) {
        LanguagesService.langs = langs;
    }
}
