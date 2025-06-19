package org.courselab.app.data.domain

import androidx.datastore.core.IOException
import org.courselab.app.data.repository.MunicipioRepository
import org.courselab.app.models.MunicipioSearchResult
class GetMunicipiosUseCase(
    private val repository: MunicipioRepository
) {
    sealed interface Result {
        data class Success(val items: List<MunicipioSearchResult>) : Result
        object Error : Result
    }

    suspend operator fun invoke(query: String): Result {
        return try {
            val lista = repository.searchMunicipios(query)
            Result.Success(lista)
        } catch (e: IOException) {
            Result.Error
        } catch (t: Throwable) {
            Result.Error
        }
    }
}

