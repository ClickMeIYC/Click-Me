package com.progmobile.clickme.ui

import android.media.MediaPlayer
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.progmobile.clickme.MainActivity
import com.progmobile.clickme.R
import com.progmobile.clickme.data.DataSource.LEVEL_PATIENCE_LONG_PRESS_DURATION
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.min

/**
 * Customizable button composable that displays the [labelResourceId]
 * and triggers [onClick] lambda when this composable is clicked
 */
@Composable
fun LevelButton(
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    longClick: Boolean = false,
    playMusic: Boolean = false,
    inLevelButton: Boolean = false,
    prefix: String = "",
    modifier: Modifier = Modifier
) {
    // Sound section
    val context = LocalContext.current

    val soundResourceId = if (inLevelButton) {
        R.raw.victory_sound
    } else {
        R.raw.click_button
    }

    val scope = rememberCoroutineScope()
    var isPressed by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val textFontSize = min(screenWidth * 0.04,20.0)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp)) // Rounded corners
            // light blue background
            .background(Color(0xFFADD8E6))
            .padding(16.dp)       // Padding for content spacing
            // Clickable area with long click
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (longClick) {

                            isPressed = true
                            // Start coroutine to wait for the hold duration
                            scope.launch {
                                delay(LEVEL_PATIENCE_LONG_PRESS_DURATION)
                                if (isPressed) {
                                    onClick()
                                    runBlocking {
                                        if (MainActivity.instance?.userSoundPreference == true && playMusic) {
                                            val mediaPlayer =
                                                MediaPlayer.create(context, soundResourceId)
                                            mediaPlayer.setOnCompletionListener { it.release() }
                                            mediaPlayer.start()
                                        }
                                    }
                                }
                            }
                            // Reset isPressed state when released
                            tryAwaitRelease()
                            isPressed = false
                        } else {
                            onClick()
                            runBlocking {
                                if (MainActivity.instance?.userSoundPreference == true && playMusic) {
                                    val mediaPlayer =
                                        MediaPlayer.create(context, soundResourceId)
                                    mediaPlayer.setOnCompletionListener { it.release() }
                                    mediaPlayer.start()
                                }
                            }
                        }
                    }
                )
            },
        contentAlignment = if(inLevelButton) Alignment.Center else Alignment.CenterStart   // Center text in Box
    ) {
        Text(
            text = prefix + stringResource(labelResourceId),
            fontSize = textFontSize.sp,
            color = Color.White,
            textAlign = if(inLevelButton) TextAlign.Center else TextAlign.Start
            //style = MaterialTheme.typography.button // Adjust font style as needed
        )
    }
}

/**
 * Composable that displays a locked level button
 */
@Composable
fun LevelButtonLocked(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp),
        enabled = false
    ) {
        Image(
            painter = painterResource(id = R.drawable.lock_icon),
            contentDescription = "Locked levels",
            modifier = Modifier
                .background(Color.Transparent)
                .wrapContentSize(Alignment.Center)
        )
    }
}

/**
 * Composable that allows the user to select the desired action to do and triggers
 * [onUnlock] lambda when the level is unlocked, replaces the default onClick action of the this composable.
 */
@Composable
fun UnlockLevel(
    @StringRes labelResourceId: Int,
    level: Int,
    modifier: Modifier = Modifier,
    levelName: String,
    navController: NavHostController,
    longClick:Boolean = false,
    onUnlock: (() -> Unit)? = null,
    playMusic: Boolean = true
) {
    LevelButton(
        onClick = {
            if (onUnlock != null) {
                onUnlock()
            } else {
                navController.navigate(levelName)
                if (MainActivity.instance?.currentLevelUnlocked!! < level) {
                    MainActivity.instance?.increaseLevel()
                }
            }
        },
        labelResourceId = labelResourceId,
        longClick = longClick,
        playMusic = playMusic,
        inLevelButton = true,
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}