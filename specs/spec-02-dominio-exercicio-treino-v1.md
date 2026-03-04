# feature: Domínio de exercício e treino (template)

Definição das entidades **Exercise**, **WorkoutTemplate** e **WorkoutExercise**, que permitem ao professor criar catálogo de exercícios e montar treinos (templates) com séries, repetições, carga e descanso. Implementação em código compartilhado (shared), camada de domínio, sem UI.

## requisitos

- **Exercise**: exercício do catálogo (ex.: "Supino reto", "Agachamento").
  - Campos: `id`, `name`, `description` (opcional).
  - `name`: obrigatório, tamanho mínimo 1, máximo 120 caracteres.
  - Criação apenas via factory; entidade imutável.
- **WorkoutExercise**: um exercício dentro de um treino, com parâmetros de execução.
  - Campos: `id`, `exerciseId` (referência ao Exercise), `order` (posição no treino, 1-based), `sets`, `reps`, `loadKg` (opcional), `restSeconds` (opcional).
  - `sets`: número de séries, inteiro ≥ 1.
  - `reps`: número de repetições por série, inteiro ≥ 1 (ou 0 para "até a falha" se o produto permitir; neste MVP pode ser ≥ 1).
  - `loadKg`: carga em kg, opcional; se presente, ≥ 0.
  - `restSeconds`: descanso em segundos entre séries, opcional; se presente, ≥ 0.
  - Criação apenas via factory; entidade imutável.
- **WorkoutTemplate**: template de treino criado pelo professor.
  - Campos: `id`, `name`, `trainerId` (referência ao User TRAINER), `exercises` (lista ordenada de WorkoutExercise), `createdAt` (opcional, Long epoch millis).
  - `name`: obrigatório, tamanho mínimo 1, máximo 120 caracteres.
  - `exercises`: lista não vazia; ordem definida por `WorkoutExercise.order`.
  - Criação apenas via factory; entidade imutável.
- Código no módulo `shared`, em `commonMain`, na camada de domínio (sem dependência de data/UI).

## regras de negócio

- Nome de Exercise e de WorkoutTemplate não pode ser nulo, vazio ou apenas espaços; máximo 120 caracteres.
- WorkoutTemplate deve ter ao menos um WorkoutExercise.
- Em um WorkoutTemplate, os `order` dos WorkoutExercise devem ser únicos e coerentes (ex.: 1, 2, 3 …); a lista pode ser ordenada por `order`.
- Identificadores (`id`) são opacos (String); geração de ID fica fora do domínio (camada data).

## critérios de aceitação

- Exercise é criado apenas com nome válido; rejeição para nome vazio ou acima de 120 caracteres.
- WorkoutExercise é criado com exerciseId válido (não vazio), sets ≥ 1, reps ≥ 1; loadKg e restSeconds opcionais e ≥ 0 quando presentes.
- WorkoutTemplate é criado com nome válido, trainerId não vazio e lista não vazia de WorkoutExercise; rejeição para nome inválido, trainerId vazio ou lista vazia.
- Parâmetros de criação com propriedades imutáveis (val/readonly onde aplicável).
- Testes unitários cobrem:
  - Criação de Exercise com dados válidos e rejeição com nome inválido.
  - Criação de WorkoutExercise com dados válidos e rejeição (exerciseId vazio, sets/reps menor que 1, loadKg/restSeconds menor que 0).
  - Criação de WorkoutTemplate com dados válidos e rejeição (nome inválido, trainerId vazio, lista vazia).
- Código de domínio vive em `shared` (commonMain), sem referências a Android, iOS ou Compose.

---

*Versão: v1 — 2025-03-03*
