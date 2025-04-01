package com.sandymist.android.common.utilities.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandymist.android.common.utilities.AppException
import com.sandymist.android.common.utilities.NetworkStatus
import com.sandymist.android.common.utilities.R

@Composable
fun ErrorScreen(
    exception: Exception,
    retry: () -> Unit,
    networkStatus: NetworkStatus = NetworkStatus.Connected,
    autoRetry: Boolean = false,
) {
    val initialNetworkStatus by remember { mutableStateOf(networkStatus == NetworkStatus.Connected) }

    if (autoRetry) {
        LaunchedEffect(networkStatus) {
            if (!initialNetworkStatus && networkStatus == NetworkStatus.Connected) {
                retry()
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.error), style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(30.dp))

        val message = when(exception) {
            is AppException -> exception.description
            else -> exception.message ?: exception.cause?.message ?: exception.javaClass.name
        }
        Text(message, style = MaterialTheme.typography.titleMedium)

        Button(
            onClick = retry,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(
        exception = Exception("Test exception"),
        retry = {},
    )
}
