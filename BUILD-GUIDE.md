# 📱 Poori Build Guide — APK, iPhone, Mac, Play Store

## ✅ Android APK — asli installable file

### Tarika 1 — GitHub se FREE (sabse aasan, ~5 minute, kuch install nahi karna)

1. Phone ya computer se [github.com/new](https://github.com/new) kholo → **Public** repo banao (naam kuch bhi, jaise `meri-app`)
2. **Add file → Upload files** par jao → is ZIP ko extract karke saari files aur folders drag-drop karo → **Commit changes**
3. Repo ke **Actions** tab kholo → “Build APK” workflow apne aap chalega (2-4 minute)
4. Hara tick aane par repo ke right side **Releases** me **App APK build** milega → usme **app-debug.apk** download karo
5. Phone me APK kholo → “Install unknown apps / Unknown sources” allow karo → **Install** — bas, app ready!

> Phone se hi sab kuch ho sakta hai — GitHub ka website mobile browser me poora chalta hai.

### Tarika 2 — Android Studio se (full control)

1. [Android Studio](https://developer.android.com/studio) install karo (free)
2. **Open** se is project ka folder kholo → Gradle sync hone do
3. **Build → Build Bundle(s)/APK(s) → Build APK(s)**
4. APK: `app/build/outputs/apk/debug/app-debug.apk`
5. Play Store ke liye: niche wala .aab section dekho

## 🛒 App ke andar print / download kaise hota hai

- Website se koi file (PDF, photo, song, zip) download karo — app use **phone ke Downloads folder** me save karta hai aur notification deta hai
- Photo/video download hone par wo **Gallery me bhi** dikhti hai — file manager (Downloads) aur Gallery dono jagah milti hai
- ⋮ gol button dabao → **Mere Downloads** — app ke andar hi download files ki list: tap karke kholo, Share bhejo, dabaye rakho = delete
- Wahi menu ek stylish **Quick Tools sheet** kholta hai — **⚡ gradient banner** ke saath. Sabse upar **Padhai ke Tools** (Page me dhoondo, Padh ke sunao, Hindi me padho, Text A+/A−, Bookmark, **Page save karo — offline**), phir **Screen ke Tools** (Night mode, Screenshot, Full screen, Desktop view, Theme badlo, Top par jao, Ghumao, **Torch jalao, Battery kitni bachi, Volume se scroll, Sound profile**), **Page ke Tools** (**Peeche jao, Aage jao**, Refresh, History), **Files aur Print** aur **Madad** (WiFi, **Bluetooth settings**) — sab icon-cards me

## 🚀 Play Store upload (.aab) — automatic

1. Repo me push karte hi **Build APK** workflow APK ke saath **Play Store .aab bhi** bana deta hai
2. **Releases** me `app-v…` tag → **app-release.aab** download karo (purane repos me `playstore-v…` tag)
3. [play.google.com/console](https://play.google.com/console) → $25 account → Create app → Production → Create new release → .aab upload
4. `play-store/STORE-LISTING.md` ka text listing me paste karo, `play-store/` wali images graphics me lagao
5. Pehli release ke baad repo ke **Settings → Secrets** me apna keystore save karo (`play-store/README.md` me step-by-step) — tab har update same signature se banega

## 🍎 iPhone (ipa) aur Mac

Apple ka rule: iPhone par har app Apple-signed hota hai — isliye direct “.ipa download” kisi builder se possible nahi.
Is ZIP ka `ios/` folder **asli Xcode project** hai — apne iPhone par 10 minute me khud chalao:

1. Kisi Mac par **Xcode** install karo (Mac App Store se, free)
2. `ios/WebApp.xcodeproj` kholo
3. Target **WebApp** → **Signing & Capabilities** → apna Apple ID (team) select karo — free account chalega
4. iPhone USB se connect karke upar select karo → **Run (▶)**
5. iPhone par: Settings → VPN & Device Management → apna Apple ID **Trust** karo

**Mac par:** Xcode me destination “My Mac (Designed for iPad)” / “My Mac (Catalyst)” choose karke Run.

**App Store:** [developer.apple.com](https://developer.apple.com) par $99/saal account → Xcode me **Product → Archive → Distribute App**.

## 🖥️ Windows / Mac / iPhone ke liye automatic builds

Repo me `.github/workflows/build-desktop.yml` bhi hai — push hone par GitHub khud sab banata hai:
- **Windows**: `windows-v1` release me `*-Setup.exe` — double-click se install
- **Windows .msix**: `windows-msix-v1` release me Store-style `.msix`
- **Mac**: `macos-v1` release me universal `.dmg` (Intel + M1/M2/M3) — kholo, app ko Applications me drag karo
- **iPhone**: `ios-v1` release me `.ipa` — [Sideloadly](https://sideloadly.net) se apne Apple ID par install (free, 7 din)
- **Linux**: `linux-v1` release me `.deb` — Ubuntu/Debian me `sudo dpkg -i` se install

Repo ke **Releases** tab me jaake seedha download karo.

## ❓ APK browser me kyun nahi banta?

APK banane ke liye Google ka Android build tool chahiye jo website me nahi chal sakta.
Isliye ye project GitHub ke **free cloud builder** ke saath aata hai — wahi asli APK banata hai,
aur tumhe sirf files upload karni hoti hain. Jo “instant APK” claim karne wali sites hain,
wo bhi yahi karte hain — ya low-quality app deti hain.
