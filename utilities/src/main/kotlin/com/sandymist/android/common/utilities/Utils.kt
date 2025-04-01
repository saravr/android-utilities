@file:Suppress("unused")

// TDOD: move to utilities repo
package com.sandymist.android.common.utilities

import java.net.URLEncoder
import java.util.Locale
import kotlin.math.abs

fun String?.encodedUrl(): String {
    return if (this != null) URLEncoder.encode(this, "UTF-8") else ""
}

private const val CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

fun generateRandomString(length: Int): String {
    return (1..length)
        .map { CHAR_SET.random() }
        .joinToString("")
}

inline fun <reified T: Number> T?.multiply(multiplier: Int): T {
    return this?.let {
        when (it) {
            is Int -> (it * multiplier) as T
            is Long -> (it * multiplier) as T
            is Float -> (it * multiplier) as T
            is Double -> (it * multiplier) as T
            is Short -> (it * multiplier) as T
            is Byte -> (it * multiplier) as T
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    } ?: 0 as T
}

// TODO: move to utils
// TODO: shows 0 months ago!! fix
fun Long?.ageString(): String {
    return this?.let {
        val nowInSeconds = System.currentTimeMillis() / 1000
        val diff = nowInSeconds - this
        val minutes = diff / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        when {
            diff < 60 -> "${diff}s"
            minutes < 60 -> "${minutes}m"
            hours < 24 -> "${hours}h"
            days < 7 -> "${days}d"
            days < 31 -> "${weeks}w"
            months < 12 -> "${months}mo"
            else -> "${years}y"
        }
    } ?: ""
}

fun <T> List<T>.plusIf(condition: Boolean, item: T): List<T> {
    return if (condition) {
        plus(item)
    } else {
        this
    }
}
