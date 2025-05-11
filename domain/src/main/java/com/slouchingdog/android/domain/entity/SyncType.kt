package com.slouchingdog.android.domain.entity

const val NOT_NEED_SYNC = "NOT_NEED"
const val ADD_SYNC = "ADD"
const val UPDATE_SYNC = "UPDATE"
const val DELETE_SYNC = "DELETE"

enum class SyncType(syncName: String) {
    NOT_NEED(NOT_NEED_SYNC),
    ADD(ADD_SYNC),
    UPDATE(UPDATE_SYNC),
    DELETE(DELETE_SYNC)
}