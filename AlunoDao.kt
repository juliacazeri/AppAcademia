package com.example.academia

import androidx.room.Dao
import androidx.room.Insert
import androix.room.Delete
import androidx.room.Query
import androix.room.Update

@Dao
interface AlunoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirAluno(aluno: Aluno)

    @Delete
    suspend fun excluirAluno(aluno: Aluno)

    @Query("SELECT * FROM Aluno")
    suspend fun getTodosAlunos(): List<Aluno>

    @Query("SELECT * FROM Aluno WHERE id = :id")
    suspend fun getAlunoPorId(id: Int): Aluno?

    @Update
    suspend fun atualizarAluno(aluno: Aluno)
}
