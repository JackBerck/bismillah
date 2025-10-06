package com.example.warasin.ui.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.WarasInTheme

@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showTermsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = uiState.registrationError) {
        uiState.registrationError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onEvent(AuthEvent.ErrorShown)
        }
    }
    LaunchedEffect(key1 = uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            Toast.makeText(context, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
        }
    }

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = {
                showTermsDialog = false
            },
            title = {
                Text("Syarat & Ketentuan", style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "Dengan mendaftar dan menggunakan aplikasi WarasIn, Anda setuju untuk:\n\n" +
                                "1. Memberikan informasi yang akurat dan benar saat pendaftaran.\n" +
                                "2. Menjaga kerahasiaan kata sandi dan keamanan akun Anda.\n" +
                                "3. Tidak menyalahgunakan platform untuk tujuan yang melanggar hukum.\n" +
                                "4. Memahami bahwa informasi kesehatan yang Anda masukkan dikelola dengan aman dan tidak akan dibagikan tanpa persetujuan Anda, sesuai dengan kebijakan privasi kami.\n\n" +
                                "Pelanggaran terhadap syarat dan ketentuan ini dapat mengakibatkan penangguhan atau penghapusan akun Anda.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Aksi saat tombol konfirmasi ditekan
                        showTermsDialog = false
                    }
                ) {
                    Text("Oke, Saya Mengerti")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Buat Akun Baru",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Dengan membuat akun, data obat & catatan kesehatanmu akan tersimpan aman dan bisa diakses kapan saja.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(28.dp))

        // Input Field Nama
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Nama Lengkap", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = { viewModel.onEvent(AuthEvent.OnFullNameChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Masukkan nama lengkap...", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nama Icon") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Field Email
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Alamat Email", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEvent(AuthEvent.OnEmailChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Masukkan alamat email...", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Field Password
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Kata Sandi", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onEvent(AuthEvent.OnPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Masukkan kata sandi...", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (uiState.showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { viewModel.onEvent(AuthEvent.ToggleShowPassword) }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Field Confirm Password
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Konfirmasi Kata Sandi", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onEvent(AuthEvent.OnConfirmPasswordChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Konfirmasi kata sandi...", style = MaterialTheme.typography.bodyMedium) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
                visualTransformation = if (uiState.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (uiState.showConfirmPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { viewModel.onEvent(AuthEvent.ToggleShowConfirmPassword) }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Terms n Condition
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.agreedToTerms,
                onCheckedChange = { viewModel.onEvent(AuthEvent.OnTermsAgreeChange(it)) },
                modifier = Modifier.offset(x = (-14).dp)
            )
            Text(
                "Setujui syarat & ketentuan",
                style = MaterialTheme.typography.bodyMedium, textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .offset(x = (-16).dp)
                    .clickable { showTermsDialog = true },
                color = Blue600
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Daftar
        Button(
            onClick = { viewModel.onEvent(AuthEvent.RegisterClick) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue600,
                contentColor = Color.White
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Daftar Sekarang!", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Link ke Halaman Masuk
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Sudah punya akun? ", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Masuk",
                color = Blue600,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    WarasInTheme {
        RegistrationScreen(
            onNavigateToLogin = {},
            onRegisterSuccess = {}
        )
    }
}