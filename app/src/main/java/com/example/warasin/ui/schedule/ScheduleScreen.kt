package com.example.warasin.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.WarasInTheme
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.ui.component.ButtonWithIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel()
) {

    val schedules by viewModel.schedules.collectAsState(emptyList())
    val medicines by viewModel.medicines.collectAsState(emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var selectedSchedule by remember { mutableStateOf<ScheduleWithMedicine?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column {
                Text(
                    text = "Jadwal Minum Obat",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Kelola jadwal minum obat Anda agar tidak lupa",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                ButtonWithIcon(
                    onClick = { showAddDialog = true },
                    text = "Tambah",
                    icon = R.drawable.baseline_add_24,
                    backgroundColor = Blue600,
                    contentColor = Gray50,
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 2. Gunakan items(list, key)
                items(items = schedules, key = { it.schedule.id }) { scheduleItem ->
                    ScheduleItem(
                        schedule = scheduleItem,
                        onEdit = {
                            selectedSchedule = scheduleItem
                            showDialog = true
                        },
                        onDelete = {
                            // 3. Panggil fungsi delete yang benar (hapus schedule, bukan medicine)
                            viewModel.deleteSchedule(scheduleItem.schedule.id)
                        },
                        onClick = {
                            selectedSchedule = scheduleItem
                            showDialog = true
                        }
                    )
                }
            }
        }

        if (showDialog && selectedSchedule != null) {
            ScheduleDetailDialog(
                schedule = selectedSchedule!!,
                onDismiss = {
                    showDialog = false
                    selectedSchedule = null
                },
                onDelete = {
                    viewModel.deleteSchedule(selectedSchedule?.schedule?.id!!)
                    showDialog = false
                    selectedSchedule = null
                },
            )
        }

        if (showAddDialog) {
            AddScheduleDialog(
                medicines = medicines,
                onDismiss = { showAddDialog = false },
                onSave = { medicineId, time ->
                    viewModel.addSchedule(medicineId, time)
                    showAddDialog = false
                },
            )
        }
    }
}