package com.devarthur.setpoint.ui.professor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.motion.LocalReduceMotion

@Composable
fun ProfessorListStudentsScreen(
    onNavigateToCreateStudent: () -> Unit,
    onNavigateToStudentHistory: (studentUserId: String) -> Unit,
    onBack: () -> Unit,
) {
    var students by remember { mutableStateOf<List<User>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        loading = true
        error = null
        val list = AppDependencies.userRepository.list().filter { it.role == Role.STUDENT }
        students = list
        loading = false
    }

    AppBarScreen(
        title = "Alunos",
        onBack = onBack,
        actions = {
            Button(onClick = onNavigateToCreateStudent) { Text("Criar aluno") }
        },
    ) {
        when {
            loading -> Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
                Text("Carregando...", modifier = Modifier.padding(top = 8.dp), style = MaterialTheme.typography.bodyMedium)
            }
            error != null -> Text(
                "Erro: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp),
            )
            students.isEmpty() -> Column(Modifier.padding(16.dp)) {
                Text("Nenhum aluno.")
                Button(onClick = onNavigateToCreateStudent, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Criar aluno")
                }
            }
            else -> LazyColumn(
                modifier = it,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(students, key = { _, u -> u.id }) { index, user ->
                    val reduceMotion = LocalReduceMotion.current
                    val visible = remember(user.id) { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(if (reduceMotion) 0L else (index * 40L).coerceAtMost(200L))
                        visible.value = true
                    }
                    AnimatedVisibility(
                        visible = visible.value,
                        enter = if (reduceMotion) fadeIn(tween(0)) else fadeIn(tween(150)) + slideInVertically(tween(150)) { it / 4 },
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToStudentHistory(user.id) },
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(user.name, style = MaterialTheme.typography.titleMedium)
                                Text(user.email, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
