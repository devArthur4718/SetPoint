package com.devarthur.setpoint.ui.student

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
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.ui.components.AppBarScreen

@Composable
fun MyHistoryScreen(
    studentUserId: String,
    onBack: () -> Unit,
) {
    var executions by remember { mutableStateOf<List<WorkoutExecution>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(studentUserId) {
        loading = true
        error = null
        AppDependencies.getMyWorkoutHistoryUseCase.execute(studentUserId).fold(
            onSuccess = { executions = it },
            onFailure = { error = it.message },
        )
        loading = false
    }

    AppBarScreen(title = "Meu histórico", onBack = onBack) {
        when {
            loading -> Text("Carregando...", modifier = Modifier.padding(16.dp))
            error != null -> Text(
                "Erro: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp),
            )
            executions.isEmpty() -> Text("Nenhuma execução.", modifier = Modifier.padding(16.dp))
            else -> LazyColumn(
                modifier = it,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(executions) { ex ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Execução: ${ex.executedAt}",
                                style = MaterialTheme.typography.titleSmall,
                            )
                            Text(
                                "${ex.setExecutions.size} série(s) registrada(s)",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}
