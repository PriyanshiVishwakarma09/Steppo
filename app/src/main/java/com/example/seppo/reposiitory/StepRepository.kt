package com.example.seppo.reposiitory

import com.example.seppo.data.DailyStep
import com.example.seppo.data.StepDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StepRepository @Inject constructor(
    private val db: StepDatabase
) {
    private val dao = db.stepDao()
    private val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private fun todayKey(): String = LocalDate.now().format(fmt)

    suspend fun incrementSteps(by: Int) {
        val key = todayKey()
        val current = dao.getByDateOnce(key)
        if (current == null) {
            dao.insert(DailyStep(key, by))
        } else {
            dao.insert(current.copy(steps = current.steps + by))
        }
    }

    fun getTodayStepsFlow(): Flow<Int> {
        val key = todayKey()
        return dao.getStepsFlow(key).map { it ?: 0 }
    }

    suspend fun getLast7Days(): List<DailyStep> {
        val today = LocalDate.now()
        val fromDate = today.minusDays(6).format(fmt)
        return dao.getFromDate(fromDate)
    }

    suspend fun resetToday() {
        val key = todayKey()
        dao.insert(DailyStep(key, 0))
    }
}