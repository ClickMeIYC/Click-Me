package com.progmobile.clickme.ui.levels

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.progmobile.clickme.MainActivity
import com.progmobile.clickme.R
import com.progmobile.clickme.Screens
import com.progmobile.clickme.ui.LevelButton
import com.progmobile.clickme.ui.theme.ClickMeTheme


/**
 * Composable that displays a drop down menu where you have to make the sentence "This is not a button".
 * Make the right sentence.
 */
@Composable
fun DropDownMenu(
    nextLevel: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var sentence by remember { mutableStateOf("") }
    var currentMenu by remember { mutableIntStateOf(1) }

    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(
                text = stringResource(id = R.string.level_drop_down_menu),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text =  stringResource(id = R.string.dropdownmenu_indication) +" : $sentence",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(100.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            Box (
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                LevelButton(
                    labelResourceId = R.string.button,
                    onClick = { expanded = true }
                )
                if (expanded) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        when (currentMenu) {
                            1 -> {
                                DropdownMenuItem(
                                    text = { Text("She") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("He") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("This") },
                                    onClick = {
                                        sentence += "This "
                                        currentMenu = 2
                                    },
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("It") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Button") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                            }

                            2 -> {
                                DropdownMenuItem(
                                    text = { Text("will") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("can") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("button") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("is") },
                                    onClick = {
                                        sentence += "is "
                                        currentMenu = 3
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("can't") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                            }

                            3 -> {
                                DropdownMenuItem(
                                    text = { Text("not") },
                                    onClick = {
                                        sentence += "not "
                                        currentMenu = 4
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("really") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("a") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("very") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("button") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                            }

                            4 -> {
                                DropdownMenuItem(
                                    text = { Text("big") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("it") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("easy") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("button") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("a") },
                                    onClick = {
                                        sentence += "a "
                                        currentMenu = 5
                                    }
                                )
                            }
                            
                            5 -> {
                                DropdownMenuItem(
                                    text = { Text("button") },
                                    onClick = {
                                        sentence += "button."
                                        expanded = false
                                        MainActivity.instance?.increaseLevel()
                                        navController.navigate(nextLevel)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("joke") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("choice") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("pipe") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("answer") },
                                    onClick = {
                                        navController.navigate(Screens.HomePage.name)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun StartDropDownMenuPreview() {
    ClickMeTheme {
        DropDownMenu(
            nextLevel = Screens.HomePage.name,
            navController = rememberNavController(),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}