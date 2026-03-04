# feature: Autenticação — Login com e-mail e senha

Definir o **fluxo de login** do SetPoint: tela de entrada com escolha de papel (professor ou aluno), **telas próprias de login** para cada papel (e-mail e senha), validação e sessão. Substitui o MVP atual (spec-13) em que o usuário apenas escolhia o papel sem credenciais.

## requisitos

- **Escopo**: entrada do app e autenticação; impacto em **composeApp** (telas, navegação, sessão) e possivelmente em **shared** (caso de uso de login, contrato com backend ou armazenamento local de credenciais).
- **Objetivo**: professor e aluno **entram com e-mail e senha** em telas dedicadas; após sucesso, o app conhece userId e role e navega para a home correspondente.

### Tela de entrada (atualizada)

- Primeira tela do app: **escolha de tipo de usuário** (como hoje), mas em vez de “logar” sem credenciais, o usuário é levado à **tela de login** do tipo escolhido.
- Opções: **“Sou professor”** e **“Sou aluno”** (ou equivalente).
- Ao tocar em “Sou professor”: navegar para a **tela de login do professor**.
- Ao tocar em “Sou aluno”: navegar para a **tela de login do aluno**.
- Em cada tela de login há **botão/link “Voltar”** para retornar à tela de entrada (trocar de tipo).

### Tela de login do professor

- **Campos**: e-mail (texto, teclado de e-mail) e senha (texto, mascarado).
- **Botão**: “Entrar” (ou “Login”) que submete as credenciais.
- **Validação na UI**: e-mail não vazio e formato válido; senha não vazia (mínimo de caracteres definido, ex.: 6). Mensagens de erro inline ou abaixo do campo.
- **Comportamento**: ao submeter, chamar o **serviço de autenticação** (use case ou repositório) com (email, senha, role = TRAINER). Se sucesso: guardar sessão (userId, role), navegar para a **home do professor** (spec-14). Se falha: exibir mensagem de erro (credenciais inválidas, usuário não encontrado, etc.) sem navegar.
- **Loading**: durante a chamada, indicador de carregamento e botão desabilitado.
- **Opcional**: link “Esqueci minha senha” (pode ficar fora do escopo nesta spec).

### Tela de login do aluno

- **Campos**: e-mail e senha (mesmo padrão da tela do professor).
- **Botão**: “Entrar” e mesma validação de campos.
- **Comportamento**: ao submeter, chamar o **serviço de autenticação** com (email, senha, role = STUDENT). Se sucesso: guardar sessão (userId, role), navegar para a **home do aluno** (spec-15). Se falha: exibir mensagem de erro.
- **Loading** e **Voltar****: iguais à tela do professor.

### Serviço de autenticação

- **Contrato**: dado (email, senha, role), retorna sucesso (userId, role) ou falha (mensagem).
- **Implementação** (escolha a ser detalhada nas tasks):
  - **Backend**: API de login (POST /auth/login ou similar) que recebe email/senha, valida e retorna token e dados do usuário (id, role); o app guarda token e userId/role na sessão.
  - **MVP local**: para desenvolvimento ou sem backend, validar email/senha contra dados locais (ex.: tabela ou estrutura que associe User a senha hash); se existir User com esse email, role correspondente e senha correta, retornar userId e role. Isso pode exigir extensão do domínio/dados (ex.: armazenamento de hash de senha por usuário) — ver dependências abaixo.
- Sessão após login: **userId** e **role** disponíveis para todo o app (como hoje); opcionalmente token para chamadas à API. Persistência da sessão (ex.: manter logado após fechar o app) pode ser definida nas tasks (DataStore, Keychain, etc.).

### Navegação e sessão

- Fluxo: **Entrada** → **Login professor** ou **Login aluno** → **Home professor** ou **Home aluno**.
- “Sair” nas homes (já existente) deve **limpar a sessão** e voltar à **tela de entrada** (não à tela de login).
- Se houver persistência de sessão: ao reabrir o app, se existir sessão válida, ir direto para a home correspondente; senão, mostrar tela de entrada.

## regras de negócio

- Apenas um usuário ativo por sessão (professor ou aluno).
- Login do professor só pode resultar em usuário com role TRAINER; login do aluno só em role STUDENT. O serviço de autenticação deve validar que o usuário retornado tem o role esperado.
- Credenciais inválidas ou usuário não encontrado: não navegar; exibir mensagem clara (ex.: “E-mail ou senha incorretos” sem revelar se o e-mail existe ou não).
- Senha: não armazenar em texto plano; uso de hash (ex.: bcrypt) no backend ou na camada que persiste senha localmente.

## dependências e impacto

- **Domínio/dados**: o modelo atual (spec-01) não inclui senha. Para login com senha é necessário:
  - **Opção A**: Backend expõe API de login; o app não armazena senha, apenas token e userId/role.
  - **Opção B**: Persistência local de credenciais (ex.: hash de senha por User): exige spec ou alteração em domínio/dados para incluir armazenamento seguro de senha (ou tabela de autenticação) e uso no shared (use case LoginUseCase que valida contra esse armazenamento).
- **Spec-13**: esta spec **substitui** o comportamento “entrada sem login” da spec-13; a tela de entrada passa a redirecionar para as telas de login em vez de definir userId fixo. As demais telas (14 e 15) continuam iguais, recebendo userId e role da sessão pós-login.

## critérios de aceitação

- Existe tela de entrada com duas opções: “Sou professor” e “Sou aluno”; cada uma leva à tela de login correspondente.
- Existe **tela de login do professor** com campos e-mail e senha, validação na UI, botão Entrar, loading e mensagem de erro em caso de falha; “Voltar” retorna à tela de entrada.
- Existe **tela de login do aluno** com os mesmos elementos; “Voltar” retorna à tela de entrada.
- Ao submeter credenciais válidas (professor): sessão guardada (userId, TRAINER), navegação para home do professor.
- Ao submeter credenciais válidas (aluno): sessão guardada (userId, STUDENT), navegação para home do aluno.
- Credenciais inválidas: mensagem de erro exibida, sem navegação.
- “Sair” nas homes limpa a sessão e volta à tela de entrada.
- Serviço de autenticação (use case ou equivalente) implementado — integrado a backend ou a armazenamento local conforme definido nas tasks.
- Implementação no composeApp (telas, navegação, sessão); shared alterado apenas se for criado LoginUseCase e/ou persistência de senha/token.

---

*Versão: v1 — esboço*
*Substitui/atualiza: fluxo de entrada da spec-13 (login com credenciais)*
*Depende de: spec-13 (navegação), spec-14 e 15 (homes); pode depender de nova spec de API de login ou extensão de dados para senha*
*Escopo: composeApp + eventual shared (use case de login)*
