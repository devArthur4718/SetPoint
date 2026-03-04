# SetPoint (KMP)

Projeto Kotlin Multiplatform com Compose para personal trainers e alunos. O app é **offline-first**: funciona o melhor possível sem rede, com cache local e sincronização quando online (ver `docs/REQUISITOS-PRODUTO.md`).

## Rodar no Android

1. Inicie um emulador (Android Studio → Device Manager) ou conecte um dispositivo.
2. Instale e rode o app:

```bash
./gradlew :composeApp:installDebug
```

3. (Opcional) Abrir o app no dispositivo/emulador:

```bash
adb shell am start -n com.devarthur.setpoint/com.devarthur.setpoint.MainActivity
```

## Rodar no iOS

1. Abra a pasta `iosApp` no Xcode.
2. Selecione um simulador (ex.: iPhone 16 Pro) e pressione **⌘R** para build e execução.

Ou pela linha de comando (macOS):

```bash
open -a Simulator
cd iosApp
xcodebuild -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 16 Pro,OS=18.6' -configuration Debug build
# Instalar e abrir no simulador (caminho do .app fica em DerivedData)
APP=$(find ~/Library/Developer/Xcode/DerivedData -name "SetPoint.app" -path "*Debug-iphonesimulator*" | head -1)
xcrun simctl install booted "$APP"
xcrun simctl launch booted com.devarthur.setpoint.SetPoint
```

## Pré-requisitos

- **Android:** `local.properties` com `sdk.dir=/caminho/para/Android/sdk` (ex.: no macOS: `~/Library/Android/sdk`).
- **iOS:** Xcode e um simulador configurado.
