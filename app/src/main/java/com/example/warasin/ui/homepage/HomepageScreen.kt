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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.warasin.ui.theme.WarasInTheme
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray200
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.Green100
import com.example.warasin.ui.theme.Green600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomepageScreen(
    // viewModel: HomepageViewModel = hiltViewModel() //
) {
    // Menggunakan Column untuk menata elemen dari atas ke bawah
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // --- Bagian Header ---
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

        // --- Kartu Jadwal Minum Obat ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Gray300),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                ) {
                Row (
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
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Paracetamol",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "1 tablet - 08:00",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green600),
                        ) {
                            Column {
                                Text(
                                    text = "Minum",
                                    color = Gray50,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Vitamin D",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "1 kapsul - 12:00",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Gray200),
                        ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon (
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
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
            ) {
                Row (
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
                Row (
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column (
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
                    Column (
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
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar tombol
        ) {
            // Tombol "Tambah Obat"
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600),
                modifier = Modifier
                    .weight(1f)
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24), // Ganti ikon
                        contentDescription = null,
                        tint = Gray50
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp)
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
            OutlinedButton (
                onClick = { /*TODO*/ },
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
                        painter = painterResource(id = R.drawable.outline_add_notes_24), // Ganti ikon
                        contentDescription = null
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp)
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


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomepageScreenPreview() {
    WarasInTheme {
        HomepageScreen()
    }
}