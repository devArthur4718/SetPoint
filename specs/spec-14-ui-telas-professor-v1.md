# feature: UI — Telas do professor

Definir as **telas do fluxo professor** no app (composeApp): lista de alunos, criar aluno, lista de treinos (templates), criar treino, atribuir treino a aluno e histórico de execuções de um aluno. Todas consomem os use cases já implementados no shared (specs 06–09).

## requisitos

- **Escopo**: módulo **composeApp**; usuário atual com role TRAINER (trainerId = userId do usuário logado/selecionado na entrada).
- **Telas**:
  1. **Home / Lista de alunos**: lista os alunos do professor. Fonte: não existe use case “listar alunos do professor” no shared — pode ser lista de Users com role STUDENT (UserRepository.list) filtrada por contexto, ou um use case futuro “ListMyStudentsUseCase”. Para MVP: usar UserRepository.list() e filtrar por role STUDENT, ou exibir todos os StudentProfile (StudentProfileRepository não tem listByTrainerId). *Nota: specs 01–04 não definem “alunos do professor”; pode-se exibir todos os STUDENT ou criar use case simples que lista estudantes (ex.: todos com role STUDENT). Para esta spec: assumir que existe forma de obter lista exibível (ex.: listar Users role STUDENT) e que a tela exibe essa lista com nome/email/displayName.*
  2. **Criar aluno**: formulário com campos email, nome, displayName (opcional). Ao submeter: chamar CreateStudentUseCase(email, name, displayName, trainerId). Sucesso: navegar de volta (ex.: para lista de alunos) e mostrar feedback. Falha: mostrar mensagem de erro (validação ou email duplicado).
  3. **Lista de treinos (templates)**: lista os WorkoutTemplate do professor. Fonte: WorkoutTemplateRepository.listByTrainerId(trainerId) — pode ser chamado por ViewModel/use case. Exibir nome do treino e quantidade de exercícios (ou resumo).
  4. **Criar treino**: formulário com nome do treino e lista de exercícios (cada um: exercício selecionado do catálogo, order, sets, reps, loadKg opcional, restSeconds opcional). Catálogo: ExerciseRepository.list(). Ao submeter: chamar CreateWorkoutTemplateUseCase(name, trainerId, items). Sucesso: voltar à lista de treinos. Falha: mostrar mensagem.
  5. **Atribuir treino a aluno**: seleção de um WorkoutTemplate e de um aluno (User STUDENT). Chamar AssignWorkoutToStudentUseCase(workoutTemplateId, studentUserId, trainerId). Sucesso: feedback e voltar. Falha: mostrar mensagem.
  6. **Histórico de um aluno**: ao selecionar um aluno (ex.: da lista de alunos), navegar para tela que exibe o histórico de execuções desse aluno. Fonte: GetStudentWorkoutHistoryUseCase(studentUserId, trainerId). Exibir atribuições e execuções (data, treino, séries realizadas).
- **Navegação**: da home do professor acessar “Criar aluno”, “Lista de treinos”, “Atribuir treino”; da lista de treinos acessar “Criar treino”; da lista de alunos acessar “Histórico” do aluno. Detalhes do grafo de navegação podem ser definidos nas tasks.
- **Estado e dados**: ViewModels (ou equivalente) que chamam os use cases (injetados); estado de loading e erro tratados na UI.

## regras de negócio

- Todas as ações usam o trainerId do usuário atual (contexto da entrada).
- Listas vazias: exibir estado vazio (mensagem ou CTA), não tela em branco.
- Erros dos use cases devem ser exibidos ao usuário (toast, snackbar ou mensagem na tela).

## critérios de aceitação

- Existe tela (ou conjunto de telas) que lista alunos e permite criar aluno via formulário; CreateStudentUseCase é chamado com trainerId correto; sucesso e falha são tratados.
- Existe tela de lista de treinos do professor (WorkoutTemplateRepository.listByTrainerId) e tela de criar treino (CreateWorkoutTemplateUseCase); sucesso e falha tratados.
- Existe fluxo para atribuir treino a aluno (AssignWorkoutToStudentUseCase); seleção de template e aluno; sucesso e falha tratados.
- Existe tela de histórico de um aluno (GetStudentWorkoutHistoryUseCase); exibe atribuições e execuções do aluno selecionado.
- Navegação entre essas telas funciona; usuário consegue voltar. Implementação no composeApp; shared não é alterado.

---

*Versão: v1 — 2025-03-03*
*Depende de: specs 06–09 (use cases), spec-13 (entrada — trainerId)*
*Escopo: composeApp*
