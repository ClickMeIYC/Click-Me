package com.progmobile.clickme.ui.levels

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.progmobile.clickme.R
import com.progmobile.clickme.Screens
import com.progmobile.clickme.ui.UnlockLevel
import com.progmobile.clickme.ui.theme.ClickMeTheme

/**
 * Composable that displays the next level button when light sensor of the device does not detect light.
 * It uses a [UnlockLevel] composable to display the next level button.
 */
@Composable
fun LightSensor(
    idLevel: Int,
    nextLevel: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lightLevel = remember { mutableStateOf<Float?>(null) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val lightSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    lightLevel.value = it.values[0]
                }
            }

            // Not necessary -> for the object
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }

        // Saving the listener
        sensorManager.registerListener(
            lightSensorListener,
            lightSensor,
            SensorManager.SENSOR_DELAY_UI
        )

        onDispose {
            sensorManager.unregisterListener(lightSensorListener)
        } // Free the resources
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Title
        Text(
            text = stringResource(id = R.string.level_light_sensor),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // Level button
        if (lightLevel.value != null && lightLevel.value!! < 5) {
            UnlockLevel(
                labelResourceId = R.string.button,
                level = idLevel,
                modifier = Modifier,
                levelName = nextLevel,
                navController = navController
            )
        }
    }
}

@Preview
@Composable
fun StartLightSensorPreview() {
    ClickMeTheme {
        LightSensor(
            idLevel = -1,
            nextLevel = Screens.HomePage.name,
            navController = rememberNavController(),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}