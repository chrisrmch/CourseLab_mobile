package org.courselab.app

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import org.courselab.app.di.initKoinAndroid
import org.courselab.app.viewmodel.getViewModelScope
import org.koin.dsl.module

class CourseLabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        getViewModelScope()
        initKoinAndroid(
            listOf(
                module {
                    single<Context> { this@CourseLabApp }
                })
        )
    }
}