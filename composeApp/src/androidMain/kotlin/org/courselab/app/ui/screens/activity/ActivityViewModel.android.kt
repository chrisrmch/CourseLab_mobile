package org.courselab.app.ui.screens.activity

import android.os.Build
import androidx.annotation.RequiresApi
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.courselab.app.data.model.RecordedTrackPoint
import org.courselab.app.data.repository.ActivityRepository
import org.courselab.app.data.repository.UserRepository
import org.courselab.app.models.ActividadDTO
import org.courselab.app.models.ActividadDTORequest
import org.courselab.app.models.LapDTO
import org.courselab.app.models.LapExtensionDTO
import org.courselab.app.models.TrackpointDTO
import org.courselab.app.models.TrackpointExtensionDTO
import org.courselab.app.viewmodel.BaseViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

data class ActivityPageState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val elapsedTime: Long = 0L,
    val distance: Float = 0f,
    val trackPoints: List<RecordedTrackPoint> = emptyList(),
    val smoothedPaceSec: Float? = null,
    val isFinished: Boolean = false
)


private const val PACE_EMA_ALPHA = 0.15f
private const val NO_PACE = 9_999f


class ActivityViewModel(
    private val activityRepository: ActivityRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {
    private val _state = MutableStateFlow(ActivityPageState())
    val state = _state.asStateFlow()

    private var lastPacePoint: RecordedTrackPoint? = null

    private var userID: Long = -1L
    private var weightKg: Float = 70f

    init {
        scope.launch {
            userRepository.getCurrentUser()?.let { user ->
                user.perfil?.peso?.let { weightKg = it }
                userID = userRepository.getUserID().toLong()
                println("userID: $userID")
            }
        }
    }

    fun startActivity() {
        _state.update {
            it.copy(
                isRunning = true,
                elapsedTime = 0L,
                distance = 0f,
                trackPoints = emptyList(),
                isFinished = false
            )
        }
    }

    fun pauseActivity() {
        _state.update { it.copy(isRunning = false, isPaused = true) }
    }

    fun resumeActivity() {
        _state.update { it.copy(isRunning = true, isPaused = false) }
    }

    fun finishActivity() {
        _state.update { it.copy(isRunning = false, isPaused = false, isFinished = true) }
    }

    fun incrementTime() {
        _state.update { it.copy(elapsedTime = it.elapsedTime + 1) }
    }


    fun appendLocation(point: Point, altitude: Float? = null, cadence: Int? = null) {
        val now = System.currentTimeMillis()
        val prevRaw = _state.value.trackPoints.lastOrNull()

        if (lastPacePoint == null && prevRaw != null) {
            lastPacePoint = prevRaw
        }

        val baseForPace = lastPacePoint ?: prevRaw
        var pace: Float? = null
        baseForPace?.let { base ->
            val distKm = haversine(base.point, point)
            val dt = (now - base.timestamp) / 1000f

            if (distKm >= 0.005f || dt >= 5f) {
                pace = dt / distKm
                lastPacePoint = RecordedTrackPoint(
                    point, now, altitude,
                    distKm, 0f, 0f, 0f, 0f, cadence
                )
            }
        }

        val segKmRaw = prevRaw?.let { haversine(it.point, point) } ?: 0f
        val deltaTRaw = prevRaw?.let { (now - it.timestamp) / 1000f } ?: 0f

        val newSmooth = pace?.let { p ->
            _state.value.smoothedPaceSec?.let { old ->
                old * (1 - PACE_EMA_ALPHA) + p * PACE_EMA_ALPHA
            } ?: p
        } ?: _state.value.smoothedPaceSec

        val newTP = RecordedTrackPoint(
            point, now, altitude,
            segKmRaw,
            if (deltaTRaw > 0) segKmRaw * 1000 / deltaTRaw else 0f,
            pace ?: NO_PACE,
            weightKg * segKmRaw,
            0f,
            cadence
        )

        _state.update {
            it.copy(
                distance = it.distance + segKmRaw,
                trackPoints = it.trackPoints + newTP,
                smoothedPaceSec = newSmooth          // ← ¡ya no será null!
            )
        }
    }

    private fun haversine(a: Point, b: Point): Float {
        val radio = 6_371e3
        val latitude1Rad = a.latitude().toRadians()
        val latitude2Rad = b.latitude().toRadians()
        val variacionLatitude = (b.latitude() - a.latitude()).toRadians()
        val variacionLongitude = (b.longitude() - a.longitude()).toRadians()
        val sinVarLat2 = sin(variacionLatitude / 2).pow(2)
        val sinVarLon2 = sin(variacionLongitude / 2).pow(2)
        val c = 2 * atan2(
            sqrt(sinVarLat2 + cos(latitude1Rad) * cos(latitude2Rad) * sinVarLon2),
            sqrt(1 - sinVarLat2 - cos(latitude1Rad) * cos(latitude2Rad) * sinVarLon2)
        )
        return ((radio * c) / 1000).toFloat()
    }

    private fun Double.toRadians(): Double = this * Math.PI / 180.0

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildActividadDTO(): ActividadDTORequest {
        val s = state.value
        val nowIso = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).toString()

        val lap = LapDTO(
            startTime = nowIso,
            totalTimeSeconds = s.elapsedTime.toFloat(),
            distanceMeters = s.distance * 1000f,
            maxSpeed = s.trackPoints.maxOfOrNull { it.speedMps } ?: 0f,
            calories = s.trackPoints.sumOf { it.calories.toDouble() }.roundToInt(),
            extension = LapExtensionDTO(
                avgSpeed = (s.distance * 1000f) / s.elapsedTime.coerceAtLeast(1),
            ),
            trackpoints = s.trackPoints.map { tp ->
                TrackpointDTO(
                    time = Instant.ofEpochMilli(tp.timestamp)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime()
                        .toString(),
                    latitude = tp.point.latitude(),
                    longitude = tp.point.longitude(),
                    altitudeMeters = tp.altitude,
                    distanceMeters = tp.distanceFromLastKm * 1000f,
                    extension = TrackpointExtensionDTO(
                        speed = tp.speedMps,
                        runCadence = tp.cadence
                    )
                )
            }
        )

        println("ID ACTIVIDAD:  $userID")

        return ActividadDTORequest(
            idUsuario = userID,
            fecha = nowIso,
            laps = listOf(lap)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finishAndUpload(onResult: (Boolean) -> Unit) {
        finishActivity()
        scope.launch {
            runCatching {
                activityRepository.uploadActividad(buildActividadDTO())
            }.onSuccess {
                onResult(true)
            }.onFailure {
                onResult(false)
            }
        }
    }
}
