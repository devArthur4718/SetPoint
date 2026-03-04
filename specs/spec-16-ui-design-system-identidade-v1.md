# feature: UI — Design system e identidade visual

Definir o **design system** e a **identidade visual** do SetPoint no composeApp: tema (cores, tipografia, formas), componentes reutilizáveis, estados visuais (vazio, loading, erro) e princípios que valorizem o produto como app de **treino e acompanhamento** (professor ↔ aluno).

## requisitos

- **Escopo**: módulo **composeApp**; aplicável a todas as telas já definidas nas specs 13–15, sem alterar comportamento de negócio.
- **Objetivo**: UI moderna, consistente e que transmita **confiança, clareza e foco em treino** — não genérica.

### Tema (Material 3)

- **Cores**:
  - Paleta alinhada ao Material 3 (ColorScheme): primary, secondary, tertiary, surface, error, etc.
  - Definir **tema claro** (obrigatório) e **tema escuro** (obrigatório) que reflitam a identidade do SetPoint (ex.: tons que remetam a energia, foco, sem ser agressivos).
  - Uso consistente: primary para ações principais; surface variants para cards e agrupamentos; error apenas para erros.
- **Tipografia**:
  - Escala tipográfica definida (Display, Headline, Title, Body, Label) com tamanhos e pesos adequados a leitura e hierarquia.
  - Títulos de tela e listas devem ter hierarquia clara; corpo de texto legível em todos os contextos.
- **Formas**:
  - Border radius consistente para cards, botões e chips (ex.: medium para cards, small para chips).
  - Elevação/sombra leve onde fizer sentido (cards, FABs), sem exagero.

### Componentes reutilizáveis

- **AppBar** (já existente): manter padrão TopAppBar com título, navegação (voltar) e ações; garantir que cores e tipografia usem o tema.
- **Cards**:
  - Cards de lista (aluno, treino, execução): mesmo estilo (padding, radius, cor de superfície); destaque sutil ao toque/estado.
  - Cards de ação (entrada “Professor”/“Aluno”, home): clicáveis, com feedback visual claro.
- **Botões**:
  - Primário (CTA principal), secundário/outlined (ações secundárias), texto (ex.: “Sair”) — uso consistente em todo o app.
- **Campos de formulário** (OutlinedTextField):
  - Estilo único (borda, label, placeholder) em telas Criar aluno, Criar treino, etc.
- **Chips / tags** (opcional): para estados (ex.: “Concluído”, “Pendente”) ou filtros, quando fizer sentido.

### Estados visuais

- **Loading**:
  - Indicador de carregamento consistente (ex.: CircularProgressIndicator ou skeleton) em listas e formulários; não apenas texto “Carregando...”.
- **Vazio**:
  - Telas e listas vazias: ilustração ou ícone + mensagem curta + CTA quando aplicável (ex.: “Nenhum aluno” + “Criar aluno”). Layout centralizado ou alinhado, não apenas texto solto.
- **Erro**:
  - Mensagens de erro visíveis (cor, talvez ícone), próximas ao campo ou no topo do conteúdo; opcional: snackbar para erros de ação.
- **Sucesso** (feedback pós-ação):
  - Após criar aluno, criar treino, atribuir treino, concluir execução: feedback claro (snackbar ou toast) com mensagem de sucesso antes de navegar ou junto com a navegação.

### Identidade do produto

- **Marca SetPoint**: nome e possível logotipo/ícone na tela de entrada e, se aplicável, na app bar (opcional).
- **Tom visual**: limpo, objetivo, adequado a uso em academia ou em casa; evitar poluição visual; priorizar conteúdo (listas, formulários) sobre decoração.
- **Acessibilidade**: contraste mínimo (WCAG 2.1 AA onde possível); áreas de toque adequadas; labels para ícones.

## regras de negócio

- Todas as telas do app devem usar o mesmo tema (light/dark) e os mesmos componentes base; não misturar estilos ad hoc.
- Estados de loading, vazio e erro devem estar presentes em todas as telas que carregam dados ou submetem ações.
- Feedback de sucesso é obrigatório nas ações de criação/atribuição/conclusão definidas nas specs 06–12.

## critérios de aceitação

- Existe um tema (Theme / ColorScheme + Typography + Shapes) aplicado em todo o app (light e dark); cores e tipografia são consistentes.
- Componentes reutilizáveis (AppBar, cards, botões, campos) estão centralizados ou referenciados de forma consistente e usados nas telas 13–15.
- Todas as listas/telas que podem estar vazias exibem estado vazio com mensagem e CTA quando aplicável (não apenas texto “Nenhum…”).
- Todas as telas que carregam dados exibem indicador de loading (não apenas texto “Carregando...”).
- Erros de use case ou validação são exibidos de forma visível (cor/ícone + mensagem).
- Após criar aluno, criar treino, atribuir treino ou registrar execução com sucesso, o usuário recebe feedback de sucesso (snackbar ou equivalente).
- Tela de entrada e homes refletem a identidade do produto (SetPoint, clareza, foco em treino).
- Implementação no composeApp; shared não é alterado.

---

*Versão: v1 — 2025-03*
*Depende de: specs 13–15 (telas existentes)*
*Escopo: composeApp*
