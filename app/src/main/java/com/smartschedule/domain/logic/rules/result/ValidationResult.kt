package com.smartschedule.domain.logic.rules.result

data class ValidationResult(
    var success: Boolean = true,
    val errors: MutableList<String> = mutableListOf(),
    val warnings: MutableList<String> = mutableListOf()
)