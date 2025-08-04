//package com.example.smartschedule.presentation.employee.add_employee_screen.states
//
//import com.example.smartschedule.domain.validation.ValidationResult
//import java.time.LocalDate
//import java.time.LocalTime
//import java.time.format.DateTimeFormatter
//
//// 🛠️ TypeMapper Implementations
//
//class StringTypeMapper : TypeMapper<String> {
//    override fun parseValue(rawValue: String): Result<String> = Result.success(rawValue)
//    override fun formatValue(value: String): String = value
//    override fun getValidationRules(): List<ValidationRule<String>> = listOf(
//        object : ValidationRule<String> {
//            override fun validate(value: String): ValidationResult {
//                return if (value.isNotBlank()) ValidationResult.Success
//                else ValidationResult.Error("שדה חובה")
//            }
//            override val errorMessage = "שדה חובה"
//        }
//    )
//}
//
//class NumberTypeMapper : TypeMapper<Int> {
//    override fun parseValue(rawValue: String): Result<Int> {
//        return rawValue.toIntOrNull()?.let { Result.success(it) }
//            ?: Result.failure(IllegalArgumentException("מספר לא תקין"))
//    }
//
//    override fun formatValue(value: Int): String = value.toString()
//
//    override fun getValidationRules(): List<ValidationRule<Int>> = listOf(
//        object : ValidationRule<Int> {
//            override fun validate(value: Int): ValidationResult {
//                return when {
//                    value < 1 -> ValidationResult.Error("חייב להיות לפחות 1")
//                    value > 7 -> ValidationResult.Error("לא יותר מ-7 משמרות")
//                    else -> ValidationResult.Success
//                }
//            }
//            override val errorMessage = "מספר משמרות לא תקין"
//        }
//    )
//}
//
//class EmailTypeMapper : TypeMapper<String> {
//    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
//
//    override fun parseValue(rawValue: String): Result<String> {
//        return if (emailRegex.matches(rawValue)) Result.success(rawValue)
//        else Result.failure(IllegalArgumentException("פורמט אימייל לא תקין"))
//    }
//
//    override fun formatValue(value: String): String = value.lowercase()
//
//    override fun getValidationRules(): List<ValidationRule<String>> = listOf(
//        object : ValidationRule<String> {
//            override fun validate(value: String): ValidationResult {
//                return if (emailRegex.matches(value)) ValidationResult.Success
//                else ValidationResult.Error("פורמט אימייל לא תקין")
//            }
//            override val errorMessage = "אימייל לא תקין"
//        }
//    )
//}
//
//class PasswordTypeMapper : TypeMapper<String> {
//    private val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{6,}$".toRegex()
//
//    override fun parseValue(rawValue: String): Result<String> {
//        return if (passwordRegex.matches(rawValue)) Result.success(rawValue)
//        else Result.failure(IllegalArgumentException("סיסמה לא עומדת בדרישות"))
//    }
//
//    override fun formatValue(value: String): String = "*".repeat(value.length)
//
//    override fun getValidationRules(): List<ValidationRule<String>> = listOf(
//        object : ValidationRule<String> {
//            override fun validate(value: String): ValidationResult {
//                return when {
//                    value.length < 6 -> ValidationResult.Error("לפחות 6 תווים")
//                    !passwordRegex.matches(value) -> ValidationResult.Error("צריך אות גדולה, קטנה, מספר ותו מיוחד")
//                    else -> ValidationResult.Success
//                }
//            }
//            override val errorMessage = "סיסמה חלשה"
//        }
//    )
//}
//
//class SelectTypeMapper : TypeMapper<String> {
//    override fun parseValue(rawValue: String): Result<String> = Result.success(rawValue)
//    override fun formatValue(value: String): String = value
//
//    override fun getValidationRules(): List<ValidationRule<String>> = listOf(
//        object : ValidationRule<String> {
//            override fun validate(value: String): ValidationResult {
//                return if (value.isNotBlank()) ValidationResult.Success
//                else ValidationResult.Error("יש לבחור ערך")
//            }
//            override val errorMessage = "לא נבחר ערך"
//        }
//    )
//}
//
//class BooleanTypeMapper : TypeMapper<Boolean> {
//    override fun parseValue(rawValue: String): Result<Boolean> {
//        return when (rawValue.lowercase()) {
//            "true", "1", "yes", "כן" -> Result.success(true)
//            "false", "0", "no", "לא" -> Result.success(false)
//            else -> Result.failure(IllegalArgumentException("ערך בוליאני לא תקין"))
//        }
//    }
//
//    override fun formatValue(value: Boolean): String = if (value) "כן" else "לא"
//
//    override fun getValidationRules(): List<ValidationRule<Boolean>> = emptyList()
//}
//
//class DateTypeMapper : TypeMapper<LocalDate> {
//    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//
//    override fun parseValue(rawValue: String): Result<LocalDate> {
//        return try {
//            Result.success(LocalDate.parse(rawValue, formatter))
//        } catch (e: Exception) {
//            Result.failure(IllegalArgumentException("פורמט תאריך לא תקין (dd/MM/yyyy)"))
//        }
//    }
//
//    override fun formatValue(value: LocalDate): String = value.format(formatter)
//
//    override fun getValidationRules(): List<ValidationRule<LocalDate>> = listOf(
//        object : ValidationRule<LocalDate> {
//            override fun validate(value: LocalDate): ValidationResult {
//                val now = LocalDate.now()
//                return when {
//                    value.isAfter(now.plusYears(1)) -> ValidationResult.Error("תאריך רחוק מדי בעתיד")
//                    value.isBefore(now.minusYears(100)) -> ValidationResult.Error("תאריך רחוק מדי בעבר")
//                    else -> ValidationResult.Success
//                }
//            }
//            override val errorMessage = "תאריך לא סביר"
//        }
//    )
//}
//
//class TimeTypeMapper : TypeMapper<LocalTime> {
//    private val formatter = DateTimeFormatter.ofPattern("HH:mm")
//
//    override fun parseValue(rawValue: String): Result<LocalTime> {
//        return try {
//            Result.success(LocalTime.parse(rawValue, formatter))
//        } catch (e: Exception) {
//            Result.failure(IllegalArgumentException("פורמט שעה לא תקין (HH:mm)"))
//        }
//    }
//
//    override fun formatValue(value: LocalTime): String = value.format(formatter)
//
//    override fun getValidationRules(): List<ValidationRule<LocalTime>> = emptyList()
//}