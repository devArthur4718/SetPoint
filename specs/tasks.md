# tasks — spec-01: Domínio de usuário e perfis

Decomposição da `spec-01-dominio-usuario-perfis-v1.md` em tarefas de implementação. **Ordem recomendada:** executar na sequência.

---

## 1. Role (enum/tipo)

- [x] Criar tipo `Role` com valores `TRAINER` e `STUDENT` (enum class ou sealed).
- [x] Colocar em `shared/commonMain`, pacote de domínio (ex.: `com.devarthur.setpoint.domain` ou equivalente).

---

## 2. Entidade User

- [x] Criar classe (ou data class) **User** imutável com: `id`, `email`, `name`, `role`.
- [x] Garantir que não há construtor público que permita estado inválido (ou que a única forma de criar é via factory).
- [x] Manter em `shared/commonMain`, camada domain.

---

## 3. Validação e factory de User

- [x] Implementar validação de email (formato aceitável; validação básica).
- [x] Implementar validação de nome: não nulo, não vazio, não só espaços, máximo 120 caracteres.
- [x] Criar factory ou função de construção (ex.: `User.create(...)` ou `UserFactory.create(...)`) que:
  - aceita parâmetros imutáveis (ex.: `email`, `name`, `role`; `id` pode ser opcional ou gerado fora),
  - retorna `User` em caso de sucesso,
  - falha (exceção ou `Result`) em caso de email vazio/inválido ou nome inválido.
- [x] Não instanciar User com dados inválidos.

---

## 4. Entidade StudentProfile

- [x] Criar classe (ou data class) **StudentProfile** imutável com: `id`, `userId`, `displayName` (opcional), `createdAt` (opcional).
- [x] Manter em `shared/commonMain`, camada domain.

---

## 5. Factory de StudentProfile

- [x] Criar factory ou função de construção que aceita `userId` (e opcionais) com parâmetros imutáveis.
- [x] Rejeitar `userId` vazio/inválido (conforme definição: ex.: string vazia ou blank).
- [x] Documentar que a regra “um StudentProfile por User STUDENT” é garantida pela camada de aplicação/repositório (não pelo domínio puro).

---

## 6. Testes unitários — User

- [x] Teste: criação de User com dados válidos, role TRAINER.
- [x] Teste: criação de User com dados válidos, role STUDENT.
- [x] Teste: rejeição quando email está vazio.
- [x] Teste: rejeição quando email é inválido (formato).
- [x] Teste: rejeição quando nome está vazio ou em branco.
- [x] Teste: rejeição quando nome tem mais de 120 caracteres.

---

## 7. Testes unitários — StudentProfile

- [x] Teste: criação de StudentProfile com `userId` válido.
- [x] Teste: rejeição quando `userId` é vazio ou inválido (conforme definido).

---

## 8. Critérios finais

- [x] Parâmetros de criação (objetos de input das factories) com propriedades imutáveis (val/readonly).
- [x] Nenhum código de domínio em androidMain/iosMain; apenas commonMain.
- [x] Rodar todos os testes e garantir que passam.

---

*Referência: specs/spec-01-dominio-usuario-perfis-v1.md*
