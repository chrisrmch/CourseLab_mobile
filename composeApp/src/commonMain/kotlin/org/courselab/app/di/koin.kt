package org.courselab.app.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val platformModule = module {
    singleOf(::initKoin)
}

interface KoinInitialization

class PlatformKoinInit() : KoinInitialization

expect fun initKoin(appContext: Any) : PlatformKoinInit

fun appModule() = listOf(platformModule, networkModule)

fun doInitKoin(){
    startKoin {
        modules(appModule())
    }
}