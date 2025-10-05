package com.example.warasin.ui.homepage

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

@Composable
fun HomepageScreen(
    navController: NavController,
    viewModel: HomepageViewModel = hiltViewModel()
) {
    val medicines by viewModel.medicines.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Selamat Pagi, Zaki!",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Jangan lupa minum obat hari ini dan tetap jaga kesehatan ya!",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                Row(
                    Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                        contentDescription = "Calendar Icon",
                        tint = Blue600,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = " Jadwal Minum Obat",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                if (medicines.isEmpty()) {
                    Text(
                        text = "Belum ada jadwal obat hari ini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray300,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(medicines) { medicine ->
                            HomeMedicineItem(
                                medicine = medicine,
                                onMarkAsTaken = {
                                    viewModel.markMedicineAsTaken(medicine.id)
                                },
                                onMarkAsNotTaken = {
                                    viewModel.markMedicineAsNotTaken(medicine.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Status Kesehatan
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
                        text = "Status Kesehatan",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .background(Blue100, shape = RoundedCornerShape(8.dp))
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Tekanan Darah",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "120/80 mmHg",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                            color = Blue600,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .background(Green100, shape = RoundedCornerShape(8.dp))
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Gula Darah",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "95 mg/dL",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                            color = Green600,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Data terakhir diperbarui 2 jam yang lalu",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Baris Tombol Aksi ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol "Tambah Obat"
            Button(
                onClick = {
                    // Now navController is accessible here
                    navController.navigate("medicine_screen") {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600),
                modifier = Modifier
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = null,
                        tint = Gray50
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp) // This was height, changed to width for horizontal spacing
                    )
                    Text(
                        text = "Tambah Obat",
                        color = Gray50,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    )
                }
            }

            // Tombol "Tambah Catatan"
            OutlinedButton(
                onClick = { /*TODO: navController.navigate("add_note_screen") or similar */ },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Blue600),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue600),
                modifier = Modifier
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_notes_24),
                        contentDescription = null
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp) // This was height, changed to width for horizontal spacing
                    )
                    Text(
                        text = "Tambah Catatan",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepageScreenPreview() {
    WarasInTheme {
        // For preview, you can use a rememberNavController or a fake NavController if needed
        val navController = rememberNavController()
        HomepageScreen(navController = navController)
    }
}
