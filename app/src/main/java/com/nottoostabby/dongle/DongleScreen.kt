package com.nottoostabby.dongle

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.focusable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.arianb.usb_hid_client.MainViewModel
import me.arianb.usb_hid_client.R
import me.arianb.usb_hid_client.input_views.DirectInput
import me.arianb.usb_hid_client.input_views.DirectInputKeyboardView
import timber.log.Timber

class DongleScreen : Screen {

    @Composable
    override fun Content() {
        DirectInput()
    }
}

@Composable
fun DonglePage(
    mainViewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier.onPreviewKeyEvent {
//      Watch for layer changes and also maybe feedback for which keys are pressed?
        Timber.d("onPreviewKeyEvent received KeyEvent: $it")
        false
    }
) {
    val keySender by mainViewModel.keySender.collectAsState()
    val uiState by mainViewModel.uiState.collectAsState()


    DirectInput()

//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
////            DirectInput()
//            Text(text = "under DirectInput")
//            DongleInput()
////            KeyMap()
//            Row(
//            ) {
////            LeftBattery()
////            RightBattery()
//            }
//        }
//    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DongleInput() {
    var inputConnection by remember { mutableStateOf<InputConnection?>(null) }



    InterceptPlatformTextInput(
        interceptor = { request, nextHandler ->
            request.createInputConnection(EditorInfo()).also {
                inputConnection = it
            }



            nextHandler.startInputMethod { outAttributes ->
                request.createInputConnection(outAttributes)
            }
        },
        content = {
            Text(
                text = "hi",
                modifier = Modifier
                    .focusable()
                    .onPreviewKeyEvent {
                        Timber.d("DongleInput received KeyEvent: ${it.nativeKeyEvent}")
                        return@onPreviewKeyEvent inputConnection?.sendKeyEvent(it.nativeKeyEvent)
                            ?: false
                    }
            )
        }
    )
}

@Composable
fun DongleScreenIconButton() {
    val localView = LocalView.current
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow

    IconButton(
        onClick = {
            navigator.push(DongleScreen())

            val etDirectInput = localView.findViewById<DirectInputKeyboardView>(R.id.etDirectInput)
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            etDirectInput.requestFocus()
            imm.showSoftInput(etDirectInput, 0)
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_github),
            contentDescription = stringResource(R.string.direct_input)
        )
    }
}