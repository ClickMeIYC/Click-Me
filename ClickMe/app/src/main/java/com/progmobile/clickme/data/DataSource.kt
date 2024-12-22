package com.progmobile.clickme.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.progmobile.clickme.R
import com.progmobile.clickme.Screens
import com.progmobile.clickme.ui.levels.Charging
import com.progmobile.clickme.ui.levels.DarkMode
import com.progmobile.clickme.ui.levels.AirplaneMode
import com.progmobile.clickme.ui.levels.LightSensor
import com.progmobile.clickme.ui.levels.ShutdownDevice
import com.progmobile.clickme.ui.levels.Place10Finger
import com.progmobile.clickme.ui.levels.Wait20s
import com.progmobile.clickme.ui.levels.SimpleButton
import com.progmobile.clickme.ui.levels.LongPressButton
import com.progmobile.clickme.ui.levels.DoubleButtons
import com.progmobile.clickme.ui.levels.Orientation
import com.progmobile.clickme.ui.levels.Screenshot
import com.progmobile.clickme.ui.levels.LightTorch
import com.progmobile.clickme.ui.levels.DropDownMenu
import com.progmobile.clickme.ui.levels.LostButton
import com.progmobile.clickme.ui.levels.Labyrinth
import com.progmobile.clickme.ui.levels.MovingButton
import com.progmobile.clickme.ui.levels.ChangeLanguage
import com.progmobile.clickme.ui.levels.ButtonInHomepage
import com.progmobile.clickme.ui.levels.ScrollToFindTheButton
import com.progmobile.clickme.ui.levels.StepCountingLevel
import com.progmobile.clickme.ui.theme.CustomTheme

object DataSource {
    // State of the application
    var isAppInForeground = mutableStateOf(false)

    // Max number of levels to display
    const val LEVEL_NUMBERS = 21
    const val STARTING_LEVEL = 0
    const val MUSIC_DEFAULT = true
    const val SOUND_DEFAULT = true
    const val HINT_TEXT_SIZE = 20f
    const val IN_BOTTOM_BAR_BUTTONS_SIZE_RELATIVE_TO_SCREEN_WIDTH = 0.2f

    // Level-specific constants
    const val LEVEL_PATIENCE_LONG_PRESS_DURATION = 3000L
    const val LEVEL_STEP_COUNT_STEP_THRESHOLD = 40
    const val LEVEL_MOVING_BUTTON_SHAKE_DIST = 10
    const val LEVEL_MOVING_BUTTON_SHAKE_TIME = 500
    const val LEVEL_MOVING_BUTTON_SHAKE_COUNT_THRESHOLD = 5
    const val LEVEL_MOVING_BUTTON_SHAKE_TIME_FRAME = 5000L


    val levels = mapOf(
        Screens.SimpleButton.name to R.string.level_simple_button,
        Screens.LongPressButton.name to R.string.level_long_press_button,
        Screens.DoubleButtons.name to R.string.level_double_buttons,
        Screens.Orientation.name to R.string.level_orientation,
        Screens.Screenshot.name to R.string.level_screenshot,
        Screens.LightTorch.name to R.string.level_light_torch,
        Screens.Charging.name to R.string.level_charging,
        Screens.Microphone.name to R.string.level_microphone,
        Screens.DropDownMenu.name to R.string.level_drop_down_menu,
        Screens.ButtonInHomepage.name to R.string.level_button_in_home_page,
        Screens.DarkMode.name to R.string.level_dark_mode,
        Screens.AirplaneMode.name to R.string.level_airplane_mode,
        Screens.Labyrinth.name to R.string.level_labyrinth,
        Screens.MovingButton.name to R.string.level_moving_button,
        Screens.ChangeLanguage.name to R.string.level_change_language,
        Screens.StepCountingLevel.name to R.string.level_step_counting,
        Screens.LightSensor.name to R.string.level_light_sensor,
        Screens.LostButton.name to R.string.level_lost_button,
        Screens.ShutdownDevice.name to R.string.level_shutdown_device,
        Screens.Place10Finger.name to R.string.level_place_ten_fingers,
        Screens.Wait20s.name to R.string.level_wait_20_seconds,
        Screens.ScrollToFindTheButton.name to R.string.level_scroll_to_find_button
    )

    val levelsMap: Map<String, (NavHostController, Modifier) -> @Composable () -> Unit> = mapOf(
       Screens.SimpleButton.name to {navController, modifier -> { SimpleButton(1, Screens.DoubleButtons.name, navController, modifier) } },
        Screens.DoubleButtons.name to { navController, modifier -> { DoubleButtons(2, Screens.LongPressButton.name, navController, modifier) } },
        Screens.LongPressButton.name to { navController, modifier -> { LongPressButton(3, Screens.Orientation.name, navController, modifier) } },
        Screens.Orientation.name to { navController, modifier -> { Orientation(4, Screens.AirplaneMode.name, navController, modifier) } },
        Screens.AirplaneMode.name to { navController, modifier -> { AirplaneMode(5, Screens.DropDownMenu.name, navController, modifier) } },
        Screens.DropDownMenu.name to { navController, modifier -> { DropDownMenu(/* idLevel = 6*/ Screens.Place10Finger.name, navController, modifier) } },
        Screens.Place10Finger.name to { navController, modifier -> { Place10Finger(7, Screens.ScrollToFindTheButton.name, navController, modifier) } },
        Screens.ScrollToFindTheButton.name to { navController, modifier -> { ScrollToFindTheButton(8, Screens.ButtonInHomepage.name, navController, modifier) } },
        Screens.ButtonInHomepage.name to { _, modifier -> { ButtonInHomepage(/* idLevel = 17*/ modifier) } },
        Screens.LightTorch.name to { navController, modifier -> { CustomTheme{LightTorch(10, Screens.Charging.name, navController, modifier)} } },
        Screens.Charging.name to { navController, modifier -> { Charging(11, Screens.ChangeLanguage.name, navController, modifier) } },
        Screens.ChangeLanguage.name to { navController, modifier -> { ChangeLanguage(12, Screens.DarkMode.name, navController, modifier) } },
        Screens.DarkMode.name to { navController, modifier -> { DarkMode(13, Screens.MovingButton.name, navController, modifier) } },
        Screens.MovingButton.name to { navController, modifier -> { MovingButton(14, Screens.Screenshot.name, navController, modifier) } },
        Screens.Screenshot.name to { navController, modifier -> { Screenshot(15, Screens.StepCountingLevel.name, navController, modifier) } },
        Screens.StepCountingLevel.name to { navController, modifier -> { StepCountingLevel(16, Screens.LostButton.name, navController, modifier) } },
        Screens.LostButton.name to { _, modifier -> { LostButton(/* idLevel = 9*/ modifier) } },
        //Screens.Microphone.name to { navController, modifier -> { Microphone(18, Screens.Labyrinth.name, navController, modifier) } },
        Screens.Labyrinth.name to { navController, modifier -> { Labyrinth(18, Screens.LightSensor.name, navController, modifier) } },
        Screens.LightSensor.name to { navController, modifier -> { LightSensor(19, Screens.ShutdownDevice.name, navController, modifier) } },
        Screens.ShutdownDevice.name to { navController, modifier -> { ShutdownDevice(20, Screens.Wait20s.name, navController, modifier) } },
        Screens.Wait20s.name to { navController, modifier -> { Wait20s(/* idLevel = 21*/ Screens.HomePage.name, navController, modifier) } },
    )

    val LEVEL_LOST_NAVIGATION_VALUE = Screens.Labyrinth.name

    val levelHints = mapOf(
        // Map game advices to Home Page
        Screens.HomePage.name to listOf(
            R.string.home_page_explication_1,
            R.string.home_page_explication_2,
        ),
        // map three hints to each level
        Screens.SimpleButton.name to listOf(
            R.string.hint_simple_button_1,
            R.string.hint_simple_button_2,
            R.string.hint_simple_button_3
        ),
        Screens.LongPressButton.name to listOf(
            R.string.hint_long_press_button_1,
            R.string.hint_long_press_button_2,
            R.string.hint_long_press_button_3
        ),
        Screens.DoubleButtons.name to listOf(
            R.string.hint_double_buttons_1,
            R.string.hint_double_buttons_2,
            R.string.hint_double_buttons_3
        ),
        Screens.Orientation.name to listOf(
            R.string.hint_orientation_1,
            R.string.hint_orientation_2,
            R.string.hint_orientation_3
        ),
        Screens.Screenshot.name to listOf(
            R.string.hint_screenshot_1,
            R.string.hint_screenshot_2,
            R.string.hint_screenshot_3
        ),
        Screens.LightTorch.name to listOf(
            R.string.hint_light_torch_1,
            R.string.hint_light_torch_2,
            R.string.hint_light_torch_3
        ),
        Screens.Charging.name to listOf(
            R.string.hint_charging_1,
            R.string.hint_charging_2
        ),
        Screens.Microphone.name to listOf(
            R.string.hint_microphone_1,
            R.string.hint_microphone_2,
            R.string.hint_microphone_3
        ),
        Screens.DropDownMenu.name to listOf(
            R.string.hint_drop_down_menu_1,
            R.string.hint_drop_down_menu_2,
            R.string.hint_drop_down_menu_3,
            R.string.hint_drop_down_menu_4
        ),
        Screens.LostButton.name to listOf(
            R.string.hint_lost_button_1,
            R.string.hint_lost_button_2
        ),
        Screens.DarkMode.name to listOf(
            R.string.hint_dark_mode_1,
            R.string.hint_dark_mode_2
        ),
        Screens.AirplaneMode.name to listOf(
            R.string.hint_airplane_mode_1,
            R.string.hint_airplane_mode_2
        ),
        Screens.Labyrinth.name to listOf(
            R.string.hint_labyrinth_1,
            R.string.hint_labyrinth_2,
            R.string.hint_labyrinth_3),
        Screens.MovingButton.name to listOf(
            R.string.hint_moving_button_1,
            R.string.hint_moving_button_2,
            R.string.hint_moving_button_3),
        Screens.ChangeLanguage.name to listOf(
            R.string.hint_change_language_1,
            R.string.hint_change_language_2,
            R.string.hint_change_language_3,
            R.string.hint_change_language_4,
            R.string.hint_change_language_5
        ),
        Screens.StepCountingLevel.name to listOf(
            R.string.hint_step_counting_1,
            R.string.hint_step_counting_2,
            R.string.hint_step_counting_3
        ),
        Screens.LightSensor.name to listOf(
            R.string.hint_light_sensor_1,
            R.string.hint_light_sensor_2,
            R.string.hint_light_sensor_3),
        Screens.ButtonInHomepage.name to listOf(
            R.string.hint_button_in_home_page_1,
            R.string.hint_button_in_home_page_2,
            R.string.hint_button_in_home_page_3),
        Screens.ShutdownDevice.name to listOf(
            R.string.hint_shutdown_device_1,
            R.string.hint_shutdown_device_2,
            R.string.hint_shutdown_device_3),
        Screens.Place10Finger.name to listOf(
            R.string.hint_place_ten_fingers_1,
            R.string.hint_place_ten_fingers_2),
        Screens.Wait20s.name to listOf(
            R.string.hint_wait_20_seconds_1,
            R.string.hint_wait_20_seconds_2,
            R.string.hint_wait_20_seconds_3),
        Screens.ScrollToFindTheButton.name to listOf(
            R.string.hint_scroll_to_find_the_button_1,
            R.string.hint_scroll_to_find_the_button_2
        )
    )
}