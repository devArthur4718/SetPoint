package com.devarthur.setpoint.ui.student

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.application.usecase.AssignedWorkoutView
import com.devarthur.setpoint.ui.motion.LocalReduceMotion
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.ui.components.AppBarScreen

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
            Text(
                text = "Sair",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = onLogout),
            )
        },
    ) {
        Column(modifier = it) {
            Button(
                onClick = onMyHistory,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Meu histórico")
            }
            if (loading) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                    Text("Carregando...", modifier = Modifier.padding(top = 8.dp), style = MaterialTheme.typography.bodyMedium)
                }
            } else if (assigned.isEmpty()) {
                Text("Nenhum treino atribuído.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(assigned, key = { _, v -> v.assignment.id }) { index, view ->
                        val reduceMotion = LocalReduceMotion.current
                        val visible = remember(view.assignment.id) { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(if (reduceMotion) 0L else (index * 40L).coerceAtMost(200L))
                            visible.value = true
                        }
                        AnimatedVisibility(
                            visible = visible.value,
                            enter = if (reduceMotion) fadeIn(tween(0)) else fadeIn(tween(150)) + slideInVertically(tween(150)) { it / 4 },
                        ) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(view.template.name, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "${view.template.exercises.size} exercício(s)",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                    Button(
                                        onClick = { onExecuteWorkout(view.assignment.id) },
                                        modifier = Modifier.padding(top = 8.dp),
                                    ) {
                                        Text("Executar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
