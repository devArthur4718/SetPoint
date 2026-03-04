# feature: Criar conta (autocadastro)

Permitir que uma pessoa **crie sua própria conta** no SetPoint como **professor** (TRAINER) ou **aluno** (STUDENT): registro com e-mail, senha e nome. O caso de uso cria o User (e, se aluno, o StudentProfile) e persiste a senha de forma segura (hash). Não envolve UI nesta spec — apenas o caso de uso no shared.

## requisitos

- **Ator**: qualquer pessoa (não autenticada) que deseja se cadastrar.
- **Entrada**: email, senha, nome, role (TRAINER ou STUDENT). Opcionalmente displayName quando role = STUDENT (nome de exibição para o professor identificar o aluno).
- **Saída**: sucesso retornando o usuário criado (id, email, name, role) ou falha com motivo (email/senha/nome inválido, email já existente).
- **Fluxo**:
  1. Validar email (formato e não vazio), senha (mínimo de caracteres, ex.: 6), nome (regras da spec-01: 1–120 caracteres), role (TRAINER ou STUDENT).
  2. Verificar se já existe User com o mesmo email; se existir, retornar falha (email já cadastrado).
  3. Gerar id do User (e do StudentProfile se STUDENT).
  4. Gerar hash da senha (ex.: bcrypt); nunca persistir senha em texto plano.
  5. Criar User via domínio (id, email, name, role) e persistir via UserRepository.
  6. Persistir credencial (userId + hash da senha) em camada de autenticação (tabela/estrutura que o login use para validar) — ver dependências abaixo.
  7. Se role = STUDENT: criar StudentProfile (idProfile, userId, displayName?) e persistir via StudentProfileRepository.
  8. Retornar sucesso com dados do User criado (id, email, name, role).
- **Caso de uso**: classe ou função (ex.: `CreateAccountUseCase` ou `RegisterUseCase`) no shared que recebe (email, password, name, role, displayName?) e retorna `Result<CreateAccountResult>`, onde CreateAccountResult expõe pelo menos userId, email, name, role. O caso de uso usa UserRepository, StudentProfileRepository (se STUDENT) e um contrato para persistir credencial (ex.: AuthRepository ou UserCredentialsRepository.save(userId, passwordHash)).
- Código em `shared/commonMain`, camada de aplicação (use case); domain e data utilizados ou estendidos conforme necessário.

## regras de negócio

- Email deve ser único no sistema (mesma regra da spec-01); não pode criar conta com email já existente.
- Senha: mínimo de caracteres definido (ex.: 6); armazenar apenas hash, nunca texto plano.
- Nome: regras do User (spec-01), 1–120 caracteres.
- Role: apenas TRAINER ou STUDENT; ao criar como STUDENT, criar também StudentProfile (com displayName opcional).
- Criar conta não exige usuário autenticado; não há trainerId (diferente de spec-06 “criar aluno”, em que o professor cria o aluno).

## dependências e impacto

- **Domínio/dados**: o modelo atual (spec-01) não inclui senha. Para criar conta é necessário:
  - **Persistência de credencial**: tabela ou estrutura que associe userId a hash de senha (ex.: UserCredentials, ou campo em tabela User se a spec de dados for estendida). O mesmo armazenamento será usado pelo login (spec-18).
  - **Repositório de credenciais**: interface (ex.: AuthRepository ou UserCredentialsRepository) com método para salvar (userId, passwordHash) e, para o login, método para obter/validar por email e senha. A implementação pode ser local (SQLDelight) ou delegada a backend (API de registro).
- **Spec-06 (Criar aluno)**: continua sendo “professor cria aluno”; não inclui senha. O aluno criado pelo professor (spec-06) pode precisar de um fluxo “definir senha” depois, ou a spec-06 pode ser estendida para aceitar senha inicial — isso pode ficar para outra spec. Nesta spec, “criar conta” é autocadastro com senha.

## critérios de aceitação

- Existe caso de uso (CreateAccountUseCase ou equivalente) que recebe email, password, name, role e opcionalmente displayName (para STUDENT) e executa o fluxo descrito.
- Em sucesso: User é persistido; credencial (hash da senha) é persistida; se STUDENT, StudentProfile é persistido; resultado expõe userId, email, name, role.
- Em falha de validação (email/senha/nome inválido): retorna Result.failure com mensagem adequada, sem persistir.
- Em falha de unicidade (email já existe): retorna Result.failure com mensagem “E-mail já cadastrado” (ou equivalente), sem alterar dados.
- Senha nunca é armazenada em texto plano.
- Testes unitários: sucesso como TRAINER; sucesso como STUDENT (com e sem displayName); falha com email inválido; falha com senha curta; falha com email duplicado.
- Código no shared (commonMain); domain e repositórios utilizados ou estendidos conforme combinado nas tasks (ex.: novo repositório de credenciais).

---

*Versão: v1*
*Depende de: spec-01 (User, StudentProfile), spec-04 (repositórios); exige persistência de senha (hash) — mesma base do login, spec-18*
*Escopo: shared (use case + eventual extensão de dados)*
