package org.courselab.app.org.courselab.app.data.domain

import kotlinx.io.IOException
import org.courselab.app.org.courselab.app.data.repository.MunicipioRepository
import org.courselab.app.org.courselab.app.models.MunicipioSearchResult
class GetMunicipiosUseCase(
    private val repository: MunicipioRepository
) {
    sealed interface Result {
        data class Success(val items: List<MunicipioSearchResult>) : Result
        object Error : Result
    }

    /**
     * Al usar operator invoke, podemos llamar directamente:
     *    val res = getMunicipiosUseCase("Madrid")
     */
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

