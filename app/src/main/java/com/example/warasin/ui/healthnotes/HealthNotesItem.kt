package com.example.warasin.ui.healthnotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Green100
import com.example.warasin.ui.theme.Green600
import com.example.warasin.ui.theme.Purple100
import com.example.warasin.ui.theme.Purple600
import com.example.warasin.ui.theme.Red100
import com.example.warasin.ui.theme.Red600
import com.example.warasin.ui.theme.Yellow100
import com.example.warasin.ui.theme.Yellow600

@Composable
fun HealthNotesItem() {
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
            text = "Senin, 15 September 2025",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        ) {
            HealthNotesSubItem(
                title = "Tekanan Darah",
                value = "120/80 mmHg",
                iconPainter = painterResource(id = R.drawable.outline_favorite_24),
                iconDescription = "Blood Preasure Icon",
                backgroundColor = Red100,
                contentColor = Red600,
                valueTextColor = Red600,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            HealthNotesSubItem(
                title = "Gula Darah",
                value = "90 mg/dL",
                iconPainter = painterResource(id = R.drawable.outline_show_chart_24),
                iconDescription = "Blood Sugar Icon",
                backgroundColor = Blue100,
                contentColor = Blue600,
                valueTextColor = Blue600,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Row (
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        ) {
            HealthNotesSubItem(
                title = "Suhu Tubuh",
                value = "36.5 °C",
                iconPainter = painterResource(id = R.drawable.outline_thermometer_24),
                iconDescription = "Thermometer Icon",
                backgroundColor = Green100,
                contentColor = Green600,
                valueTextColor = Green600,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            HealthNotesSubItem(
                title = "Mood",
                value = "Bahagia",
                iconPainter = painterResource(id = R.drawable.outline_emoji_language_24),
                iconDescription = "Mood Icon",
                backgroundColor = Purple100,
                contentColor = Purple600,
                valueTextColor = Purple600,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Yellow100, shape = RoundedCornerShape(8.dp)),
        ) {
            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Catatan",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Yellow600
                )
                Text(
                    text = "Data terakhir diperbarui 2 jam yang lalu",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Yellow600
                )
            }
        }
    }
}

@Preview
@Composable
fun HealthNotesItemPreview() {
    HealthNotesItem()
}