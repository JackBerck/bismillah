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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import com.example.warasin.data.model.Medicine
import com.example.warasin.ui.medicine.AddMedicineDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineScreen(
    // viewModel: MedicineViewModel = hiltViewModel()
) {
    // State dan Data tetap sama
    var showDialog by remember { mutableStateOf(false) }
    var selectedMedicine by remember { mutableStateOf<Medicine?>(null) }

    // --- STATE BARU UNTUK FORM TAMBAH OBAT ---
    var showAddDialog by remember { mutableStateOf(false) }

    // State untuk setiap field di dalam form
    var medicineName by remember { mutableStateOf("") }
    var medicineDosage by remember { mutableStateOf("") }
    var medicineNotes by remember { mutableStateOf("") }

    val medicines = remember {
        mutableStateListOf(
            Medicine(1, "Paracetamol", "500mg", listOf("09:00", "15:00"), "Obat ini digunakan untuk meredakan demam dan nyeri ringan hingga sedang."),
            Medicine(2, "Amoxicillin", "250mg", listOf("08:00", "20:00"), "Antibiotik untuk mengobati infeksi bakteri. Harus dihabiskan."),
            Medicine(3, "Vitamin C", "1000mg", listOf("07:00"), "Suplemen untuk menjaga daya tahan tubuh.")
        )
    }

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
                    onClick = { showAddDialog = true }, // Tampilkan dialog tambah
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

        // --- PANGGIL DIALOG TAMBAH DI SINI ---
        if (showAddDialog) {
            AddMedicineDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name, dosage, time, notes ->
                    // Tambahkan obat baru ke dalam list
                    medicines.add(
                        Medicine(
                            id = medicines.size + 1,
                            name = name,
                            dosage = dosage,
                            times = time,
                            description = notes
                        )
                    )
                    // Tutup dialog
                    showAddDialog = false
                    Log.d("MedicineScreen", "Obat Disimpan: $name, $dosage, $time, $notes")
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