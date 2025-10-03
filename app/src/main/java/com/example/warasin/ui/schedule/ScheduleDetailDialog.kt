package com.example.warasin.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.warasin.R
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.Red600

@Composable
fun ScheduleDetailDialog(
    schedule: ScheduleWithMedicine,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val timeText = schedule.schedule.time.ifEmpty {
        "Tidak ada jadwal obat"
    }
    val scheduleCondition = schedule.schedule.isTaken ?.let {
        if (it) "Sudah diminum" else "Belum diminum"
    } ?: "Belum ada data"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                        contentDescription = "Calendar Icon",
                        tint = Blue600,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Detail Jadwal Obat",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailItem("Dosis", schedule.medicine.dosage)
                    DetailItem("Waktu", timeText)
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
                        backgroundColor = Red600
                    )
                    ButtonWithoutIcon(
                        onClick = onDismiss,
                        text = "Tutup",
                        backgroundColor = Gray950,
                        contentColor = Gray50
                    )
                }
            }
        }
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
            style = MaterialTheme.typography.bodyLarge
        )
    }
}