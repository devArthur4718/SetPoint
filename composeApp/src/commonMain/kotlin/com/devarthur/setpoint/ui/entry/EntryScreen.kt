package com.devarthur.setpoint.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarthur.setpoint.ui.components.SetPointActionCard
import com.devarthur.setpoint.ui.components.SetPointLogo

@Composable
fun EntryScreen(
    onSelectProfessor: () -> Unit,
    onSelectStudent: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SetPointLogo(size = 96.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SetPoint",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Treino e acompanhamento",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Escolha como entrar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.weight(1f))
        SetPointActionCard(
            onClick = onSelectProfessor,
            modifier = Modifier.fillMaxWidth().widthIn(max = 400.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Text(
                text = "Entrar como professor",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        SetPointActionCard(
            onClick = onSelectStudent,
            modifier = Modifier.fillMaxWidth().widthIn(max = 400.dp),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ) {
            Text(
                text = "Entrar como aluno",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onCreateAccount) {
            Text(
                text = "Criar conta",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
