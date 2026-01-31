package com.nottoostabby.superdongle

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import me.arianb.usb_hid_client.MainViewModel
import me.arianb.usb_hid_client.settings.SettingsViewModel

@Composable
fun DirectInput(
    mainViewModel: MainViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel(),
) {
    val keySender by mainViewModel.keySender.collectAsState()
    val userPreferencesState by settingsViewModel.userPreferencesFlow.collectAsState()

    Box(
        modifier = Modifier.focusable()
    )
}