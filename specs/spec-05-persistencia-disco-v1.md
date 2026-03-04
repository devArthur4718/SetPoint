# feature: Persistência em disco (cache local persistente)

Implementar uma **fonte de dados local persistente** que grava as entidades em disco (banco SQL local), substituindo ou complementando a implementação in-memory da spec-04. Os repositórios continuam usando a interface [LocalDataSource]; apenas a implementação passa a persistir entre sessões do app.

Conforme `docs/REQUISITOS-PRODUTO.md`: cache local obrigatório; dados devem ser armazenados no dispositivo.

## requisitos

- **Tecnologia**: usar **SQLDelight** para banco SQL type-safe em Kotlin Multiplatform (Android, iOS, JVM). O schema fica em arquivos `.sq` no módulo shared; o driver é configurado por plataforma (expect/actual ou injeção).
- **Implementação de [LocalDataSource]**: nova classe (ex.: `SqlDelightLocalDataSource` ou `PersistentLocalDataSource`) que implementa a interface [LocalDataSource] existente, persistindo User, StudentProfile, Exercise, WorkoutTemplate, WorkoutExercise, WorkoutAssignment, WorkoutExecution e SetExecution em tabelas SQL.
- **Schema**:
  - Tabelas para cada entidade; entidades aninhadas (WorkoutExercise no template, SetExecution na execução) como tabelas com chave estrangeira (workoutTemplateId, workoutExecutionId).
  - User: id, email, name, role; índice único em email.
  - StudentProfile: id, userId (único), displayName, createdAt.
  - Exercise: id, name, description.
  - WorkoutTemplate: id, name, trainerId, createdAt.
  - WorkoutExercise: id, workoutTemplateId, exerciseId, order, sets, reps, loadKg, restSeconds.
  - WorkoutAssignment: id, workoutTemplateId, studentUserId, assignedAt.
  - WorkoutExecution: id, workoutAssignmentId, executedAt.
  - SetExecution: id, workoutExecutionId, workoutExerciseId, setNumber, actualReps, actualLoadKg.
- **Driver SQLDelight**: configuração por target (Android, iOS, JVM); em commonMain usar expect/actual para obter o driver, ou receber o driver por construtor na implementação de LocalDataSource.
- As **implementações de repositório** (UserRepositoryImpl, etc.) **não mudam de contrato**; passam a receber a implementação persistente de LocalDataSource (injeção/configuração), permitindo trocar in-memory por persistente sem alterar repositórios.
- Código em `shared`: schema e queries em fonte SQLDelight; implementação de LocalDataSource em `data.local`; sem dependência de rede.

## regras de negócio

- Unicidade de email (User) e de userId (StudentProfile) mantida no repositório; o schema pode ter UNIQUE nas colunas correspondentes para garantir integridade no banco.
- Ao carregar WorkoutTemplate, carregar WorkoutExercise ordenados por order; ao carregar WorkoutExecution, carregar SetExecution (ordenados por workoutExerciseId/setNumber conforme necessário).

## critérios de aceitação

- Existe schema SQLDelight com as tabelas descritas e queries para insert/select/update/delete (ou upsert) por entidade.
- Existe implementação de [LocalDataSource] que usa SQLDelight para persistir e recuperar todas as entidades; assinaturas compatíveis com a interface atual.
- Repositórios existentes funcionam sem alteração de código ao receber a nova implementação de LocalDataSource (teste de integração ou teste que injeta a implementação persistente e valida save/get após “reabrir” o banco ou nova instância).
- Testes: pelo menos um fluxo (ex.: salvar User e recuperar após criar nova instância do driver/DataSource) que comprova persistência em disco.
- Build para Android e iOS (ou JVM) compila e, se aplicável, testes de persistência passam.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-04-camada-dados-cache-v1*
