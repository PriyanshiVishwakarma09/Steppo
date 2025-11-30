package com.example.seppo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {

    //only works one
//    Did I already insert today's steps?
//    Load stored steps after boot
    @Query("SELECT * FROM daily_steps WHERE date = :date LIMIT 1")
    suspend fun getByDateOnce(date: String): DailyStep?


//    Observes only the steps column for that date.
//    Returns a Flow<Int?>, meaning:
//    It emits updates automatically whenever the database changes.
//    If the row is updated (new steps), the Flow emits updated value.
    @Query("SELECT steps FROM daily_steps WHERE date = :date LIMIT 1")
    fun getStepsFlow(date: String): Flow<Int?>

    //insert the daily steps
//    Inserts a new row into the table.
//    If the date already exists, it replaces the item.
//    This is important because each date should have only one row.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyStep: DailyStep)


//    Fetches all rows where date is greater than or equal to fromDate.
//    Returns them sorted by date (ascending).
    @Query("SELECT * FROM daily_steps WHERE date >= :fromDate ORDER BY date ASC")
    suspend fun getFromDate(fromDate: String): List<DailyStep>

}