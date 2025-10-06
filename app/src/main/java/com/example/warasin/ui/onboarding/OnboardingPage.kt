package com.example.warasin.ui.onboarding

import androidx.annotation.DrawableRes
import com.example.warasin.R

sealed class OnboardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object FirstPages : OnboardingPage(
        image = R.drawable.onboard_1,
        title = "Jangan Lagi Lupa Minum Obah",
        description = "Aplikasi akan mengingatkan jadwal obatmu secara otomatis, agar pengobatan lebih teratur dan efektif."
    )

    object SecondPages : OnboardingPage(
        image = R.drawable.onboard_2,
        title = "Pantau Kesehatan dengan Mudah",
        description = "Catat tekanan darah, gula darah, atau kondisi harianmu hanya dengan beberapa klik. Semua tersimpan rapi di satu tempat."
    )

    object ThirdPages : OnboardingPage(
        image = R.drawable.onboard_3,
        title = "Mudah Digunakan untuk Semua",
        description = "Desain sederhana dengan tulisan besar dan navigasi mudah, cocok untuk lansia maupun pasien penyakit kronis."
    )
}
