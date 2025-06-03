package org.courselab.app.di

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

/**
 * Creates a DataStore instance for storing preferences.
 *
 * For more information on using DataStore in Kotlin Multiplatform, refer to:
 * [Local Preferences in Kotlin Multiplatform using DataStore](https://medium.com/@mohaberabi98/local-preferences-in-kotlin-multiplatform-using-datastore-c23ec677a35f)
 * @param producePath A function that returns the path to the DataStore file.
 */

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = { producePath().toPath() }
    )

const val dataStoreFileName = "cmp.preferences_pb"
