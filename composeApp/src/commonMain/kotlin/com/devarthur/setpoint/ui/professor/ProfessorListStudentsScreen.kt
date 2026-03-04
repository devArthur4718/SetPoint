package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.EmptyState
import com.devarthur.setpoint.ui.components.ErrorMessage
import com.devarthur.setpoint.ui.components.LoadingContent
import com.devarthur.setpoint.ui.components.SetPointListCard
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton

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
        actions = { Button(onClick = onNavigateToCreateStudent) { Text("Criar aluno") } },
    ) {
        when {
            loading -> LoadingContent(modifier = it)
            error != null -> ErrorMessage(
                "Erro: $error",
                modifier = it,
            )
            students.isEmpty() -> EmptyState(
                message = "Nenhum aluno cadastrado.",
                modifier = it,
                action = {
                    SetPointPrimaryButton(
                        onClick = onNavigateToCreateStudent,
                        text = "Criar aluno",
                    )
                },
            )
            else -> LazyColumn(
                modifier = it,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(students) { user ->
                    SetPointListCard(onClick = { onNavigateToStudentHistory(user.id) }) {
                        Text(user.name, style = MaterialTheme.typography.titleMedium)
                        Text(user.email, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
