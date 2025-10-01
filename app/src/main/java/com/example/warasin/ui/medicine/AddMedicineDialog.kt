package com.example.warasin.ui.medicine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.warasin.R
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.component.LabeledTextField
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Red600
import java.util.Calendar

@Composable
fun AddMedicineDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, dosage: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var times by remember { mutableStateOf(mutableStateListOf("--:--")) }
    var notes by remember { mutableStateOf("") }

    var showTimePickerForIndex by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current

    showTimePickerForIndex?.let { index ->
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = android.app.TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                times[index] = String.format("%02d:%02d", selectedHour, selectedMinute)
                showTimePickerForIndex = null
            },
            hour,
            minute,
            true
        )

        timePickerDialog.setOnCancelListener {
            showTimePickerForIndex = null
        }

        timePickerDialog.show()

        DisposableEffect(Unit) {
            onDispose {
                timePickerDialog.dismiss()
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
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
                        text = "Tambah Pengingat Obat",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp,
                    )
                }

                LabeledTextField(
                    label = "Nama Obat",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Contoh: Paracetamol"
                )

                LabeledTextField(
                    label = "Dosis",
                    value = dosage,
                    onValueChange = { dosage = it },
                    placeholder = "Contoh: 1 tablet, 500 mg"
                )

                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Waktu Minum",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    times.forEachIndexed { index, time ->
                        OutlinedTextField(
                            value = time,
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth().clickable() { showTimePickerForIndex = index },
                            trailingIcon = { Icon(painterResource(id = R.drawable.outline_timer_24), "Pilih Waktu") },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            singleLine = true
                        )
                    }
                }


                // Tombol untuk menambah input waktu baru
                TextButton(
                    onClick = { times.add("--:--") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(painterResource(id = R.drawable.baseline_add_24), contentDescription = "Tambah Waktu")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah Waktu")
                }

                LabeledTextField(
                    label = "Catatan (Opsional)",
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = "Contoh: Setelah makan",
                )
            }
        }
    }
}