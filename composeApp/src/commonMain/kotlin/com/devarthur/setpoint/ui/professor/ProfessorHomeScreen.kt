package com.devarthur.setpoint.ui.professor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.ui.components.AppBarScreen

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
        Column(
            modifier = it,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onListStudents),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Text(
                    text = "Lista de alunos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(20.dp),
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onListWorkouts),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Text(
                    text = "Lista de treinos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(20.dp),
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAssignWorkout),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Text(
                    text = "Atribuir treino a aluno",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(20.dp),
                )
            }
        }
    }
}
