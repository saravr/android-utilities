@file:Suppress("unused")

package com.sandymist.android.common.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.WebView
import timber.log.Timber

@Suppress("unused")
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

fun Context.getAppVersion(): String {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}

fun Context.getShortAppVersion() = getAppVersion().substringBefore('-')

fun Context.isDebuggable() = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
