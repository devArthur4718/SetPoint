# tasks — spec-06: Feature criar aluno

Decomposição da `spec-06-feature-criar-aluno-v1.md`. **Ordem recomendada:** executar na sequência.

---

## 1. Modelo de resultado

- [x] Criar tipo (data class ou sealed) **CreateStudentResult** com User e StudentProfile criados (ou apenas User, com profile opcional).
- [x] Colocar em pacote de aplicação/use case (ex.: `application.usecase` ou `domain.usecase`).

---

## 2. CreateStudentUseCase

- [x] Criar classe (ou função) **CreateStudentUseCase** que recebe: email, name, displayName (opcional), trainerId (contexto).
- [x] Dependências: UserRepository, StudentProfileRepository; receber por construtor.
- [x] Retorno: `Result<CreateStudentResult>` (ou equivalente com sucesso/falha).

---

## 3. Fluxo no use case

- [x] Gerar id para User e para StudentProfile (ex.: UUID; pode usar expect/actual ou lib comum).
- [x] Chamar User.create(id, email, name, Role.STUDENT); em falha, retornar Result.failure com mensagem.
- [x] Chamar userRepository.save(user); em falha (email duplicado), retornar Result.failure.
- [x] Chamar StudentProfile.create(idProfile, userId, displayName, createdAt); em falha, retornar Result.failure.
- [x] Chamar studentProfileRepository.save(profile); em falha, retornar Result.failure.
- [x] Retornar Result.success(CreateStudentResult(user, profile)).

---

## 4. Testes unitários — sucesso

- [x] Teste: criar aluno com email e nome válidos; verificar que User e StudentProfile estão nos repositórios (mock ou in-memory) e que o resultado contém os dados corretos.

---

## 5. Testes unitários — falhas

- [x] Teste: email inválido (vazio ou formato) → Result.failure, nenhum save.
- [x] Teste: nome vazio ou em branco → Result.failure, nenhum save.
- [x] Teste: email já existente (outro User) → Result.failure ao chamar userRepository.save (ou use case retorna falha), nenhum StudentProfile criado.

---

## 6. Critérios finais

- [x] Domain e repositórios não alterados; use case apenas os utiliza.
- [x] Rodar `:shared:jvmTest` e garantir que passam.

---

*Referência: specs/spec-06-feature-criar-aluno-v1.md*
