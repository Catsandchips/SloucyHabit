package com.slouchingdog.android.data2.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitTypeConverters {
    @TypeConverter
    fun fromIntArray(mutableList: MutableList<Long>?): String? {
        return mutableList?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toIntArray(json: String?): MutableList<Long>? {
        return json?.let {
            val type = object : TypeToken<MutableList<Long>>() {}.type
            Gson().fromJson(json, type)
        }
    }
}