package com.example.warasin.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.warasin.R
import com.example.warasin.data.model.DayOfWeek
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.Red600
import com.example.warasin.util.toSelectedDaysText

@Composable
fun ScheduleDetailDialog(
    schedule: ScheduleWithMedicine,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val timeText = schedule.schedule.time
    val scheduleCondition = if (schedule.schedule.isTaken) "Sudah diminum" else "Belum diminum"

    // Convert selectedDays string ke nama hari
    val selectedDaysText = getSelectedDaysText(schedule.schedule.selectedDays)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Gray950
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                            contentDescription = "Calendar Icon",
                            tint = Blue600,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Detail Jadwal Obat",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 32.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailItem("Obat", schedule.medicine.name)
                        DetailItem("Dosis", schedule.medicine.dosage)
                        DetailItem("Waktu", timeText)
                        DetailItem("Hari", selectedDaysText) // Tambahkan ini
                        DetailItem("Kondisi", scheduleCondition)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ButtonWithoutIcon(
                            onClick = onDelete,
                            text = "Hapus",
                            backgroundColor = Red600,
                            modifier = Modifier.weight(1f)
                        )
                        ButtonWithoutIcon(
                            onClick = onEdit,
                            text = "Edit",
                            backgroundColor = Blue600,
                            contentColor = Gray50,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// Fungsi helper untuk mengkonversi selectedDays ke text
@Composable
private fun getSelectedDaysText(selectedDays: String): String {
    if (selectedDays.isEmpty()) return "Tidak ada hari dipilih"

    val allDays = DayOfWeek.getAllDays()
    val selectedDayIds = selectedDays.split(",")
        .filter { it.isNotEmpty() }
        .mapNotNull { it.toIntOrNull() }

    val dayNames = allDays
        .filter { selectedDayIds.contains(it.id) }
        .map { it.name }

    return when {
        dayNames.isEmpty() -> "Tidak ada hari dipilih"
        dayNames.size == 7 -> "Setiap hari"
        dayNames.size == 1 -> dayNames.first()
        else -> dayNames.joinToString(", ")
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
