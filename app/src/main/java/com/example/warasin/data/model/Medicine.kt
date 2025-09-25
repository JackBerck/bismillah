package com.example.warasin.data.model

data class Medicine (
    val id: Int,
    val name: String,
    val dosage: String,
    val times: List<String>,
    val description: String
)