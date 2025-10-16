package com.smartschedule.presentation.constraints

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartschedule.domain.use_cases.constraint.AddConstraintUseCase
import com.smartschedule.domain.use_cases.constraint.AddRecurringConstraintUseCase
import com.smartschedule.domain.use_cases.constraint.DeleteConstraintUseCase
import com.smartschedule.domain.use_cases.constraint.GetConstraintsForWeekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConstraintsViewModel @Inject constructor(
    private val getConstraintsForWeekUseCase: GetConstraintsForWeekUseCase,
    private val addConstraintUseCase: AddConstraintUseCase,
    private val deleteConstraintUseCase: DeleteConstraintUseCase,
    private val addRecurringConstraintUseCase: AddRecurringConstraintUseCase
) : ViewModel() {

    fun getConstraintForWeek() = viewModelScope.launch {  }
    fun addConstraint() = viewModelScope.launch {  }
    fun deleteConstraint() = viewModelScope.launch {  }
    fun addRecurringConstraint() = viewModelScope.launch {  }

    /*
    שליפת אילוצים לפי שבוע (GetConstraintsForWeekUseCase)

הוספת אילוץ (AddConstraintUseCase)

מחיקת אילוץ (DeleteConstraintUseCase)

ניהול אילוצים חוזרים (AddRecurringConstraintUseCase)

תצוגה יומית / שבועית של אילוצים
     */
}