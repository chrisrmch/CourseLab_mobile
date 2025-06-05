package org.courselab.app.ui.screens.onboarding

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.courselab.app.data.UserPreferencesDataStore
import org.courselab.app.data.UserRepository
import org.courselab.app.models.Notification
import org.courselab.app.models.TrainingActivity
import org.courselab.app.models.UserProfile
import org.courselab.app.viewmodel.BaseViewModel


data class UserUiState(
    val userID: Int = 0,
    val profile: UserProfile? = null,
    val activities: List<TrainingActivity> = emptyList(),
    val notifications: List<Notification> = emptyList(),
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val dateOfBirth: String = "",
    val sex: String = "",
    private val role: String = "ROLE_USER"
)

class UserViewModel(
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val userRepository: UserRepository
) : BaseViewModel() {

    init {
        loadUserData()
    }


    val userId : Flow<Int?> = userPreferencesDataStore.userId


    private fun loadUserData() {
         scope.launch {
             userId.collect { id -> id?.let { updateUserUserID(it) } }
         }
        println("Loading user data !!!!!!!!!!!!!!!!!!!!! USER_ID: $userId")
        TODO("Not yet implemented")
    }

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    /**
     *
     *LA MEJOR FORMA DE HACER ACTUALIZACIONES DEL UI STATE:
     *
     *La forma en que estás actualizando el `_uiState` usando `_uiState.update { currentState -> currentState.copy(...) }`
     *es generalmente la **mejor práctica recomendada** para actualizar `StateFlow` en ViewModels de Android
     *cuando se trabaja con Kotlin y coroutines.
     *
     *Ventajas de este enfoque:
     *1.  **Atomicidad**: La función `update` es atómica. Garantiza que la transformación del estado (leer el valor actual y escribir el nuevo valor) ocurra como una operación única e indivisible. Esto previene condiciones de carrera si múltiples coroutines intentan actualizar el estado concurrentemente.
     *2.  **Inmutabilidad**: Al usar `copy()` en una `data class`, estás creando una nueva instancia del estado en lugar de modificar la existente. Esto es fundamental para que `StateFlow` (y Compose, si lo usas para la UI) detecte correctamente los cambios y active las recomposiciones/actualizaciones necesarias. Los flujos de estado están diseñados para funcionar con estados inmutables.
     *3.  **Concisieón y Legibilidad**: Es una forma clara y concisa de expresar la transformación del estado. El lambda dentro de `update` recibe el estado actual (`currentState`) y devuelve el nuevo estado.
     *4.  **Seguridad de Hilos (Thread Safety)**: `MutableStateFlow` y su función `update` están diseñados para ser seguros para usar desde múltiples hilos.
     *
     *Alternativas y por qué son menos ideales en este contexto:
     *1.  `_uiState.value = _uiState.value.copy(...)`:
     *    *   **No es atómico**: Esta operación implica dos pasos separados: leer `_uiState.value` y luego asignar un nuevo valor. Si otra coroutina modifica `_uiState.value` entre estos dos pasos, la actualización podría basarse en un estado obsoleto, perdiendo la actualización intermedia.
     *    *   **Más propenso a errores**: Es más fácil cometer errores, especialmente en escenarios concurrentes.
     *
     *Consideraciones Adicionales para "la mejor forma":
     **   **Mantén la lógica de actualización simple**: Si la lógica para derivar el nuevo estado es compleja, considera extraerla a funciones privadas dentro del ViewModel para mejorar la legibilidad y la capacidad de prueba.
     **   **Evita operaciones de bloqueo**: Nunca realices operaciones de bloqueo (como llamadas de red o acceso a disco) directamente dentro del lambda de `update`. Estas operaciones deben realizarse en coroutines separadas (por ejemplo, usando `viewModelScope.launch`) y luego el resultado se usa para actualizar el estado.
     **   **Para actualizaciones complejas que dependen de múltiples fuentes asíncronas**: Puedes usar operadores de `Flow` como `combine` o `zip` para crear un nuevo `StateFlow` que derive su estado de otros flujos, y luego recolectar ese flujo resultante en el `viewModelScope` para actualizar tu `_uiState` principal.
     *
     *En resumen, el método `_uiState.update { ... }` es la forma robusta, segura y idiomática de actualizar el `StateFlow` en tu `UserViewModel`.
     *
     * Common problems with _uiState.update:
     * 1. Race Conditions: If multiple coroutines try to update the state concurrently without proper synchronization,
     *    it can lead to unexpected behavior or lost updates. The `update` function itself is atomic for a single update,
     *    but sequences of reads and then updates from different coroutines can still interfere.
     * 2. Complex State Logic: If the logic within the update block becomes too complex, it can be hard to reason about
     *    and debug. It's often better to extract complex state transformations into separate functions.
     * 3. Over-updating: Updating the state too frequently, especially with complex objects, can lead to performance issues
     *    as it triggers recomposition or other reactions in observers.
     * 4. Not Collecting the Flow: If the `uiState` flow is not collected by any observer (e.g., in a Composable),
     *    updates to `_uiState` will happen, but no UI changes or other actions will be triggered.
     * 5. Blocking Operations: Performing long-running or blocking operations directly inside the `update` lambda
     *    can block the thread it's running on, potentially leading to ANRs if it's the main thread.
     */
    fun updateUserName(name: String) {
        _uiState.update { currentState -> currentState.copy(name = name) }
    }

    fun updateUserEmail(email: String) {
        _uiState.update { currentState -> currentState.copy(email = email) }
    }

    fun updateUserUserID(userID: Int) {
        _uiState.update { currentState -> currentState.copy(userID = userID) }
    }

    fun updateUserProfile(profile: UserProfile) {
        _uiState.update { currentState -> currentState.copy(profile = profile) }
    }

    fun updateUserActivities(activities: List<TrainingActivity>) {
        _uiState.update { currentState -> currentState.copy(activities = activities) }
    }

    fun updateUserNotifications(notifications: List<Notification>) {
        _uiState.update { currentState -> currentState.copy(notifications = notifications) }
    }

    fun updateUserSurname(surname: String) {
        _uiState.update { currentState -> currentState.copy(surname = surname) }
    }

    fun updateUserPassword(password: String) {
        _uiState.update { currentState -> currentState.copy(password = password) }
    }

    fun updateUserDateOfBirth(dateOfBirth: String) {
        _uiState.update { currentState -> currentState.copy(dateOfBirth = dateOfBirth) }
    }

    fun updateUserSex(sex: String) {
        _uiState.update { currentState -> currentState.copy(sex = sex) }
    }


}
