# Specs — SetPoint (Spec Driven Development)

Este diretório contém as **especificações** do projeto. São a **fonte da verdade** para implementação e validação.

## Requisitos de produto (cross-cutting)

Antes de escrever specs de **camada de dados**, **repositórios** ou **sincronização**, consulte:

- **`docs/REQUISITOS-PRODUTO.md`** — o app é **offline-first**; cache local obrigatório, funcionar o melhor possível sem rede, sincronização quando online.

As specs de domínio (01–03) não precisam citar offline; as de data e sync devem desenhar para cache e sync.

## Estrutura de uma spec

- **Título / feature** — nome claro da funcionalidade ou domínio.
- **Requisitos** — o que o sistema deve fazer; restrições técnicas.
- **Regras de negócio** — validações, invariantes.
- **Critérios de aceitação** — condições testáveis que definem “pronto”.

Sem critérios de aceitação a spec não deve ser implementada (conforme `AGENTS.md`).

## Convenção de nomes

- `spec-<nn>-<nome-curto>-v<k>.md`
- Ex.: `spec-01-dominio-usuario-perfis-v1.md`, `spec-02-dominio-exercicio-treino-v1.md`, `spec-03-dominio-atribuicao-execucao-v1.md`, `spec-04-camada-dados-cache-v1.md`, `spec-05-persistencia-disco-v1.md`, `spec-06-feature-criar-aluno-v1.md`

## Specs que faltam criar (backlog)

Lista das specs **ainda não redigidas** (criar treino, atribuir treino, visualizar histórico, executar treino, etc.): **`specs/BACKLOG-SPECS.md`**.

## Specs e tasks por tema

| Spec | Tema | Tasks |
|------|------|--------|
| 01 | Domínio usuário e perfis | `tasks.md` (spec-01) |
| 02 | Domínio exercício e treino | `tasks.md` (spec-02) |
| 03 | Domínio atribuição e execução | `tasks.md` (spec-03) |
| 04 | Camada de dados / cache (offline-first) | `tasks.md` (spec-04) |
| 05 | Persistência em disco (SQLDelight) | `tasks-05-persistencia-disco.md` |
| 06 | Feature: criar aluno | `tasks-06-feature-criar-aluno.md` |

Ao implementar spec-05 ou 06, usar o arquivo de tasks correspondente (ou gerar/atualizar `tasks.md` com o conteúdo da spec em foco).

## Fluxo

1. Escrever ou editar a spec.
2. **Gerar ou atualizar `tasks.md`** — o arquivo **canônico** é sempre `specs/tasks.md`. Preencher este arquivo com as tasks da spec em foco. **Não pular esta etapa:** implementação só começa depois de existir spec + tasks em `tasks.md`.
3. Pedir implementação ao agente com referência à spec.
4. Validar critérios de aceitação.

Para mudanças de comportamento: alterar a spec primeiro, depois atualizar tasks.md se necessário, depois pedir nova implementação.
