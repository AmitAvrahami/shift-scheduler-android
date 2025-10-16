package com.smartschedule.presentation.constraints

import androidx.lifecycle.ViewModel
import com.smartschedule.domain.use_cases.constraint.AddConstraintUseCase
import com.smartschedule.domain.use_cases.constraint.AddRecurringConstraintUseCase
import com.smartschedule.domain.use_cases.constraint.DeleteConstraintUseCase
import com.smartschedule.domain.use_cases.constraint.GetConstraintsForWeekUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConstraintViewModel @Inject constructor(
    private val getConstraintsForWeekUseCase: GetConstraintsForWeekUseCase,
    private val addConstraintUseCase: AddConstraintUseCase,
    private val addRecurringConstraintUseCase: AddRecurringConstraintUseCase,
    private val deleteConstraintUseCase: DeleteConstraintUseCase,
) : ViewModel() {
    // TODO: Implement the ViewModel
}
