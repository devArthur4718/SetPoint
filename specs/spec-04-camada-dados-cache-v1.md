# feature: Camada de dados com cache local (offline-first)

Definição das **interfaces de repositório** e de uma **fonte de dados local** para persistir entidades no dispositivo, permitindo uso offline. Leitura e escrita são feitas prioritariamente no cache local; sincronização com backend fica para spec futura.

Conforme `docs/REQUISITOS-PRODUTO.md`: cache local obrigatório, fonte local como primária.

## requisitos

- **Interfaces de repositório** (em `shared`, pacote `data` ou `domain.repository`): contratos que a aplicação usa para obter e salvar entidades. Implementações vivem na camada data e usam uma fonte local.
  - Cada repositório expõe operações necessárias ao MVP, por exemplo: getById, list, save/insert, delete (quando fizer sentido).
  - Tipos de retorno e parâmetros usam entidades de domínio (User, Exercise, WorkoutTemplate, etc.); não expor DTOs ou tipos de banco na interface.
- **Fonte de dados local (cache)**:
  - Abstração: uma interface ou contrato que permite salvar/recuperar/listar entidades (ou representações serializáveis) por tipo. A implementação pode ser in-memory nesta spec (validar o desenho) ou persistente (ex.: SQLDelight em spec futura).
  - Nesta spec: implementar **LocalDataSource** in-memory (armazenamento em memória durante a sessão) para que os repositórios funcionem sem rede; em spec futura pode ser substituído por persistência em disco (ex.: SQLDelight).
- **Implementações de repositório**: cada interface tem uma implementação que usa o LocalDataSource (in-memory); não acessar rede.
- Código em `shared/commonMain`: interfaces em pacote exposto (ex.: `data.repository`), implementações e LocalDataSource em `data.local` ou equivalente; domain não depende de data.

## regras de negócio

- Fluxo de leitura/escrita: UI/ViewModel → Repository → LocalDataSource. Não ler do backend diretamente para fluxos que precisam funcionar offline.
- Unicidade (ex.: User por email, um StudentProfile por userId) deve ser garantida na implementação do repositório ao salvar (consultar antes de inserir; falhar ou atualizar conforme regra).

## escopo por entidade (MVP)

- **User**: salvar, getById, getByEmail, list (ou listByRole).
- **StudentProfile**: salvar, getById, getByUserId.
- **Exercise**: salvar, getById, list.
- **WorkoutTemplate**: salvar, getById, listByTrainerId.
- **WorkoutExercise**: faz parte do WorkoutTemplate (não repositório separado no MVP; template carrega com exercises).
- **WorkoutAssignment**: salvar, getById, listByStudentUserId (treinos atribuídos ao aluno).
- **WorkoutExecution**: salvar, getById, listByWorkoutAssignmentId (histórico de execuções de uma atribuição).
- **SetExecution**: faz parte de WorkoutExecution (salvo junto; não repositório separado no MVP).

Repositórios: UserRepository, StudentProfileRepository, ExerciseRepository, WorkoutTemplateRepository, WorkoutAssignmentRepository, WorkoutExecutionRepository.

## critérios de aceitação

- Existem interfaces UserRepository, StudentProfileRepository, ExerciseRepository, WorkoutTemplateRepository, WorkoutAssignmentRepository, WorkoutExecutionRepository com os métodos descritos acima (assinaturas em Kotlin; podem usar suspending/coroutines se o projeto usar).
- Existe abstração de fonte local (interface LocalDataSource ou similar) usada pelas implementações.
- Implementações in-memory de cada repositório que delegam ao LocalDataSource; ao salvar User/StudentProfile, respeitar regras de unicidade (ex.: getByEmail antes de inserir User).
- Testes unitários: pelo menos um repositório (ex.: UserRepository ou ExerciseRepository) testado com LocalDataSource in-memory — salvar, getById, list; e rejeição de User duplicado por email (se aplicável).
- Nenhuma dependência de rede ou HTTP nas implementações desta spec; domain não depende de módulo data (data pode depender de domain).

---

*Versão: v1 — 2025-03-03*
