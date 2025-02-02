@file:Suppress("unused")

package com.sandymist.android.common.utilities

import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

@Composable
fun String.ifNotEmpty(handler: @Composable (String) -> Unit) {
    if (isNotEmpty()) {
        handler(this)
    }
}

fun String.titleCase() = replaceFirstChar { it.uppercase() }

fun String.formatJsonString(): String {
    val json = Json { prettyPrint = true }
    val jsonElement = json.parseToJsonElement(this)
    return json.encodeToString(JsonElement.serializer(), jsonElement)
}

fun String?.isContentTypeJson(): Boolean {
    return this?.startsWith("application/json") ?: false
}

fun String?.isPrintable(): Boolean {
    val printableTypes = listOf(
        "text/",         // All text types (e.g., text/plain, text/html)
        "application/json",
        "application/xml",
        "application/javascript",
        "application/x-www-form-urlencoded"
    )

    return printableTypes.any { this?.startsWith(it) == true }
}

fun String.prettyPrintJson(): String {
    return try {
        val jsonParser = Json { prettyPrint = true }
        val jsonElement = Json.parseToJsonElement(this)
        jsonParser.encodeToString(JsonObject.serializer(), jsonElement.jsonObject)
    } catch (e: Exception) {
        "Invalid JSON: $this - ${e.message}"
    }
}

fun Boolean.toInt() = if (this) 1 else 0
