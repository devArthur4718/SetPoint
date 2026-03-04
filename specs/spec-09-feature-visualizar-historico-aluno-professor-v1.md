# feature: Visualizar histórico do aluno (professor)

Permitir que o **professor** (User com role TRAINER) **visualize as execuções e a evolução de um aluno**: listar os WorkoutAssignment do aluno e, para cada um (ou para um aluno em geral), as WorkoutExecution com suas SetExecution (carga/reps reais). Leitura a partir do cache local (offline-first); não envolve UI nem rede nesta spec — apenas o caso de uso que agrega os dados.

## requisitos

- **Ator**: usuário autenticado com role TRAINER (o “professor”).
- **Entrada**:
  - `studentUserId`: id do User que é o aluno (role STUDENT) cujo histórico se quer ver.
  - `trainerId`: contexto do professor que está consultando (para validar que pode ver esse aluno, se houver regra; nesta spec pode ser apenas auditoria ou filtro futuro).
- **Saída**: sucesso retornando uma estrutura com as atribuições do aluno e as execuções de cada uma (ex.: lista de atribuições com suas execuções, ou lista plana de execuções com referência ao template/atribuição); ou falha (aluno inexistente, usuário não é STUDENT).
- **Fluxo**:
  1. Validar studentUserId não vazio.
  2. Buscar User por id (UserRepository.getById(studentUserId)); se não existir ou não for role STUDENT, retornar erro.
  3. (Opcional) Verificar que o professor pode ver esse aluno (ex.: em MVP sem auth restrita, qualquer TRAINER pode ver; ou validar relação trainer–aluno se existir).
  4. Buscar atribuições do aluno: WorkoutAssignmentRepository.listByStudentUserId(studentUserId).
  5. Para cada atribuição (ou conforme necessário), buscar execuções: WorkoutExecutionRepository.listByWorkoutAssignmentId(assignment.id).
  6. Montar resultado (ex.: lista de pares atribuição + execuções, ou DTO com execuções ordenadas por data).
  7. Retornar sucesso com o resultado.
- **Caso de uso**: uma classe ou função (ex.: `GetStudentWorkoutHistoryUseCase`) no shared que recebe studentUserId e trainerId (contexto), e retorna `Result<StudentWorkoutHistoryResult>` (ou tipo que exponha atribuições e execuções). Usa UserRepository, WorkoutAssignmentRepository e WorkoutExecutionRepository; não acessa UI nem rede.
- Código em `shared/commonMain`, pacote `application.usecase`; domain e data já existem.

## regras de negócio

- Só se consulta histórico de um User com role STUDENT.
- Dados lidos do cache local (repositórios); compatível com uso offline.

## critérios de aceitação

- Existe caso de uso que recebe studentUserId e trainerId e retorna o histórico de execuções do aluno (atribuições + execuções).
- Em sucesso: resultado contém as atribuições do aluno e as execuções de cada atribuição (WorkoutExecution com SetExecution).
- Em falha quando o aluno não existe ou não é STUDENT: retorna Result.failure.
- Testes unitários: sucesso com aluno existente e atribuições/execuções no repositório; falha com studentUserId inexistente; falha com usuário que não é STUDENT; aluno sem atribuições retorna lista vazia (ou estrutura vazia).
- Domain e repositórios não são alterados (apenas utilizados); sem rede nem UI.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-03 (domínio WorkoutExecution/SetExecution), spec-04 (repositórios)*
