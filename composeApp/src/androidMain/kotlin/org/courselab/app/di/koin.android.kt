package org.courselab.app.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


val androidModule = module {
  single<PlatformKoinInit> { initKoin(get()) }
}

actual fun initKoin(appContext: Any): PlatformKoinInit {
    val modules = sharedKoinModules + viewModelModules + repositoriesModules //+ databaseModule
    val context = appContext as Context
    println("-----------------------------------Koin initialized in ANDROID-----------------------------------")
    startKoin(
        appDeclaration = {
            androidContext(context)
            modules(modules)
        })
    return PlatformKoinInit()
}