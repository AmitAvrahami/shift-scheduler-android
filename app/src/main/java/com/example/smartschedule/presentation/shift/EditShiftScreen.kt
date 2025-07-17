package com.example.smartschedule.presentation.shift

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType
import java.time.LocalDateTime

@Composable
fun EditShiftScreen(
    shift: Shift,
    onSaveClick: (Shift) -> Unit = {},
    onBackClick: () -> Unit = {}
){

}


@Preview(showBackground = true)
@Composable
fun EditShiftScreenPreview() {
    val sampleShift = Shift(
        id = "1",
        startTime = LocalDateTime.of(2024, 1, 15, 6, 45),
        endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
        shiftType = ShiftType.MORNING,
        assignedEmployeeId = "emp1"
    )

    EditShiftScreen(shift = sampleShift)
}
