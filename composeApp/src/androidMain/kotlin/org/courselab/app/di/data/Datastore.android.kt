package org.courselab.app.di.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import org.courselab.app.data.createDataStore
import org.courselab.app.data.PREFERENCES_DB
import org.koin.dsl.module


fun dataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = { context.filesDir.resolve(PREFERENCES_DB).absolutePath }
    )