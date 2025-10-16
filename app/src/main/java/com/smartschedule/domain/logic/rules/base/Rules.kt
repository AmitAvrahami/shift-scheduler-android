package com.smartschedule.domain.logic.rules.base

import com.smartschedule.domain.logic.rules.context.ValidationContext
import com.smartschedule.domain.logic.rules.result.ValidationResult

interface RulesHandler{
    fun handle(context: ValidationContext): ValidationResult
    fun setNext(handler: RulesHandler): RulesHandler
}

abstract class BaseRulesHandler(private var nextHandler: RulesHandler? = null) : RulesHandler {
    override fun setNext(handler: RulesHandler): RulesHandler {
        this.nextHandler = handler
        return handler
    }
    protected abstract fun validate(context: ValidationContext, result: ValidationResult)
    override fun handle(context: ValidationContext): ValidationResult {
        val result = ValidationResult()
        validate(context, result)

        return if (result.success) {
            nextHandler?.handle(context) ?: result
        } else {
            result
        }
    }
}
abstract class RulesContainer : BaseRulesHandler() {

    protected val rules = mutableListOf<RulesHandler>()

    fun addRule(rule: RulesHandler): RulesContainer {
        rules.add(rule)
        return this
    }

    override fun validate(context: ValidationContext, result: ValidationResult) {
        for (rule in rules) {
            val ruleResult = rule.handle(context)
            result.errors.addAll(ruleResult.errors)
            result.warnings.addAll(ruleResult.warnings)

            if (!ruleResult.success) {
                result.copy(false)
                break
            }
        }
    }
}

//class PreventingSequenceNights : BaseRulesHandler() {
//
//}


//class PermantEmployeesRule : BaseRulesHandler() {
//
//}


