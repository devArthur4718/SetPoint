# Backlog — Specs existentes e pendentes

Referência única para saber **quais specs existem** e **quais ainda faltam criar**.

---

## Próxima spec pendente

- **Spec 18** — `spec-18-autenticacao-login-v1.md` (Autenticação: login com e-mail e senha).  
  Ordem: criar branch `spec/18-autenticacao-login`, atualizar `tasks.md` com as tasks da spec-18, implementar e testar, abrir PR para main.

---

## Próximos passos (visão geral)

- **Feito:** Specs **01–17** (domínio, dados, features shared 06–12, UI entrada/navegação e telas professor/aluno 13–15, design system 16, animações e motion 17).
- **Agora:** Implementar a **spec-18** (autenticação: login com e-mail e senha).
- **Em seguida:** Specs **19–21** (marca/logo, criar conta e UI criar conta), na ordem que fizer sentido (ex.: 19 → 20 → 21).

---

## Specs existentes (já criadas)

| # | Arquivo | Tema | Implementada? |
|---|---------|------|----------------|
| 01 | `spec-01-dominio-usuario-perfis-v1.md` | Domínio usuário e perfis | Sim |
| 02 | `spec-02-dominio-exercicio-treino-v1.md` | Domínio exercício e treino | Sim |
| 03 | `spec-03-dominio-atribuicao-execucao-v1.md` | Domínio atribuição e execução | Sim |
| 04 | `spec-04-camada-dados-cache-v1.md` | Camada de dados / cache (offline-first) | Sim |
| 05 | `spec-05-persistencia-disco-v1.md` | Persistência em disco (SQLDelight) | Sim |
| 06 | `spec-06-feature-criar-aluno-v1.md` | Feature: criar aluno | Sim |
| 07 | `spec-07-feature-criar-treino-v1.md` | Feature: criar treino (template) | Sim |
| 08 | `spec-08-feature-atribuir-treino-ao-aluno-v1.md` | Feature: atribuir treino ao aluno | Sim |
| 09 | `spec-09-feature-visualizar-historico-aluno-professor-v1.md` | Feature: visualizar histórico do aluno (professor) | Sim |
| 10 | `spec-10-feature-visualizar-treino-atribuido-aluno-v1.md` | Feature: visualizar treino atribuído (aluno) | Sim |
| 11 | `spec-11-feature-executar-treino-aluno-v1.md` | Feature: executar treino (aluno) | Sim |
| 12 | `spec-12-feature-historico-treinos-aluno-v1.md` | Feature: histórico de treinos (aluno) | Sim |
| 13 | `spec-13-ui-entrada-navegacao-v1.md` | UI: entrada no app e navegação | Sim |
| 14 | `spec-14-ui-telas-professor-v1.md` | UI: telas do professor | Sim |
| 15 | `spec-15-ui-telas-aluno-v1.md` | UI: telas do aluno | Sim |
| 16 | `spec-16-ui-design-system-identidade-v1.md` | UI: design system e identidade visual | Sim |
| 17 | `spec-17-ui-animacoes-motion-v1.md` | UI: animações e motion | Sim |
| 18 | `spec-18-autenticacao-login-v1.md` | Autenticação: login com e-mail e senha | Em PR |
| 19 | `spec-19-marca-logo-setpoint-v1.md` | Marca: logo simples e estilizada | Não |
| 20 | `spec-20-feature-criar-conta-v1.md` | Feature: criar conta (autocadastro) | Não |
| 21 | `spec-21-ui-criar-conta-v1.md` | UI: criar conta (telas de cadastro) | Não |

---

## Specs criar conta (20–21)

| # | Arquivo | Conteúdo |
|---|---------|----------|
| 20 | `spec-20-feature-criar-conta-v1.md` | **Use case** (shared): CreateAccountUseCase — email, senha, nome, role (TRAINER/STUDENT); validação; criar User + persistir hash de senha; se STUDENT, criar StudentProfile. Depende de persistência de credencial (mesma base do login, spec-18). |
| 21 | `spec-21-ui-criar-conta-v1.md` | **UI** (composeApp): link “Criar conta” na entrada e nas telas de login; tela de cadastro (e-mail, senha, confirmar senha, nome, papel, opcional displayName); chamada ao use case; sucesso → login ou home. |

Ordem: implementar **spec-20** (use case + persistência de senha) antes da **spec-21** (telas). Tasks em `tasks.md` (blocos spec-20 e spec-21).

---

## Spec de marca / logo (19)

| # | Arquivo | Conteúdo |
|---|---------|----------|
| 19 | `spec-19-marca-logo-setpoint-v1.md` | Logo simples e estilizada para SetPoint: conceito (símbolo limpo, associado a meta/treino), uso na **tela de entrada**, como **ícone do app** (Android e iOS), opcional na app bar. Assets vetoriais e tamanhos por plataforma. |

---

## Spec de autenticação (18)

| # | Arquivo | Conteúdo |
|---|---------|----------|
| 18 | `spec-18-autenticacao-login-v1.md` | Tela de entrada → escolha “Sou professor” / “Sou aluno” → **tela de login** própria para cada um (e-mail + senha). Serviço de autenticação (backend ou local); sessão (userId, role); “Sair” volta à entrada. Substitui o fluxo sem login da spec-13. |

Dependências: pode exigir API de login no backend e/ou extensão de domínio/dados para senha (hash). Tasks em `tasks.md` (bloco spec-18).

---

## Specs de UI moderna (16–17)

As specs **16 e 17** focam em **valorizar o produto** com UI moderna:

| # | Arquivo | Conteúdo |
|---|---------|----------|
| 16 | `spec-16-ui-design-system-identidade-v1.md` | Tema (cores, tipografia, formas), componentes reutilizáveis, estados visuais (loading, vazio, erro, sucesso), identidade SetPoint (marca, tom, acessibilidade). |
| 17 | `spec-17-ui-animacoes-motion-v1.md` | Transições de tela, animações em listas, micro-interações (botões, cards), feedback animado (snackbar), preferência “reduzir movimento”. |

Ordem sugerida: implementar **spec-16** (design system) primeiro; depois **spec-17** (animações). Tasks em `specs/tasks.md` (blocos spec-16 e spec-17).

---

## Specs que faltam criar (backlog)

Estas specs **ainda não existem** como arquivo. Ao criar, usar o próximo número (19, 20, …) e salvar em `specs/spec-<nn>-<nome-curto>-v1.md`.

| # | Tema sugerido | Descrição breve |
|---|----------------|-----------------|
| — | (Opcional) Sincronização com backend | Envio/recebimento de dados quando online. |
| — | (Opcional) API de login / armazenamento de senha | Backend ou extensão de dados para validar e-mail e senha (complementa spec-18). |

---

## Specs de apresentação / UI (13–15: fluxos; 16–17: design e motion)

As specs **13, 14 e 15** definem a camada de apresentação no `composeApp` (telas e fluxos):

| # | Arquivo | Conteúdo |
|---|---------|----------|
| 13 | `spec-13-ui-entrada-navegacao-v1.md` | Tela inicial; seleção professor/aluno (MVP); navegação por role para fluxo professor ou aluno. |
| 14 | `spec-14-ui-telas-professor-v1.md` | Telas: lista alunos, criar aluno, lista treinos, criar treino, atribuir treino, histórico de um aluno. |
| 15 | `spec-15-ui-telas-aluno-v1.md` | Telas: treinos atribuídos, executar treino (registrar séries), meu histórico. |

As specs **16 e 17** definem **UI moderna** (design system, identidade, animações); ver seção “Specs de UI moderna (16–17)” acima. Implementação: tasks em `tasks.md`; composeApp (Compose, tema, motion).

---

## Onde encontrar

- **Specs já criadas:** pasta `specs/`, arquivos `spec-01-...md` a `spec-21-...md`.
- **Specs que faltam:** não há arquivo ainda; use esta lista como backlog e crie o documento quando for redigir a spec (seguindo o formato das specs 01–06 e o fluxo em `README.md`).

*Última atualização: 2025-03-04*
