package com.example.warasin.ui.medicine

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.WarasInTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


data class Medicine(
    val id: Int,
    val name: String,
    val dosage: String,
    val frequency: String,
    val description: String
)

@Composable
fun MedicineDetailDialog(
    medicine: Medicine,
    onDismiss: () -> Unit // Lambda untuk menutup dialog
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = medicine.name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Dosis: ${medicine.dosage}")
                Text("Frekuensi: ${medicine.frequency}")
                Spacer(modifier = Modifier.size(8.dp))
                Text(medicine.description)
            }
        },
        confirmButton = {
            TextButton (onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

@Composable
fun MedicineItem(
    medicine: Medicine,
    onClick: () -> Unit // Lambda untuk menangani klik
) {
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
                    text = medicine.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Dosis: ${medicine.dosage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Frekuensi: ${medicine.frequency}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineScreen(
    // viewModel: MedicineViewModel = hiltViewModel()
) {
    // State dan Data tetap sama
    var showDialog by remember { mutableStateOf(false) }
    var selectedMedicine by remember { mutableStateOf<Medicine?>(null) }

    val medicines = listOf(
        Medicine(1, "Paracetamol", "500mg", "3 kali sehari", "Obat ini digunakan untuk meredakan demam dan nyeri ringan hingga sedang."),
        Medicine(2, "Amoxicillin", "250mg", "2 kali sehari", "Antibiotik untuk mengobati infeksi bakteri. Harus dihabiskan."),
        Medicine(3, "Vitamin C", "1000mg", "1 kali sehari", "Suplemen untuk menjaga daya tahan tubuh.")
    )

    // PENTING: Bungkus semua konten dengan Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Ini adalah konten utama layar Anda
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Daftar Obat",
                    style = MaterialTheme.typography.titleLarge,
                )
                Button(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue600),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = null,
                        tint = Gray50,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(medicines) { medicine ->
                    MedicineItem(
                        medicine = medicine,
                        onClick = {
                            Log.d("MedicineScreen", "Item clicked: ${medicine.name}") // Tambahkan log untuk debug
                            selectedMedicine = medicine
                            showDialog = true
                        }
                    )
                }
            }
        }

        // PENTING: Dialog dipanggil di sini, di dalam Box tapi di luar Column
        // Ini membuatnya "mengambang" di atas konten Column
        if (showDialog && selectedMedicine != null) {
            MedicineDetailDialog(
                medicine = selectedMedicine!!,
                onDismiss = {
                    showDialog = false
                    selectedMedicine = null
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MedicineScreenPreview() {
    WarasInTheme {
        MedicineScreen()
    }
}