package com.example.warasin.ui.medicine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.warasin.data.model.Medicine
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.Red600

@Composable
fun MedicineDetailDialog(
    medicine: Medicine,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
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
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Dosis: ${medicine.dosage}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Frekuensi: ${medicine.times.size} kali sehari",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Waktu: ${medicine.times.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = medicine.notes,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ButtonWithoutIcon(
                        onClick = onDelete,
                        text = "Hapus",
                        backgroundColor = Red600
                    )
                    Spacer(Modifier.width(8.dp))
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

@Preview
@Composable
fun MedicineDetailDialogPreview() {
    val sampleMedicine = Medicine(
        id = 1,
        name = "Paracetamol",
        dosage = "500 mg",
        times = listOf("08:00", "14:00", "20:00"),
        notes = "Minum setelah makan"
    )
    MedicineDetailDialog(
        medicine = sampleMedicine,
        onDismiss = {},
        onDelete = {}
    )
}