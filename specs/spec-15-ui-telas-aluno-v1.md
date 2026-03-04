# feature: UI — Telas do aluno

Definir as **telas do fluxo aluno** no app (composeApp): lista de treinos atribuídos, executar treino (registrar séries) e histórico de execuções. Todas consomem os use cases já implementados no shared (specs 10–12).

## requisitos

- **Escopo**: módulo **composeApp**; usuário atual com role STUDENT (studentUserId = userId do usuário logado/selecionado na entrada).
- **Telas**:
  1. **Home / Treinos atribuídos**: lista os treinos atribuídos ao aluno. Fonte: GetMyAssignedWorkoutsUseCase(studentUserId). Exibir para cada item: nome do treino (template.name), quantidade de exercícios, e ação “Executar” (ou “Iniciar”) que leva à tela de executar treino para aquela atribuição.
  2. **Executar treino**: tela associada a um WorkoutAssignment. Exibir o template (nome, exercícios com sets/reps/carga/descanso). Permitir “Iniciar execução” (registrar WorkoutExecution com lista vazia de séries) ou “Registrar séries”: para cada WorkoutExercise do template, permitir informar por série o actualReps e actualLoadKg (opcional). Ao submeter: chamar RecordWorkoutExecutionUseCase(workoutAssignmentId, studentUserId, setExecutionItems, executedAt). executedAt = momento atual (epoch millis — obtido na plataforma e passado ao use case). Sucesso: feedback e voltar ou ir para histórico. Falha: mostrar mensagem.
  3. **Meu histórico**: lista as execuções do aluno ordenadas por data (mais recente primeiro). Fonte: GetMyWorkoutHistoryUseCase(studentUserId). Exibir para cada execução: data (executedAt), treino (pode enriquecer com nome do template se o use case retornar ou buscar por assignment), e resumo das séries (SetExecution).
- **Navegação**: da home do aluno acessar “Executar” um treino atribuído e “Meu histórico”; da tela executar treino voltar ou ir para histórico. Detalhes do grafo nas tasks.
- **Estado e dados**: ViewModels que chamam GetMyAssignedWorkoutsUseCase, RecordWorkoutExecutionUseCase, GetMyWorkoutHistoryUseCase; loading e erro tratados na UI.
- **Offline**: dados lidos do cache (use cases já leem do repositório local); telas devem funcionar sem rede; mensagens de erro apenas para falhas do use case (ex.: validação).

## regras de negócio

- Todas as ações usam o studentUserId do usuário atual.
- Lista vazia de treinos atribuídos: exibir estado vazio (ex.: “Nenhum treino atribuído”).
- Erros dos use cases (ex.: atribuição não pertence ao aluno, workoutExerciseId inválido) devem ser exibidos ao usuário.

## critérios de aceitação

- Existe tela que lista treinos atribuídos ao aluno (GetMyAssignedWorkoutsUseCase); cada item permite abrir “Executar treino”.
- Existe tela “Executar treino” para uma atribuição: exibe template e exercícios; permite registrar execução (com ou sem séries); chama RecordWorkoutExecutionUseCase com executedAt; sucesso e falha tratados.
- Existe tela “Meu histórico” com lista de execuções do aluno (GetMyWorkoutHistoryUseCase), ordenada por data.
- Navegação entre essas telas funciona. Implementação no composeApp; shared não é alterado.

---

*Versão: v1 — 2025-03-03*
*Depende de: specs 10–12 (use cases), spec-13 (entrada — studentUserId)*
*Escopo: composeApp*
