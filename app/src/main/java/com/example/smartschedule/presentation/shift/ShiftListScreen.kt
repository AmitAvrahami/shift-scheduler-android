package com.example.smartschedule.presentation.shift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType
import java.time.LocalDateTime

@Composable
fun ShiftListScreen(
    shifts: List<Shift>,
    modifier: Modifier = Modifier,
    onAddShiftClick: () -> Unit = {},
    ) {


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddShiftClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Shift"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(shifts){shift ->
                ShiftCard(shift)
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun ShiftListScreenPreview() {
    val sampleShifts = listOf(
        Shift(
            id = "1",
            startTime = LocalDateTime.of(2024, 1, 15, 6, 45),
            endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
            shiftType = ShiftType.MORNING,
            assignedEmployeeId = "emp1"
        )
    )

    ShiftListScreen(
        shifts = sampleShifts,
        onAddShiftClick = { }
    )
}