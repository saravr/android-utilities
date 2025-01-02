package com.sandymist.android.common.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Context.sharedPrefStringListAsFlow(key: String, defaultValue: String = ""): Flow<List<String>> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, prefKey ->
        if (prefKey == key) {
            val prefValue = sharedPreferences.getString(key, defaultValue) ?: defaultValue
            val prefs = StringList.decode(prefValue)
            trySend(prefs.asList())
        }
    }
    sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

    val prefValue = sharedPreferences.getString(key, defaultValue) ?: defaultValue
    val prefs = StringList.decode(prefValue)
    send(prefs.asList())

    awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
}

fun Context.addToStringList(key: String, value: String, defaultValue: String = "", max: Int) {
    require(max > 0) // TODO(1) - check effective value to be > 0

    val storedValue = value.toLowerCase(Locale.current)
    val prefValue = sharedPreferences.getString(key, defaultValue) ?: defaultValue
    val prefs = StringList.decode(prefValue)
    if (prefs.contains(storedValue)) {
        return
    }

    val updatedValue = prefs.add(value, max)
    val updatedValueAsString = updatedValue.encode()

    sharedPreferences.edit()
        .putString(key, updatedValueAsString)
        .apply()
}

fun Context.removeFromStringList(key: String, value: String, defaultValue: String = "") {
    val prefValue = sharedPreferences.getString(key, defaultValue) ?: defaultValue
    val prefs = StringList.decode(prefValue)
    val updatedValue = prefs.minus(value)
    val updatedValueAsString = updatedValue.encode()

    sharedPreferences.edit()
        .putString(key, updatedValueAsString)
        .apply()
}
