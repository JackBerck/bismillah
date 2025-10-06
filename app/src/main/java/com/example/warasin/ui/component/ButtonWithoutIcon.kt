package com.example.warasin.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.example.warasin.ui.theme.Gray50

@Composable
fun ButtonWithoutIcon(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = com.example.warasin.ui.theme.Blue600,
    contentColor: Color = Gray50,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = textStyle,
            color = contentColor
        )
    }
}

