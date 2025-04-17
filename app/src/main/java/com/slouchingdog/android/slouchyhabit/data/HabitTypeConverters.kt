package com.slouchingdog.android.slouchyhabit.data

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson

class HabitTypeConverters {
    @TypeConverter
    fun fromIntArray(array: IntArray?): String?{
        return array?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun fromJsonString(json: String?): IntArray?{
        return json?.let {
            val type = object : TypeToken<IntArray>() {}.type
            Gson().fromJson(json, type)
        }
    }
}