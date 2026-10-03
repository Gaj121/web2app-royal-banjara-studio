# Royal Banjara Studio — Android + iPhone App Project

Apni website **https://rbstudiosmusic.kliv.site/** ka complete, build-ready app project.
Is ZIP ko extract karo aur **asli APK** neeche diye tarike se bana lo (2-4 minute).

## 📦 Is ZIP me kya hai

| File / Folder | Kya hai |
|---|---|
| `app/` | Poora Android app code (WebView + menu + intro + welcome + hide engine) |
| `app/src/main/res/mipmap-*/ic_launcher.png` | Tumhara app icon — saare sizes me |
| `.github/workflows/build-apk.yml` | GitHub par FREE automatic builder — **APK + Play Store .aab dono** |
| `.github/workflows/build-aab.yml` | Alag .aab builder (Actions tab se manually bhi chala sakte ho) |
| `play-store/` | Play Store upload kit — listing text, privacy policy, icon + feature graphic |
| `desktop/` | Windows + Mac ka asli desktop app (Setup.exe + .dmg) |
| `.github/workflows/build-desktop.yml` | GitHub par FREE Windows/Mac/iPhone builder |
| `ios/` | iPhone + Mac ka Xcode project (asli iOS app) |
| `pwa/` | Website ko install-able web app (PWA) banane ka kit |
| `BUILD-GUIDE.md` | Poori Hindi guide — APK, iPhone, Mac, Play Store |

## ✅ Built-in features

- Poora website app ke andar khulta hai — fast WebView (JavaScript, cookies, downloads sab support)
- File upload kaam karta hai — site par photo/PDF/audio choose karke upload kar sakte ho
- **Welcome slider** — app ke upar se slide hoke “Welcome to Royal Banjara Studio Music Distribution Company” ke saath stylish swagat screen (Skip / Get Started ke saath)
- Stylish floating neeche menu bar: Home, Login , Contact, Support  — gradient pill design, scroll karo to apne aap chhup jaata hai
- Website ka footer automatic hide
- “Created with Kliv / Made with Wix” jaisi builder-branding automatic + permanent hide ( MutationObserver se late-load par bhi)
- Tumhari “kya chhupana hai” list permanent hide: "Created with Kliv"
- Neeche khinch kar refresh (pull-to-refresh)
- Internet band hone par friendly “No Internet” page
- Bahar ke links phone ke browser me khulte hain, user app me nahi fas-ta

## 🔧 APK kaise banao (sabse aasan — FREE)

1. [github.com/new](https://github.com/new) par jao → naya **Public** repository banao
2. **Add file → Upload files** se is ZIP ke saare files+folders upload karo → Commit
3. Repo ke **Actions** tab kholo → **Build APK** apne aap chalega (2-4 min)
4. Banne ke baad repo ke **Releases** (right side) → latest **App APK build** → **app-debug.apk** download
   — ye APK phone me seedha install hota hai, browser se bhi download kar sakte ho
5. Ya us run ke andar **app-debug-apk** artifact download karo

> ☁️ Bonus: AppBanao builder me **“APK Cloud Build”** button hai — files apne aap GitHub par jate hain aur direct APK download milta hai (GitHub token setup ke baad).

Poori step-by-step guide (photos ke saath jo kahani): **BUILD-GUIDE.md** kholo.

## 🚀 Play Store par upload — file ready hai

1. Repo ke **Actions** me **Build APK** chalao — wahi run **APK + Play Store .aab dono** banata hai. **Releases** me `app-v…` tag ke andar **app-debug.apk** aur **app-release.aab** dono milengi. .aab wali file Play Store par upload hoti hai.
2. [play.google.com/console](https://play.google.com/console) par jao → one-time $25 (~₹200) developer account banao → **Create app**
3. **Production → Create new release** → `app-release.aab` upload karo
4. `play-store/STORE-LISTING.md` se naam/description copy-paste karo; `play-store/icon-512.png` aur `play-store/feature-graphic-1024x500.png` graphics ke liye use karo
5. Privacy policy: `play-store/PRIVACY-POLICY.md` ko apni website par daal kar uska URL Console me do

> ⚠️ Signature ka note: jab tak GitHub Secrets me `KEYSTORE_BASE64` set nahi hota, har build naya signing key banata hai — pehli upload ke **baad** secrets set kar lena taaki updates same key se jaayein. Pura tarika `play-store/README.md` me.

## 💻 Android Studio se (full control)

1. [Android Studio](https://developer.android.com/studio) free download karke install karo
2. **Open** se is folder kholo — Gradle sync apne aap hoga
3. **Build → Build Bundle(s)/APK(s) → Build APK(s)**
4. APK yahan milega: `app/build/outputs/apk/debug/app-debug.apk`

## 🍎 iPhone / Mac app

`ios/` folder me **Xcode project** ready hai — Mac par Xcode se kholo, apna Apple ID lagao,
iPhone connect karke Run dabao. Mac par bhi chalta hai (“My Mac” destination).
Details: `ios/README.md`

## 💡 Notes

- Debug APK direct install ke liye bilkul theek hai; **Play Store ke liye .aab** (upar dekho) hi bhejna.
- Branding-hide engine `MainActivity.kt` ke `HIDE_JS` me hai — badalna ho to wahan edit karke dobara build karo.
- App ka naam/package badalna ho to `app/build.gradle` me `applicationId` aur `res/values/strings.xml` me `app_name` edit karo.

— Banaya gaya AppBanao se ❤️
