package com.example.warasin.ui.healthnotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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

@Composable
fun AddHealthNoteDialog(
    onDismiss: () -> Unit,
    onSave: (bloodPressure: String, bloodSugar: String, bodyTemperature: String, mood: String, notes: String) -> Unit,
    initialBloodPressure: String = "",
    initialBloodSugar: String = "",
    initialBodyTemperature: String = "",
    initialMood: String = "",
    initialNotes: String = ""
) {
    var bloodPressure by remember { mutableStateOf(initialBloodPressure) }
    var bloodSugar by remember { mutableStateOf(initialBloodSugar) }
    var bodyTemperature by remember { mutableStateOf(initialBodyTemperature) }
    var mood by remember { mutableStateOf(initialMood) }
    var notes by remember { mutableStateOf(initialNotes) }

    // Fungsi sanitasi
    fun sanitizeBloodPressure(input: String): String {
        return input.filterIndexed { idx, c ->
            c.isDigit() || (c == '/' && !input.take(idx).contains('/'))
        }.take(7) // Format: 120/80 (max 7 karakter)
    }

    fun sanitizeNumberInput(input: String): String {
        return input.filter { it.isDigit() || it == '.' }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                        contentDescription = "Calendar Icon",
                        tint = Blue600,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (initialBloodPressure.isEmpty()) "Tambah Catatan Harian" else "Edit Catatan Harian",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp,
                    )
                }
                Spacer(Modifier.size(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    LabeledTextField(
                        label = "Tekanan Darah",
                        value = bloodPressure,
                        onValueChange = { bloodPressure = sanitizeBloodPressure(it) },
                        placeholder = "120/80",
                        modifier = Modifier.weight(1f)
                    )
                    LabeledTextField(
                        label = "Gula Darah",
                        value = bloodSugar,
                        onValueChange = { bloodSugar = sanitizeNumberInput(it).take(3) },
                        placeholder = "90",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    LabeledTextField(
                        label = "Suhu Tubuh",
                        value = bodyTemperature,
                        onValueChange = { bodyTemperature = sanitizeNumberInput(it).take(5) },
                        placeholder = "36.5",
                        modifier = Modifier.weight(1f)
                    )
                    LabeledTextField(
                        label = "Mood",
                        value = mood,
                        onValueChange = { mood = it.take(10) },
                        placeholder = "Bahagia",
                        modifier = Modifier.weight(1f)
                    )
                }
                LabeledTextField(
                    label = "Catatan (Opsional)",
                    value = notes,
                    onValueChange = { notes = it.take(50) },
                    placeholder = "Setelah makan",
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ButtonWithoutIcon(
                        onClick = {
                            onSave(bloodPressure, bloodSugar, bodyTemperature, mood, notes)
                            onDismiss()
                        },
                        text = "Simpan",
                        backgroundColor = Blue600,
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
}