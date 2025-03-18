package com.oltrysifp.matule

fun cutString(str: String, maxChar: Int = 60): String {
    return if (str.length > maxChar) {
        str.substring(0, maxChar) + " ..."
    } else {
        str
    }
}