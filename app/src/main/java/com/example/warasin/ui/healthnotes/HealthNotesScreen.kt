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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.warasin.ui.theme.WarasInTheme
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Green100
import com.example.warasin.ui.theme.Green600
import androidx.navigation.compose.rememberNavController // Import for Preview
import com.example.warasin.data.model.HealthNote
import com.example.warasin.data.model.Medicine
import com.example.warasin.ui.component.ButtonWithIcon
import com.example.warasin.ui.medicine.AddMedicineDialog
import com.example.warasin.ui.theme.Red100
import com.example.warasin.ui.theme.Red600

@Composable
fun HealthNotesScreen(
    viewModel: HealthNotesViewModel = hiltViewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedHealthNote by remember { mutableStateOf<HealthNote?>(null) }

    val healthNotes by viewModel.healthNotes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(), // LazyColumn sekarang mengisi lebar Column parent
            verticalArrangement = Arrangement.spacedBy(16.dp) // Jarak antar item di LazyColumn
        ) {
            item {
                Column { // Gunakan Column di dalam `item` jika perlu mengelompokkan beberapa komponen
                    Text(
                        text = "Catatan Kesehatan",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Pantau kondisi kesehatan harian!",
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
            }

            // --- DAFTAR ITEM ---
            // `items` dipanggil langsung di dalam LazyColumn
            items(healthNotes) { healthNote ->
                HealthNoteItem(
                    healthNote = healthNote,
                    onEdit = {
                        selectedHealthNote = healthNote
                        showEditDialog = true
                    },
                    onDelete = {
                        viewModel.deleteHealthNote(healthNote)
                    },
                    onClick = {
                        selectedHealthNote = healthNote
                        showDetailDialog = true
                    }
                )
            }
        }

        // Dialog Add
        if (showAddDialog) {
            AddHealthNoteDialog(
                onDismiss = { showAddDialog = false },
                onSave = { bloodPressure, bloodSugar, bodyTemperature, mood, notes ->
                    viewModel.addHealthNote(bloodPressure, bloodSugar, bodyTemperature, mood, notes)
                    showAddDialog = false
                }
            )
        }

        // Dialog Edit
        if (showEditDialog && selectedHealthNote != null) {
            AddHealthNoteDialog(
                onDismiss = { showEditDialog = false },
                onSave = { bloodPressure, bloodSugar, bodyTemperature, mood, notes ->
                    viewModel.updateHealthNote(
                        selectedHealthNote!!.copy(
                            bloodPressure = bloodPressure,
                            bloodSugar = bloodSugar,
                            bodyTemperature = bodyTemperature,
                            mood = mood,
                            notes = notes
                        )
                    )
                    showEditDialog = false
                    selectedHealthNote = null
                },
                initialBloodPressure = selectedHealthNote!!.bloodPressure,
                initialBloodSugar = selectedHealthNote!!.bloodSugar,
                initialBodyTemperature = selectedHealthNote!!.bodyTemperature,
                initialMood = selectedHealthNote!!.mood,
                initialNotes = selectedHealthNote!!.notes
            )
        }

        // Dialog Detail
        if (showDetailDialog && selectedHealthNote != null) {
            HealthNoteDetailDialog(
                healthNote = selectedHealthNote!!,
                onEdit = {
                    showDetailDialog = false
                    showEditDialog = true
                },
                onDelete = {
                    viewModel.deleteHealthNote(selectedHealthNote!!)
                    showDetailDialog = false
                    selectedHealthNote = null
                },
                onDismiss = {
                    showDetailDialog = false
                    selectedHealthNote = null
                }
            )
        }
    }
}
