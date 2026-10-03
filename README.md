# Royal Banjara Studio — Android + iPhone App Project

Apni website **https://rbstudiosmusic.kliv.site/** ka complete, build-ready app project.
Is ZIP ko extract karo aur **asli APK** neeche diye tarike se bana lo (2-4 minute).

## 📦 Is ZIP me kya hai

| File / Folder | Kya hai |
|---|---|
| `app/` | Poora Android app code (WebView + menu + branding hide engine) |
| `app/src/main/res/mipmap-*/ic_launcher.png` | Tumhara app icon — saare sizes me |
| `.github/workflows/build-apk.yml` | GitHub par FREE automatic APK builder |
| `ios/` | iPhone + Mac ka Xcode project (asli iOS app) |
| `pwa/` | Website ko install-able web app (PWA) banane ka kit |
| `BUILD-GUIDE.md` | Poori Hindi guide — APK, iPhone, Mac, Play Store |

## ✅ Built-in features

- Poora website app ke andar khulta hai — fast WebView (JavaScript, cookies, downloads sab support)
- Neeche icon menu: Home, Products, Contact — tap par seedha us page par
- Website ka footer automatic hide
- “Created with Kliv / Made with Wix” jaisi builder-branding automatic + permanent hide ( MutationObserver se late-load par bhi)
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

## 💻 Android Studio se (full control)

1. [Android Studio](https://developer.android.com/studio) free download karke install karo
2. **Open** se is folder kholo — Gradle sync apne aap hoga
3. **Build → Build Bundle(s)/APK(s) → Build APK(s)**
4. APK yahan milega: `app/build/outputs/apk/debug/app-debug.apk`

## 🍎 iPhone / Mac app

`ios/` folder me **Xcode project** ready hai — Mac par Xcode se kholo, apna Apple ID lagao,
iPhone connect karke Run dabao. Mac par bhi chalta hai (“My Mac” destination).
Details: `ios/README.md`

## 🚀 Play Store par upload

1. [play.google.com/console](https://play.google.com/console) par jao → one-time $25 (~₹200) developer account banao
2. Android Studio me: **Build → Generate Signed Bundle / APK → APK** → naya keystore banao (backup zaroor rakho!)
3. Play Console me **Create app** → signed APK upload karo → listing details bharo → Submit

## 💡 Notes

- Debug APK direct install ke liye bilkul theek hai; **Play Store ke liye signed release APK** hi bhejna.
- Branding-hide engine `MainActivity.kt` ke `HIDE_JS` me hai — badalna ho to wahan edit karke dobara build karo.
- App ka naam/package badalna ho to `app/build.gradle` me `applicationId` aur `res/values/strings.xml` me `app_name` edit karo.

— Banaya gaya AppBanao se ❤️
