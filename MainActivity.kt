import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.app.ui.theme.YourTheme
import com.example.app.model.Aluno
import com.example.app.model.Professor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(
    alunoViewModel: AlunoViewModel = remember { AlunoViewModel() },
    professorViewModel: ProfessorViewModel = remember { ProfessorViewModel() }
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        
        composable("login") {
            LoginScreen(navController)
        }

        composable("principal") {
            val alunos by alunoViewModel.alunos.collectAsState() 
            ListaAlunosScreen(
                navController = navController,
                alunos = alunos
            )
        }

        composable("cadastroAluno") {
            CadastroAlunoScreen(
                navController = navController,
                onSave = { aluno ->
                    alunoViewModel.adicionarOuAtualizarAluno(aluno) 
                }
            )
        }

        composable(
            route = "detalhesAluno/{alunoId}",
            arguments = listOf(navArgument("alunoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val alunoId = backStackEntry.arguments?.getInt("alunoId")
            val aluno = alunoViewModel.obterAlunoPorId(alunoId)

            DetalhesAlunoScreen(
                navController = navController,
                alunoId = alunoId,
                onSave = { alunoAtualizado ->
                    alunoViewModel.adicionarOuAtualizarAluno(alunoAtualizado) 
                },
                onDelete = { id ->
                    alunoViewModel.excluirAluno(id) 
                }
            )
        }

        composable("cadastroProfessor") {
            CadastroProfessorScreen(
                navController = navController,
                professor = null, 
                onSave = { professor ->
                    professorViewModel.adicionarOuAtualizarProfessor(professor)
                },
                onDelete = { id ->
                    professorViewModel.excluirProfessor(id) 
                }
            )
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = usuario, onValueChange = { usuario = it }, label = { Text("Usuário") })
        TextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation())

        Button(onClick = {
            if (usuario == "admin" && senha == "senha") { 
                navController.navigate("principal")
            } else {
                 Toast.makeText(context, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Entrar")
        }
    }
}

@Composable
fun CadastroAlunoScreen(navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var plano by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
        TextField(value = idade, onValueChange = { idade = it }, label = { Text("Idade") })
        TextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Telefone") })
        TextField(value = id, onValueChange = { id = it }, label = { Text("ID") })
        TextField(value = plano, onValueChange = { plano = it }, label = { Text("Plano") })

        Button(onClick = {
            if (nome.isNotEmpty() && idade.isNotEmpty() && telefone.isNotEmpty() && id.isNotEmpty() && plano.isNotEmpty()) {
               
            } else {
                Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Cadastrar")
        }
    }
}

@Composable
fun ListaAlunosScreen(navController: NavHostController, alunos: List<Aluno>) {
    var searchQuery by remember { mutableStateOf("") }

    val alunosFiltrados = remember(searchQuery, alunos) {
        if (searchQuery.isEmpty()) {
            alunos
        } else {
            alunos.filter {
                it.nome.contains(searchQuery, ignoreCase = true) || it.id.toString().contains(searchQuery)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Pesquisar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (alunosFiltrados.isEmpty()) {
            Text("Nenhum aluno encontrado", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(alunosFiltrados) { aluno ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { navController.navigate("detalhesAluno/${aluno.id}") },
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nome: ${aluno.nome}")
                            Text("ID: ${aluno.id}")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DetalhesAlunoScreen(
    navController: NavHostController,
    alunoId: Int?,
    onSave: (Aluno) -> Unit, 
    onDelete: (Int) -> Unit  
) {
    val aluno = alunoId?.let {
       
        buscarAlunoPeloId(it)
    }
    var nome by remember { mutableStateOf(aluno?.nome ?: "") }
    var idade by remember { mutableStateOf(aluno?.idade?.toString() ?: "") }
    var telefone by remember { mutableStateOf(aluno?.telefone ?: "") }
    var plano by remember { mutableStateOf(aluno?.plano ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = idade,
            onValueChange = { idade = it },
            label = { Text("Idade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = plano,
            onValueChange = { plano = it },
            label = { Text("Plano") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nome.isNotEmpty() && idade.isNotEmpty() && telefone.isNotEmpty() && plano.isNotEmpty()) {
                    val alunoAtualizado = Aluno(
                        id = alunoId ?: 0,
                        nome = nome,
                        idade = idade.toInt(),
                        telefone = telefone,
                        plano = plano
                    )
                    onSave(alunoAtualizado) 
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Alterações")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                alunoId?.let {
                    onDelete(it) 
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Excluir")
        }
    }
}
    

@Composable
fun CadastroProfessorScreen(
    navController: NavHostController,
    professor: Professor? = null, 
    onDelete: (Int) -> Unit, 
    onSave: (Professor) -> Unit 
) {
    var nome by remember { mutableStateOf(professor?.nome ?: "") }
    var contato by remember { mutableStateOf(professor?.contato ?: "") }
    var especializacoes by remember { mutableStateOf(professor?.especializacoes ?: "") }
    var id by remember { mutableStateOf(professor?.id?.toString() ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = contato,
            onValueChange = { contato = it },
            label = { Text("Contato") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = especializacoes,
            onValueChange = { especializacoes = it },
            label = { Text("Especializações") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth(),
            enabled = professor == null 
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nome.isNotEmpty() && contato.isNotEmpty() && especializacoes.isNotEmpty() && id.isNotEmpty()) {
                    val novoProfessor = Professor(
                        id = id.toInt(),
                        nome = nome,
                        contato = contato,
                        especializacoes = especializacoes
                    )
                    onSave(novoProfessor) 
                    navController.popBackStack()
                } else {
                     Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (professor == null) "Cadastrar" else "Salvar Alterações")
        }

        if (professor != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onDelete(professor.id) /
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Excluir")
            }
        }
    }
}


