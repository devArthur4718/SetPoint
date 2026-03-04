# tasks — spec em foco (canônico)

**Este é o arquivo canônico de tasks.** O agente deve sempre gerar ou atualizar **este** arquivo (`specs/tasks.md`) com as tasks da spec que vai implementar. Não pular esta etapa: spec → **atualizar tasks.md** → implementar.

**Spec em foco atual:** **spec-17** (UI: animações e motion). Ao implementar: branch `spec/17-ui-animacoes-motion`, tasks abaixo (bloco spec-17), composeApp. Specs 13–16 já implementadas; 09–12 (shared) já implementadas.

---

# tasks — spec-09: Visualizar histórico do aluno (professor)

Decomposição da `spec-09-feature-visualizar-historico-aluno-professor-v1.md`. **Ordem recomendada:** executar na sequência.

---

## 1. Modelo de resultado

- [x] Definir tipo de retorno do caso de uso (ex.: `StudentWorkoutHistoryResult` ou data class) que expõe atribuições do aluno e, para cada uma, a lista de WorkoutExecution (com setExecutions). Ou lista plana de execuções com referência à atribuição.

## 2. GetStudentWorkoutHistoryUseCase

- [x] Criar classe `GetStudentWorkoutHistoryUseCase` em `application.usecase` no `shared/commonMain`.
- [x] Injetar `UserRepository`, `WorkoutAssignmentRepository`, `WorkoutExecutionRepository`.
- [x] Método: recebe studentUserId, trainerId; retorna `Result<StudentWorkoutHistoryResult>` (ou tipo definido acima) (suspending).

## 3. Validação e resolução do aluno

- [x] Validar studentUserId não vazio; em falha retornar `Result.failure`.
- [x] Chamar `UserRepository.getById(studentUserId)`; se não existir ou role != STUDENT, retornar `Result.failure`.

## 4. Agregação do histórico

- [x] Chamar `WorkoutAssignmentRepository.listByStudentUserId(studentUserId)`.
- [x] Para cada atribuição, chamar `WorkoutExecutionRepository.listByWorkoutAssignmentId(assignment.id)`.
- [x] Montar resultado (atribuições + execuções por atribuição) e retornar `Result.success(...)`.

## 5. Testes unitários

- [x] Teste: sucesso com aluno existente (STUDENT) e atribuições/execuções no repositório; resultado contém dados corretos.
- [x] Teste: falha quando studentUserId inexistente; falha quando usuário não é STUDENT.
- [x] Teste: aluno sem atribuições retorna estrutura vazia (lista vazia ou sem execuções).
- [x] Usar repositórios in-memory; `commonTest`, `kotlin.test`.

## 6. Integração

- [x] Domain e repositórios apenas utilizados; build e testes passando.

---

*Referência: specs/spec-09-feature-visualizar-historico-aluno-professor-v1.md*

---

# tasks — spec-10: Visualizar treino atribuído (aluno)

Decomposição da `spec-10-feature-visualizar-treino-atribuido-aluno-v1.md`.

---

## 1. Modelo de resultado

- [x] Definir tipo (ex.: `AssignedWorkoutView` ou data class) que expõe WorkoutAssignment + WorkoutTemplate (com exercises) para cada treino atribuído.

## 2. GetMyAssignedWorkoutsUseCase

- [x] Criar classe `GetMyAssignedWorkoutsUseCase` em `application.usecase`.
- [x] Injetar `WorkoutAssignmentRepository`, `WorkoutTemplateRepository`; opcionalmente `UserRepository` para validar studentUserId.
- [x] Método: recebe studentUserId; retorna `Result<List<AssignedWorkoutView>>` (suspending).

## 3. Fluxo

- [x] Validar studentUserId não vazio; (opcional) validar User existe e é STUDENT.
- [x] `WorkoutAssignmentRepository.listByStudentUserId(studentUserId)`.
- [x] Para cada atribuição, `WorkoutTemplateRepository.getById(assignment.workoutTemplateId)`; se template existir, montar item (atribuição + template); senão omitir ou tratar.
- [x] Retornar `Result.success(lista)`.

## 4. Testes unitários

- [x] Sucesso com atribuições e templates existentes; lista contém template com exercícios.
- [x] Aluno sem atribuições retorna lista vazia.
- [x] (Opcional) Falha com studentUserId inválido. commonTest, kotlin.test.

## 5. Integração

- [x] Domain/repositórios apenas utilizados; build e testes passando.

---

*Referência: specs/spec-10-feature-visualizar-treino-atribuido-aluno-v1.md*

---

# tasks — spec-11: Executar treino (aluno)

Decomposição da `spec-11-feature-executar-treino-aluno-v1.md`.

---

## 1. Modelo de entrada das séries

- [x] Definir tipo para item de série (ex.: workoutExerciseId, setNumber, actualReps?, actualLoadKg?) — data class no use case ou em `application.usecase`.

## 2. RecordWorkoutExecutionUseCase (ou ExecuteWorkoutUseCase)

- [x] Criar classe em `application.usecase`; injetar `WorkoutAssignmentRepository`, `WorkoutTemplateRepository`, `WorkoutExecutionRepository`, `IdGenerator`.
- [x] Método: recebe workoutAssignmentId, studentUserId, lista de itens de série (ou vazia); retorna `Result<WorkoutExecution>` (suspending).

## 3. Validação e ownership

- [x] Validar workoutAssignmentId e studentUserId não vazios.
- [x] Buscar WorkoutAssignment; se não existir, retornar `Result.failure`.
- [x] Verificar assignment.studentUserId == studentUserId; se não, retornar `Result.failure`.

## 4. Validação dos workoutExerciseId e construção de SetExecution

- [x] Buscar WorkoutTemplate da atribuição (`WorkoutTemplateRepository.getById(assignment.workoutTemplateId)`); obter lista de WorkoutExercise do template.
- [x] Para cada item em setExecutions: verificar que workoutExerciseId existe no template; gerar id; `SetExecution.create(...)`; em falha retornar `Result.failure`.

## 5. WorkoutExecution e persistência

- [x] Gerar id da WorkoutExecution; executedAt = epoch millis (ou injetado).
- [x] `WorkoutExecution.create(id, workoutAssignmentId, executedAt, setExecutions)`; em falha retornar `Result.failure`.
- [x] `WorkoutExecutionRepository.save(execution)`; propagar falha ou retornar `Result.success(execution)`.

## 6. Testes unitários

- [x] Sucesso com lista vazia de séries (só inicia execução); sucesso com uma ou mais séries válidas.
- [x] Falha quando atribuição não é do aluno; falha com workoutExerciseId que não pertence ao template; falha com setNumber < 1 ou valores negativos.
- [x] commonTest, kotlin.test.

## 7. Integração

- [x] Domain/repositórios apenas utilizados; build e testes passando.

---

*Referência: specs/spec-11-feature-executar-treino-aluno-v1.md*

---

# tasks — spec-12: Histórico de treinos (aluno)

Decomposição da `spec-12-feature-historico-treinos-aluno-v1.md`.

---

## 1. GetMyWorkoutHistoryUseCase

- [x] Criar classe em `application.usecase`; injetar `WorkoutAssignmentRepository`, `WorkoutExecutionRepository`; opcionalmente `UserRepository`, `WorkoutTemplateRepository` (para enriquecer com nome do treino).
- [x] Método: recebe studentUserId; retorna `Result<List<WorkoutExecution>>` ou `Result<MyWorkoutHistoryResult>` (suspending).

## 2. Fluxo

- [x] Validar studentUserId não vazio; (opcional) validar User existe e é STUDENT.
- [x] `WorkoutAssignmentRepository.listByStudentUserId(studentUserId)`.
- [x] Para cada atribuição, `WorkoutExecutionRepository.listByWorkoutAssignmentId(assignment.id)`.
- [x] Agregar todas as execuções; ordenar por executedAt (ex.: decrescente).
- [x] (Opcional) Enriquecer com nome do treino por atribuição.
- [x] Retornar `Result.success(lista)`.

## 3. Testes unitários

- [x] Sucesso com atribuições e execuções; lista ordenada por executedAt.
- [x] Aluno sem execuções retorna lista vazia.
- [x] (Opcional) Falha com studentUserId inválido. commonTest, kotlin.test.

## 4. Integração

- [x] Domain/repositórios apenas utilizados; build e testes passando.

---

*Referência: specs/spec-12-feature-historico-treinos-aluno-v1.md*

---

# tasks — spec-13: UI Entrada no app e navegação

Decomposição da `spec-13-ui-entrada-navegacao-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **composeApp**.

---

## 1. Estado do usuário atual (session)

- [x] Definir onde guardar userId e role do “usuário atual” (ex.: classe Session/UserSession, estado hoisted, ou ViewModel na raiz). Deve estar acessível às telas dos fluxos professor e aluno.
- [x] Para MVP: definir IDs fixos de um User TRAINER e um User STUDENT (ex.: constantes ou config) para usar quando o usuário escolher “professor” ou “aluno” na entrada.

## 2. Tela de entrada (Entry / Role selection)

- [x] Criar tela Compose que é a primeira tela do app (content root ou primeira rota).
- [x] Exibir duas opções: “Entrar como professor” e “Entrar como aluno” (botões ou cards).
- [x] Ao escolher professor: definir estado do usuário (userId = id do trainer fixo, role = TRAINER) e navegar para a home do professor.
- [x] Ao escolher aluno: definir estado (userId = id do student fixo, role = STUDENT) e navegar para a home do aluno.

## 3. Navegação (graph / router)

- [x] Configurar navegação (ex.: NavController + rotas, ou tipo de navegador único) com pelo menos: rota da tela de entrada, rota da home professor, rota da home aluno.
- [x] Garantir que a tela de entrada é a start destination; da entrada só se navega para home professor ou home aluno conforme a escolha.

## 4. Integração com fluxos 14 e 15

- [x] Passar userId e role (ou Session) para as telas/home do professor (spec-14) e do aluno (spec-15), para que usem trainerId ou studentUserId nos use cases.
- [x] (Opcional) Botão “Sair” ou “Trocar usuário” nas homes que volta à tela de entrada e limpa o usuário atual.

## 5. Critérios de aceitação

- [x] Tela de entrada visível ao abrir o app; escolha professor/aluno leva à home correta.
- [x] userId e role disponíveis nas telas seguintes. Implementação no composeApp; shared não alterado.
- [ ] (Opcional) Teste de UI: após escolher professor, tela exibida é a home professor; idem para aluno.

---

*Referência: specs/spec-13-ui-entrada-navegacao-v1.md*

---

# tasks — spec-14: UI Telas do professor

Decomposição da `spec-14-ui-telas-professor-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **composeApp**. Depende de spec-13 (userId/role disponíveis; trainerId = userId quando role = TRAINER).

---

## 1. Home / Lista de alunos

- [x] Criar tela (e ViewModel se usar MVVM) que exibe a lista de alunos. Fonte: UserRepository.list() filtrado por role == STUDENT (ou use case simples que retorne lista de estudantes). Exibir nome, email e opcionalmente displayName (buscar StudentProfile por userId).
- [x] Estado vazio: quando não houver alunos, exibir mensagem (ex.: “Nenhum aluno”) e CTA “Criar aluno”.
- [x] Navegação: botão/ícone “Criar aluno” → tela Criar aluno; ao tocar em um aluno → tela Histórico do aluno (passar studentUserId).
- [x] Loading e erro: tratar loading da lista e exibir erro se falhar.

## 2. Criar aluno

- [x] Formulário com campos: email, nome, displayName (opcional). Validação básica na UI (não vazio onde obrigatório).
- [x] ViewModel chama CreateStudentUseCase(email, name, displayName, trainerId). trainerId = userId do usuário atual (spec-13).
- [x] Sucesso: navegar de volta (ex.: lista de alunos) e mostrar feedback (snackbar/toast). Falha: exibir mensagem de erro (validação ou email duplicado).
- [x] Botão “Voltar” ou “Cancelar” sem submeter.

## 3. Lista de treinos (templates)

- [x] Tela que lista os WorkoutTemplate do professor. Fonte: WorkoutTemplateRepository.listByTrainerId(trainerId) — chamada a partir do ViewModel (ou use case wrapper se preferir).
- [x] Exibir por item: nome do treino, quantidade de exercícios (template.exercises.size). Estado vazio: mensagem e CTA “Criar treino”.
- [x] Navegação: “Criar treino” → tela Criar treino; opcional: tocar em um treino para detalhe/editar (fora do escopo mínimo se não estiver na spec).
- [x] Loading e erro tratados.

## 4. Criar treino

- [x] Formulário: nome do treino; lista de exercícios do treino. Cada item: seleção de Exercise (catálogo = ExerciseRepository.list()), order (ou ordem implícita), sets, reps, loadKg (opcional), restSeconds (opcional). Permitir adicionar/remover itens.
- [x] ViewModel chama CreateWorkoutTemplateUseCase(name, trainerId, items). items = lista de WorkoutTemplateItem(exerciseId, order, sets, reps, loadKg?, restSeconds?).
- [x] Sucesso: voltar à lista de treinos e feedback. Falha: exibir mensagem.
- [x] Validar lista não vazia e nome não vazio antes de submeter.

## 5. Atribuir treino a aluno

- [x] Tela (ou bottom sheet/dialog): seleção de um WorkoutTemplate (lista do professor) e de um aluno (lista de Users STUDENT). Dois seletores ou listas.
- [x] ViewModel chama AssignWorkoutToStudentUseCase(workoutTemplateId, studentUserId, trainerId).
- [x] Sucesso: feedback e voltar. Falha: exibir mensagem (template não encontrado, aluno não encontrado, etc.).

## 6. Histórico de um aluno

- [x] Tela que recebe studentUserId (ex.: argumento de navegação a partir da lista de alunos). ViewModel chama GetStudentWorkoutHistoryUseCase(studentUserId, trainerId).
- [x] Exibir resultado: atribuições e, para cada uma, lista de execuções (executedAt, resumo de SetExecution). Formato legível (data, nome do treino se possível, séries).
- [x] Estado vazio: “Nenhuma execução registrada”. Loading e erro tratados.
- [x] Botão/gesto voltar para lista de alunos.

## 7. Navegação do fluxo professor

- [x] Home professor: acesso a “Lista de alunos”, “Lista de treinos”, “Atribuir treino”. Pode ser uma home com cards/links ou drawer/tabs.
- [x] Lista alunos → Criar aluno; Lista alunos → Histórico (aluno). Lista treinos → Criar treino. Todas as telas com volta funcionando.
- [x] Garantir que trainerId é sempre o userId do usuário atual (spec-13).

## 8. Injeção de dependências (composeApp)

- [x] Repositórios e use cases (CreateStudentUseCase, CreateWorkoutTemplateUseCase, AssignWorkoutToStudentUseCase, GetStudentWorkoutHistoryUseCase, UserRepository, WorkoutTemplateRepository, ExerciseRepository, etc.) disponíveis no composeApp (ex.: Koin, manual, ou factory). Shared não é alterado; composeApp depende de shared.

---

*Referência: specs/spec-14-ui-telas-professor-v1.md*

---

# tasks — spec-15: UI Telas do aluno

Decomposição da `spec-15-ui-telas-aluno-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **composeApp**. Depende de spec-13 (studentUserId = userId quando role = STUDENT).

---

## 1. Home / Treinos atribuídos

- [x] Tela (e ViewModel) que chama GetMyAssignedWorkoutsUseCase(studentUserId). studentUserId = userId do usuário atual (spec-13).
- [x] Exibir lista: para cada AssignedWorkoutView, mostrar template.name e quantidade de exercícios; botão “Executar” (ou “Iniciar”) que navega para a tela Executar treino passando workoutAssignmentId (e dados necessários).
- [x] Estado vazio: “Nenhum treino atribuído”. Loading e erro tratados.
- [x] Navegação: “Executar” → tela Executar treino; link/aba “Meu histórico” → tela Meu histórico.

## 2. Executar treino

- [x] Tela que recebe workoutAssignmentId (e, se necessário, template/assignment para exibição). Exibir nome do treino e lista de exercícios do template (nome do exercício, sets, reps, loadKg, restSeconds).
- [x] Permitir “Iniciar execução” (sem séries): chamar RecordWorkoutExecutionUseCase(workoutAssignmentId, studentUserId, emptyList(), executedAt). executedAt = epoch millis atual (platform: expect/actual ou lib).
- [ ] Permitir “Registrar séries”: para cada WorkoutExercise, permitir informar por série (setNumber 1..N) o actualReps e actualLoadKg (opcional). Coletar em estado; ao submeter “Concluir treino”, montar lista de SetExecutionItem e chamar RecordWorkoutExecutionUseCase(workoutAssignmentId, studentUserId, setExecutionItems, executedAt).
- [x] Sucesso: feedback e navegar de volta (ou para Meu histórico). Falha: exibir mensagem.
- [x] Botão voltar sem concluir (não salva execução).

## 3. Meu histórico

- [x] Tela que chama GetMyWorkoutHistoryUseCase(studentUserId). Exibir lista de WorkoutExecution ordenada por executedAt (já vem ordenada do use case).
- [x] Por execução: data (executedAt formatada), opcionalmente nome do treino (se enriquecer no use case ou buscar por assignment), resumo das séries (SetExecution).
- [x] Estado vazio: “Nenhuma execução”. Loading e erro tratados.
- [x] Botão/gesto voltar para home aluno.

## 4. Navegação do fluxo aluno

- [x] Home aluno: acesso a lista de treinos atribuídos e “Meu histórico”. Executar treino → volta ou “Meu histórico”.
- [x] Garantir que studentUserId é sempre o userId do usuário atual (spec-13).

## 5. Injeção de dependências (composeApp)

- [x] GetMyAssignedWorkoutsUseCase, RecordWorkoutExecutionUseCase, GetMyWorkoutHistoryUseCase (e repositórios necessários) disponíveis no composeApp. executedAt para RecordWorkoutExecution: obter tempo atual na plataforma (expect/actual ou kotlinx-datetime) e passar ao use case.

---

*Referência: specs/spec-15-ui-telas-aluno-v1.md*

---

# tasks — spec-16: UI Design system e identidade visual

Decomposição da `spec-16-ui-design-system-identidade-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **composeApp**.

---

## 1. Tema (Material 3)

- [x] Definir ColorScheme para tema claro: primary, secondary, tertiary, surface, error, onSurface, etc., alinhados à identidade SetPoint (energia/foco, não genérico).
- [x] Definir ColorScheme para tema escuro (mesma paleta adaptada).
- [x] Aplicar tema no root do app (MaterialTheme / SetPointTheme) com darkTheme conforme configuração do sistema.
- [x] Definir escala tipográfica (Typography): Display, Headline, Title, Body, Label com tamanhos e pesos consistentes.
- [x] Definir Shapes (border radius para cards, botões, chips) e usar em componentes.

## 2. Componentes reutilizáveis

- [x] Garantir que AppBarScreen (ou equivalente) use cores e tipografia do tema.
- [x] Padronizar estilo de cards (lista e ação): padding, radius, surface color; ripple/feedback ao toque.
- [x] Padronizar botões: primário, secundário/outlined, texto; uso consistente em todas as telas.
- [x] Padronizar OutlinedTextField (formulários): estilo único em Criar aluno, Criar treino, etc.
- [x] (Opcional) Chips/tags: para estados (ex.: "Concluído", "Pendente") ou filtros, quando fizer sentido.
- [x] Acessibilidade: garantir labels para ícones (contentDescription / accessibilityLabel) em AppBar e ações.

## 3. Estados visuais

- [x] Substituir texto "Carregando..." por indicador animado (CircularProgressIndicator ou skeleton) em listas e telas que carregam dados.
- [x] Estado vazio: em cada lista/tela vazia, exibir layout dedicado (ícone/ilustração + mensagem + CTA quando aplicável), centralizado ou alinhado.
- [x] Erro: mensagens com cor de erro e opcional ícone; posição consistente (campo ou topo do conteúdo); opcional snackbar para erros de ação.
- [x] Snackbar (ou toast) de sucesso após: criar aluno, criar treino, atribuir treino, registrar execução; mensagem clara e duração curta.

## 4. Identidade do produto

- [x] Tela de entrada: reforçar marca SetPoint (nome, opcional ícone); tom limpo e objetivo.
- [x] Homes (professor e aluno): clareza e foco em treino; sem poluição visual.
- [x] Verificar contraste e áreas de toque (acessibilidade básica).

## 5. Critérios de aceitação

- [x] Tema light/dark aplicado em todo o app; cores e tipografia consistentes.
- [x] Listas vazias com estado vazio dedicado (mensagem + CTA quando aplicável).
- [x] Loading com indicador animado (não apenas texto).
- [x] Erros exibidos de forma visível; sucesso com snackbar (ou equivalente).
- [x] Implementação no composeApp; shared não alterado.

---

*Referência: specs/spec-16-ui-design-system-identidade-v1.md*

---

# tasks — spec-17: UI Animações e motion

Decomposição da `spec-17-ui-animacoes-motion-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **composeApp**. Depende de spec-16 (design system).

---

## 1. Transições de tela

- [x] Implementar transição animada ao trocar de tela (avançar/voltar): slide horizontal ou fade+slide, duração 200–300 ms.
- [x] Integrar com o mecanismo de navegação atual (estado currentScreen); mesma transição para todo o app.
- [ ] Garantir que “avançar” e “voltar” usem o mesmo padrão (ex.: entrada pela direita, saída pela esquerda ao voltar).

## 2. Animações em listas

- [x] Listas (alunos, treinos, atribuídos, histórico): itens entram com animação (fade-in + slide ou stagger).
- [x] Duração total da lista < 400–500 ms; não bloquear interação.
- [ ] Estado vazio da lista: mensagem/ilustração com fade-in suave.

## 3. Micro-interações

- [x] Botões: feedback ao toque (ripple ou scale); garantir que CTAs tenham feedback visível.
- [x] Cards clicáveis: ripple ou leve scale/elevation ao toque.
- [x] AppBar (voltar, ações): ripple ao toque.

## 4. Feedback animado

- [x] Snackbar de sucesso/erro: aparecer e desaparecer com animação (slide from bottom ou fade).
- [x] Indicador de loading já animado (CircularProgressIndicator ou skeleton) — alinhado com spec-16.

## 5. Preferência “reduzir movimento”

- [ ] Onde o sistema oferecer (Android/iOS), respeitar preferência de acessibilidade “reduzir movimento”: desativar ou reduzir animações.

## 6. Critérios de aceitação

- [x] Navegação entre telas com transição animada (200–400 ms).
- [x] Listas com animação de entrada dos itens.
- [x] Botões e cards com feedback ao toque.
- [x] Snackbar com animação de entrada/saída.
- [ ] Respeito à preferência “reduzir movimento” quando disponível.
- [x] Implementação no composeApp; shared não alterado.

---

*Referência: specs/spec-17-ui-animacoes-motion-v1.md*

---

# tasks — spec-18: Autenticação — Login com e-mail e senha

Decomposição da `spec-18-autenticacao-login-v1.md`. **Ordem recomendada:** executar na sequência. Substitui o fluxo “entrada sem login” da spec-13.

---

## 1. Tela de entrada (atualizada)

- [ ] Manter tela de entrada com duas opções: “Sou professor” e “Sou aluno”.
- [ ] Ao tocar “Sou professor”: navegar para **tela de login do professor** (nova tela).
- [ ] Ao tocar “Sou aluno”: navegar para **tela de login do aluno** (nova tela).
- [ ] Remover lógica que definia userId fixo ao escolher papel; entrada apenas redireciona para login.

## 2. Tela de login do professor

- [ ] Criar tela com campos: **e-mail** (teclado de e-mail) e **senha** (mascarado).
- [ ] Botão “Entrar”; validação na UI: e-mail não vazio e formato válido, senha não vazia (mín. 6 caracteres).
- [ ] “Voltar”: retorna à tela de entrada.
- [ ] Ao submeter: chamar serviço de autenticação (email, senha, role = TRAINER). Loading durante a chamada; em falha exibir mensagem (ex.: “E-mail ou senha incorretos”).
- [ ] Em sucesso: guardar sessão (userId, role), navegar para home do professor.

## 3. Tela de login do aluno

- [ ] Criar tela com campos e-mail e senha (mesmo padrão do professor).
- [ ] Botão “Entrar”, “Voltar”, validação, loading e mensagem de erro.
- [ ] Ao submeter: chamar serviço de autenticação (email, senha, role = STUDENT). Sucesso: sessão e navegação para home do aluno.

## 4. Serviço de autenticação

- [ ] Definir contrato: (email, senha, role) → Result com (userId, role) ou equivalente.
- [ ] Implementar: (A) chamada a API de login no backend, ou (B) validação local (ex.: tabela/estrutura com hash de senha por User). Se (B): criar/extender domínio ou dados para armazenar e validar senha (hash).
- [ ] Validar que o usuário retornado tem o role esperado (TRAINER para login professor, STUDENT para login aluno).

## 5. Sessão e “Sair”

- [ ] Após login bem-sucedido: guardar userId e role na sessão (estado do app ou persistido — DataStore/Keychain opcional).
- [ ] “Sair” nas homes: limpar sessão e navegar para **tela de entrada** (não para tela de login).
- [ ] Opcional: ao reabrir o app, se sessão válida persistida, ir direto para a home; senão, tela de entrada.

## 6. Critérios de aceitação

- [ ] Entrada leva a login professor ou login aluno; cada login tem tela própria com e-mail e senha.
- [ ] Login válido (professor ou aluno) preenche sessão e navega para a home correta.
- [ ] Credenciais inválidas: mensagem de erro, sem navegação.
- [ ] “Voltar” no login retorna à entrada; “Sair” na home retorna à entrada e limpa sessão.
- [ ] Implementação: composeApp (telas, navegação, sessão); shared apenas se LoginUseCase e/ou persistência de senha forem criados.

---

*Referência: specs/spec-18-autenticacao-login-v1.md*

---

# tasks — spec-19: Marca — Logo simples e estilizada

Decomposição da `spec-19-marca-logo-setpoint-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **composeApp** (assets + UI).

---

## 1. Conceito e design da logo

- [ ] Definir símbolo da logo: simples, estilizado, reconhecível em tamanho pequeno (ex.: alvo, ponto, seta, forma geométrica minimalista).
- [ ] Garantir que evoca meta/treino/precisão sem ser literal; sem texto dentro do símbolo.
- [ ] Definir cores: alinhadas ao tema (primary); variante para tema escuro ou para ícone do app (ex.: branco sobre fundo colorido).

## 2. Assets

- [ ] Produzir logo em formato vetorial (SVG ou XML drawable para Android; vetor ou PDF para iOS).
- [ ] Android: criar/atualizar adaptive icon (foreground com a logo; background com cor do tema) nos drawables; respeitar safe zone.
- [ ] iOS: adicionar App Icon com a logo em todos os tamanhos exigidos (Assets.xcassets); sem transparência onde a plataforma exigir.
- [ ] Asset escalável ou em tamanho adequado para exibição na tela de entrada (ex.: 80–120 dp).

## 3. Tela de entrada

- [ ] Exibir a **logo** (símbolo) na tela de entrada, acima ou ao lado do texto “SetPoint”, com tamanho destacado (ex.: 80–120 dp).
- [ ] Manter hierarquia: logo + nome + subtítulo/opções.

## 4. Ícone do app

- [ ] Android: configurar launcher (ic_launcher, ic_launcher_round) usando a logo como foreground do adaptive icon.
- [ ] iOS: configurar App Icon no projeto (Assets) com a logo nos tamanhos do catálogo.

## 5. Opcional: App bar

- [ ] Se definido no design system: exibir pequena versão da logo (ícone) na app bar das homes ou em telas principais.

## 6. Critérios de aceitação

- [ ] Logo existe e é usada na tela de entrada junto ao nome “SetPoint”.
- [ ] Ícone do app (Android e iOS) usa a logo.
- [ ] Logo legível em tamanho pequeno e em tema claro/escuro (ou com variante).
- [ ] Implementação no composeApp; assets em recursos Android e iOS.

---

*Referência: specs/spec-19-marca-logo-setpoint-v1.md*

---

# tasks — spec-20: Feature Criar conta (autocadastro)

Decomposição da `spec-20-feature-criar-conta-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **shared** (use case + eventual persistência de credencial).

---

## 1. Persistência de credencial (senha)

- [ ] Definir onde armazenar hash de senha por usuário (ex.: tabela UserCredentials com userId + passwordHash, ou extensão da tabela User). Mesma estrutura usada pelo login (spec-18).
- [ ] Criar interface (ex.: AuthRepository ou UserCredentialsRepository): save(userId, passwordHash), e para login: validate(email, password) ou getHashByUserId.
- [ ] Implementar repositório (SQLDelight ou in-memory para testes); usar função de hash (ex.: bcrypt ou equivalente disponível em KMP).

## 2. CreateAccountUseCase

- [ ] Criar classe CreateAccountUseCase em application.usecase no shared.
- [ ] Entrada: email, password, name, role (TRAINER ou STUDENT), displayName (opcional, para STUDENT).
- [ ] Validar: email formato válido e não vazio; senha mínimo 6 caracteres; nome 1–120 caracteres; role TRAINER ou STUDENT.
- [ ] Verificar se existe User com mesmo email (UserRepository); se existir, retornar Result.failure("E-mail já cadastrado").
- [ ] Gerar ids (User, StudentProfile se STUDENT); gerar hash da senha; criar User e persistir; persistir credencial (userId, hash); se STUDENT, criar e persistir StudentProfile.
- [ ] Retornar Result.success com userId, email, name, role (CreateAccountResult ou equivalente).

## 3. Testes unitários

- [ ] Sucesso como TRAINER: User criado, credencial persistida, resultado correto.
- [ ] Sucesso como STUDENT: User + StudentProfile criados, credencial persistida.
- [ ] Falha: email inválido; senha curta; email já existente; nome vazio.
- [ ] Usar repositórios in-memory; commonTest, kotlin.test.

---

*Referência: specs/spec-20-feature-criar-conta-v1.md*

---

# tasks — spec-21: UI Criar conta

Decomposição da `spec-21-ui-criar-conta-v1.md`. **Ordem recomendada:** executar na sequência. Escopo: **composeApp**. Depende de spec-20 (CreateAccountUseCase).

---

## 1. Acesso à tela de criar conta

- [ ] Na tela de entrada: adicionar link/botão "Criar conta" (ou "Cadastrar-se"); ao tocar, navegar para tela de criar conta.
- [ ] Nas telas de login (professor e aluno): adicionar link "Criar conta" (ou "Não tem conta? Cadastre-se"); ao tocar, navegar para tela de criar conta (opcional: passar papel pré-selecionado).

## 2. Tela de criar conta

- [ ] Criar tela com TopAppBar (título "Criar conta", botão Voltar).
- [ ] Campos: e-mail, senha, confirmar senha (mascarados), nome, papel (Professor/Aluno — chips ou radio), nome de exibição (opcional, visível quando papel = Aluno).
- [ ] Validação na UI: e-mail válido; senha mín. 6 caracteres; confirmar senha = senha; nome não vazio e ≤ 120 caracteres.
- [ ] Botão "Criar conta"; ao submeter: chamar CreateAccountUseCase; loading durante a chamada; em falha exibir mensagem (ex.: "E-mail já cadastrado"); em sucesso: navegar para tela de login do papel ou fazer login automático e ir para a home (definir qual fluxo).
- [ ] "Voltar" retorna à tela anterior (entrada ou login).

## 3. Navegação e integração

- [ ] Incluir rota/tela CreateAccount no grafo de navegação (App.kt / AppScreen); passar argumentos se necessário (ex.: role pré-selecionado).
- [ ] Garantir que CreateAccountUseCase está disponível no composeApp (injeção/dependências).

## 4. Critérios de aceitação

- [ ] "Criar conta" acessível da entrada e das telas de login.
- [ ] Formulário completo com validação; uso do CreateAccountUseCase; sucesso e falha tratados; Voltar funcionando.
- [ ] Implementação no composeApp; uso do padrão AppBarScreen (spec atual).

---

*Referência: specs/spec-21-ui-criar-conta-v1.md*
