package com.example.seppo.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun todayKey(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }
}
