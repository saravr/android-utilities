package com.sandymist.android.common.utilities

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class StringList(private val items: List<String>) {
    fun asList(): List<String> = items

    fun encode(): String = Json.encodeToString(this)

    fun contains(value: String) = items.contains(value)

    fun minus(value: String): StringList {
        val updated = items.minus(value)
        return StringList(updated)
    }

    fun add(value: String, max: Int): StringList {
        val updatedItems = if (items.size < max) {
            items
        } else {
            items.take(max - 1)
        }
        return StringList(listOf(value).plus(updatedItems))
    }

    companion object {
        private val EMPTY = StringList(emptyList())

        fun decode(value: String) = if (value.isNotBlank()) Json.decodeFromString<StringList>(value) else EMPTY
    }
}
