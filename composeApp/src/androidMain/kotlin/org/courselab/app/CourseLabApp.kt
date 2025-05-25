package org.courselab.app

import android.app.Application
import org.courselab.app.di.initKoin
import org.courselab.app.di.sharedKoinModules
import org.courselab.app.viewmodel.getViewModelScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CourseLabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        getViewModelScope()
        initKoin()
    }

    private fun initKoin() {
        val modules = sharedKoinModules
        startKoin {
            androidContext(this@CourseLabApp)
            modules(modules)
        }
    }
}