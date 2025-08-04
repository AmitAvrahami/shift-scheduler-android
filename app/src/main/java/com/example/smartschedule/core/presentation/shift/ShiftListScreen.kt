package com.example.smartschedule.core.presentation.shift

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
import com.example.smartschedule.core.domain.models.Shift
import com.example.smartschedule.core.domain.models.ShiftType
import com.example.smartschedule.core.domain.models.User
import com.example.smartschedule.core.domain.models.UserType
import java.time.LocalDateTime

@Composable
fun ShiftListScreen(
    modifier: Modifier = Modifier,
    user: User,
    shifts: List<Shift>,
    onAddShiftClick: () -> Unit = {},
    onEditShiftClick: (Shift) -> Unit = {},
    onDeleteShiftClick: (Shift) -> Unit = {}
) {


    Scaffold(
        floatingActionButton = {
            if(user.type.canManageSchedules()) {
                FloatingActionButton(
                    onClick = onAddShiftClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Shift"
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(shifts){shift ->
                ShiftCard(
                    shift,
                    onEditClick = onEditShiftClick,
                    onDeleteClick = onDeleteShiftClick
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun ShiftListScreenPreview() {

    val user = User(
        id = "1",
        name = "שאול ביטון",
        email = "manager@example.com",
        type = UserType.MANAGER,
    )

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
        user = user,
        shifts = sampleShifts,
        onAddShiftClick = { },
        onEditShiftClick = { }
    )
}