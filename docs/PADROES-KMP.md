# Padrões técnicos e de projeto — SetPoint (KMP)

Este documento resume os padrões definidos para o projeto Kotlin Multiplatform. As regras detalhadas para o Cursor/IA estão em `.cursor/rules/` (arquivos `.mdc`).

## 1. Formato de código

- **Padrão**: [Kotlin Official Code Style](https://kotlinlang.org/docs/coding-conventions.html).
- **Ferramentas**: ktlint (linter/formatter), `.editorconfig` para max line length, indent, trailing comma.
- **Nomenclatura**: pacotes em lowercase; classes em PascalCase; funções/propriedades em camelCase.
- **Shared**: considerar `explicitApi()` no compiler para APIs públicas.

Ver: `.cursor/rules/kmp-code-format.mdc`

## 2. Arquitetura

- **Shared (commonMain)**: lógica de negócio, domínio, dados, rede, cache, estado — sem UI, sem APIs de uma única plataforma.
- **Apps (composeApp)**: UI e integração por plataforma (Android, iOS, JVM, JS, Wasm).
- **Source sets**: commonMain → todos os targets; platform source sets (androidMain, iosMain, etc.) só quando necessário; dependência sempre common ← platform.
- **Camadas no shared**: domain → data; opcionalmente di/presentation; sem referências a Compose ou SDKs nativos.

Ver: `.cursor/rules/kmp-architecture.mdc`

## 3. O que DEVE ser evitado

- Usar APIs JVM/iOS só em commonMain (usar expect/actual ou libs KMP).
- `Throwable.printStackTrace()` em código Kotlin/Native (risco de travar UI).
- Shared depender de módulos de app ou de plataforma.
- Colocar UI no shared; escape sequences Android em strings de recursos no iOS.
- APIs instáveis sem `@RequiresOptIn` e sem documentação.

Ver: `.cursor/rules/kmp-avoid.mdc`

## 4. Estratégia de modularização

- **:shared**: código compartilhado (múltiplos targets).
- **:composeApp**: app Compose; depende de `:shared`.
- **:server**: backend; independente do shared na compilação.
- Regra: apps e server dependem de shared; shared não depende de app nem server.
- Crescimento: extrair :core (domínio) e módulos por feature; usar source sets intermediários (ex.: appleMain) para compartilhar só entre plataformas similares.

Ver: `.cursor/rules/kmp-modularization.mdc`

---

*Referências: documentação oficial Kotlin Multiplatform, KMP production guides, ktlint/detekt, práticas de modularização KMP.*
