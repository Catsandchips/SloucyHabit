package com.slouchingdog.android.slouchyhabit

fun setDeclension(count: Int, one: String, two: String, few: String): String {
    val stringTypes = listOf(few, one, two, two, two, few)
    return stringTypes[if (count % 100 > 10 && count % 100 < 20) 0 else (count % 10).coerceAtMost(5)]
}