package org.courselab.app.di

import android.content.Context
import org.courselab.app.AndroidAppLocaleManager
import org.courselab.app.AndroidUrlLauncher
import org.courselab.app.AppLocaleManager
import org.courselab.app.UrlLauncher
import org.courselab.app.di.data.dataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single {
            println("DATASTORE INICIALIZADO")
            dataStore(get<Context>())
        }
        single<AppLocaleManager> { AndroidAppLocaleManager(get<Context>()) }
        single<UrlLauncher> { AndroidUrlLauncher(get<Context>()) }
    }