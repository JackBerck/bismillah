package com.example.warasin.ui.healthnotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.ui.theme.Red100
import com.example.warasin.ui.theme.Red600
import com.example.warasin.ui.theme.WarasInTheme

@Composable
fun HealthNotesSubItem(
    title: String,
    value: String,
    iconPainter: Painter,
    iconDescription: String,
    backgroundColor: Color,
    contentColor: Color, // Used for icon and main text color
    valueTextColor: Color, // Specific color for the value text
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier // Use the passed modifier first
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = iconDescription,
            tint = contentColor, // Use contentColor for icon tint
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor, // Use contentColor for title text
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
            color = valueTextColor, // Use the dedicated valueTextColor
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HealthNotesSubItemPreview() {
    WarasInTheme {
        HealthNotesSubItem(
            title = "Tekanan Darah",
            value = "120/80 mmHg",
            iconPainter = painterResource(id = R.drawable.outline_favorite_24),
            iconDescription = "Heart Icon",
            backgroundColor = Red100,
            contentColor = Red600,
            valueTextColor = Red600,
            modifier = Modifier.fillMaxWidth() // Occupy full width in this preview
        )
    }
}
