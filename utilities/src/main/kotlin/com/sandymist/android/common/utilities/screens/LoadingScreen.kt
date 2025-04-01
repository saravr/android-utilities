package com.sandymist.android.common.utilities.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sandymist.android.common.utilities.R

@Composable
fun LoadingScreen(content: @Composable () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(stringResource(R.string.loading), style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Preview
@Composable
fun PreviewLoadingScreen() {
    LoadingScreen()
}
