package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.domain.WorkoutTemplate
import com.devarthur.setpoint.ui.components.AppBarScreen

@Composable
fun ProfessorListWorkoutsScreen(
    trainerId: String,
    onNavigateToCreateWorkout: () -> Unit,
    onBack: () -> Unit,
) {
    var templates by remember { mutableStateOf<List<WorkoutTemplate>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(trainerId) {
        loading = true
        templates = AppDependencies.workoutTemplateRepository.listByTrainerId(trainerId)
        loading = false
    }

    AppBarScreen(
        title = "Treinos",
        onBack = onBack,
        actions = { Button(onClick = onNavigateToCreateWorkout) { Text("Criar treino") } },
    ) {
        when {
            loading -> Text("Carregando...", modifier = Modifier.padding(16.dp))
            templates.isEmpty() -> Column(Modifier.padding(16.dp)) {
                Text("Nenhum treino.")
                Button(onClick = onNavigateToCreateWorkout, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Criar treino")
                }
            }
            else -> LazyColumn(
                modifier = it,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(templates) { t ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(t.name, style = MaterialTheme.typography.titleMedium)
                            Text("${t.exercises.size} exercício(s)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
