package com.example.warasin.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = com.example.warasin.ui.theme.Blue600,
    contentColor: Color = Gray50,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    icon: Int = R.drawable.baseline_check_24
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "$text Icon",
                tint = contentColor,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = textStyle,
                color = contentColor
            )
        }
    }
}