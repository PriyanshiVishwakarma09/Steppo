package com.example.seppo.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "step_prefs")

    private val LAST_SENSOR_TOTAL = longPreferencesKey("last_sensor_total")
    private val LAST_SAVED_DATE = stringPreferencesKey("last_saved_date")

    suspend fun getLastSensorTotal(): Long? =
        context.dataStore.data.first()[LAST_SENSOR_TOTAL]

    suspend fun setLastSensorTotal(value: Long) {
        context.dataStore.edit { it[LAST_SENSOR_TOTAL] = value }
    }

    suspend fun getLastSavedDate(): String? =
        context.dataStore.data.first()[LAST_SAVED_DATE]

    suspend fun setLastSavedDate(value: String) {
        context.dataStore.edit { it[LAST_SAVED_DATE] = value }
    }
}
