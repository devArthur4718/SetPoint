# feature: UI — Animações e motion

Definir **animações e motion** no SetPoint (composeApp): transições entre telas, animações em listas, micro-interações e feedback animado, para uma experiência moderna e que valorize o produto.

## requisitos

- **Escopo**: módulo **composeApp**; aplicável às telas e fluxos das specs 13–15, sem alterar regras de negócio.
- **Objetivo**: motion com propósito — orientar o usuário, dar feedback e tornar a UI viva, sem atrapalhar ou atrasar o uso.

### Transições de tela

- **Navegação**:
  - Ao trocar de tela (entrada → home; home → lista; lista → detalhe/formulário; voltar), usar **transição animada** em vez de troca instantânea.
  - Sugestão: slide horizontal (entrada da direita ao avançar, saída pela esquerda ao voltar) ou fade + slide; duração curta (200–300 ms) para não atrasar.
  - A transição deve ser consistente em todo o app (mesmo padrão para “avançar” e “voltar”).
- **Implementação**: pode ser via AnimatedContent, composable customizado com animate*AsState, ou integração com o mecanismo de navegação existente (estado de tela atual); detalhes nas tasks.

### Animações em listas

- **Entrada de itens**:
  - Ao exibir uma lista (alunos, treinos, execuções, histórico), os itens podem **entrar com animação** (ex.: fade-in + leve slide, ou stagger — um após o outro com pequeno delay).
  - Duração curta (total da lista < 400–500 ms); não bloquear interação.
- **Reordenação / atualização** (quando aplicável):
  - Se a lista for atualizada (ex.: após criar aluno e voltar), a inserção do novo item pode ser animada (fade-in ou expand).
- **Estado vazio**:
  - A mensagem/ilustração de lista vazia pode aparecer com fade-in suave.

### Micro-interações

- **Botões**:
  - Feedback ao toque: escala leve (scale down) ou ripple já fornecido pelo Material; garantir que botões primários e secundários tenham feedback visível.
- **Cards clicáveis**:
  - Ao toque: indicação clara (ripple ou leve elevação/scale); opcional: animação de “press” (scale 0.98).
- **Ícone de voltar / ações da AppBar**:
  - Feedback tátil/visual ao toque (ripple); opcional: ícone com animação leve ao pressionar.
- **Formulários**:
  - Campos: transição suave ao focar (borda, label); mensagem de erro pode aparecer com fade-in.

### Feedback animado de ações

- **Sucesso**:
  - Snackbar de sucesso (spec-16) pode aparecer com animação (slide from bottom ou fade-in); permanência curta (2–3 s) e desaparecer com animação.
- **Erro**:
  - Mensagem de erro na tela: aparecer com fade-in; snackbar de erro, se usado, com mesma lógica de entrada/saída que sucesso.
- **Loading**:
  - Indicador de carregamento (spec-16): preferir animação contínua (CircularProgressIndicator ou skeleton pulsando) em vez de estático.

### Princípios de motion

- **Duração**: animações curtas (100–300 ms para micro; 200–400 ms para transições de tela e listas).
- **Easing**: curvas suaves (ex.: FastOutSlowInEasing, ou equivalente) para transições; evitar linear em entradas/saídas.
- **Não bloquear**: animações não devem impedir o usuário de tocar ou navegar; evitar delays longos antes de exibir conteúdo.
- **Consistência**: mesmo tipo de transição para o mesmo tipo de navegação em todo o app.

## regras de negócio

- Animações são opcionais em dispositivos/contextos onde o usuário preferir reduzir motion (respeitar configuração do sistema quando disponível: preferência “reduzir movimento”).
- Transições de tela não devem alterar a lógica de navegação (mesmo estado, mesmos callbacks); apenas a apresentação visual.

## critérios de aceitação

- Ao navegar entre telas (avançar e voltar), há transição animada visível (slide, fade ou combinação) com duração entre 200–400 ms.
- Listas (alunos, treinos, atribuídos, histórico) exibem itens com animação de entrada (fade-in e/ou slide, possivelmente stagger).
- Botões e cards clicáveis têm feedback visual ao toque (ripple ou scale/elevação).
- Snackbar (ou equivalente) de sucesso e erro aparece e desaparece com animação.
- Indicador de loading é animado (ex.: CircularProgressIndicator ou skeleton).
- Onde o sistema oferecer, a preferência “reduzir movimento” é respeitada (animações desativadas ou reduzidas).
- Implementação no composeApp; shared não é alterado.

---

*Versão: v1 — 2025-03*
*Depende de: specs 13–15 (telas), spec-16 (design system / snackbar)*
*Escopo: composeApp*
