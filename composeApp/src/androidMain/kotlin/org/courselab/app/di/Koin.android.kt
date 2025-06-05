package org.courselab.app.di

import org.courselab.app.di.data.dataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single {
            println("DATASTORE INICIALIZADO")
            dataStore(get())
        }
    }