package com.slouchingdog.android.data2.entity

import com.slouchingdog.android.domain2.HabitEntity
import java.util.UUID

typealias HabitsDBOList = List<HabitDBO>
typealias HabitsDomainModelList = List<HabitEntity>
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
    doneDates
)

fun HabitDTO.mapToDomainModel() = HabitEntity(
    UUID.randomUUID().toString(),
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

fun HabitEntity.mapToDBO() = HabitDBO(
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

fun HabitEntity.mapToDTO() = HabitDTO(
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
fun HabitsDomainModelList.mapToDBOList() = map { it.mapToDBO() }
fun HabitsDomainModelList.mapToDTOList() = map { it.mapToDTO() }


