@file:Suppress("unused")

package com.sandymist.android.common.utilities

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent, // Remove background when focused
    unfocusedContainerColor = Color.Transparent, // Remove background when unfocused
    disabledContainerColor = Color.Transparent, // Remove background when disabled

    focusedTextColor = Color.Black,     // Text color when focused
    unfocusedTextColor = Color.Gray,    // Text color when unfocused
    disabledTextColor = Color.LightGray, // Text color when disabled

    cursorColor = Color.Blue,            // Cursor color
    focusedIndicatorColor = Color.Blue,  // Underline color when focused
    unfocusedIndicatorColor = Color.Gray // Underline color when unfocused
)

@Composable
fun getBasicTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
)
