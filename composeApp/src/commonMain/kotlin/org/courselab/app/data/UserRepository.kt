package org.courselab.app.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import org.courselab.app.models.User

class UserRepository(
    private val client: HttpClient,
    private val baseUrl: String,
    private val userPreferencesDataStore: UserPreferencesDataStore
    ) {

}