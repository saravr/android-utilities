@file:Suppress("unused")

package com.sandymist.android.common.utilities

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

fun <T> noneNull(vararg elements: T) = elements.any { it == null }.not()

operator fun <T> List<T>.component6(): T = get(5)

fun Float.roundTo(n: Int): Float {
    return "%.${n}f".format(this).toFloat()
}

fun Double.roundTo(n: Int): Double {
    return "%.${n}f".format(this).toDouble()
}

fun String.titleCase() = replaceFirstChar { it.uppercase() }

fun Boolean.toInt() = if (this) 1 else 0

fun String.formatJsonString(): String {
    val json = Json { prettyPrint = true }
    val jsonElement = json.parseToJsonElement(this)
    return json.encodeToString(JsonElement.serializer(), jsonElement)
}
