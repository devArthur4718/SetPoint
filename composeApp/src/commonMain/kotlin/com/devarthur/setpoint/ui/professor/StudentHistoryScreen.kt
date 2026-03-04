package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.devarthur.setpoint.ui.components.EmptyState
import com.devarthur.setpoint.ui.components.ErrorMessage
import com.devarthur.setpoint.ui.components.LoadingContent
import com.devarthur.setpoint.ui.components.SetPointListCard

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
            loading -> LoadingContent(modifier = it)
            error != null -> ErrorMessage("Erro: $error", modifier = it)
            result == null || result!!.assignmentsWithExecutions.isEmpty() -> EmptyState(
                message = "Nenhuma execução registrada.",
                modifier = it,
            )
            else -> LazyColumn(
                modifier = it,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(result!!.assignmentsWithExecutions) { awe ->
                    SetPointListCard(onClick = {}) {
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
