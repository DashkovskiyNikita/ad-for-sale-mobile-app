package com.dashkovskiy.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun dateTimeDisplay(date: String): String {
    val dateTime = LocalDateTime.parse(date)
    val formatter = DateTimeFormatter.ofPattern("dd.mm.yy HH:mm")
    return formatter.format(dateTime)
}