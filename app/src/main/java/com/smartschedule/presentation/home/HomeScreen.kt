package com.smartschedule.presentation.home


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { HomeTopBar() },
        bottomBar = { HomeBottomBar() }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item { ScheduleSummaryCard() }
            item { WeekNavigationCard() }
            item { ScheduleProgressCard() }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = { Text("שיבוץ חכם", style = MaterialTheme.typography.titleMedium) },
        actions = {
            IconButton(onClick = { /* TODO: notifications */ }) {
                BadgedBox(badge = { Badge { Text("3") } }) {
                    Icon(Icons.Default.Notifications, contentDescription = "התראות")
                }
            }
            IconButton(onClick = { /* TODO: open menu */ }) {
                Icon(Icons.Default.Menu, contentDescription = "תפריט")
            }
        },
        navigationIcon = {
            Icon(Icons.Default.CalendarMonth, contentDescription = "שיבוץ חכם")
        }
    )
}


@Composable
fun ScheduleSummaryCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("לוח השיבוץ החכם", style = MaterialTheme.typography.headlineSmall)
        Text(
            "ניהול משמרות שבועי עם זיהוי אוטומטי של קונפליקטים",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Button(
            onClick = { /* TODO: create shift */ },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("יצירת משמרת")
        }

        OutlinedButton(
            onClick = { /* TODO: open conflicts */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
            Spacer(Modifier.width(4.dp))
            Text("קונפליקטים (0)")
        }
    }
}

@Composable
fun WeekNavigationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* TODO: previous week */ }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "שבוע קודם")
                }
                Text("12–18 באוקטובר 2025 | שבוע 42")
                IconButton(onClick = { /* TODO: next week */ }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "שבוע הבא")
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryChip("0", "קונפליקטים", Color.Red)
                SummaryChip("0", "לא מאוישות", Color(0xFFFFC107))
                SummaryChip("0", "משמרות מאוישות", Color(0xFF4CAF50))
            }

            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = 0f,
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun HomeBottomBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            label = { Text("התראות") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Group, contentDescription = null) },
            label = { Text("ניהול") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
            label = { Text("לוח") },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("דף") },
            selected = false,
            onClick = {}
        )
    }
}


@Composable
fun SummaryChip(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, style = MaterialTheme.typography.headlineSmall)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
@Composable
fun ScheduleProgressCard(
    progress: Float = 0.65f // לדוגמה 65% הושלם
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "השלמת השיבוץ השבועי",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
