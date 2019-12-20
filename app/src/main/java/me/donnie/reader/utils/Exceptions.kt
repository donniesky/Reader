package me.donnie.reader.utils

import android.content.Intent

fun unsupported(
    errorMessage: String? = null
): Nothing = throw UnsupportedOperationException(errorMessage)

fun unsupportedAction(intent: Intent): Nothing = unsupported("Unsupported action: ${intent.action}")

fun unexpectedValue(value: Any?): Nothing = throw IllegalStateException("Unexpected value: $value")

fun illegalArgs(errorMessage: String? = null): Nothing = throw IllegalArgumentException(errorMessage)

fun illegalArgs(argument: Any?): Nothing = throw IllegalArgumentException("Illegal argument: $argument")