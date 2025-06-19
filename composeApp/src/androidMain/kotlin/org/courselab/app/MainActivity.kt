package org.courselab.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource

class MainActivity : ComponentActivity(), PermissionsListener {

    private var permissionsManager: PermissionsManager? = null

    private val _locationGranted = MutableStateFlow(false)
    val locationGranted: StateFlow<Boolean> = _locationGranted


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        _locationGranted.value =
            PermissionsManager.areLocationPermissionsGranted(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CompositionLocalProvider(
                LocalRequestLocationPermission provides { askForLocation() },
                LocalLocationPermissionGranted provides locationGranted.collectAsState().value
            ) {
                AndroidApp()
            }
        }
    }

    private fun askForLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            _locationGranted.value = true
            enableLocationComponent()
        } else {
            permissionsManager = PermissionsManager(this).also {
                it.requestLocationPermissions(this)
            }
        }
    }


    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(
            this,
            "CourseLab necesita conocer tu ubicación para ofrecerte todas las característcas",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        _locationGranted.value = granted
        if (granted) enableLocationComponent()
        else Toast.makeText(
            this,
            "Sin permiso de ubicación no se mostrará tu posición en el mapa.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun enableLocationComponent() {
        val location = createDefault2DPuck(true)
    }

    override fun onRestart() {
        super.onRestart()
        recreate()
    }
}

@Composable
fun AndroidApp() {
    App()
}