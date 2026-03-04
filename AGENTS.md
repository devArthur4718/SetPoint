# AGENTS.md — SetPoint (Spec Driven Development)

Este arquivo orienta o comportamento do agente de IA ao implementar o projeto. Sempre que for implementar ou revisar código, siga estas regras.

---

## Stack e contexto do projeto

- **Kotlin Multiplatform (KMP)** com Compose
- **Módulos**: `shared` (lógica comum), `composeApp` (UI Android/iOS)
- **Shared**: camadas domain → data; sem UI, sem APIs de uma única plataforma
- **Padrões**: ver `docs/PADROES-KMP.md` e `.cursor/rules/`
- **Offline-first**: o app deve funcionar o melhor possível offline; cache local é obrigatório, leitura prioritária do cache, sincronização com backend quando online. Ver `docs/REQUISITOS-PRODUTO.md`.

---

## Regras gerais

- **Sempre ler os arquivos em `/specs` antes de implementar.**
- **Nunca implementar sem critérios de aceitação** definidos na spec.
- **Nunca pular o passo de tasks:** antes de implementar, gerar ou atualizar `tasks.md` com a decomposição da spec.
- Código simples e legível; evitar overengineering.
- Respeitar arquitetura KMP: domain e data no shared; UI no composeApp.
- Em specs de **data**, **repositórios** ou **sync**: considerar sempre cache local e uso offline (ver `docs/REQUISITOS-PRODUTO.md`).
- Não inventar requisitos que não estejam na spec.

---

## Fluxo obrigatório (Spec Driven)

1. **Ler a spec** do diretório `/specs` referente à tarefa.
2. **Gerar ou atualizar `specs/tasks.md`** — decompor a spec em tarefas concretas e ordenadas no **arquivo canônico** `specs/tasks.md`. Este passo é **obrigatório e não pode ser pulado**: sempre preencher/atualizar `tasks.md` com as tasks da spec em foco antes de qualquer implementação.
3. **Implementar** estritamente com base nas tasks (e na spec).
4. **Criar testes automatizados** que cubram os critérios de aceitação da spec.
5. **Garantir** que todos os critérios de aceitação da spec são atendidos.

**Importante:** implementação só começa após existir spec + tasks. Não ir direto do “implemente a spec” para código sem passar por tasks.

---

## Testes

- Priorizar cobertura dos **critérios de aceitação** da spec.
- Testes devem ser claros e diretos.
- Usar `kotlin.test` e testes compartilhados em `commonTest` quando possível.

---

## Restrições

- **Não inventar** requisitos não descritos na spec.
- **Não alterar** comportamento sem atualizar a spec antes.
- **Não editar** código manualmente para “contornar” a spec; ajustes vêm pela spec (editar spec → depois regerar/ajustar código).

---

## Loop SDD (lembrete)

1. Ajustar a **spec** (não o código diretamente).
2. Revisar a spec.
3. **Gerar/atualizar tasks.md** a partir da spec.
4. Agente gera/atualiza testes e código com base nas tasks e na spec.
5. Escrever novas specs para novas funcionalidades.

**Fonte da verdade:** as especificações em `/specs`. **Ordem de execução:** spec → tasks → implementação → testes.

---

## Workflow Git (obrigatório)

- **Branch base:** o trabalho de implementação parte sempre da branch **main** (atualizada).
- **Uma spec = uma branch:** cada spec deve ser desenvolvida em uma **branch dedicada**. Ex.: para a spec-16, criar e usar a branch `spec/16-design-system` (ou `spec/16-ui-design-system`). O nome deve deixar claro qual spec está em foco.
- **Fluxo:**  
  1. Garantir que está em `main` e atualizado (`git pull origin main`).  
  2. Criar branch a partir de main: `git checkout -b spec/NN-nome-curto`.  
  3. Implementar a spec (tasks + código + testes) **somente nessa branch**.  
  4. Commits na branch da spec, com mensagens que referenciem a spec (ex.: `feat(spec-16): tema Material 3 e estados visuais`).
- **Pull request:** quando a spec estiver **implementada e testada**, **obrigatoriamente** abrir **pull request** da branch da spec para a **main**. Não fazer merge direto na main a partir da branch da spec; a integração é via PR.
- **Resumo:** `main` → branch `spec/NN-...` → implementar e testar → **abrir PR para main**.
