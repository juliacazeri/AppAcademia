package com.example.academia

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlunoViewModel : ViewModel() {
    private val _alunos = MutableStateFlow<List<Aluno>>(emptyList())
    val alunos: StateFlow<List<Aluno>> = _alunos

    init {
        carregarAlunos()
    }

    private fun carregarAlunos() {
        _alunos.value = listOf(
            Aluno(1, "João", 25, "12345678", "Mensal"),
            Aluno(2, "Maria", 22, "87654321", "Trimestral")
        )
    }

    fun adicionarOuAtualizarAluno(aluno: Aluno) {
        val listaAtualizada = _alunos.value.toMutableList()
        val index = listaAtualizada.indexOfFirst { it.id == aluno.id }
        if (index != -1) {
            listaAtualizada[index] = aluno
        } else {
            listaAtualizada.add(aluno)
        }
        _alunos.value = listaAtualizada
    }

    fun excluirAluno(id: Int) {
        _alunos.value = _alunos.value.filter { it.id != id }
    }

    fun obterAlunoPorId(id: Int?): Aluno? {
        return _alunos.value.find { it.id == id }
    }
}
