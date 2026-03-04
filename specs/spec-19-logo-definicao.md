# Definição da logo SetPoint (spec-19)

Documento de referência para o **símbolo** da marca SetPoint: conceito, forma, cores e uso. O nome “SetPoint” permanece como texto da UI, fora do símbolo.

---

## 1. Conceito

- **Ideia:** “Set point” = ponto que você define — meta, alvo, referência. Associado a treino, precisão e progresso.
- **Símbolo:** Alvo minimalista — **anel externo + ponto central**. Transmite foco no objetivo e clareza, sem ser literal.
- **Estilo:** Geométrico, limpo, reconhecível em tamanhos pequenos (48 dp) e grandes (80–120 dp). Sem texto dentro do símbolo.

---

## 2. Forma geométrica

- **Elementos:**
  1. **Anel (círculo externo):** um único círculo em traço (stroke), espessura ~10% do raio externo. Não preenchido.
  2. **Ponto central:** círculo preenchido no centro, diâmetro ~25–30% do diâmetro total do símbolo.

- **Proporções (viewport normalizado 0 0 24 24):**
  - Centro: (12, 12)
  - Anel: centro (12, 12), raio externo 10, espessura do traço 2 → raio interno efetivo 9
  - Ponto: centro (12, 12), raio 3 (preenchido)

- **Safe zone (ícone do app):** manter o símbolo dentro de ~80% do viewport (centrado) para respeitar recortes em Android (adaptive icon) e iOS.

---

## 3. Cores

| Uso | Cor | Observação |
|-----|-----|------------|
| **Tema claro (UI)** | Primary do tema (verde #5C7C00) | Símbolo na tela de entrada e na app bar |
| **Tema escuro (UI)** | Primary do tema (lime #C6FF00) | Mesmo símbolo, cor do MaterialTheme.colorScheme.primary |
| **Ícone do app (foreground)** | Branco #FFFFFF ou primary | Sobre fundo colorido (adaptive icon); contraste garantido |
| **Variante alternativa** | Primary sobre fundo claro/escuro | Conforme acessibilidade (contraste mínimo 4.5:1) |

A cor deve ser sempre obtida de `MaterialTheme.colorScheme.primary` na UI Compose, ou do token equivalente ao exportar para assets (Android drawable pode usar `?attr/colorPrimary` ou cor fixa no XML).

---

## 4. Onde a logo é usada

1. **Tela de entrada (obrigatório)** — Símbolo acima do texto “SetPoint”, altura ~80–120 dp; hierarquia: logo + nome + subtítulo + opções.
2. **Ícone do app Android** — Foreground do adaptive icon com o símbolo (centrado, dentro da safe zone).
3. **Ícone do app iOS** — App Icon nos tamanhos do catálogo (20–1024 pt), mesmo símbolo.
4. **App bar (opcional)** — Pequena versão (~24–32 dp) ao lado do título nas homes.

---

## 5. Restrições

- Nenhum texto dentro do símbolo.
- Legível e reconhecível em 48×48 dp (launcher).
- Respeitar safe zone nos ícones (Android: 66% central; iOS: sem transparência onde exigido).
- Formato vetorial preferido para todos os usos (escala sem perda).

---

## 6. Arquivos de referência

- **Vetor Android (drawable):** `composeApp/src/androidMain/res/drawable/ic_setpoint_logo.xml` — símbolo em path para uso no adaptive icon e na UI Android.
- **Compose (commonMain):** `composeApp/.../ui/components/SetPointLogo.kt` — componente `SetPointLogo(size, color)` que desenha o mesmo símbolo com `MaterialTheme.colorScheme.primary`; usado na tela de entrada e reutilizável na app bar.

---

*Versão: 1 — alinhado à spec-19-marca-logo-setpoint-v1.md e ao design system (spec-16).*
