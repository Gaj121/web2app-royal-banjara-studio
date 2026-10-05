# Royal Banjara Studio  — iPhone / Mac App

Ye **https://rbstudiosmusic.kliv.site/** ka asli iOS app project hai (WKWebView).
Apple ka rule hai ki iPhone par koi bhi app Apple-signed .ipa se hi install hota hai —
isliye direct .ipa file browser se banayi nahi ja sakti. Ye project Xcode se 10 minute me
aapke apne iPhone par chala deta hai — bilkul free.

## iPhone par chalane ke steps

1. Kisi **Mac** par [Xcode](https://apps.apple.com/app/xcode/id497799835) install karo (free)
2. `WebApp.xcodeproj` kholo
3. Left me target **WebApp** → **Signing & Capabilities** → apna **Apple ID team** select karo (free account chalega)
4. iPhone ko cable se connect karke upar device select karo → **Run** (▶) dabao
5. iPhone par Settings → General → VPN & Device Management → apna Apple ID **Trust** karo

## Mac par chalana

Xcode me top ke device selector me **My Mac (Designed for iPad)** ya **My Mac (Catalyst)** choose karke Run.

## App Store par daalna

1. [developer.apple.com](https://developer.apple.com) par $99/saal ka account lo
2. Xcode me **Product → Archive** → **Distribute App → App Store Connect**
3. App Store Connect me app listing banao → build attach karo → submit

> Note: free Apple ID se app 7 din baad expire ho jati hai — dobara Run karke fresh kar lo.
> App Store wala rasta permanent hai.
