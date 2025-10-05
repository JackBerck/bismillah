package com.example.warasin.ui.medicine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons // <-- Import Icons
import androidx.compose.material.icons.filled.Close // <-- Import Ikon Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.Red600

@Composable
fun MedicineDetailDialog(
    medicine: MedicineWithSchedules,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val jadwalText = if (medicine.schedules.isNullOrEmpty()) {
        "Tidak ada jadwal obat"
    } else {
        medicine.schedules.joinToString(", ") { it.time }
    }

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
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup Dialog"
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = medicine.medicine.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    DetailItem(
                        label = "Dosis",
                        value = medicine.medicine.dosage
                    )
                    DetailItem(
                        label = "Frekuensi",
                        value = "${medicine.schedules.size} kali sehari"
                    )
                    DetailItem(
                        label = "Jadwal",
                        value = jadwalText
                    )
                    DetailItem(
                        label = "Catatan",
                        value = medicine.medicine.notes ?: "Tidak ada"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ButtonWithoutIcon(
                            onClick = onDelete,
                            text = "Hapus",
                            backgroundColor = Red600,
                            modifier = Modifier.weight(1f),
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
