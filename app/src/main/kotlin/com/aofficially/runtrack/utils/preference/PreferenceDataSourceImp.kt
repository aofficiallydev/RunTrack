package com.aofficially.runtrack.utils.preference

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceDataSourceImp @Inject constructor(@ApplicationContext val context: Context) :
    PreferenceDataSource {
    companion object {
        private const val KEY_PREFERENCE = "runTrackPreference"
        const val KEY_RACE_ID = "key_raceID"
        const val KEY_STATION_ID = "key_stationID"
        const val KEY_STATION_NAME = "key_stationName"
    }

    private fun sharedPreferences() =
        context.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE)

    override fun putLong(key: String, value: Long) {
    }

    override fun putInt(key: String, value: Int) {
        sharedPreferences().edit()
            .putInt(key, value)
            .apply()
    }

    override fun getLong(key: String, default: Long): Long =
        sharedPreferences().getLong(key, default)

    override fun getInt(key: String, default: Int): Int = sharedPreferences().getInt(key, default)

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences().edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences().getString(key, defaultValue) ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        val editor = sharedPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences().getBoolean(key, defaultValue)
    }

    override fun <T> putList(key: String, list: List<T>) {
        val jsonString = Gson().toJson(list)
        sharedPreferences().edit()
            .putString(key, jsonString)
            .apply()
    }

    override fun <T> getList(key: String): List<T> {
        val jsonString = sharedPreferences().getString(key, "[]")
        return Gson().fromJson(jsonString, object : TypeToken<List<T>>() {}.type)
    }

    override fun remove(key: String) {
        sharedPreferences().edit()
            .remove(key)
            .apply()
    }

    override fun removeAllKeys() {
        val editor = sharedPreferences().edit()
        sharedPreferences().all.keys.forEach { editor.remove(it) }
        editor.apply()
    }
}