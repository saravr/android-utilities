@file:Suppress("unused")

package com.sandymist.android.common.utilities

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

const val YEAR_MONTH_DATE_HOURS_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"
const val SEMI_MONTH_DATE_FORMAT_PATTERN = "MMM dd, yyyy"

fun Int.hoursToMilliseconds() = this * 60 * 60 * 1000L

fun Int.minutesToMilliseconds() = this * 60 * 1000L

fun Int.minutesToSeconds() = this * 60L

fun Int.secondsToMilliseconds() = this * 1000L

fun nowInSeconds() = System.currentTimeMillis() / 1000L

fun Long.secondsToMilliseconds() = this * 1000

fun Long.millisecondsToSeconds(): Int = (this / 1000).toInt()

fun String.isNegativeInt() = startsWith("-")

fun List<Int>.getAsTimeUnit(index: Int, negative: Boolean = false) = (getOrNull(index) ?: 0) * (if (negative) -1 else 1)

fun String.minuteSecondsToMilliseconds(): Long {
    return split(":").map { it.toInt() }.let {
        if (it.size != 2) throw IllegalArgumentException("Not in mm:ss format")
        (it.getAsTimeUnit(0).minutesToMilliseconds()) + it.getAsTimeUnit(1, isNegativeInt()).secondsToMilliseconds()
    }.toLong()
}

fun Long.millisecondsToDurationString(
    context: Context,
    prefix: String = "",
    suffix: String = "",
    short: Boolean = false,
    firstUnitOnly: Boolean = false,
    secondsIfRequired: Boolean = false,
) =
    millisecondsToDurationStringInternal(
        resString = { id -> context.getString(id) },
        prefix = prefix,
        suffix = suffix,
        short = short,
        firstUnitOnly = firstUnitOnly,
        secondsIfRequired = secondsIfRequired,
    )

internal fun Long.millisecondsToDurationStringInternal(
    resString: (Int) -> String,
    prefix: String = "",
    suffix: String = "",
    short: Boolean = false,
    firstUnitOnly: Boolean = false,
    secondsIfRequired: Boolean = false,
): String {
    val duration = this / 1000
    if (duration == 0L) {
        return ""
    }

    val (hr, hrs, min, mins, sec, secs) = if (short) {
        listOf(
            resString(R.string.h),
            resString(R.string.h),
            resString(R.string.m),
            resString(R.string.m),
            resString(R.string.s),
            resString(R.string.s),
        )
    } else {
        listOf(
            resString(R.string.hr),
            resString(R.string.hrs),
            resString(R.string.min),
            resString(R.string.mins),
            resString(R.string.sec),
            resString(R.string.secs),
        )
    }

    val hours = duration / 3600L
    val minutes = (duration % 3600L) / 60L
    val seconds = if (hours > 0) {
        (duration % (3600L * 60L))
    } else {
        (duration % 60L)
    }

    val space = if (short) "" else " "
    val secUnit = if (seconds == 1L) sec else secs
    val minUnit = if (minutes == 1L) min else mins
    val hrUnit = if (hours == 1L) hr else hrs

    val hoursString = if (hours > 0) "$hours$space$hrUnit" else ""
    val minutesString = when {
        (!firstUnitOnly || hoursString.isEmpty()) && minutes > 0L -> "$minutes$space$minUnit"
        else -> ""
    }
    val secondsString = when {
        secondsIfRequired && (hours > 0L || minutes > 0L) -> ""
        (!firstUnitOnly || (hoursString.isEmpty() && minutesString.isEmpty())) && seconds > 0L && seconds < 60L -> "$seconds$space$secUnit"
        else -> ""
    }
    return listOf(prefix, hoursString, minutesString, secondsString, suffix)
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .trim()
}

// TODO(2) combine with millisecondsToDurationString
fun Long.millisecondsToDuration(): String {
    val negative = this < 0L
    val value = abs(this)
    val minutes = (value / 1000 / 60)
    val seconds = ((value / 1000) % 60)
    return String.format("%s%02d:%02d", if (negative) "-" else "", minutes, seconds)
}

fun Long.toFormattedDateString(pattern: String = YEAR_MONTH_DATE_HOURS_FORMAT_PATTERN, locale: Locale = Locale.getDefault()): String {
    val sdf = SimpleDateFormat(pattern, locale)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date(this))
}

fun Long?.toSemiMonthDateFormat(): String {
    return this?.let { milliSecondsTime ->
        val date = Date(milliSecondsTime.secondsToMilliseconds())
        val df = SimpleDateFormat(SEMI_MONTH_DATE_FORMAT_PATTERN, Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC")
        return df.format(date)
    } ?: ""
}
