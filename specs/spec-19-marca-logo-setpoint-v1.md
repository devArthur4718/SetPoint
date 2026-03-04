# feature: Marca — Logo simples e estilizada (SetPoint)

Definir e implementar uma **logo simples e estilizada** para o SetPoint, usada na tela de entrada, opcionalmente na app bar, e como ícone do app (launcher) em Android e iOS. A logo deve reforçar a identidade do produto (treino, meta, precisão) sem poluir a interface.

## requisitos

- **Escopo**: identidade visual; impacto em **composeApp** (assets, tela de entrada, ícone do app) e em **recursos multiplataforma** (Android drawable/mipmap, iOS Assets.xcassets, ou recurso compartilhado quando possível).
- **Objetivo**: o app passa a ter uma **logo reconhecível** — ícone/símbolo estilizado que pode ser exibido junto ao nome “SetPoint” ou sozinho em contextos pequenos (app icon).

### Conceito da logo

- **Estilo**: simples, estilizado, fácil de reconhecer em tamanhos pequenos (ex.: 48 dp) e maiores (ex.: 96–120 dp na entrada).
- **Associação ao produto**: o símbolo pode evocar meta, alvo, ponto de referência, progresso ou treino (ex.: alvo, seta, marca de “ponto”, forma geométrica minimalista). Não é obrigatório ser literal; deve ser limpo e memorável.
- **Formas**: preferência por traços ou formas geométricas claras (círculo, ponto, seta, arco), evitando detalhes que se percam no ícone do app.
- **Cores**: alinhadas ao tema do app (spec-16); a logo deve funcionar em tema claro e escuro (ou ter variante para fundo escuro). Uso da cor primary (ou equivalente) para o símbolo quando exibido na UI.

### Onde a logo é usada

1. **Tela de entrada (obrigatório)**  
   - Exibir a **logo** (símbolo) acima ou ao lado do texto “SetPoint”, com tamanho destacado (ex.: 80–120 dp de altura).  
   - Manter hierarquia clara: logo + nome; abaixo, o subtítulo ou as opções de entrada.

2. **Ícone do app / launcher (obrigatório)**  
   - **Android**: usar a logo (ou versão adaptada) como ícone do app (adaptive icon: foreground com o símbolo; background com cor do tema).  
   - **iOS**: usar a logo no App Icon (Assets.xcassets), nos tamanhos exigidos pelo Apple (ex.: 1024×1024 para App Store, e os tamanhos para dispositivo).

3. **App bar (opcional)**  
   - Em telas principais (ex.: home professor, home aluno), exibir pequena versão da logo (ícone) ao lado do título “Professor” / “Aluno”, ou apenas o título; definição nas tasks ou no design system (spec-16).

### Formatos e assets

- **Formato preferido**: vetorial (SVG ou XML drawable no Android; PDF ou vetor no iOS; ou representação em Compose como path/drawable) para escalar bem em todos os tamanhos.
- **Tamanhos mínimos a cobrir**:
  - Android: mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi para launcher (ou adaptive icon com drawable vetorial); ícone para tela de entrada pode ser um único asset escalável.
  - iOS: App Icon nos tamanhos do catálogo (ex.: 20, 29, 40, 60, 76, 83.5, 1024 pt).
- **Versões**: símbolo em cor (primary) para tema claro; opcionalmente versão em branco ou invertida para tema escuro ou para foreground do adaptive icon sobre fundo colorido.

### Restrições

- Logo não deve depender de texto dentro do símbolo (o nome “SetPoint” fica fora, como texto da UI).
- Deve ser legível e reconhecível em tamanho pequeno (ex.: 48×48 dp no launcher).
- Diretrizes de cada plataforma: Android (adaptive icon, safe zone); iOS (App Icon, sem transparência em alguns contextos) — respeitar nas exportações.

## regras de negócio

- A logo é um ativo de marca do SetPoint; uso consistente na entrada e no ícone do app.
- Cores e estilo devem estar alinhados ao design system (spec-16) quando este existir.

## critérios de aceitação

- Existe uma **logo (símbolo) simples e estilizada** definida e entregue como asset vetorial ou em tamanhos necessários.
- A **tela de entrada** exibe a logo junto ao nome “SetPoint” (logo com tamanho destacado, ex.: 80–120 dp).
- O **ícone do app no Android** usa a logo (adaptive icon com foreground baseado na logo).
- O **ícone do app no iOS** usa a logo (App Icon nos tamanhos exigidos).
- A logo funciona em tema claro; existe variante ou cor adequada para tema escuro e para ícone do app.
- Implementação no composeApp (integração na entrada e, se aplicável, na app bar); assets em pastas de recursos Android e iOS (ou recurso comum quando suportado).

---

*Versão: v1*
*Depende de: spec-13 (tela de entrada), spec-16 (tema/cores) — opcional para alinhar cores*
*Escopo: composeApp (assets + UI)*
