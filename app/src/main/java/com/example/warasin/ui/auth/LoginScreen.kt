package com.example.warasin.ui.auth

import android.credentials.GetCredentialException
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.warasin.R
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.WarasInTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Tampilkan Toast atau navigasi berdasarkan perubahan state di ViewModel
    LaunchedEffect(key1 = uiState.registrationError) {
        uiState.registrationError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onEvent(AuthEvent.ErrorShown)
        }
    }
    LaunchedEffect(key1 = uiState.registrationSuccess) {
        if (uiState.registrationSuccess) {
            Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
            onLoginSuccess()
        }
    }

    // Fungsi Google Sign-In, sekarang memanggil ViewModel
    fun signInWithGoogle() {
        coroutineScope.launch {
            val credentialManager = CredentialManager.create(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(request = request, context = context)
                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                // Panggil fungsi di ViewModel untuk memproses kredensial
                viewModel.signInWithGoogleCredential(firebaseCredential)

            } catch (e: GetCredentialException) {
                Toast.makeText(context, "Gagal ambil credential: ${e.message}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header
        Text(
            text = "Masuk ke Akunmu",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Selamat datang kembali! Akses jadwal obat dan catatan kesehatanmu dalam sekali login.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Input Field Email
        Column (modifier = Modifier.fillMaxWidth()){
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
        Column (modifier = Modifier.fillMaxWidth()){
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

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Login
        Button(
            onClick = { viewModel.onEvent(AuthEvent.LoginClick) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue600,
                contentColor = Color.White
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Masuk", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Google
        OutlinedButton(
            onClick = { signInWithGoogle() },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Google Logo",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Masuk dengan Google", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Link ke Halaman Daftar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Belum punya akun? ", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Daftar",
                color = Blue600,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    WarasInTheme {
        LoginScreen(
            onNavigateToRegister = {},
            onLoginSuccess = {}
        )
    }
}