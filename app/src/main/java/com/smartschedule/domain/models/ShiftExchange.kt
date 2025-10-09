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
    init {
        require(id >= 0L) { "id must be >= 0" }
        require(fromEmployeeId > 0L) { "fromEmployeeId must be > 0" }
        require(shiftId > 0L) { "shiftId must be > 0" }

        if (toEmployeeId != null) {
            require(toEmployeeId > 0L) { "toEmployeeId must be > 0 when provided" }
            require(toEmployeeId != fromEmployeeId) { "toEmployeeId must differ from fromEmployeeId" }
        }

        if (resolvedAt != null) {
            require(!resolvedAt.isBefore(requestedAt)) { "resolvedAt must be >= requestedAt" }
        }

        when (status) {
            ExchangeStatus.PENDING -> {
                require(resolvedAt == null) { "PENDING must not have resolvedAt" }
            }
            ExchangeStatus.APPROVED -> {
                require(resolvedAt != null) { "APPROVED must have resolvedAt" }
                require(toEmployeeId != null) { "APPROVED requires a target toEmployeeId" }
            }
            ExchangeStatus.REJECTED,
            ExchangeStatus.CANCELLED -> {
                require(resolvedAt != null) { "REJECTED/CANCELLED must have resolvedAt" }
            }
        }

        require(managerNote == null || managerNote.length <= 500) {
            "managerNote must be at most 500 characters"
        }
    }
}