package com.smartschedule.domain.models

import java.time.LocalDateTime

data class ShiftExchange(
    val id: Long,
    val fromEmployeeId: Long,
    val toEmployeeId: Long?,
    val shiftId: Long,
    val status: ExchangeStatus,
    val requestedAt: LocalDateTime,
    val resolvedAt: LocalDateTime? = null,
    val managerNote: String?
){

}