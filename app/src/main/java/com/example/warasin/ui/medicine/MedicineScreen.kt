package com.example.warasin.ui.medicine

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.WarasInTheme
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.ui.component.ButtonWithIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineScreen(
    viewModel: MedicineViewModel = hiltViewModel()
) {
    val medicines by viewModel.medicines.collectAsState(emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var selectedMedicine by remember { mutableStateOf<MedicineWithSchedules?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column {
                Text(
                    text = "Daftar Obat",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Pantau pengingat obat harianmu!",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                ButtonWithIcon(
                    onClick = { showAddDialog = true },
                    text = "Tambah",
                    icon = R.drawable.baseline_add_24,
                    backgroundColor = Blue600,
                    contentColor = Gray50,
                )
            }

            Spacer(modifier = Modifier.size(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(medicines) { medicine ->
                    MedicineItem(
                        medicine = medicine,
                        onClick = {
                            selectedMedicine = medicine
                            showDialog = true
                        }
                    )
                }
            }
        }

        if (showDialog && selectedMedicine != null) {
            MedicineDetailDialog(
                medicine = selectedMedicine!!,
                onDismiss = {
                    showDialog = false
                    selectedMedicine = null
                },
                onDelete = {
                    viewModel.deleteMedicine(selectedMedicine?.medicine?.id!!)
                    showDialog = false
                    selectedMedicine = null
                },
            )
        }

        if (showAddDialog) {
            AddMedicineDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name, dosage, notes ->
                    viewModel.addMedicine(name, dosage, notes)
                    showAddDialog = false
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