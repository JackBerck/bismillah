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
    var notes by remember { mutableStateOf("") }

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

                LabeledTextField(
                    label = "Catatan (Opsional)",
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = "Contoh: Setelah makan",
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ButtonWithoutIcon(
                        onClick = {
                            onSave(name, dosage, notes)
                            onDismiss()
                        },
                        text = "Simpan",
                        backgroundColor = Blue600,
                        enabled = name.isNotBlank() && dosage.isNotBlank(),
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