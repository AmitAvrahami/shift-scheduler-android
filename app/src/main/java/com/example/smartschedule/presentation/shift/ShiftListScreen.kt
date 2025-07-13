package com.example.smartschedule.presentation.shift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType
import java.time.LocalDateTime

@Composable
fun ShiftListScreen(modifier: Modifier = Modifier) {

    val shifts = listOf(
        Shift(
            id = "1",
            startTime = LocalDateTime.of(2024, 1, 15, 6, 45),
            endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
            shiftType = ShiftType.MORNING,
            assignedEmployeeId = "emp1"
        ),
        Shift(
            id = "2",
            startTime = LocalDateTime.of(2024, 1, 15, 14, 45),
            endTime = LocalDateTime.of(2024, 1, 15, 22, 45),
            shiftType = ShiftType.AFTERNOON,
            assignedEmployeeId = null
        ),
        Shift(
            id = "3",
            startTime = LocalDateTime.of(2024, 1, 15, 22, 45),
            endTime = LocalDateTime.of(2024, 1, 16, 6, 45),
            shiftType = ShiftType.NIGHT,
            assignedEmployeeId = "emp2"
        )
    )

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(shifts){shift ->
            ShiftCard(shift)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShiftListScreenPreview() {
    ShiftListScreen()
}