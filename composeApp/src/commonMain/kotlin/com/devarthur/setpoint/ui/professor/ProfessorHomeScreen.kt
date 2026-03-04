package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.ui.components.AppBarScreen
import com.devarthur.setpoint.ui.components.SetPointActionCard
import com.devarthur.setpoint.ui.components.SetPointTextButton

@Composable
fun ProfessorHomeScreen(
    onListStudents: () -> Unit,
    onListWorkouts: () -> Unit,
    onAssignWorkout: () -> Unit,
    onLogout: () -> Unit,
) {
    AppBarScreen(
        title = "Professor",
        onBack = null,
        actions = {
            SetPointTextButton(
                onClick = onLogout,
                modifier = Modifier.semantics { contentDescription = "Sair" },
                text = "Sair",
            )
        },
    ) {
        Column(
            modifier = it,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SetPointActionCard(
                onClick = onListStudents,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    text = "Lista de alunos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            SetPointActionCard(
                onClick = onListWorkouts,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ) {
                Text(
                    text = "Lista de treinos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            SetPointActionCard(
                onClick = onAssignWorkout,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Text(
                    text = "Atribuir treino a aluno",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}
