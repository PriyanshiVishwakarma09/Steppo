package com.example.seppo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DailyStep::class] , version = 1 , exportSchema = false)
abstract class StepDatabase : RoomDatabase() {
    abstract fun stepDao(): StepDao
}