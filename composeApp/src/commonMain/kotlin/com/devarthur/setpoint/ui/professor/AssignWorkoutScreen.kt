package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.WorkoutTemplate
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.ErrorMessage
import com.devarthur.setpoint.ui.components.SetPointPrimaryButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AssignWorkoutScreen(
    trainerId: String,
    onSuccess: () -> Unit,
    onBack: () -> Unit,
) {
    var templates by remember { mutableStateOf<List<WorkoutTemplate>>(emptyList()) }
    var students by remember { mutableStateOf<List<User>>(emptyList()) }
    var selectedTemplateId by remember { mutableStateOf<String?>(null) }
    var selectedStudentId by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(trainerId) {
        templates = AppDependencies.workoutTemplateRepository.listByTrainerId(trainerId)
        students = AppDependencies.userRepository.list().filter { it.role == Role.STUDENT }
    }

    AppBarScreen(
        title = "Atribuir treino",
        onBack = onBack,
        snackbarHostState = snackbarHostState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            Text("Template:", style = MaterialTheme.typography.titleSmall)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(templates) { t ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp),
                    ) {
                        RadioButton(
                            selected = selectedTemplateId == t.id,
                            onClick = { selectedTemplateId = t.id },
                        )
                        Text(t.name, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Text("Aluno:", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(students) { s ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp),
                    ) {
                        RadioButton(
                            selected = selectedStudentId == s.id,
                            onClick = { selectedStudentId = s.id },
                        )
                        Text("${s.name} (${s.email})", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            if (error != null) {
                ErrorMessage("Erro: $error", modifier = Modifier.fillMaxWidth())
            }
            SetPointPrimaryButton(
                onClick = {
                    val tid = selectedTemplateId
                    val sid = selectedStudentId
                    if (tid == null || sid == null) {
                        error = "Selecione template e aluno"
                        return@SetPointPrimaryButton
                    }
                    loading = true
                    error = null
                    scope.launch {
                        val result = AppDependencies.assignWorkoutToStudentUseCase.execute(tid, sid, trainerId)
                        loading = false
                        result.fold(
                            onSuccess = {
                                snackbarHostState.showSnackbar("Treino atribuído com sucesso!")
                                delay(1500)
                                onSuccess()
                            },
                            onFailure = { error = it.message ?: "Erro" },
                        )
                    }
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                text = if (loading) "Atribuindo..." else "Atribuir",
            )
        }
    }
}
