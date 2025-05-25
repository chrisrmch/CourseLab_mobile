package org.courselab.app

import android.app.Application
import org.courselab.app.di.sharedKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CourseLabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKotlin()
    }

    private fun initKotlin() {
            val modules = sharedKoinModules   // viewModelsModule + databaseModule

        startKoin {
            androidContext(this@CourseLabApp)
            modules(modules)
        }
    }
}