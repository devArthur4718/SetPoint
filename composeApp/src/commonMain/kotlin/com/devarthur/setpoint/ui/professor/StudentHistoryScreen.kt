package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import com.devarthur.setpoint.application.usecase.StudentWorkoutHistoryResult
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.ui.components.AppBarScreen

@Composable
fun StudentHistoryScreen(
    studentUserId: String,
    trainerId: String,
    onBack: () -> Unit,
) {
    var result by remember { mutableStateOf<StudentWorkoutHistoryResult?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(studentUserId, trainerId) {
        loading = true
        error = null
        AppDependencies.getStudentWorkoutHistoryUseCase.execute(studentUserId, trainerId)
            .fold(
                onSuccess = { result = it },
                onFailure = { error = it.message },
            )
        loading = false
    }

    AppBarScreen(title = "Histórico do aluno", onBack = onBack) {
        when {
            loading -> Text("Carregando...", modifier = Modifier.padding(16.dp))
            error != null -> Text(
                "Erro: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp),
            )
            result == null || result!!.assignmentsWithExecutions.isEmpty() -> Text(
                "Nenhuma execução registrada.",
                modifier = Modifier.padding(16.dp),
            )
            else -> LazyColumn(
                modifier = it,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(result!!.assignmentsWithExecutions) { awe ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Atribuição ${awe.assignment.id.take(8)}...", style = MaterialTheme.typography.titleSmall)
                            awe.executions.forEach { ex ->
                                Text(
                                    "  Execução: ${ex.executedAt} — ${ex.setExecutions.size} série(s)",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
