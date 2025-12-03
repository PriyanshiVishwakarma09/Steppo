package com.example.seppo.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun todayKey() :Long{
        val sdf = SimpleDateFormat("yyyyMMdd" , Locale.getDefault())
        return sdf.format(Date()).toLong()
    }
}