package com.progmobile.clickme

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.progmobile.clickme.data.DataSource.MUSIC_DEFAULT
import com.progmobile.clickme.data.DataSource.SOUND_DEFAULT
import com.progmobile.clickme.data.DataSource.STARTING_LEVEL
import com.progmobile.clickme.data.DataSource.isAppInForeground
import com.progmobile.clickme.ui.levels.isLanguageChanged
import com.progmobile.clickme.ui.theme.ClickMeTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

// Datastore, declared out of the class to be able to use it in multiple functions
private val Context.dataStore by preferencesDataStore("user_prefs")

/**
 * Main Activity of the application.
 * Provides for :
 * - Permissions
 * - Sound and music state
 * - Level unlocked state
 * - Detection of language change
 */
class MainActivity : ComponentActivity(), LifecycleObserver {

    // =========== LifeCycle ===========

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        isAppInForeground.value = hasFocus
    }

    // ========== PERMISSIONS ==========
    @SuppressLint("InlinedApi")
    private val permissionsToCheck: Array<String> = if (Build.VERSION.SDK_INT < 29) {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BODY_SENSORS
        )
    } else {
        arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.BODY_SENSORS
        )
    }

    private fun checkPermissions() {
        val permissionsToRequest = permissionsToCheck.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestMultiplePermissions.launch(permissionsToRequest.toTypedArray())
        }
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val deniedPermissions = permissions.filter { (_, isGranted) -> !isGranted }

        if (deniedPermissions.isNotEmpty()) {
            showPermissionDeniedDialog()
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_denied)
            .setMessage(R.string.permission_denied_msg)
            .setPositiveButton(R.string.settings) { _, _ ->
                // Open parameters
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    // ========== LEVEL ==========
    var currentLevelUnlocked by mutableIntStateOf(STARTING_LEVEL)

    // ========== MEDIA PLAYER ==========
    companion object {
        var instance: MainActivity? = null
    }

    private var mediaPlayer: MediaPlayer? = null

    var userMusicPreference by mutableStateOf(MUSIC_DEFAULT)
    var userSoundPreference by mutableStateOf(SOUND_DEFAULT)
    var isFirstLaunch by mutableStateOf(false)

    // ========= MAIN ACTIVITY ==========
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instance = this // Set the instance to the current activity
        enableEdgeToEdge()
        checkPermissions()

        setContent {
            ClickMeTheme {
                ClickMeApp(mainActivityInstance = this)
            }

        }
        //intent = Intent(this, MusicService::class.java)
        //startService(intent)

        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.background_music_1)
        mediaPlayer?.isLooping = true
    }

    override fun onDestroy() {
        super.onDestroy()
        //stopService(intent)
        mediaPlayer?.release()
        mediaPlayer = null
        instance = null // Set the instance to null to avoid memory leaks
    }

    override fun onPause() {
        super.onPause()
        // Stop the music when the app is paused
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()

        // Get music state in datastore : if true, user wants it on, then reactivate it.
        // If false, user wants it off, don't do anything as it has been paused at onPause or stopped at onDestroy
        userMusicPreference = runBlocking { getUserMusicPreference() }
        if (userMusicPreference) runBlocking { switchMusicState(stopMusic = false) }

        // Get sound state, for the buttons to know if they should play sound when clicked
        userSoundPreference = runBlocking { getUserSoundPreference() }

        // Get current level unlocked
        currentLevelUnlocked = runBlocking { getCurrentLevelUnlocked() }

        isFirstLaunch = runBlocking { getFirstLaunch() }

        // Get previous language
        val previousLanguage = runBlocking { getPreviousLanguage() }

        //Get current system's language
        val currentLanguage = Locale.getDefault().language

        if (previousLanguage != null && previousLanguage != currentLanguage) {
            // Language has been modified
            isLanguageChanged(true)
        }
    }

    override fun onStop() {
        super.onStop()
        //Set previous language to the current language when leaving the settings
        runBlocking {setPreviousLanguage(Locale.getDefault().language)}
    }

    private suspend fun getFirstLaunch(): Boolean {
        val firstLaunchKey = booleanPreferencesKey(R.string.first_launch_key.toString())
        val dataStore = this.dataStore

        // get boolean first launch state
        val firstLaunch = dataStore.data.map { preferences ->
            preferences[firstLaunchKey] ?: true
        }.first()
        return firstLaunch
    }

    fun switchFirstLaunch() {
        val firstLaunchKey = booleanPreferencesKey(R.string.first_launch_key.toString())
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[firstLaunchKey] = false
            }
        }
    }

    private suspend fun getUserMusicPreference(): Boolean {
        val musicKey = booleanPreferencesKey(R.string.music_key.toString())
        val dataStore = this.dataStore
        // get boolean music state
        val musicState = dataStore.data.map { preferences ->
            preferences[musicKey] ?: MUSIC_DEFAULT
        }.first()
        return musicState
    }

    private suspend fun getUserSoundPreference(): Boolean {
        val soundKey = booleanPreferencesKey(R.string.sound_key.toString())
        val dataStore = this.dataStore
        // get boolean sound state
        val soundState = dataStore.data.map { preferences ->
            preferences[soundKey] ?: SOUND_DEFAULT
        }.first()
        return soundState
    }

    private suspend fun getCurrentLevelUnlocked(): Int {
        val levelKey = intPreferencesKey(R.string.level_key.toString())
        val dataStore = this.dataStore
        // get boolean music state
        val currentLevel = dataStore.data.map { preferences ->
            preferences[levelKey] ?: STARTING_LEVEL
        }.first()
        return currentLevel
    }

    fun switchMusicState(stopMusic: Boolean = false) {
        val musicKey = booleanPreferencesKey(R.string.music_key.toString())
        // normal scope not for composable
        if (stopMusic) {
            mediaPlayer?.pause()
            lifecycleScope.launch {
                dataStore.edit { preferences ->
                    preferences[musicKey] = false
                }
            }
            userMusicPreference = false
        } else {
            mediaPlayer?.start()
            lifecycleScope.launch {
                dataStore.edit { preferences ->
                    preferences[musicKey] = true
                }
            }
            userMusicPreference = true
        }
    }

    fun switchSoundState() {
        val soundKey = booleanPreferencesKey(R.string.sound_key.toString())
        // normal scope not for composable
        if (userSoundPreference) {
            lifecycleScope.launch {
                dataStore.edit { preferences ->
                    preferences[soundKey] = false
                }
            }
            userSoundPreference = false
        } else {
            lifecycleScope.launch {
                dataStore.edit { preferences ->
                    preferences[soundKey] = true
                }
            }
            userSoundPreference = true
        }
    }

    fun increaseLevel() {
        currentLevelUnlocked++
        val levelKey = intPreferencesKey(R.string.level_key.toString())
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[levelKey] = currentLevelUnlocked
            }
        }
    }

    fun resetLevels() {
        currentLevelUnlocked = STARTING_LEVEL
        val levelKey = intPreferencesKey(R.string.level_key.toString())
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[levelKey] = currentLevelUnlocked
            }
        }
    }

    private fun setPreviousLanguage(language: String)
    {
        val languageKey = stringPreferencesKey("LanguageKey")
        lifecycleScope.launch {
            dataStore.edit { preferences ->
                preferences[languageKey] = language
            }
        }
    }

    private suspend fun getPreviousLanguage(): String? {
        val previousLanguageKey = stringPreferencesKey("LanguageKey")
        return dataStore.data.map { preferences ->
            preferences[previousLanguageKey]
        }.firstOrNull()
    }
}