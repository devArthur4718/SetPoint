# feature: UI — Criar conta (telas de cadastro)

Definir as **telas de criar conta** no SetPoint (composeApp): acesso a partir da entrada ou do login, formulário de cadastro (e-mail, senha, confirmação de senha, nome, papel) e integração com o caso de uso CreateAccount (spec-20). Após sucesso, redirecionar para login ou fazer login automático.

## requisitos

- **Escopo**: módulo **composeApp**; consome CreateAccountUseCase (spec-20). Funciona em conjunto com a entrada (spec-13) e o login (spec-18).
- **Objetivo**: o usuário que não tem conta pode **criar uma** como professor ou aluno, informando e-mail, senha e nome; após sucesso, entrar no app (via tela de login ou login automático).

### Acesso à tela de criar conta

- **Da tela de entrada**: link ou botão **“Criar conta”** (ou “Cadastrar-se”) visível na tela inicial, ao lado ou abaixo das opções “Sou professor” e “Sou aluno”. Ao tocar, navegar para a **tela de criar conta**.
- **Das telas de login**: em cada tela de login (professor e aluno), link **“Criar conta”** (ou “Não tem conta? Cadastre-se”) que leva à tela de criar conta. Opcional: já pré-selecionar o papel (professor ou aluno) conforme a tela de login de onde veio.
- Em todos os casos, a tela de criar conta deve ter **“Voltar”** (TopAppBar ou botão) para retornar à tela anterior (entrada ou login).

### Tela de criar conta

- **Campos**:
  - **E-mail**: texto, teclado de e-mail; obrigatório.
  - **Senha**: texto mascarado; obrigatório; mínimo de caracteres (ex.: 6), indicado na UI.
  - **Confirmar senha**: texto mascarado; obrigatório; deve ser igual à senha.
  - **Nome**: texto; obrigatório (regras do domínio: 1–120 caracteres).
  - **Papel (role)**: se não vier pré-definido pelo fluxo, selector (Professor ou Aluno) — ex.: dois chips, radio ou dropdown.
  - **Nome de exibição** (opcional): apenas quando papel = Aluno; mesmo conceito do StudentProfile.displayName.
- **Validação na UI**:
  - E-mail não vazio e formato válido.
  - Senha não vazia e com mínimo de caracteres (ex.: 6).
  - Confirmar senha igual à senha.
  - Nome não vazio e dentro do limite (ex.: 120 caracteres).
  - Mensagens de erro inline ou abaixo dos campos, sem submeter até válido (ou exibir erros após primeiro submit).
- **Botão**: “Criar conta” (ou “Cadastrar”) que submete o formulário.
- **Comportamento ao submeter**:
  - Chamar CreateAccountUseCase(email, password, name, role, displayName?) com os dados do formulário.
  - **Loading**: indicador de carregamento e botão desabilitado durante a chamada.
  - **Sucesso**: opção A — navegar para a **tela de login** do papel escolhido (e opcionalmente exibir mensagem “Conta criada. Faça login.”); opção B — **login automático**: após criar conta, chamar o serviço de login com o mesmo e-mail/senha, guardar sessão e navegar para a home correspondente. A escolha (A ou B) pode ser definida nas tasks.
  - **Falha**: exibir mensagem de erro (ex.: “E-mail já cadastrado”, “Dados inválidos”) sem navegar.
- **Voltar**: retorna à tela de entrada ou à tela de login de onde veio.

### Navegação

- Fluxos possíveis:
  - **Entrada** → “Criar conta” → **Tela criar conta** → (sucesso) → **Login** ou **Home**.
  - **Login professor** → “Criar conta” → **Tela criar conta** (papel professor pré-selecionado) → Voltar → Login professor.
  - **Login aluno** → “Criar conta” → **Tela criar conta** (papel aluno pré-selecionado) → Voltar → Login aluno.
- A tela de criar conta deve usar o mesmo padrão visual das demais (TopAppBar com voltar, spec-16 quando aplicável).

## regras de negócio

- Só é possível criar conta com e-mail que ainda não exista no sistema (validado pelo use case).
- Senha e confirmação devem coincidir; senha deve respeitar o mínimo definido no use case.
- Papel (professor ou aluno) determina se o use case cria apenas User ou User + StudentProfile.

## critérios de aceitação

- Existe **link/botão “Criar conta”** na tela de entrada e nas telas de login (professor e aluno).
- Existe **tela de criar conta** com campos: e-mail, senha, confirmar senha, nome, papel (e opcionalmente nome de exibição para aluno).
- Validação na UI para todos os campos obrigatórios e para “senha = confirmar senha”.
- Ao submeter com dados válidos: CreateAccountUseCase é chamado; em sucesso, usuário é redirecionado para login ou para a home (login automático); em falha (ex.: e-mail já cadastrado), mensagem de erro é exibida.
- “Voltar” retorna à tela anterior (entrada ou login).
- Loading e mensagens de erro tratados.
- Implementação no composeApp; shared não alterado (apenas consumo do use case). Telas seguem o padrão de layout (ex.: AppBarScreen) quando existir.

---

*Versão: v1*
*Depende de: spec-20 (CreateAccountUseCase), spec-13 (entrada), spec-18 (login — telas e fluxo)*
*Escopo: composeApp*
