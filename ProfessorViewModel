import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfessorViewModel : ViewModel() {
    private val _professores = MutableStateFlow<List<Professor>>(emptyList())
    val professores: StateFlow<List<Professor>> = _professores

    init {
        carregarProfessores()
    }

    private fun carregarProfessores() {
        _professores.value = listOf(
            Professor(1, "Carlos", "123456789", "Musculação"),
            Professor(2, "Ana", "987654321", "Yoga")
        )
    }

    fun adicionarOuAtualizarProfessor(professor: Professor) {
        val listaAtualizada = _professores.value.toMutableList()
        val index = listaAtualizada.indexOfFirst { it.id == professor.id }
        if (index != -1) {
            listaAtualizada[index] = professor
        } else {
            listaAtualizada.add(professor)
        }
        _professores.value = listaAtualizada
    }

    fun excluirProfessor(id: Int) {
        _professores.value = _professores.value.filter { it.id != id }
    }
}
