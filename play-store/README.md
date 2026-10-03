# 🚀 Play Store par upload karo — step by step

## 1) .aab file lo (ban chuki hai)

- Repo ke **Actions** tab me “Build Play Store AAB” workflow chalega
- **Releases** me `playstore-v…` tag ke andar **app-release.aab** download karo
- Ya Actions run ke artifact **play-store-aab** se lo

## 2) Play Console account

1. [play.google.com/console](https://play.google.com/console) kholo
2. One-time **$25** (~₹200) developer fee bharo
3. **Create app** → naam: **Royal Banjara Studio** → language Hindi/English → App/Game: App

## 3) Listing bharo

- `STORE-LISTING.md` ka text copy karke daalo (title, short description, full description sab ready hai)
- App icon: `icon-512.png` (512×512 PNG)
- Feature graphic: `feature-graphic-1024x500.png` (1024×500 PNG)
- Phone screenshot: app chala kar 2-8 screenshot lo (1080×1920 ya usse bada)

## 4) Privacy policy

Play Store privacy policy **zaroori** hai:
1. `PRIVACY-POLICY.md` kholo → apni email/naam bhar do
2. Use apni website par ek page par daalo (ya GitHub page par host karo)
3. Us page ka URL Play Console → App content → Privacy policy me daalo

## 5) .aab upload

1. **Production** (ya pehle Internal testing — behtar hai) → **Create new release**
2. `app-release.aab` upload karo
3. Release notes likho → Save → Review → **Start rollout**
4. Review me usually 1-7 din lagte hain

## ⚠️ 6) Signing key — SABSE ZAROORI (updates ke liye)

Har naya build apna naya key banata hai jab tak tum apna fix keystore GitHub Secrets me save nahi karte.
**Pehli release upload karne se pehle** ye karo:

1. Computer par ek baar keytool se keystore banao:
   `keytool -genkeypair -v -keystore meri-app.keystore -alias meriapp -keyalg RSA -keysize 2048 -validity 10000`
2. Us file ko base64 me badlo (Windows: `certutil -encode meri-app.keystore out.txt`, Mac/Linux: `base64 -i meri-app.keystore -o out.txt`)
3. Repo → **Settings → Secrets and variables → Actions** me 4 secrets banao:
   - `KEYSTORE_BASE64` — out.txt ka content
   - `KEYSTORE_PASSWORD` — keystore ka password
   - `KEY_ALIAS` — `meriapp`
   - `KEY_PASSWORD` — wahi password
4. Ab har .aab isi key se banega — aur `.keystore` file + password ka **backup zaroor** rakhna!

Kabhi key kho jaaye to Play Console ka key reset hi bachao hai — isliye backup sasta hai.
