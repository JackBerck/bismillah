package com.example.warasin.ui.schedule

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.warasin.R
import com.example.warasin.data.model.DayOfWeek
import com.example.warasin.data.model.Medicine
import com.example.warasin.data.model.MedicineWithSchedules
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.component.LabeledTextField
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Red600
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleDialog(
    medicines: List<MedicineWithSchedules>,
    onDismiss: () -> Unit,
    onSave: (medicineId: Int, time: String, selectedDays: List<Int>) -> Unit,
    initialMedicineId: Medicine? = null,
    initialTime: Calendar? = null,
    initialSelectedDays: List<Int> = emptyList(),
    isEditMode: Boolean = false,
) {
    var selectedMedicine by remember { mutableStateOf(initialMedicineId) }
    var selectedTime by remember { mutableStateOf(initialTime) }
    var selectedDays by remember { mutableStateOf(initialSelectedDays) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val allDays = remember { DayOfWeek.getAllDays() }

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

                // Dropdown Medicine
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

                // Time Picker
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
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                // Day Selection
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Pilih Hari",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(allDays) { day ->
                            DaySelector(
                                day = day,
                                isSelected = selectedDays.contains(day.id),
                                onToggle = {
                                    selectedDays = if (selectedDays.contains(day.id)) {
                                        selectedDays - day.id
                                    } else {
                                        selectedDays + day.id
                                    }
                                }
                            )
                        }
                    }

                    if (selectedDays.isNotEmpty()) {
                        val dayNames = allDays.filter { selectedDays.contains(it.id) }
                            .joinToString(", ") { it.shortName }
                        Text(
                            text = "Dipilih: $dayNames",
                            style = MaterialTheme.typography.bodySmall,
                            color = Blue600
                        )
                    }
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
                                    onSave(med.id, timeStr, selectedDays)
                                    onDismiss()
                                }
                            }
                        },
                        text = if (isEditMode) "Simpan Perubahan" else "Simpan",
                        backgroundColor = Blue600,
                        enabled = selectedMedicine != null && selectedTime != null && selectedDays.isNotEmpty(),
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

    // Time Picker Dialog
    if (showTimePicker) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val newTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            selectedTime = newTime
            showTimePicker = false
        }

        val timePickerDialog = TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        )

        timePickerDialog.setOnCancelListener {
            showTimePicker = false
        }

        timePickerDialog.show()
    }
}

@Composable
fun DaySelector(
    day: DayOfWeek,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onToggle,
        label = {
            Text(
                text = day.shortName,
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Blue600,
            selectedLabelColor = Gray50
        )
    )
}