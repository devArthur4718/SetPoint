# feature: Visualizar treino atribuído (aluno)

Permitir que o **aluno** (User com role STUDENT) **visualize os treinos atribuídos a ele**: listar os WorkoutAssignment cujo studentUserId é o do aluno, e para cada um obter o WorkoutTemplate (nome, exercícios com séries, reps, carga, descanso) para exibição. Leitura a partir do cache local (offline-first); não envolve UI nem rede nesta spec — apenas o caso de uso.

## requisitos

- **Ator**: usuário autenticado com role STUDENT (o “aluno”).
- **Entrada**:
  - `studentUserId`: id do User aluno (contexto da aplicação, ex.: usuário logado).
- **Saída**: sucesso retornando lista de itens que representam “treino atribuído” (ex.: atribuição + template com exercícios), ordenável por data de atribuição ou nome do treino; ou falha (studentUserId vazio, usuário inexistente ou não STUDENT).
- **Fluxo**:
  1. Validar studentUserId não vazio.
  2. (Opcional) Buscar User por id e verificar role STUDENT; se não existir ou não for STUDENT, retornar erro.
  3. Buscar atribuições: WorkoutAssignmentRepository.listByStudentUserId(studentUserId).
  4. Para cada WorkoutAssignment, buscar WorkoutTemplate: WorkoutTemplateRepository.getById(assignment.workoutTemplateId).
  5. Montar resultado: lista de pares (ou DTO) atribuição + template (com exercises) para cada atribuição cujo template exista.
  6. Retornar sucesso com a lista.
- **Caso de uso**: uma classe ou função (ex.: `GetMyAssignedWorkoutsUseCase`) no shared que recebe studentUserId e retorna `Result<List<AssignedWorkoutView>>` (ou tipo equivalente: atribuição + template). Usa WorkoutAssignmentRepository e WorkoutTemplateRepository (e opcionalmente UserRepository para validação); não acessa UI nem rede.
- Código em `shared/commonMain`, pacote `application.usecase`; domain e data já existem.

## regras de negócio

- Apenas o próprio aluno (studentUserId) vê seus treinos atribuídos.
- Dados lidos do cache local; fluxo funciona offline.

## critérios de aceitação

- Existe caso de uso que recebe studentUserId e retorna a lista de treinos atribuídos ao aluno (atribuição + template com exercícios).
- Em sucesso: lista contém para cada atribuição o WorkoutTemplate com WorkoutExercise (nome, sets, reps, loadKg, restSeconds, order).
- Se algum template foi removido (getById retorna null), pode omitir essa atribuição ou tratar conforme definido (ex.: omitir).
- Em falha quando studentUserId vazio ou usuário inexistente/não STUDENT (se validação existir): retorna Result.failure.
- Testes unitários: sucesso com atribuições e templates existentes; aluno sem atribuições retorna lista vazia; (opcional) falha com studentUserId inválido.
- Domain e repositórios não são alterados (apenas utilizados); sem rede nem UI.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-02 (domínio WorkoutTemplate), spec-03 (WorkoutAssignment), spec-04 (repositórios)*
