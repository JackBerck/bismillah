package com.example.warasin.ui.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.example.warasin.data.model.ScheduleWithMedicine
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Green100
import com.example.warasin.ui.theme.Green600
import com.example.warasin.ui.theme.Red100
import com.example.warasin.ui.theme.Red600
import com.example.warasin.util.toSelectedDaysShortText
import kotlin.sequences.ifEmpty

@Composable
fun ScheduleItem(
    schedule: ScheduleWithMedicine,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val timeText = schedule.schedule.time.ifEmpty { "Tidak ada jadwal obat" }
    val daysText = schedule.schedule.selectedDays.toSelectedDaysShortText()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Gray300), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_trending_up_24),
                contentDescription = "Calendar Icon",
                tint = Green600,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = schedule.medicine.name,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                /*ScheduleSubItem(
                    title = "Dosis",
                    value = schedule.medicine.dosage,
                    backgroundColor = Red100,
                    contentColor = Red600,
                    valueTextColor = Red600,
                    modifier = Modifier.weight(1f)
                )*/
                ScheduleSubItem(
                    title = "Waktu",
                    value = timeText,
                    backgroundColor = Blue100,
                    contentColor = Blue600,
                    valueTextColor = Blue600,
                    modifier = Modifier.weight(1f)
                )
                ScheduleSubItem(
                    title = "Hari",
                    value = daysText,
                    backgroundColor = Green100, // Tambahkan warna ini di theme
                    contentColor = Green600,
                    valueTextColor = Green600,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}