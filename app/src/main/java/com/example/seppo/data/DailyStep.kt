package com.example.seppo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class DailyStep(
    @PrimaryKey val date : String,
    val steps: Int
)
//data class must provide all the properties inside the primary constructor not inside the class body