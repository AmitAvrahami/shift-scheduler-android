package com.example.smartschedule.presentation.shift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartschedule.domain.models.Shift
import com.example.smartschedule.domain.models.ShiftType
import compose.icons.TablerIcons
import compose.icons.tablericons.CalendarEvent
import compose.icons.tablericons.Clock
import compose.icons.tablericons.User
import java.time.LocalDateTime

@Composable
fun ShiftCard(
    shift: Shift,
    onEditClick: (Shift) -> Unit = {},
    ) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = TablerIcons.CalendarEvent,
                        contentDescription = "Calender Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(shift.shiftType.displayName)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = TablerIcons.Clock,
                        contentDescription = "Clock Icon",
                        modifier = Modifier.size(20.dp)

                    )
                    Text(
                        "${shift.startTime.hour}:${
                            shift.startTime.minute.toString().padStart(2, '0')
                        } - ${shift.endTime.hour}:${
                            shift.endTime.minute.toString().padStart(2, '0')
                        }"
                    )

                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = TablerIcons.User,
                        contentDescription = "Employee Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(shift.assignedEmployeeId ?: "לא משובץ")
                }
            }
            IconButton(
                onClick = { onEditClick(shift) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Shift"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShiftCardPreview() {
    val sampleShift = Shift(
        id = "1",
        startTime = LocalDateTime.of(2024, 1, 15, 6, 45),
        endTime = LocalDateTime.of(2024, 1, 15, 14, 45),
        shiftType = ShiftType.MORNING,
        assignedEmployeeId = "emp1"
    )

    ShiftCard(
        shift = sampleShift,
        onEditClick = {  }
        )
}