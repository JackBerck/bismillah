package com.example.warasin.ui.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray950
import com.example.warasin.ui.theme.WarasInTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityPrivacyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Keamanan dan Privasi",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Blue600
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
        ) {
            item {
                Text(
                    text = "Kebijakan Keamanan dan Privasi",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Terakhir diperbarui: 5 Oktober 2025",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray300
                )
            }

            item {
                PolicyCard(
                    paragraph = "WarasIn berkomitmen untuk melindungi privasi dan keamanan data pribadi Anda. Kebijakan Privasi ini menjelaskan jenis informasi yang kami kumpulkan, bagaimana kami menggunakannya, serta hak-hak Anda terkait data tersebut."
                )
            }

            item {
                PolicyCard(
                    title = "Informasi yang Kami Kumpulkan",
                    items = listOf(
                        "Data Akun: Saat Anda mendaftar, kami mengumpulkan nama lengkap dan alamat email Anda untuk membuat dan mengelola akun Anda.",
                        "Data Kesehatan: Informasi yang Anda masukkan secara sukarela, seperti jadwal minum obat dan catatan kesehatan, disimpan secara aman dan hanya dapat diakses oleh Anda.",
                        "Data Penggunaan: Kami dapat mengumpulkan data anonim tentang bagaimana Anda berinteraksi dengan aplikasi untuk tujuan perbaikan layanan."
                    )
                )
            }

            item {
                PolicyCard(
                    title = "Bagaimana Kami Menggunakan Informasi Anda",
                    paragraph = "Informasi Anda digunakan untuk menyediakan, memelihara, dan meningkatkan layanan kami. Kami tidak akan membagikan informasi pribadi Anda kepada pihak ketiga tanpa persetujuan Anda, kecuali diwajibkan oleh hukum."
                )
            }

            item {
                PolicyCard(
                    title = "Keamanan Data",
                    paragraph = "WarasIn menggunakan enkripsi dan penyimpanan berbasis server yang aman untuk melindungi data Anda dari akses yang tidak sah. Namun, kami tetap menganjurkan Anda menjaga keamanan akun secara pribadi, seperti tidak membagikan kata sandi."
                )
            }

            item {
                PolicyCard(
                    title = "Hak Anda",
                    paragraph = "Anda memiliki hak untuk :",
                    items = listOf(
                        "Melihat atau memperbarui data pribadi Anda,",
                        "Menghapus akun dan seluruh data yang terkait,",
                        "Menghubungi kami jika ingin menarik persetujuan penggunaan data."
                    )
                )
            }

            item {
                ContactSection()
            }
        }
    }
}

@Composable
private fun PolicyCard(
    title: String? = null,
    paragraph: String? = null,
    items: List<String>? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, Blue600.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Gray950
                    )
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .height(1.dp),
                    thickness = DividerDefaults.Thickness,
                    color = Blue600.copy(alpha = 0.2f)
                )
            }
            paragraph?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )
                )
            }
            items?.forEach { itemText ->
                ListItemModern(text = itemText)
            }
        }
    }
}

@Composable
private fun ListItemModern(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(start = 4.dp)
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyLarge.copy(color = Blue600)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}

@Composable
private fun ContactSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blue600.copy(alpha = 0.08f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Butuh Bantuan atau Ingin Menghubungi Kami?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Blue600
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = Blue600
                )
                Text(
                    text = "support@warasin.id",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Blue600)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Telepon",
                    tint = Blue600
                )
                Text(
                    text = "+62 812 3456 7890",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Blue600)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tim kami siap membantu pertanyaan atau permintaan Anda terkait privasi dan keamanan data.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Blue600.copy(alpha = 0.8f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecurityPrivacyScreenPreview() {
    WarasInTheme {
        SecurityPrivacyScreen(navController = rememberNavController())
    }
}
