# feature: Domínio de usuário e perfis

Definição das entidades e regras de negócio para **User** (com role TRAINER ou STUDENT) e **StudentProfile**, que sustentam os dois perfis do app (personal trainer e aluno). Implementação em código compartilhado (shared), camada de domínio, sem UI.

## requisitos

- **User**: entidade com identificador único, email, nome e role.
  - Campos: `id`, `email`, `name`, `role`.
  - `role`: enum ou tipo fixo com valores `TRAINER` e `STUDENT`.
  - `email`: obrigatório, único no sistema, formato válido de email.
  - `name`: obrigatório, tamanho mínimo 1, máximo 120 caracteres.
- **StudentProfile**: perfil do aluno, vinculado a um único User com role STUDENT.
  - Campos: `id`, `userId` (referência ao User), `displayName` (opcional, para o professor identificar o aluno), `createdAt` (opcional, para auditoria).
  - Um User com role STUDENT pode ter no máximo um StudentProfile.
- Entidades e value objects devem ser imutáveis quando fizer sentido (ex.: parâmetros de criação podem ser final/readonly).
- Criação de User e StudentProfile via factory ou funções de construção no domínio, sem expor construtores vazios que permitam estado inválido.
- Código no módulo `shared`, em `commonMain`, na camada de domínio (sem dependência de data/UI).

## regras de negócio

- Não pode existir mais de um User com o mesmo email.
- User com role STUDENT pode ter zero ou um StudentProfile; User com role TRAINER não possui StudentProfile.
- Email deve ser válido (formato aceitável; validação básica sem depender de lib externa pesada).
- Nome não pode ser nulo, vazio ou apenas espaços; máximo 120 caracteres.
- Identificadores (`id`) são opacos (String ou tipo próprio); geração de ID fica fora do domínio puro (pode ser feita na camada data ao persistir).

## critérios de aceitação

- Existe tipo/enum para `role` com valores `TRAINER` e `STUDENT`.
- User é criado apenas com email, name e role válidos; tentativa com email vazio/inválido ou nome inválido resulta em falha (exceção ou Result) e não instancia User inválido.
- StudentProfile é criado apenas com `userId` referenciando um User; regra "um StudentProfile por User STUDENT" é respeitada na construção/factory (ou documentada como garantida pela camada de aplicação).
- Parâmetros de criação (ex.: `CreateUser`, `CreateStudentProfile`) são imutáveis (propriedades final/readonly onde aplicável).
- Testes unitários cobrem:
  - Criação de User com dados válidos (TRAINER e STUDENT).
  - Rejeição de User com email vazio, email inválido, nome vazio ou nome acima de 120 caracteres.
  - Criação de StudentProfile com `userId` válido.
  - Rejeição de StudentProfile com `userId` inválido ou vazio (conforme definido na implementação).
- Código de domínio vive em `shared` (commonMain), sem referências a Android, iOS ou Compose.

---

*Versão: v1 — 2025-03-03*
