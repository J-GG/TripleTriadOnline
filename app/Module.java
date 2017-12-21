/*
 * Copyright (C) 2014 - 2017 PayinTech, SAS - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import play.libs.akka.AkkaGuiceSupport;
import service.LanguagesService;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.21
 * @since 17.12.21
 */
public class Module extends AbstractModule implements AkkaGuiceSupport {

    /**
     * Configures {@link Binder} via the exposed methods.
     *
     * @since 17.12.21
     */
    @Override
    public void configure() {
        bind(LanguagesService.class).asEagerSingleton();
    }
}
