package com.example.academia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Professor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val contato: String,
    val especializacoes: String
)
