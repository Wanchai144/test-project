package com.example.my_testappkg.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Preferences @Inject constructor(@ApplicationContext private var context: Context) {

    companion object {
        const val FILENAME = "user_preference"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
        const val LocationDefault = "LocationDefault"

    }

    fun saveLocationDefault(lat: String) {
        saveString(LocationDefault, lat)
    }

    fun getLocationDefault(): String? {
        return getString(LocationDefault)
    }


    fun saveLat(lat: String) {
        saveString(LATITUDE, lat)
    }

    fun getLat(): String? {
        return getString(LATITUDE)
    }

    fun getLng(): String? {
        return getString(LONGITUDE)
    }

    fun saveLng(lng: String) {
        saveString(LONGITUDE, lng)
    }

    internal fun clear() {
        getSharedPreferences().edit().clear().apply()
    }

    private fun saveBoolean(key: String, value: Boolean) {
        val editor = getSharedPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun saveObject(key: String, classObject: Any){
        val editor = getSharedPreferences().edit()
        val gSon = Gson()
        val json = gSon.toJson(classObject)
        editor.putString(key, json)
        editor.apply()
    }

    private fun saveString(key: String, value: String) {
        val editor = getSharedPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getString(key: String): String? {
        return getSharedPreferences().getString(key, null)
    }


    private fun getBoolean(key: String): Boolean {
        return getSharedPreferences().getBoolean(key, false)
    }

    fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    }
}