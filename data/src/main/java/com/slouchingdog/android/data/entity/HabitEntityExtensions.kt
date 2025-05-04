package com.slouchingdog.android.data.entity

import com.slouchingdog.android.domain.entity.SyncType
import com.slouchingdog.android.domain.entity.HabitEntity
import java.util.UUID

typealias HabitsDBOList = List<HabitDBO>
typealias HabitsDTOList = List<HabitDTO>

fun HabitDBO.mapToDomainModel() = HabitEntity(
    id,
    title,
    description,
    priority,
    type,
    periodicityTimes,
    periodicityDays,
    color,
    date,
    doneDates,
    syncType
)

fun HabitDBO.mapToDTO() = HabitDTO(
    id,
    title,
    description,
    priority,
    type,
    periodicityTimes,
    periodicityDays,
    color,
    date,
    doneDates
)

fun HabitDTO.mapToDBO() = HabitDBO(
    id ?: UUID.randomUUID().toString(),
    title,
    description,
    priority,
    type,
    periodicityTimes,
    periodicityDays,
    color,
    date,
    doneDates,
    SyncType.NOT_NEED
)

fun HabitEntity.mapToDBO() = HabitDBO(
    id ?: UUID.randomUUID().toString(),
    title,
    description,
    priority,
    type,
    periodicityTimes,
    periodicityDays,
    color,
    date,
    doneDates,
    syncType
)

fun HabitEntity.mapToDTO() = HabitDTO(
    id,
    title,
    description,
    priority,
    type,
    periodicityTimes,
    periodicityDays,
    color,
    date,
    doneDates
)

fun HabitsDBOList.mapToDomainModels() = map { it.mapToDomainModel() }
fun HabitsDTOList.mapToDBOList() = map { it.mapToDBO() }



