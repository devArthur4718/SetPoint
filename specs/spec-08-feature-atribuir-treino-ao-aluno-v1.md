# feature: Atribuir treino ao aluno

Permitir que o **professor** (User com role TRAINER) **atribua um template de treino a um aluno**: criar um WorkoutAssignment associando um WorkoutTemplate existente a um User com role STUDENT. A operação usa os repositórios e o domínio já definidos (specs 02, 03 e 04); não envolve UI nem rede nesta spec — apenas o caso de uso e a validação de fluxo.

## requisitos

- **Ator**: usuário autenticado com role TRAINER (o “professor”).
- **Entrada**:
  - `workoutTemplateId`: id do WorkoutTemplate a ser atribuído.
  - `studentUserId`: id do User que é o aluno (deve ser User com role STUDENT).
  - O professor que está atribuindo (`trainerId`) é identificado pelo contexto da aplicação (ex.: usuário logado).
- **Saída**: sucesso retornando o WorkoutAssignment criado ou falha com motivo (template inexistente, template não pertence ao professor, aluno inexistente, aluno não é STUDENT, falha ao persistir).
- **Fluxo**:
  1. Validar workoutTemplateId e studentUserId não vazios.
  2. Buscar WorkoutTemplate por id (WorkoutTemplateRepository.getById); se não existir, retornar erro.
  3. Verificar se o template pertence ao professor (template.trainerId == trainerId); se não, retornar erro.
  4. Buscar User por id (UserRepository.getById(studentUserId)); se não existir ou não for role STUDENT, retornar erro.
  5. Gerar id do WorkoutAssignment (use case ou IdGenerator); assignedAt opcional (ex.: epoch millis).
  6. Criar WorkoutAssignment via WorkoutAssignment.create(id, workoutTemplateId, studentUserId, assignedAt?); em caso de falha, retornar erro.
  7. Salvar WorkoutAssignment via WorkoutAssignmentRepository.save(assignment); se falhar, retornar erro.
  8. Retornar sucesso com o WorkoutAssignment criado.
- **Caso de uso**: uma classe ou função (ex.: `AssignWorkoutToStudentUseCase`) no shared que recebe workoutTemplateId, studentUserId e trainerId (contexto), e retorna `Result<WorkoutAssignment>`. O caso de uso usa WorkoutTemplateRepository, UserRepository e WorkoutAssignmentRepository (injetados ou recebidos); não acessa UI nem rede.
- Código em `shared/commonMain`: pacote de aplicação ou use case (ex.: `application.usecase`); domain e data já existem.

## regras de negócio

- WorkoutAssignment associa um template a um aluno; workoutTemplateId e studentUserId obrigatórios (não vazios), conforme spec-03.
- O WorkoutTemplate deve existir e pertencer ao professor que está atribuindo (template.trainerId == trainerId).
- O aluno deve existir e ter role STUDENT (User com role STUDENT).
- Geração de id (WorkoutAssignment.id) e assignedAt (opcional) fica no use case ou na camada de dados.

## critérios de aceitação

- Existe caso de uso (ou função equivalente) que recebe workoutTemplateId, studentUserId e trainerId e executa o fluxo descrito.
- Em sucesso: WorkoutAssignment é persistido via WorkoutAssignmentRepository e o resultado expõe a atribuição criada.
- Em falha quando o template não existe: retorna Result.failure sem persistir.
- Em falha quando o template não pertence ao professor (trainerId diferente): retorna Result.failure sem persistir.
- Em falha quando o aluno não existe ou não é STUDENT: retorna Result.failure sem persistir.
- Em falha de validação (workoutTemplateId ou studentUserId vazios): retorna Result.failure com mensagem adequada, sem persistir.
- Em falha ao salvar (WorkoutAssignmentRepository.save retorna falha): retorna Result.failure propagando o erro.
- Testes unitários do caso de uso: sucesso com template e aluno válidos; falha com template inexistente; falha com template de outro professor; falha com aluno inexistente; falha com usuário que não é STUDENT; falha com workoutTemplateId ou studentUserId vazios (mock ou repositório in-memory).
- Domain e repositórios não são alterados (apenas utilizados); a feature não adiciona rede nem UI.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-02 (domínio WorkoutTemplate), spec-03 (domínio WorkoutAssignment), spec-04 (repositórios)*
