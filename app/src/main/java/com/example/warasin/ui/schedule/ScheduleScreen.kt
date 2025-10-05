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
import com.example.warasin.ui.medicine.AddMedicineDialog
import com.example.warasin.util.stringToCalendar
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel()
) {

    val schedules by viewModel.schedules.collectAsState(emptyList())
    val medicines by viewModel.medicines.collectAsState(emptyList())

    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedSchedule by remember { mutableStateOf<ScheduleWithMedicine?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

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
                items(items = schedules, key = { it.schedule.id }) { scheduleItem ->
                    ScheduleItem(
                        schedule = scheduleItem,
                        onEdit = {
                            selectedSchedule = scheduleItem
                            showEditDialog = true
                        },
                        onDelete = {
                            viewModel.deleteSchedule(scheduleItem.schedule.id)
                        },
                        onClick = {
                            selectedSchedule = scheduleItem
                            showDetailDialog = true
                        }
                    )
                }
            }
        }

        if (showDetailDialog && selectedSchedule != null) {
            ScheduleDetailDialog(
                schedule = selectedSchedule!!,
                onDismiss = {
                    showDetailDialog = false
                    selectedSchedule = null
                },
                onDelete = {
                    viewModel.deleteSchedule(selectedSchedule?.schedule?.id!!)
                    showDetailDialog = false
                    selectedSchedule = null
                },
                onEdit = {
                    showDetailDialog = false
                    showEditDialog = true
                },
            )
        }

        if (showAddDialog) {
            AddScheduleDialog(
                medicines = medicines,
                onDismiss = { showAddDialog = false },
                onSave = { medicineId, time, selectedDays ->
                    viewModel.addSchedule(medicineId, time, selectedDays)
                    showAddDialog = false
                },
            )
        }

        if (showEditDialog && selectedSchedule != null) {
            AddScheduleDialog (
                onDismiss = { showEditDialog = false },
                onSave = { medicineId: Int, time: String, _ ->
                    viewModel.updateSchedule(
                        selectedSchedule!!.schedule.copy(
                            medicineId = medicineId,
                            time = time
                        )
                    )
                    showEditDialog = false
                    selectedSchedule = null
                },
                initialMedicineId = selectedSchedule!!.medicine,
                initialTime = stringToCalendar(selectedSchedule!!.schedule.time),
                medicines = medicines,
                isEditMode = true
            )
        }
    }
}