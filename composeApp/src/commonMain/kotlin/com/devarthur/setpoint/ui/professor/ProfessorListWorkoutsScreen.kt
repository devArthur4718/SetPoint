package com.devarthur.setpoint.ui.professor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
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
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.domain.WorkoutTemplate
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.EmptyState
import com.devarthur.setpoint.ui.components.LoadingContent
import com.devarthur.setpoint.ui.components.SetPointListCard
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton
import com.devarthur.setpoint.ui.motion.LocalReduceMotion

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
            loading -> LoadingContent(modifier = it)
            templates.isEmpty() -> EmptyState(
                message = "Nenhum treino cadastrado.",
                modifier = it,
                action = {
                    SetPointPrimaryButton(onClick = onNavigateToCreateWorkout, text = "Criar treino")
                },
            )
            else -> LazyColumn(
                modifier = it,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(templates, key = { _, t -> t.id }) { index, t ->
                    val reduceMotion = LocalReduceMotion.current
                    val visible = remember(t.id) { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(if (reduceMotion) 0L else (index * 40L).coerceAtMost(200L))
                        visible.value = true
                    }
                    AnimatedVisibility(
                        visible = visible.value,
                        enter = if (reduceMotion) fadeIn(tween(0)) else fadeIn(tween(150)) + slideInVertically(tween(150)) { it / 4 },
                    ) {
                        SetPointListCard(onClick = {}) {
                            Text(t.name, style = MaterialTheme.typography.titleMedium)
                            Text("${t.exercises.size} exercício(s)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
