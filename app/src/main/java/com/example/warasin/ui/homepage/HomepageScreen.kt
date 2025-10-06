package com.example.warasin.ui.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.warasin.R
import com.example.warasin.data.model.HealthNote
import com.example.warasin.ui.auth.AuthViewModel
import com.example.warasin.ui.theme.Blue100
import com.example.warasin.ui.theme.Blue600
import com.example.warasin.ui.theme.Gray300
import com.example.warasin.ui.theme.Gray50
import com.example.warasin.ui.theme.Green100
import com.example.warasin.ui.theme.Green600
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomepageScreen(
    navController: NavController,
    viewModel: HomepageViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val schedules by viewModel.schedules.collectAsState()
    val healthNotes by viewModel.healthNotes.collectAsState()
    val (_, userName) = authViewModel.getCurrentUserData()

    val displayName = userName.ifEmpty { "User" }
    val calendar = Calendar.getInstance()

    val greeting = when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 0..10 -> "Selamat Pagi"
        in 11..14 -> "Selamat Siang"
        in 15..17 -> "Selamat Sore"
        else -> "Selamat Malam"
    }

    val todaySchedules = schedules.filter { scheduleWithMedicine ->
        val todayId = calendar.get(Calendar.DAY_OF_WEEK)
        val todayInYourSystem = when (todayId) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            Calendar.SUNDAY -> 7
            else -> -1
        }
        val daysString = scheduleWithMedicine.schedule.selectedDays
        val daysList = if (daysString.isNotBlank()) daysString.split(',') else emptyList()
        daysList.contains(todayInYourSystem.toString())
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            Column {
                Text(
                    text = "$greeting, $displayName!",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Jangan lupa minum obat hari ini dan tetap jaga kesehatan ya!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Gray300), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            text = "Jadwal Minum Obat",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    if (todaySchedules.isEmpty()) {
                        Text(
                            text = "Belum ada jadwal obat hari ini",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Gray300,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            todaySchedules.forEach { schedule ->
                                HomeMedicineItem(
                                    schedule = schedule,
                                    onMarkAsTaken = { viewModel.markScheduleAsTaken(schedule.schedule.id) },
                                    onMarkAsNotTaken = { viewModel.markScheduleAsNotTaken(schedule.schedule.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Gray300), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                if (healthNotes.isNotEmpty()) {
                    RecentHealthNotesSection(healthNotes = healthNotes[0])
                } else {
                    Text(
                        text = "Belum ada catatan kesehatan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray300,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate("medicine_screen") {
                            popUpTo("homepage") { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue600),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = null,
                            tint = Gray50
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tambah Obat",
                            color = Gray50,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate("note_screen") {
                            popUpTo("homepage") { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Blue600),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue600),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_add_notes_24),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tambah Catatan",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentHealthNotesSection(healthNotes: HealthNote) {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_trending_up_24),
                contentDescription = "Trending Icon",
                tint = Green600,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Status Kesehatan",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Blue100, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Tekanan Darah",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${healthNotes.bloodPressure} mmHg",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue600,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Green100, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Gula Darah",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${healthNotes.bloodSugar} mg/dL",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Green600,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Terakhir diperbarui: ${dateFormatter.format(Date(healthNotes.timestamp))}",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
