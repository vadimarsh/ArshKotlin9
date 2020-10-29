package com.example.arshkotlin9

import java.util.regex.Pattern

fun isValid(password: String) =
    Pattern.compile("(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()

fun verboseTime(time: Int): String {
    val secs = System.currentTimeMillis() / 1000 - time + 1
    val mins: Long = 60
    val hours = mins * 60
    val days = hours * 24
    val weeks = days * 7
    val months = weeks * 4
    val years = months * 12

    val result = when (secs) {
        in 0..59 -> "менее минуты назад"
        in mins..hours -> {
            "${secs / mins} минут назад"
        }
        in hours..days -> {
            "${secs / hours} часов назад"
        }
        in days..weeks -> {
            "${secs / (days)} дней назад"
        }
        in weeks..months -> {
            "${secs / (weeks)} недель назад"
        }
        in months..years -> {
            "${secs / (months)} месяцев назад"
        }
        else -> "больше года назад"
    }
    return result
}