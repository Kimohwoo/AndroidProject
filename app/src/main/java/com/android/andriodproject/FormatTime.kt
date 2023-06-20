package com.android.andriodproject

import android.icu.text.SimpleDateFormat
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

fun getTime(formating: String): String {
    val now = Date(System.currentTimeMillis())
    val formatTime = SimpleDateFormat(formating)
    val getformating = formatTime.format(now)

    return getformating
}
