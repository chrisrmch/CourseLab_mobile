package org.courselab.app.di

import org.koin.core.context.startKoin

val sharedKoinModules = listOf(
    networkModule, repositoriesModules, viewModelModules
)

fun initKoin() {
    startKoin {
        modules(
            sharedKoinModules
        )
    }
}