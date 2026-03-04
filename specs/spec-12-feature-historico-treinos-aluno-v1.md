# feature: Histórico de treinos (aluno)

Permitir que o **aluno** (User com role STUDENT) **visualize seu próprio histórico de execuções**: listar as WorkoutExecution do aluno (através das atribuições cujo studentUserId é o dele), ordenadas por data (executedAt), com detalhes das SetExecution. Leitura a partir do cache local (offline-first); não envolve UI nem rede nesta spec — apenas o caso de uso.

## requisitos

- **Ator**: usuário autenticado com role STUDENT (o “aluno”).
- **Entrada**:
  - `studentUserId`: id do User aluno (contexto da aplicação, ex.: usuário logado).
- **Saída**: sucesso retornando lista de execuções (WorkoutExecution com setExecutions), possivelmente enriquecida com referência ao template/nome do treino (para exibição); ou falha (studentUserId vazio, usuário inexistente ou não STUDENT).
- **Fluxo**:
  1. Validar studentUserId não vazio.
  2. (Opcional) Buscar User e verificar role STUDENT; se não existir ou não for STUDENT, retornar erro.
  3. Buscar atribuições do aluno: WorkoutAssignmentRepository.listByStudentUserId(studentUserId).
  4. Para cada atribuição, buscar execuções: WorkoutExecutionRepository.listByWorkoutAssignmentId(assignment.id).
  5. Agregar todas as execuções em uma lista; ordenar por executedAt (mais recente primeiro ou último, conforme definido).
  6. (Opcional) Para cada execução, enriquecer com nome do treino (WorkoutTemplateRepository.getById(assignment.workoutTemplateId)?.name).
  7. Retornar sucesso com a lista ordenada.
- **Caso de uso**: uma classe ou função (ex.: `GetMyWorkoutHistoryUseCase`) no shared que recebe studentUserId e retorna `Result<List<WorkoutExecution>>` ou `Result<MyWorkoutHistoryResult>` (lista de execuções, possivelmente com nome do treino). Usa WorkoutAssignmentRepository, WorkoutExecutionRepository e opcionalmente WorkoutTemplateRepository/UserRepository; não acessa UI nem rede.
- Código em `shared/commonMain`, pacote `application.usecase`; domain e data já existem.

## regras de negócio

- Apenas o próprio aluno vê seu histórico (studentUserId).
- Dados lidos do cache local; fluxo funciona offline.
- Ordenação por executedAt (ex.: decrescente = mais recente primeiro).

## critérios de aceitação

- Existe caso de uso que recebe studentUserId e retorna a lista de execuções do aluno (WorkoutExecution com setExecutions), ordenada por data.
- Em sucesso: lista contém todas as execuções das atribuições do aluno, cada uma com suas SetExecution.
- Em falha quando studentUserId vazio ou usuário inexistente/não STUDENT (se validação existir): retorna Result.failure.
- Testes unitários: sucesso com atribuições e execuções existentes; aluno sem execuções retorna lista vazia; ordem por executedAt respeitada.
- Domain e repositórios não são alterados (apenas utilizados); sem rede nem UI.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-03 (domínio WorkoutExecution/SetExecution), spec-04 (repositórios)*
