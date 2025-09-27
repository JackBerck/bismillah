package com.example.warasin.ui.healthnotes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
// import androidx.compose.material3.ExperimentalMaterial3Api // No longer needed for Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.platform.LocalContext // Not directly used in the provided snippet
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.warasin.ui.theme.WarasInTheme
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
// import com.example.warasin.ui.theme.Gray200 // Not directly used
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray50
// import com.example.warasin.ui.theme.Gray950 // Not directly used
import com.example.warasin.ui.theme.Green100
import com.example.warasin.ui.theme.Green600
import androidx.navigation.compose.rememberNavController // Import for Preview
import com.example.warasin.ui.component.ButtonWithIcon
import com.example.warasin.ui.theme.Red100
import com.example.warasin.ui.theme.Red600

@Composable
fun HealthNotesScreen(
    // viewModel: HealthNotesViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "Catatan Kesehatan",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Pantau dan catat kondisi kesehatan Anda secara berkala untuk hasil terbaik.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            ButtonWithIcon(
                onClick = { /* TODO: Navigate to add health note screen */ },
                text = "Tambah",
                icon = R.drawable.baseline_add_24,
                backgroundColor = Blue600,
                contentColor = Gray50,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Gray300),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                ) {
                    HealthNotesItem()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthNotesScreenPreview() {
    WarasInTheme {
        HealthNotesScreen()
    }
}
