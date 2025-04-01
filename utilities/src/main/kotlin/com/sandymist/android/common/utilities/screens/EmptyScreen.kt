package com.sandymist.android.common.utilities.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sandymist.android.common.utilities.R
import com.sandymist.android.common.utilities.isNetworkAvailable

@Composable
fun EmptyScreen(
    message: String,
    actionLabel: String? = null,
    action: (() -> Unit)? = null,
) {
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.Clear,
            contentDescription = "No data"
        )
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )

        if (!context.isNetworkAvailable()) {
            Text(
                stringResource(R.string.check_connectivity),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        action?.let {
            Button(
                onClick = action,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(actionLabel ?: stringResource(R.string.retry))
            }
        }
    }
}

@Preview
@Composable
fun PreviewEmptyScreen() {
    EmptyScreen("Sorry, no data") {}
}
