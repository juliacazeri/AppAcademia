package com.example.academia

import androidx.room.Dao
import androidx.room.Insert
import androix.room.Delete
import androidx.room.Query
import androix.room.Update

@Dao
interface ProfessorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirProfessor(professor: Professor)

    @Update
    suspend fun atualizarProfessor(professor: Professor)

    @Delete
    suspend fun deletarProfessor(professor: Professor)

}
