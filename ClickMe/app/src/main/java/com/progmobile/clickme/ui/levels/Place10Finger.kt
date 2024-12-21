package com.progmobile.clickme.ui.levels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
 * Composable that displays the next level button when their is 10 fingers on the device.
 * It uses a [UnlockLevel] composable to display the next level button.
 */
@Composable
fun Place10Finger(
    idLevel: Int,
    nextLevel: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val fingerCount = remember { mutableIntStateOf(0) }
    val showButton = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val touches = event.changes.filter { it.pressed }

                        fingerCount.intValue = touches.size
                        if (fingerCount.intValue >= 5) {
                            showButton.value = true
                        }
                    }
                }
            }
    ) {

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.level_place_ten_fingers),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            // Level button
            if (showButton.value) {
                UnlockLevel(
                    labelResourceId = R.string.button,
                    level = idLevel,
                    modifier = Modifier,
                    levelName = nextLevel,
                    navController = navController
                )
            } else {
                Text(
                    text = "${fingerCount.intValue}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,

                )
            }
        }
    }
}

@Preview
@Composable
fun StartPlace10FingerPreview() {
    ClickMeTheme {
        Place10Finger(
            idLevel = -1,
            nextLevel = Screens.HomePage.name,
            navController = rememberNavController(),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}