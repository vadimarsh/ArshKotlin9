package com.example.arshkotlin9

import java.util.regex.Pattern

fun isValid(password: String) =
    Pattern.compile("(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()

