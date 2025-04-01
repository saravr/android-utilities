package com.sandymist.android.common.utilities

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

/**
 * A composable function that provides a pull-to-refresh behavior within a [Box].
 *
 * This function wraps its content in a [Box] and adds pull-to-refresh functionality.
 * When the user pulls down on the content, a refresh indicator is shown. If a network
 * connection is available, the `onRefresh` callback is invoked. Otherwise, a snackbar
 * indicating "No network connection" is displayed.
 *
 * @param isRefreshing A boolean indicating whether a refresh operation is currently in progress.
 *        This value should be updated by the caller to reflect the actual refreshing state.
 * @param onRefresh A callback invoked when the user triggers a refresh gesture and a network is available.
 *        This callback is responsible for initiating the refresh operation.
 * @param modifier Modifier to be applied to the underlying [Box].
 * @param state The state of the pull-to-refresh
 * @param contentAlignment Alignment for the content of the [Box].
 * @param indicator The composable to be displayed as the pull-to-refresh indicator.
 * @param content The content to be displayed inside the [Box].
 */
@Composable
@ExperimentalMaterial3Api
fun PullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val indicatorAlignment = if (isLandscape) Alignment.Center else Alignment.TopCenter
        PullToRefreshDefaults.Indicator(
            modifier = Modifier.align(indicatorAlignment),
            isRefreshing = isRefreshing,
            state = state
        )
    },
    snackbarHostState: SnackbarHostState,
    content: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier.pullToRefresh(
            state = state,
            isRefreshing = isRefreshing,
            onRefresh = {
                if (context.isNetworkAvailable()) {
                    onRefresh()
                } else {
                    scope.launch {
                        state.animateToHidden()
                        snackbarHostState.showNoConnection(context)
                    }
                }
            }
        ),
        contentAlignment = contentAlignment
    ) {
        content()
        indicator()
    }
}

suspend fun SnackbarHostState.showNoConnection(context: Context) {
    showSnackbar(context.getString(R.string.no_connection))
}
