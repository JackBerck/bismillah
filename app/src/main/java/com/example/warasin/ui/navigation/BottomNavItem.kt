package com.example.warasin.ui.navigation

import androidx.annotation.DrawableRes
import com.example.warasin.R

data class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "Beranda",
        icon = R.drawable.outline_home_24,
        route = "home_screen"
    ),
    BottomNavItem(
        title = "Obat",
        icon = R.drawable.outline_medication_24,
        route = "medicine_screen"
    ),
    BottomNavItem(
        title = "Riwayat",
        icon = R.drawable.outline_history_24,
        route = "history_screen"
    ),
    BottomNavItem(
        title = "Catatan",
        icon = R.drawable.outline_notes_24,
        route = "note_screen"
    ),
    BottomNavItem(
        title = "Profil",
        icon = R.drawable.outline_person_24,
        route = "profile_screen"
    )
)