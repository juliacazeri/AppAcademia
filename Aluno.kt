package com.example.academia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Aluno(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val idade: Int,
    val telefone: String,
    val plano: String
)
