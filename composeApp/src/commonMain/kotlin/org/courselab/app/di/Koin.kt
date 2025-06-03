package org.courselab.app.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


interface TargetModule {
    val module: Module
}

val sharedKoinModules = listOf(
    networkModule, repositoriesModules, viewModelModules
)

fun initKoinAndroid(additionalModules: List<Module>) {
    startKoin {
        modules(additionalModules + getBaseModules())
    }
}

internal fun getBaseModules() = sharedKoinModules + platformModule

expect val platformModule : Module;

fun initKoiniOS(targetModule: TargetModule) {
    (listOf(module { single { targetModule.module } } + sharedKoinModules))
}


fun initKoin() {
    startKoin {
        modules(
            sharedKoinModules
        )
    }
}