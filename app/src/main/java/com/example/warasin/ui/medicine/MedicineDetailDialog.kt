package com.example.warasin.ui.medicine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.warasin.data.model.Medicine

@Composable
fun MedicineDetailDialog(
    medicine: Medicine,
    onDismiss: () -> Unit // Lambda untuk menutup dialog
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f),
        title = {
            Text(
                text = medicine.name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Dosis: ${medicine.dosage}")
                Text("Frekuensi: ${medicine.times.size} kali sehari")
                Text("Waktu: ${medicine.times.joinToString(", ")}")
                Spacer(modifier = Modifier.size(8.dp))
                Text(medicine.description)
            }
        },
        confirmButton = {
            TextButton (onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}