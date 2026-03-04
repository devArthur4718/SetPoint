# feature: Executar treino (aluno)

Permitir que o **aluno** (User com role STUDENT) **inicie e registre uma execução de treino**: criar uma WorkoutExecution para um WorkoutAssignment (treino atribuído a ele) e opcionalmente registrar as séries realizadas (SetExecution: carga e repetições reais). A operação usa os repositórios e o domínio já definidos (specs 03, 04); não envolve UI nem rede nesta spec — apenas o caso de uso.

## requisitos

- **Ator**: usuário autenticado com role STUDENT (o “aluno”).
- **Entrada**:
  - `workoutAssignmentId`: id da atribuição (treino atribuído) que o aluno está executando.
  - `studentUserId`: contexto do aluno que está executando (deve ser o dono da atribuição).
  - `setExecutions`: lista opcional de séries realizadas; cada item: workoutExerciseId, setNumber (1-based), actualReps (opcional), actualLoadKg (opcional). Pode ser vazia (aluno só “iniciou” a execução).
- **Saída**: sucesso retornando a WorkoutExecution criada (com suas SetExecution) ou falha (atribuição inexistente, atribuição não pertence ao aluno, workoutExerciseId não pertence ao template da atribuição, validação de setNumber/actualReps/actualLoadKg, falha ao persistir).
- **Fluxo**:
  1. Validar workoutAssignmentId e studentUserId não vazios.
  2. Buscar WorkoutAssignment por id; se não existir, retornar erro.
  3. Verificar se assignment.studentUserId == studentUserId; se não, retornar erro (atribuição não é do aluno).
  4. Gerar id da WorkoutExecution; executedAt = epoch millis atual (ou injetado).
  5. Para cada item em setExecutions: validar workoutExerciseId pertence ao template da atribuição (buscar template, verificar se existe WorkoutExercise com esse id); criar SetExecution via SetExecution.create(id, executionId, workoutExerciseId, setNumber, actualReps?, actualLoadKg?); em falha retornar erro.
  6. Criar WorkoutExecution via WorkoutExecution.create(id, workoutAssignmentId, executedAt, setExecutions); em falha retornar erro.
  7. Salvar WorkoutExecution via WorkoutExecutionRepository.save(execution); se falhar, retornar erro.
  8. Retornar sucesso com a WorkoutExecution criada.
- **Caso de uso**: uma classe ou função (ex.: `ExecuteWorkoutUseCase` ou `RecordWorkoutExecutionUseCase`) no shared que recebe workoutAssignmentId, studentUserId e lista de itens de série (workoutExerciseId, setNumber, actualReps?, actualLoadKg?), e retorna `Result<WorkoutExecution>`. Usa WorkoutAssignmentRepository, WorkoutTemplateRepository (para validar workoutExerciseId), WorkoutExecutionRepository e IdGenerator; não acessa UI nem rede.
- Código em `shared/commonMain`, pacote `application.usecase`; domain e data já existem.

## regras de negócio

- Apenas o aluno dono da atribuição pode registrar execução para essa atribuição.
- WorkoutExecution.executedAt obrigatório (Long epoch millis).
- SetExecution: setNumber ≥ 1; actualReps e actualLoadKg opcionais e ≥ 0 quando presentes; workoutExerciseId deve pertencer ao WorkoutTemplate da atribuição.
- Persistência: WorkoutExecution e SetExecutions salvos juntos (repositório existente já suporta).

## critérios de aceitação

- Existe caso de uso que recebe workoutAssignmentId, studentUserId e lista de séries (ou vazia) e executa o fluxo descrito.
- Em sucesso: WorkoutExecution (com setExecutions) é persistida e retornada.
- Em falha quando a atribuição não existe ou não pertence ao aluno: retorna Result.failure sem persistir.
- Em falha quando algum workoutExerciseId não pertence ao template da atribuição: retorna Result.failure sem persistir.
- Em falha de validação (setNumber < 1, actualReps/actualLoadKg negativos): retorna Result.failure sem persistir.
- Testes unitários: sucesso com lista vazia de séries; sucesso com uma ou mais séries válidas; falha quando atribuição não é do aluno; falha com workoutExerciseId inválido; falha com setNumber ou valores negativos.
- Domain e repositórios não são alterados (apenas utilizados); sem rede nem UI.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-03 (domínio WorkoutExecution/SetExecution), spec-04 (repositórios)*
