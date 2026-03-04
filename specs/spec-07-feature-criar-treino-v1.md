# feature: Criar treino (template)

Permitir que o **professor** (User com role TRAINER) crie um **template de treino** no sistema: um WorkoutTemplate com nome e uma lista de exercícios (WorkoutExercise) com séries, repetições, carga e descanso. A operação usa os repositórios e o domínio já definidos (specs 02, 04 e 05); não envolve UI nem rede nesta spec — apenas o caso de uso e a validação de fluxo.

## requisitos

- **Ator**: usuário autenticado com role TRAINER (o “professor”).
- **Entrada**:
  - Nome do treino (obrigatório).
  - Lista de exercícios do treino: para cada item: `exerciseId` (referência a um Exercise existente), `order` (posição no treino, 1-based), `sets`, `reps`, `loadKg` (opcional), `restSeconds` (opcional).
  - O professor que está criando (`trainerId`) é identificado pelo contexto da aplicação (ex.: usuário logado).
- **Saída**: sucesso retornando o WorkoutTemplate criado (com sua lista de WorkoutExercise) ou falha com motivo (nome inválido, lista vazia, exercício inexistente, parâmetros inválidos, falha ao persistir).
- **Fluxo**:
  1. Validar nome do treino (regras de domínio: 1–120 caracteres, não vazio).
  2. Validar que a lista de exercícios do treino não está vazia.
  3. Para cada item da lista: verificar se o Exercise existe (ExerciseRepository.getById(exerciseId)); se algum não existir, retornar erro.
  4. Para cada item: criar WorkoutExercise via WorkoutExercise.create(id, exerciseId, order, sets, reps, loadKg?, restSeconds?); em caso de falha de validação, retornar erro. Geração de ids (WorkoutTemplate.id, WorkoutExercise.id) fica no use case ou na camada de dados (ex.: UUID).
  5. Criar WorkoutTemplate via WorkoutTemplate.create(id, name, trainerId, exercises, createdAt?); em caso de falha, retornar erro.
  6. Salvar WorkoutTemplate via WorkoutTemplateRepository.save(template); se falhar, retornar erro.
  7. Retornar sucesso com o WorkoutTemplate criado.
- **Caso de uso**: uma classe ou função (ex.: `CreateWorkoutTemplateUseCase`) no shared que recebe nome, lista de itens (exerciseId, order, sets, reps, loadKg?, restSeconds?) e trainerId (contexto), e retorna `Result<WorkoutTemplate>`. O caso de uso usa ExerciseRepository e WorkoutTemplateRepository (injetados ou recebidos); não acessa UI nem rede.
- Código em `shared/commonMain`: pacote de aplicação ou use case (ex.: `application.usecase` ou `domain.usecase`); domain e data já existem.

## regras de negócio

- Nome do treino deve obedecer às regras da spec-02 (WorkoutTemplate): não vazio, 1–120 caracteres.
- O treino deve ter ao menos um exercício; cada item deve referenciar um Exercise existente no repositório.
- WorkoutExercise: sets ≥ 1, reps ≥ 1; loadKg e restSeconds opcionais e ≥ 0 quando presentes; order deve ser inteiro ≥ 1 (e coerente na lista, ex.: 1, 2, 3 …).
- Geração de ids (WorkoutTemplate.id, WorkoutExercise.id): pode ser UUID ou delegada à camada de dados; não faz parte do domínio puro.

## critérios de aceitação

- Existe caso de uso (ou função equivalente) que recebe nome, lista de itens (exerciseId, order, sets, reps, loadKg?, restSeconds?) e trainerId e executa o fluxo descrito.
- Em sucesso: WorkoutTemplate (com WorkoutExercise) é persistido via WorkoutTemplateRepository e o resultado expõe o template criado.
- Em falha de validação (nome inválido, lista vazia, sets/reps/loadKg/restSeconds inválidos): retorna Result.failure com mensagem adequada, sem persistir.
- Em falha quando algum exerciseId não existe no ExerciseRepository: retorna Result.failure sem alterar dados.
- Em falha ao salvar (WorkoutTemplateRepository.save retorna falha): retorna Result.failure propagando o erro.
- Testes unitários do caso de uso: sucesso com dados válidos e exercícios existentes; falha com nome vazio; falha com lista vazia; falha com exerciseId inexistente; falha com sets ou reps inválidos (mock ou repositório in-memory).
- Domain e repositórios não são alterados (apenas utilizados); a feature não adiciona rede nem UI.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-02 (domínio WorkoutTemplate/WorkoutExercise), spec-04 (repositórios), spec-05 (persistência)*
