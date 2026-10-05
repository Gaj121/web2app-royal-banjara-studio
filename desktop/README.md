# Desktop App (Windows + Mac)

Ye Electron app tumhari website ko asli install-able desktop app banata hai.

## Apne computer par chalao

```bash
cd desktop
npm install
npm start
```

## Installer banana

```bash
npm run dist
```

- Windows par `dist/` me `*-Setup.exe` banta hai — double-click se install hota hai.
- Mac par `dist/` me `.dmg` banta hai — kholo aur app ko Applications me drag karo.
- Linux par `dist/` me `.deb` banta hai — `sudo dpkg -i file.deb` se install karo.

## Automatic build (recommended)

Ye repo GitHub par push karo — `.github/workflows/build-desktop.yml` khud build karta hai:
- **Windows**: `windows-v1` release me `*-Setup.exe`
- **Mac**: `macos-v1` release me universal `.dmg` (Intel + M1/M2/M3 dono)
- **iPhone**: `ios-v1` release me unsigned `.ipa` — Sideloadly se iPhone par install karo
- **Linux**: `linux-v1` release me `.deb` — Ubuntu/Debian me install karo

Icon `desktop/icon.png` hai — badalna ho to 512x512 ya usse bada PNG rakho.
