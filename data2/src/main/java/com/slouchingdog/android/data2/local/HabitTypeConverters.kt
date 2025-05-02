package com.slouchingdog.android.data2.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitTypeConverters {
    @TypeConverter
    fun fromIntArray(array: IntArray?): String? {
        return array?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toIntArray(json: String?): IntArray? {
        return json?.let {
            val type = object : TypeToken<IntArray>() {}.type
            Gson().fromJson(json, type)
        }
    }
}