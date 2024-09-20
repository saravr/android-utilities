@file:Suppress("unused")

package com.sandymist.android.common.utilities.network

sealed class NetworkStatus {
    data object Unknown : NetworkStatus()
    data object Connected : NetworkStatus()
    data object Disconnected : NetworkStatus()
}
