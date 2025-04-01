package com.sandymist.android.common.utilities

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

// Displays network status in snackbar whenever it changes
//
@Composable
fun ShowNetworkStatus(
    networkStatus: NetworkStatus,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    var lostNetworkConnection by remember { mutableStateOf(false) }

    LaunchedEffect(networkStatus) {
        when (networkStatus) {
            NetworkStatus.Connected -> {
                if (lostNetworkConnection) {
                    snackbarHostState.showSnackbar(context.getString(R.string.got_connected))
                    lostNetworkConnection = false
                }
            }

            NetworkStatus.Disconnected -> {
                lostNetworkConnection = true
                snackbarHostState.showSnackbar(context.getString(R.string.lost_network_connection))
            }

            NetworkStatus.Unknown -> {}
        }
    }
}
