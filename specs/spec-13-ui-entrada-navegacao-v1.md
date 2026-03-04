# feature: UI — Entrada no app e navegação

Definir a **tela de entrada** do app e a **navegação por papel** (professor vs aluno), permitindo que o usuário acesse o fluxo correto. Implementação no módulo **composeApp** (Compose Multiplatform); consome apenas a camada shared (sem novos use cases — usa contexto de usuário/role para decidir destino).

## requisitos

- **Tela inicial (entrada)**:
  - Primeira tela exibida ao abrir o app.
  - Em MVP sem autenticação: permitir **seleção de papel** (ex.: dois botões ou opções: “Entrar como professor” e “Entrar como aluno”) ou uso de **usuário fixo** para desenvolvimento (ex.: um User TRAINER e um User STUDENT pré-carregados ou selecionáveis).
  - Alternativa futura: se houver login (spec opcional), a entrada mostra tela de login e, após sucesso, redireciona conforme role do usuário.
- **Navegação por role**:
  - Se **TRAINER**: navegar para o **fluxo professor** (ex.: tela principal/home do professor). Ver spec-14.
  - Se **STUDENT**: navegar para o **fluxo aluno** (ex.: tela principal/home do aluno). Ver spec-15.
- **Contexto do usuário**:
  - O app precisa conhecer o **userId** e o **role** do usuário “logado” para passar aos use cases (trainerId, studentUserId). Em MVP pode ser um estado em memória definido na entrada (ex.: ao escolher “professor”, definir userId = id de um trainer fixo; ao escolher “aluno”, userId = id de um student fixo). Fonte desse estado: seleção na tela inicial ou, no futuro, sessão após login.
- Código no **composeApp**: telas Compose, navegação (ex.: navegador único com rotas ou gráfico de navegação), estado do usuário atual (ViewModel ou estado hoisted).

## regras de negócio

- Apenas um “usuário” ativo por sessão (professor ou aluno).
- Navegação: da entrada só se vai para fluxo professor ou fluxo aluno; não há troca de role no meio do fluxo nesta spec (pode voltar à tela inicial para “trocar de usuário” se a entrada permitir).

## critérios de aceitação

- Existe uma tela de entrada (primeira tela do app) que permite escolher papel professor ou aluno (ou equivalente para MVP).
- Ao escolher professor: navegação para a home/lista do professor (spec-14).
- Ao escolher aluno: navegação para a home/lista do aluno (spec-15).
- O userId e role do “usuário atual” estão disponíveis para as telas seguintes (ex.: injetados no gráfico de navegação ou em ViewModel compartilhado).
- Implementação no composeApp; não altera shared. Opcional: testes de UI (Compose Test) que verificam que a escolha de papel leva à tela correta.

---

*Versão: v1 — 2025-03-03*
*Depende de: spec-14 (fluxo professor), spec-15 (fluxo aluno) — definição das telas de destino*
*Escopo: composeApp (Android/iOS)*
