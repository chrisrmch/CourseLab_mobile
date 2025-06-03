package org.courselab.app.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single {
            dataStore(get())
        }
    }