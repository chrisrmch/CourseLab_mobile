package org.courselab.app

import android.app.Application
import org.courselab.app.di.initKoin

class CourseLabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}