@file:Suppress("unused")
package com.sandymist.android.common.utilities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.WebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.InetAddress

import android.app.ActivityManager
import android.content.Context
import android.os.Debug

fun onUIThread() = (Looper.getMainLooper() == Looper.myLooper())

@SuppressLint("WebViewApiAvailability")
fun supportsWebView(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Timber.v("Webview package: " + WebView.getCurrentWebViewPackage()?.packageName)
        WebView.getCurrentWebViewPackage()?.packageName != null
    } else {
        runCatching { CookieManager.getInstance() }.isSuccess
    }
}

fun isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("google/sdk_gphone")
            || Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || (Build.BRAND.startsWith("google") && Build.DEVICE.startsWith("generic"))
            || "google_sdk" == Build.PRODUCT)
}

fun isEmulatorNetwork(): Boolean {
    try {
        runBlocking {
            val ipAddress = withContext(Dispatchers.IO) { InetAddress.getLocalHost().hostAddress } ?: ""
            return@runBlocking (ipAddress == "10.0.2.15" || ipAddress.startsWith("10.0.2."))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun getMemoryUsage(context: Context): MemInfo {
    val memoryInfo = Debug.MemoryInfo()
    Debug.getMemoryInfo(memoryInfo)

    // Total memory used by the app (in bytes)
    val totalPrivateDirtyMemory = memoryInfo.totalPrivateDirty * 1024 // Convert to bytes
    val totalSharedDirtyMemory = memoryInfo.totalSharedDirty * 1024 // Convert to bytes
    val totalPrivateCleanMemory = memoryInfo.totalPrivateClean * 1024 // Convert to bytes
    val totalSharedCleanMemory = memoryInfo.totalSharedClean * 1024 // Convert to bytes

    // Get native heap size
    val nativeHeapSize = Debug.getNativeHeapAllocatedSize()

    // Log memory usage
    println("Total Private Dirty Memory: $totalPrivateDirtyMemory bytes")
    println("Total Shared Dirty Memory: $totalSharedDirtyMemory bytes")
    println("Total Private Clean Memory: $totalPrivateCleanMemory bytes")
    println("Total Shared Clean Memory: $totalSharedCleanMemory bytes")

    println("Native Heap Size: $nativeHeapSize bytes")

    // Optionally, use ActivityManager to get additional memory info
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo2 = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo2)

    println("Available Memory: ${memoryInfo2.availMem} bytes")
    println("Total Memory: ${memoryInfo2.totalMem} bytes")

    return MemInfo(
        privateDirtyMemoryInBytes = totalPrivateDirtyMemory,
        sharedDirtyMemoryInBytes = totalSharedDirtyMemory,
        privateCleanMemoryInBytes = totalPrivateCleanMemory,
        sharedCleanMemoryInBytes = totalSharedCleanMemory,
        nativeHeapSizeInBytes = nativeHeapSize,
        availableMemoryInBytes = memoryInfo2.availMem,
        totalMemoryInBytes = memoryInfo2.totalMem
    )
}

data class MemInfo(
    val privateDirtyMemoryInBytes: Int,
    val sharedDirtyMemoryInBytes: Int,
    val privateCleanMemoryInBytes: Int,
    val sharedCleanMemoryInBytes: Int,
    val nativeHeapSizeInBytes: Long,
    val availableMemoryInBytes: Long,
    val totalMemoryInBytes: Long,
) {
    val dirtyMemoryInBytes: Int
        get() = privateDirtyMemoryInBytes + sharedDirtyMemoryInBytes
    val cleanMemoryInBytes: Int
        get() = privateCleanMemoryInBytes + sharedCleanMemoryInBytes
}
