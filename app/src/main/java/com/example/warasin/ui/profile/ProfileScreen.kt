package com.example.warasin.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
fun ProfileScreen(
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfileImage(it) }
    }

    // Show error snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    // Profile Image with click functionality
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable { photoPickerLauncher.launch("image/*") }
                    ) {
                        if (uiState.profileImageUri.isNotEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.profileImageUri)
                                    .build(),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.wrench),
                                contentDescription = "Default Profile Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Camera overlay icon
                        Icon(
                            painter = painterResource(id = R.drawable.outline_edit_24),
                            contentDescription = "Edit Profile Image",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(24.dp)
                        )
                    }

                    Text(
                        text = uiState.name,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = uiState.email,
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
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Button(
                            onClick = { viewModel.toggleEditMode() },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isEditMode) Red600 else Blue600,
                                contentColor = Gray50
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = Gray50,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(
                                            id = if (uiState.isEditMode) R.drawable.baseline_cancel_24
                                            else R.drawable.outline_edit_24
                                        ),
                                        contentDescription = if (uiState.isEditMode) "Cancel Edit" else "Edit Icon",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = if (uiState.isEditMode) "Batal" else "Edit",
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        }
                    }

                    LabeledTextField(
                        label = "Nama Lengkap",
                        value = uiState.name,
                        onValueChange = { viewModel.updateName(it) },
                        placeholder = "Masukkan nama lengkap Anda",
                        enabled = uiState.isEditMode
                    )
                    LabeledTextField(
                        label = "Email",
                        value = uiState.email,
                        onValueChange = { /* Email tidak bisa diubah */ },
                        placeholder = "Email tidak dapat diubah",
                        enabled = false // Email selalu disabled
                    )
                    LabeledTextField(
                        label = "Usia",
                        value = uiState.age,
                        onValueChange = { viewModel.updateAge(it) },
                        placeholder = "Masukkan usia Anda",
                        enabled = uiState.isEditMode
                    )
                    LabeledTextField(
                        label = "Nomor Telepon",
                        value = uiState.phoneNumber,
                        onValueChange = { viewModel.updatePhoneNumber(it) },
                        placeholder = "Masukkan nomor telepon Anda",
                        enabled = uiState.isEditMode
                    )

                    AnimatedVisibility(
                        visible = uiState.isEditMode,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ButtonWithoutIcon(
                            onClick = { viewModel.saveProfile() },
                            text = if (uiState.isLoading) "Menyimpan..." else "Simpan",
                            backgroundColor = Blue600,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading
                        )
                    }
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
                    onClick = { navController.navigate("securityprivacy_screen") }
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
                    onClick = { navController.navigate("helpsupport_screen") }
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
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
            .clickable { onClick() }
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