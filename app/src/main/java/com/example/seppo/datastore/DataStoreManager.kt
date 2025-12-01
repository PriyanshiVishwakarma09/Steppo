package com.example.seppo.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "seppo_prefs")
class DataStoreManager(private val context: Context) {
    companion object {
        private val KEY_LAST_SENSOR_TOTAL = longPreferencesKey("last_sensor_total")
        private val KEY_LAST_SAVED_DATE = stringPreferencesKey("last_saved_date")
    }

    suspend fun getLastSensorTotal(): Long? {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_LAST_SENSOR_TOTAL]
    }

    suspend fun setLastSensorTotal(value: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_SENSOR_TOTAL] = value
        }
    }

    suspend fun getLastSavedDate(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_LAST_SAVED_DATE]
    }

    suspend fun setLastSavedDate(date: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_SAVED_DATE] = date
        }
    }
}
