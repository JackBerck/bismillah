package com.example.warasin.ui.medicine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray300

@Composable
fun MedicineItem(
    medicine: MedicineWithSchedules,
    onClick: () -> Unit
) {
    val jadwalText = if (medicine.schedules.isNullOrEmpty()) {
        "Tidak ada jadwal obat"
    } else {
        medicine.schedules.joinToString(", ") { it.time }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Gray300, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick) // <-- BUAT BOX INI BISA DIKLIK
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_pill_24),
                contentDescription = null,
                tint = Blue600,
                modifier = Modifier
                    .size(64.dp)
                    .padding(12.dp)
                    .align(Alignment.CenterVertically)
                    .background(Blue100, RoundedCornerShape(8.dp)),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = medicine.medicine.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Dosis: ${medicine.medicine.dosage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Jadwal: $jadwalText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}