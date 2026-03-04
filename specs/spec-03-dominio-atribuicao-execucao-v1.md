# feature: Domínio de atribuição e execução de treino

Definição das entidades **WorkoutAssignment**, **WorkoutExecution** e **SetExecution**, que permitem ao professor atribuir um treino (template) ao aluno e ao aluno registrar a execução (séries realizadas, carga e repetições reais). Implementação em código compartilhado (shared), camada de domínio, sem UI.

## requisitos

- **WorkoutAssignment**: atribuição de um template a um aluno pelo professor.
  - Campos: `id`, `workoutTemplateId`, `studentUserId` (id do User com role STUDENT), `assignedAt` (opcional, Long epoch millis).
  - Referências: `workoutTemplateId` e `studentUserId` obrigatórias (não vazias).
  - Criação apenas via factory; entidade imutável.
- **WorkoutExecution**: uma sessão de execução do treino pelo aluno (uma vez que o aluno “fez” o treino).
  - Campos: `id`, `workoutAssignmentId`, `executedAt` (Long epoch millis), `setExecutions` (lista de SetExecution).
  - `setExecutions`: pode ser vazia (aluno iniciou e não registrou séries) ou preenchida; ordem pode ser definida por workoutExerciseId + setNumber.
  - Criação apenas via factory; entidade imutável.
- **SetExecution**: registro de uma série executada (repetições e carga reais).
  - Campos: `id`, `workoutExecutionId`, `workoutExerciseId` (referência ao WorkoutExercise do template), `setNumber` (número da série, 1-based), `actualReps` (opcional), `actualLoadKg` (opcional).
  - `setNumber`: inteiro ≥ 1.
  - `actualReps`: se presente, ≥ 0.
  - `actualLoadKg`: se presente, ≥ 0.
  - Criação apenas via factory; entidade imutável.
- Código no módulo `shared`, em `commonMain`, na camada de domínio (sem dependência de data/UI).

## regras de negócio

- WorkoutAssignment associa um template a um aluno; a unicidade (ex.: não duplicar mesma atribuição no mesmo dia) fica a cargo da camada de aplicação/repositório.
- WorkoutExecution pertence a um WorkoutAssignment; cada SetExecution pertence a uma WorkoutExecution e referencia um WorkoutExercise do template.
- Identificadores (`id`) são opacos (String); geração de ID fica fora do domínio (camada data).

## critérios de aceitação

- WorkoutAssignment é criado com workoutTemplateId e studentUserId não vazios; rejeição para qualquer um vazio.
- WorkoutExecution é criado com workoutAssignmentId não vazio e executedAt válido; setExecutions pode ser lista vazia; rejeição para workoutAssignmentId vazio.
- SetExecution é criado com workoutExecutionId e workoutExerciseId não vazios, setNumber ≥ 1; actualReps e actualLoadKg opcionais e ≥ 0 quando presentes; rejeição para dados inválidos.
- Parâmetros de criação com propriedades imutáveis (val/readonly onde aplicável).
- Testes unitários cobrem:
  - Criação e rejeição de WorkoutAssignment (ids vazios).
  - Criação e rejeição de WorkoutExecution (assignmentId vazio, lista vazia de setExecutions aceita).
  - Criação e rejeição de SetExecution (ids vazios, setNumber menor que 1, actualReps/actualLoadKg negativos).
- Código de domínio vive em `shared` (commonMain), sem referências a Android, iOS ou Compose.

---

*Versão: v1 — 2025-03-03*
