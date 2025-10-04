package com.example.warasin.ui.schedule

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.warasin.R
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.component.LabeledTextField
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Red600
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleDialog(
    medicines: List<MedicineWithSchedules>,
    onDismiss: () -> Unit,
    onSave: (medicineId: Int, time: String) -> Unit,
    initialMedicineId: Medicine? = null,
    initialTime: Calendar? = null,
    isEditMode: Boolean = false,
) {
    var selectedMedicine by remember { mutableStateOf(initialMedicineId) }
    var selectedTime by remember { mutableStateOf(initialTime) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                        contentDescription = "Calendar Icon",
                        tint = Blue600,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditMode) "Edit Jadwal Obat" else "Tambah Jadwal Obat",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp,
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                ) {
                    LabeledTextField(
                        label = "Pilih Obat",
                        value = selectedMedicine?.name ?: "",
                        onValueChange = {},
                        placeholder = "Contoh: Paracetamol",
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                        },
                    )
                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        medicines.forEach { medicine ->
                            DropdownMenuItem(
                                text = { Text(medicine.medicine.name) },
                                onClick = {
                                    selectedMedicine = medicine.medicine
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // --- Input untuk memilih waktu ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true }
                ) {
                    LabeledTextField(
                        value = selectedTime?.let { timeFormatter.format(it.time) } ?: "",
                        onValueChange = {},
                        label = "Pilih Waktu",
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_timer_24),
                                contentDescription = "Pilih Waktu"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false, // Tambahkan ini agar tidak ada interaksi langsung
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ButtonWithoutIcon(
                        onClick = {
                            selectedMedicine?.let { med ->
                                selectedTime?.let { time ->
                                    val timeStr = timeFormatter.format(time.time)
                                    onSave(med.id, timeStr)
                                    onDismiss()
                                }
                            }
                        },
                        text = if (isEditMode) "Simpan Perubahan" else "Simpan",
                        backgroundColor = Blue600,
                        enabled = selectedMedicine != null && selectedTime != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ButtonWithoutIcon(
                        onClick = onDismiss,
                        text = "Batal",
                        backgroundColor = Red600,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    // --- Logika untuk menampilkan TimePickerDialog ---
    // Blok ini akan dieksekusi ketika state 'showTimePicker' menjadi true
    if (showTimePicker) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val newTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            selectedTime = newTime
            showTimePicker = false // Tutup picker setelah waktu dipilih
        }

        val timePickerDialog = TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true // Format 24 jam
        )

        // Atur agar state kembali false jika pengguna menekan tombol "cancel" atau area luar
        timePickerDialog.setOnCancelListener {
            showTimePicker = false
        }

        timePickerDialog.show()
    }
}