package org.courselab.app.di.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.courselab.app.data.PREFERENCES_DB
import org.courselab.app.data.createDataStore


fun dataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = { context.filesDir.resolve(PREFERENCES_DB).absolutePath }
    )