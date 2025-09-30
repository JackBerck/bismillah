package com.example.warasin.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.warasin.R
import com.example.warasin.ui.component.ButtonWithoutIcon
import com.example.warasin.ui.component.LabeledTextField
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.Green600
import com.example.warasin.ui.theme.Red600

@Composable
fun ProfileScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wrench),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Wrench",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "wrench@watchdog.com",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Gray300), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_frame_person_24),
                            contentDescription = "Personal Info Icon",
                            tint = Green600,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Informasi Pribadi",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Button(
                        onClick = { /* TODO: Edit action */ },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue600,
                            contentColor = Gray50
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_edit_24),
                                contentDescription = "Edit Icon",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Edit",
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }

                LabeledTextField(
                    label = "Nama Lengkap",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Masukkan nama lengkap Anda"
                )
                LabeledTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Masukkan email Anda"
                )
                LabeledTextField(
                    label = "Usia",
                    value = age,
                    onValueChange = { age = it },
                    placeholder = "Masukkan usia Anda"
                )
                LabeledTextField(
                    label = "Nomor Telepon",
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = "Masukkan nomor telepon Anda"
                )

                ButtonWithoutIcon(
                    onClick = { /* TODO: Save action */ },
                    text = "Simpan",
                    backgroundColor = Blue600,
                    modifier = Modifier.fillMaxWidth()
                )
                ButtonWithoutIcon(
                    onClick = { /* TODO: Cancel action */ },
                    text = "Batal",
                    backgroundColor = Red600,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            ProfileSectionItem(
                title = "Keamanan & Privasi",
                iconResId = R.drawable.outline_shield_24,
                iconTint = Gray950,
                onClick = { /* TODO: Navigate to Security & Privacy */ }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            ProfileSectionItem(
                title = "Bantuan & Dukungan",
                iconResId = R.drawable.outline_question_mark_24,
                iconTint = Gray950,
                onClick = { /* TODO: Navigate to Help & Support */ }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            ProfileSectionItem(
                title = "Keluar",
                iconResId = R.drawable.outline_logout_24,
                iconTint = Red600,
                textColor = Red600,
                onClick = { /* TODO: Logout action */ }
            )
        }
    }
}

@Composable
fun ProfileSectionItem(
    title: String,
    iconResId: Int,
    iconTint: Color,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Gray300), shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}

