package com.sandymist.android.common.utilities

import android.content.Context
import android.content.SharedPreferences

val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences("UtilitiesPrefs", Context.MODE_PRIVATE)

fun Context.getPrefBoolean(key: String, defaultValue: Boolean) =
    sharedPreferences.getBoolean(key, defaultValue)

fun Context.setPrefBoolean(key: String, value: Boolean) {
    sharedPreferences.edit()
        .putBoolean(key, value)
        .apply()
}
