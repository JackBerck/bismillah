package com.example.warasin.ui.healthnotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.warasin.R
import com.example.warasin.data.model.HealthNote
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Red600

@Composable
fun HealthNoteDetailDialog(
    healthNote: HealthNote,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        text = "Detail Catatan",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                    )
                }

                // Detail data
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailItem("Tekanan Darah", "${healthNote.bloodPressure} mmHg")
                    DetailItem("Gula Darah", "${healthNote.bloodSugar} mg/dL")
                    DetailItem("Suhu Tubuh", "${healthNote.bodyTemperature} °C")
                    DetailItem("Mood", healthNote.mood)
                    if (healthNote.notes.isNotEmpty()) {
                        DetailItem("Catatan", healthNote.notes)
                    }
                }

                // Tombol Edit & Delete
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ButtonWithoutIcon(
                        onClick = onEdit,
                        text = "Edit",
                        backgroundColor = Blue600,
                        modifier = Modifier.weight(1f)
                    )
                    ButtonWithoutIcon(
                        onClick = onDelete,
                        text = "Hapus",
                        backgroundColor = Red600,
                        modifier = Modifier.weight(1f)
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