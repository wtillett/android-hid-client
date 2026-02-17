package me.arianb.usb_hid_client

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition
import com.topjohnwu.superuser.Shell
import me.arianb.usb_hid_client.settings.SettingsViewModel
import me.arianb.usb_hid_client.troubleshooting.ProductionTree
import me.arianb.usb_hid_client.ui.standalone_screens.OnboardingScreen
import timber.log.Timber

// Overriding Application so that the loggers only get created once, regardless of Activity lifecycle
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Shell.enableVerboseLogging = true
        }

        Timber.plant(ProductionTree())
    }
}


// TODO: move all misc strings used in snackbars and alerts throughout the app into strings.xml for translation purposes.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            ViewCompat.onApplyWindowInsets(view, windowInsets)
        }

        setContent {
            Entrypoint()
        }
    }
}

@Composable
fun Entrypoint(
    settingsViewModel: SettingsViewModel = viewModel(),
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val userPreferencesState by settingsViewModel.userPreferencesFlow.collectAsState()

    // If this is the first time the app has been opened, then show OnboardingActivity
    val onboardingDone = userPreferencesState.isOnboardingDone
    val startScreen = if (!onboardingDone) {
        OnboardingScreen()
    } else {
        MainScreen()
    }

    ScreenEntrypoint(startScreen)
}

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun ScreenEntrypoint(startScreen: Screen) {
    // dispose params are due to this issue: https://github.com/adrielcafe/voyager/issues/106
    Navigator(
        startScreen,
        disposeBehavior = NavigatorDisposeBehavior(disposeSteps = false),
    ) { navigator ->
        SlideTransition(
            navigator,
            disposeScreenAfterTransitionEnd = true
        )
    }
}
