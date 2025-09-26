package com.example.warasin.ui.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.Green600

@Composable
fun HomeMedicineItem(
    medicine: Medicine,
    onMarkAsTaken: () -> Unit,
    onMarkAsNotTaken: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = medicine.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${medicine.dosage} - ${medicine.times.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                if (medicine.isTaken) {
                    onMarkAsNotTaken()
                } else {
                    onMarkAsTaken()
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (medicine.isTaken) Gray300 else Green600
            ),
        ) {
            if (medicine.isTaken) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_24),
                        contentDescription = "Check Icon",
                        tint = Gray950,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sudah",
                        color = Gray950,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    )
                }
            } else {
                Text(
                    text = "Minum",
                    color = Gray50,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                )
            }
        }
    }
}