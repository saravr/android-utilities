package com.sandymist.android.common.utilities

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T?.toJsonString(): String {
    val prettyJson = Json {
        prettyPrint = true
    }

    return prettyJson.encodeToString(this)
}
