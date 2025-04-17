package com.slouchingdog.android.slouchyhabit.data.repository

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.slouchingdog.android.slouchyhabit.data.HabitType
import java.lang.reflect.Type

class EnumTypeAdapter : JsonSerializer<HabitType>, JsonDeserializer<HabitType> {
    override fun serialize(
        src: HabitType?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return Gson().toJsonTree(src?.ordinal)
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HabitType? {
        val habitTypeOrdinal = json?.asInt
        if (habitTypeOrdinal != null) {
            return HabitType.entries[habitTypeOrdinal]
        } else return null
    }
}