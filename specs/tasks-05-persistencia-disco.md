# tasks — spec-05: Persistência em disco

Decomposição da `spec-05-persistencia-disco-v1.md`. **Ordem recomendada:** executar na sequência.

---

## 1. Dependência SQLDelight

- [ ] Adicionar plugin e dependência SQLDelight ao projeto (shared e/ou raiz); configurar versão compatível com KMP.
- [ ] Configurar source set dos arquivos `.sq` (pasta `src/commonMain/sq` ou equivalente).

---

## 2. Schema — tabelas principais

- [ ] Criar tabela **User** (id, email, name, role); índice UNIQUE em email.
- [ ] Criar tabela **StudentProfile** (id, userId UNIQUE, displayName, createdAt).
- [ ] Criar tabela **Exercise** (id, name, description).
- [ ] Criar tabela **WorkoutTemplate** (id, name, trainerId, createdAt).

---

## 3. Schema — tabelas relacionadas

- [ ] Criar tabela **WorkoutExercise** (id, workoutTemplateId, exerciseId, order, sets, reps, loadKg, restSeconds).
- [ ] Criar tabela **WorkoutAssignment** (id, workoutTemplateId, studentUserId, assignedAt).
- [ ] Criar tabela **WorkoutExecution** (id, workoutAssignmentId, executedAt).
- [ ] Criar tabela **SetExecution** (id, workoutExecutionId, workoutExerciseId, setNumber, actualReps, actualLoadKg).

---

## 4. Queries SQLDelight

- [ ] Queries para User: insert, selectById, selectByEmail, selectAll.
- [ ] Queries para StudentProfile, Exercise, WorkoutTemplate, WorkoutExercise, WorkoutAssignment, WorkoutExecution, SetExecution: insert (ou upsert), selectById, select por FK (por templateId, assignmentId, etc.).

---

## 5. Driver (expect/actual)

- [ ] Definir expect para obter [Driver] SQLDelight no commonMain (ex.: expect fun createDriver(): SqlDriver).
- [ ] Implementar actual para Android, iOS e JVM (ou apenas os targets necessários).

---

## 6. SqlDelightLocalDataSource

- [ ] Implementar classe que implementa [LocalDataSource] e usa o Driver + queries para persistir/recuperar cada entidade.
- [ ] Mapear entidades de domínio ↔ linhas/tabelas (conversão nos métodos da interface).

---

## 7. Testes de persistência

- [ ] Teste: salvar User, criar nova instância do DataSource (novo driver/banco em memória ou arquivo), recuperar User e validar dados.
- [ ] (Opcional) Teste com WorkoutTemplate + WorkoutExercise para validar relacionamento.

---

## 8. Integração

- [ ] Garantir que repositórios existentes funcionam ao receber SqlDelightLocalDataSource (injeção ou factory).
- [ ] Build Android/iOS/JVM e testes passando.

---

*Referência: specs/spec-05-persistencia-disco-v1.md*
