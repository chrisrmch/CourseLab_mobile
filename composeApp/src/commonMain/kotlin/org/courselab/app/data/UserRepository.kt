package org.courselab.app.data

import io.ktor.client.HttpClient

class UserRepository(
    private val client: HttpClient,
    private val baseUrl: String,
    private val userPreferencesDataStore: UserPreferencesDataStore
    ) {

}