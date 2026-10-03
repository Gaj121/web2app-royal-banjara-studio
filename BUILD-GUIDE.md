# 📱 Poori Build Guide — APK, iPhone, Mac

## ✅ Android APK — asli installable file

### Tarika 1 — GitHub se FREE (sabse aasan, ~5 minute, kuch install nahi karna)

1. Phone ya computer se [github.com/new](https://github.com/new) kholo → **Public** repo banao (naam kuch bhi, jaise `meri-app`)
2. **Add file → Upload files** par jao → is ZIP ko extract karke saari files aur folders drag-drop karo → **Commit changes**
3. Repo ke **Actions** tab kholo → “Build APK” workflow apne aap chalega (2-4 minute)
4. Hara tick aane par repo ke right side **Releases** me **App APK build** milega → usme **app-debug.apk** download karo
5. Phone me APK kholo → “Install unknown apps / Unknown sources” allow karo → **Install** — bas, app ready!

> Phone se hi sab kuch ho sakta hai — GitHub ka website mobile browser me poora chalta hai.

### Tarika 2 — Android Studio se (full control + Play Store signing)

1. [Android Studio](https://developer.android.com/studio) install karo (free)
2. **Open** se is project ka folder kholo → Gradle sync hone do
3. **Build → Build Bundle(s)/APK(s) → Build APK(s)**
4. APK: `app/build/outputs/apk/debug/app-debug.apk`
5. Play Store ke liye: **Build → Generate Signed Bundle/APK** → apna keystore banao → [play.google.com/console](https://play.google.com/console) par upload

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

## ❓ APK browser me kyun nahi banta?

APK banane ke liye Google ka Android build tool chahiye jo website me nahi chal sakta.
Isliye ye project GitHub ke **free cloud builder** ke saath aata hai — wahi asli APK banata hai,
aur tumhe sirf files upload karni hoti hain. Jo “instant APK” claim karne wali sites hain,
wo bhi yahi karte hain — ya low-quality app deti hain.
