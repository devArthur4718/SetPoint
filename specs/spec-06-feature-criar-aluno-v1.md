# feature: Criar aluno (primeira feature — professor)

Permitir que o **professor** (User com role TRAINER) crie um **aluno** no sistema: um User com role STUDENT e um StudentProfile associado. A operação usa os repositórios e o domínio já definidos (specs 01 e 04); não envolve UI nem rede nesta spec — apenas o caso de uso e a validação de fluxo.

## requisitos

- **Ator**: usuário autenticado com role TRAINER (o “professor”).
- **Entrada**: email do aluno, nome do aluno, opcionalmente displayName (nome de exibição para o professor identificar o aluno na lista). O professor que está criando (trainerId) é identificado pelo contexto da aplicação (ex.: usuário logado).
- **Saída**: sucesso retornando o aluno criado (User e StudentProfile) ou falha com motivo (email inválido, nome inválido, email já existente).
- **Fluxo**:
  1. Validar email e nome com as regras de domínio (User.create); role = STUDENT.
  2. Gerar ou receber id do User e do StudentProfile (geração de id fica fora do domínio puro; pode ser feita no use case ou na camada de dados).
  3. Criar User via User.create(id, email, name, Role.STUDENT); em caso de falha, retornar erro.
  4. Salvar User via UserRepository.save; se falhar (ex.: email duplicado), retornar erro.
  5. Criar StudentProfile via StudentProfile.create(idProfile, userId, displayName, createdAt?); em caso de falha, retornar erro.
  6. Salvar StudentProfile via StudentProfileRepository.save; se falhar, retornar erro.
  7. Retornar sucesso com o User (e opcionalmente o StudentProfile) criado.
- **Caso de uso**: uma classe ou função (ex.: `CreateStudentUseCase`) no shared que recebe (email, name, displayName?, trainerId para contexto) e retorna `Result<CreateStudentResult>` onde CreateStudentResult expõe o User e o StudentProfile criados. O caso de uso usa UserRepository e StudentProfileRepository (injetados ou recebidos); não acessa UI nem rede.
- Código em `shared/commonMain`: pacote de aplicação ou use case (ex.: `application.usecase` ou `domain.usecase`); domain e data já existem.

## regras de negócio

- Email e nome devem obedecer às regras da spec-01 (User): email válido e único, nome 1–120 caracteres.
- Só pode existir um StudentProfile por userId; ao criar aluno, criamos um User STUDENT e um StudentProfile vinculado.
- Geração de ids (User.id, StudentProfile.id): pode ser UUID ou delegada à camada de dados; não faz parte do domínio puro.

## critérios de aceitação

- Existe caso de uso (ou função equivalente) que recebe email, name, displayName opcional e trainerId (ou contexto do professor) e executa o fluxo descrito.
- Em sucesso: User e StudentProfile são persistidos nos repositórios e o resultado expõe os dados criados.
- Em falha de validação (email/nome inválido): retorna Result.failure com mensagem adequada, sem persistir.
- Em falha de unicidade (email já existe): retorna Result.failure sem alterar dados.
- Testes unitários do caso de uso: sucesso com dados válidos; falha com email inválido; falha com nome vazio; falha com email duplicado (mock ou repositório in-memory).
- Domain e repositórios não são alterados (apenas utilizados); a feature não adiciona rede nem UI.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-01 (domínio User/StudentProfile), spec-04 (repositórios)*
