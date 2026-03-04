package com.devarthur.setpoint.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.devarthur.setpoint.application.usecase.AssignedWorkoutView
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.EmptyState
import com.devarthur.setpoint.ui.components.LoadingContent
import com.devarthur.setpoint.ui.components.SetPointListCard
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton
import com.devarthur.setpoint.ui.components.SetPointTextButton

@Composable
fun StudentHomeScreen(
    studentUserId: String,
    onExecuteWorkout: (assignmentId: String) -> Unit,
    onMyHistory: () -> Unit,
    onLogout: () -> Unit,
) {
    var assigned by remember { mutableStateOf<List<AssignedWorkoutView>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(studentUserId) {
        loading = true
        AppDependencies.getMyAssignedWorkoutsUseCase.execute(studentUserId).getOrNull()?.let { assigned = it }
        loading = false
    }

    AppBarScreen(
        title = "Aluno",
        onBack = null,
        actions = {
            SetPointTextButton(onClick = onLogout, text = "Sair")
        },
    ) {
        Column(modifier = it) {
            SetPointPrimaryButton(
                onClick = onMyHistory,
                text = "Meu histórico",
            )
            if (loading) {
                LoadingContent(modifier = Modifier.fillMaxWidth())
            } else if (assigned.isEmpty()) {
                EmptyState(
                    message = "Nenhum treino atribuído.",
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(assigned) { view ->
                        SetPointListCard(onClick = { onExecuteWorkout(view.assignment.id) }) {
                            Text(view.template.name, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${view.template.exercises.size} exercício(s)",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            SetPointPrimaryButton(
                                onClick = { onExecuteWorkout(view.assignment.id) },
                                modifier = Modifier.padding(top = 8.dp),
                                text = "Executar",
                            )
                        }
                    }
                }
            }
        }
    }
}
