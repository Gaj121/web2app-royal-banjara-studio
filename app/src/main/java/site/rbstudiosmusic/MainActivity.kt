package site.rbstudiosmusic

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.print.PrintManager
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.text.InputType
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.JavascriptInterface
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Locale

class NavEntry(val label: String, val url: String, val icon: Int)

class ToolItem(val icon: Int, val label: String, val sub: String, val accent: Int, val section: String, val action: () -> Unit)

class MainActivity : AppCompatActivity() {

    companion object {
        const val HOME_URL = "https://rbstudiosmusic.kliv.site/"
        const val HOME_HOST = "rbstudiosmusic.kliv.site"
        const val APP_NAME = "Royal Banjara Studio"
        // App me dikhne wala site ka naam (jaise www.mysite.com) — Quick Tools header aur share text me
        const val SITE_LABEL = "www.rbstudiosmusic.site"
        const val REPLACE_JS = "(function(){\nvar FROM=\"rbstudiosmusic.kliv.site\",TO=\"www.rbstudiosmusic.site\";\nfunction rp(s){return s.split(FROM).join(TO);}\nfunction fix(root){try{\n if(!root){return;}\n var w=document.createTreeWalker(root,NodeFilter.SHOW_TEXT,null,false);\n var n,b=[];\n while((n=w.nextNode())){if(n.nodeValue&&n.nodeValue.indexOf(FROM)!==-1){b.push(n);}}\n for(var i=0;i<b.length;i++){b[i].nodeValue=rp(b[i].nodeValue);}\n if(root.querySelectorAll){var els=root.querySelectorAll('[placeholder],[title],[alt],[aria-label]');\n  for(var j=0;j<els.length;j++){var el=els[j];var ats=['placeholder','title','alt','aria-label'];\n   for(var k=0;k<ats.length;k++){var v=el.getAttribute(ats[k]);if(v&&v.indexOf(FROM)!==-1){el.setAttribute(ats[k],rp(v));}}}}\n}catch(err){}}\nfix(document.body);\ntry{if(document.title&&document.title.indexOf(FROM)!==-1){document.title=rp(document.title);}}catch(err){}\ntry{\n if(window.__rbTxtObs){window.__rbTxtObs.disconnect();}\n window.__rbTxtObs=new MutationObserver(function(ms){\n  for(var i=0;i<ms.length;i++){var ad=ms[i].addedNodes;\n   for(var j=0;j<ad.length;j++){var nd=ad[j];\n    if(nd.nodeType===3){if(nd.nodeValue&&nd.nodeValue.indexOf(FROM)!==-1){nd.nodeValue=rp(nd.nodeValue);}}\n    else{fix(nd);}}}});\n window.__rbTxtObs.observe(document.documentElement||document.body,{childList:true,subtree:true});\n}catch(err){}\n})();"
        const val KEEPCR_ON = true
        const val BRIGHTNESS_ON = true
        const val AUTOSCROLL_ON = true
        const val SITE_SEARCH_ON = true
        const val DICT_TOOL_ON = true
        const val TTS_SPEED_ON = true
        const val COOKIES_CLEAR_ON = true
        const val COPY_TEXT_ON = true
        const val FONT_STYLE_ON = true
        const val AUTO_REFRESH_ON = true
        const val IMAGE_SAVE_ON = true
        const val QR_TOOL_ON = true
        const val DATA_SAVER_ON = true
        const val VIDEO_BLOCK_ON = true
        const val ADBLOCK_TOOL_ON = true
        const val PAGE_SHARE_ON = true
        const val DATA_SAVER_JS = "(function(){try{\nvar q=function(t,f){var l=document.querySelectorAll(t);for(var i=0;i<l.length;i++)f(l[i])};\nq('video',function(v){try{v.removeAttribute('autoplay');v.autoplay=false;v.preload='none';if(!v.paused)v.pause()}catch(e){}});\nq('iframe',function(f){var u=(f.src||'').toLowerCase();if(/(doubleclick|googlesyndication|facebook|hotjar|clarity|analytics)/.test(u)){try{f.style.display='none'}catch(e){}}});\nq('script',function(s){var u=(s.src||'').toLowerCase();if(/(googletagmanager|google-analytics|googlesyndication|doubleclick|facebook.net|fbq|hotjar|clarity.ms|mixpanel|amplitude|segment.io|adsystem)/.test(u)){try{s.remove()}catch(e){}}});\nq('img',function(im){try{if(!im.loading)im.loading='lazy'}catch(e){}});\n}catch(e){}})();"
        const val VIDEO_BLOCK_JS = "(function(){try{\nvar q=function(t,f){var l=document.querySelectorAll(t);for(var i=0;i<l.length;i++)f(l[i])};\nq('video',function(v){try{v.removeAttribute('autoplay');v.autoplay=false;v.preload='none';v.pause()}catch(e){}});\nif(!window.__rbVidObs){window.__rbVidObs=new MutationObserver(function(ms){for(var i=0;i<ms.length;i++){var ns=ms[i].addedNodes;for(var j=0;j<ns.length;j++){var n=ns[j];if(n&&n.tagName==='VIDEO'){try{n.autoplay=false;n.preload='none';n.pause()}catch(e){}}}}});window.__rbVidObs.observe(document.documentElement,{childList:true,subtree:true})}\n}catch(e){}})();"
        const val THEME_COLOR = "#3DDC84"
        const val SPLASH_COLOR = "#0C0F14"
        const val HIDE_ON = true
        const val HIDE_CSS = "footer{display:none !important;}.footer{display:none !important;}#footer{display:none !important;}.site-footer{display:none !important;}#powered-by{display:none !important;}.powered-by{display:none !important;}#credit{display:none !important;}[data-kliv-badge]{display:none !important;}.kliv-badge{display:none !important;}#kliv-badge{display:none !important;}[class*=\"kliv-badge\"]{display:none !important;}[id*=\"kliv-badge\"]{display:none !important;}a[href*=\"kliv.site\"]{display:none !important;}a[href*=\"kliv.com\"]{display:none !important;}a[href*=\"kliv.dev\"]{display:none !important;}[data-kliv-footer]{display:none !important;}[class*=\"kliv-footer\"]{display:none !important;}[id*=\"kliv-footer\"]{display:none !important;}"
        const val HIDE_JS = "(function(){\nif(window.__web2appHide){window.__web2appHide();return;}\nvar CSS=\"footer{display:none !important;}.footer{display:none !important;}#footer{display:none !important;}.site-footer{display:none !important;}#powered-by{display:none !important;}.powered-by{display:none !important;}#credit{display:none !important;}[data-kliv-badge]{display:none !important;}.kliv-badge{display:none !important;}#kliv-badge{display:none !important;}[class*=\\\"kliv-badge\\\"]{display:none !important;}[id*=\\\"kliv-badge\\\"]{display:none !important;}a[href*=\\\"kliv.site\\\"]{display:none !important;}a[href*=\\\"kliv.com\\\"]{display:none !important;}a[href*=\\\"kliv.dev\\\"]{display:none !important;}[data-kliv-footer]{display:none !important;}[class*=\\\"kliv-footer\\\"]{display:none !important;}[id*=\\\"kliv-footer\\\"]{display:none !important;}\";\nvar PATTERNS=[\"created with kliv\",\"made with kliv\",\"powered by kliv\",\"built with kliv\",\"made with wix\",\"created with wix\",\"this site was made with wix\",\"powered by wix\",\"powered by wordpress\",\"proudly powered by wordpress\",\"powered by wordpress.com\",\"built on godaddy\",\"created with godaddy\",\"powered by shopify\",\"made in webflow\",\"made with webflow\",\"made with carrd\",\"made on carrd\",\"powered by squarespace\",\"powered by weebly\",\"powered by jimdo\",\"made with tilda\",\"built on tilda\",\"powered by blogger\",\"website created with\",\"website made with\",\"this site was created with\",\"this website was created with\",\"created by kliv\",\"made by kliv\",\"built by kliv\",\"designed by kliv\",\"website by kliv\",\"site by kliv\",\"hosted on kliv\",\"kliv.site\"];\nvar MAX=200;\nfunction applyCss(){\n var s=document.getElementById('web2app-hide-css');\n if(!s){s=document.createElement('style');s.id='web2app-hide-css';(document.head||document.documentElement).appendChild(s);}\n s.textContent=CSS;\n}\nfunction hit(t){for(var i=0;i<PATTERNS.length;i++){if(t.indexOf(PATTERNS[i])!==-1){return true;}}return false;}\nfunction fullText(e){return (e.textContent||'').replace(/\\s+/g,' ').trim().toLowerCase();}\nfunction hideEl(e){e.setAttribute('data-web2app-hidden','1');e.style.setProperty('display','none','important');\n var p=e.parentElement,k=0;\n while(p&&p!==document.body&&k<4){var pt=fullText(p);\n  if(p.children.length<=2&&pt&&pt.length<=MAX&&hit(pt)){p.setAttribute('data-web2app-hidden','1');p.style.setProperty('display','none','important');p=p.parentElement;k++;}else{break;}}}\nfunction hideByText(){if(!PATTERNS.length){return;}\n var n=document.querySelectorAll('a,div,span,p,small,li,section,aside,footer,i,b,em,strong,label,h1,h2,h3,h4,h5,h6,button');\n for(var i=0;i<n.length;i++){var e=n[i];\n  if(e.getAttribute('data-web2app-hidden')){continue;}\n  var t=fullText(e);\n  if(t&&t.length<=MAX&&hit(t)){hideEl(e);}\n }}\nfunction run(){try{applyCss();hideByText();}catch(err){}}\nwindow.__web2appHide=run;\nrun();\nvar tmr=null;\ntry{\n new MutationObserver(function(){if(tmr){clearTimeout(tmr);}tmr=setTimeout(run,150);}).observe(document.documentElement||document.body,{childList:true,subtree:true});\n}catch(err){}\nwindow.addEventListener('load',function(){run();});\n})();"
        const val SHOW_NAV = true
        const val OPEN_EXTERNAL = true
        const val OFFLINE_PAGE = true
        const val PULL_REFRESH = true
        const val INTRO_ON = true
        const val WELCOME_ON = true
        const val WELCOME_TEXT = "Welcome to Royal Banjara Studio Music Distribution Company"
        const val VERSION_NAME = "1.1"
        const val TOOLS_ON = true
        const val DOWNLOADS_ON = true
        const val GALLERY_ON = true
        const val DL_LIST_ON = true
        const val PINCH_ZOOM = true
        const val KEEP_SCREEN_ON = true
        const val FULLSCREEN_ON = true
        const val WHATSAPP_ON = true
        const val WHATSAPP_NUMBER = "919370612297"
        const val WHATSAPP_MESSAGE = "Hello! Mujhe jaankari chahiye"
        const val NIGHT_MODE_ON = true
        const val TEXT_SIZE_ON = true
        const val BACK_TWICE_ON = true
        const val CLEAR_CACHE_ON = true
        const val EXIT_ITEM_ON = true
        const val LONGPRESS_DL_ON = true
        const val DESKTOP_VIEW_ON = true
        const val AD_BLOCK_ON = true
        const val THEME_PICKER_ON = true
        const val FULLSCREEN_TOOL_ON = true
        const val GO_TOP_ON = true
        const val ROTATE_TOOL_ON = true
        const val FIND_TOOL_ON = true
        const val APPINFO_TOOL_ON = true
        const val READ_ALOUD_ON = true
        const val BOOKMARKS_ON = true
        const val SUPPORT_EMAIL = "adegajanancsc8@gmail.com"
        const val DIRECTION_TOOL_ON = true
        const val MAP_QUERY = "Royal Banjara Studio Music Distribution"
        const val TRANSLATE_TOOL_ON = true
        const val HISTORY_TOOL_ON = true
        const val CALL_TOOL_ON = true
        const val CALL_NUMBER = "+919370612297"
        const val SCREENSHOT_ON = true
        const val SHARE_APP_ON = true
        const val HISTORY_NAV_ON = true
        const val OFFLINE_SAVE_ON = true
        const val AUTO_NIGHT_ON = true
        const val READING_MODE_ON = true
        const val MUTE_TOOL_ON = true
        const val DATA_SAVE_ON = true
        const val URL_TOOL_ON = true
        const val AD_BLOCK_JS = "(function(){try{var s=document.createElement('style');s.id='appbanao-adblock';s.textContent=\"ins.adsbygoogle,.adsbygoogle,[id^='google_ads'],[id^='div-gpt-ad'],[id^='taboola'],[class^='popunder'],iframe[src*='doubleclick.net'],iframe[src*='googlesyndication'],iframe[src*='adserver'],.ad-banner,.ad-banner-top,.ad-container,.ad-wrapper,.ad-slot,.advert,.advertisement,.google-ad,.sidebar-ad,.sticky-ad{display:none !important;visibility:hidden !important;}\";(document.head||document.documentElement).appendChild(s);}catch(e){}})()"
        val THEME_PRESETS = arrayOf("Royal Blue|#2563EB", "Midnight Black|#111827", "Emerald Green|#10B981", "Ocean Cyan|#0EA5E9", "Sunset Orange|#F97316", "Grape Purple|#8B5CF6", "Rose Pink|#EC4899", "Royal Gold|#D4AF37", "Teal Fresh|#14B8A6", "Deep Indigo|#6366F1", "Crimson Red|#DC2626", "Amber Glow|#F59E0B", "Lime Punch|#84CC16", "Sky Light|#38BDF8", "Chocolate Brown|#92400E", "Slate Grey|#475569", "Neon Violet|#7C3AED", "Magenta Rush|#E11D48", "Forest Green|#15803D", "Deep Navy|#1E40AF", "Coral Peach|#FF7F50", "Mint Aqua|#06D6A0", "Jade Stone|#00A896", "Bronze Copper|#B87333", "Orchid Pink|#DA70D6", "Plum Velvet|#7E22CE", "Steel Blue|#4682B4", "Ruby Red|#E0115F", "Arctic Ice|#22D3EE", "Coffee Dark|#6F4E37", "Saffron Desi|#FF9933", "Peacock Blue|#0288D1", "Henna Maroon|#800000", "Banana Yellow|#FBC02D", "Grapefruit|#FF6347", "Lavender Soft|#9575CD", "Olive Green|#6B8E23", "Turquoise Sea|#40E0D0", "Fuchsia Flash|#D500F9", "Graphite Steel|#37474F", "Lagoon Deep|#0891B2", "Blush Rose|#F472B6", "Kiwi Fresh|#65A30D", "Storm Slate|#64748B", "Wine Berry|#9D174D", "Citrus Lemon|#EAB308", "Iceberg Blue|#93C5FD", "Mahogany Wood|#A0522D", "Pine Forest|#2D6A4F", "Berry Purple|#A21CAF", "Ink Blue|#1A237E", "Terracotta Mitti|#C0563B", "Spearmint Green|#00C853", "Bubblegum Pink|#FF69B4", "Bright Sky|#00B0FF", "Deep Teal|#00695C", "Sunrise Peach|#FF8A65", "Velvet Night|#311B92", "Leaf Green|#43A047", "Desert Sand|#C2A878", "Sindoor Red|#E53935", "Nilkamal Blue|#3949AB", "Kesar Saffron|#FF9800", "Jamun Purple|#6A1B9A", "Amaltas Yellow|#FBC02D", "Moong Green|#7CB342", "Mehendi Green|#558B2F", "Rani Pink|#D81B60", "Badal Grey Blue|#546E7A", "Chandan Brown|#8D6E63")
        const val DESKTOP_UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
        const val BLOB_HOOK_JS = "(function(){\nif (window.__appbanaoDl) return; window.__appbanaoDl = 1;\nvar CH = 262144;\nfunction sendBlob(blob, name, mime) {\n  try {\n    var total = blob.size;\n    var off = 0;\n    var fr = new FileReader();\n    window.AndroidDownloads && window.AndroidDownloads.blobSaveStart(name || 'download.bin', (mime || blob.type || 'application/octet-stream').split(',')[0]);\n    fr.onload = function() {\n      try {\n        var arr = new Uint8Array(fr.result);\n        var s = '';\n        for (var i = 0; i < arr.length; i++) s += String.fromCharCode(arr[i]);\n        window.AndroidDownloads && window.AndroidDownloads.blobSaveChunk(btoa(s));\n      } catch (e) { window.AndroidDownloads && window.AndroidDownloads.blobSaveFail(); return; }\n      off += CH;\n      if (off < total) fr.readAsArrayBuffer(blob.slice(off, off + CH));\n      else window.AndroidDownloads && window.AndroidDownloads.blobSaveDone();\n    };\n    fr.onerror = function() { window.AndroidDownloads && window.AndroidDownloads.blobSaveFail(); };\n    fr.readAsArrayBuffer(blob.slice(0, CH));\n  } catch (e) { window.AndroidDownloads && window.AndroidDownloads.blobSaveFail(); }\n}\nfunction grab(url, name) {\n  try {\n    fetch(url).then(function(r) { return r.blob(); }).then(function(b) { sendBlob(b, name, b.type); }).catch(function() { window.AndroidDownloads && window.AndroidDownloads.blobSaveFail(); });\n  } catch (e) { window.AndroidDownloads && window.AndroidDownloads.blobSaveFail(); }\n}\nwindow.__appbanaoGrab = grab;\ndocument.addEventListener('click', function(e) {\n  var t = e.target;\n  while (t && t.tagName !== 'A') t = t.parentElement;\n  if (!t) return;\n  var href = t.getAttribute('href') || '';\n  if (href.indexOf('blob:') === 0 || href.indexOf('data:') === 0) {\n    e.preventDefault(); e.stopPropagation();\n    var nm = t.getAttribute('download') || (document.title ? document.title.replace(/[\\\\/:*?\"<>|]/g, '').slice(0, 40) : 'download.bin');\n    grab(href, nm);\n  }\n}, true);\n})();"
    }

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private var navBar: LinearLayout? = null
    private var introOverlay: FrameLayout? = null
    private var welcomeOverlay: FrameLayout? = null
    private var welcomeCenter: LinearLayout? = null
    private var welcomeHeader: LinearLayout? = null
    private var welcomeBottom: LinearLayout? = null
    private var accentColor = 0
    private var moreBtn: TextView? = null
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private var fullScreenOn = false
    private var currentNavIndex = 0
    private val navIcons = mutableListOf<ImageView>()
    private val navLabels = mutableListOf<TextView>()
    private val pillHolders = mutableListOf<FrameLayout>()
    private val pillBackgrounds = mutableListOf<GradientDrawable>()
    private var fileChooserCallback: ValueCallback<Array<Uri>>? = null
    private lateinit var filePicker: ActivityResultLauncher<String>
    private var pendingDownload: Array<String>? = null
    private var lastDownloadId: Long = -1L
    private val downloadIds = mutableSetOf<Long>()
    private val downloadMimes = mutableMapOf<Long, String>()
    private var downloadReceiver: BroadcastReceiver? = null
    private val themeColorInt: Int by lazy { Color.parseColor(THEME_COLOR) }
    private val prefs by lazy { getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    private val splashColorInt: Int by lazy { Color.parseColor(SPLASH_COLOR) }
    private var nightOn = false
    private var textZoomLevel = 100
    private var lastBackAt = 0L
    private var whatsappFab: FrameLayout? = null
    private val blobBuffer = ByteArrayOutputStream()
    private var blobName = "download.bin"
    private var blobMime = "application/octet-stream"
    private var desktopView = false
    private var baseUa = ""
    private var readingModeOn = false
    private var muted = false
    private var dataSaveOn = false
    private var dataSaverCacheOn = DATA_SAVER_ON
    private var videoBlockOn = VIDEO_BLOCK_ON
    private var adBlockOn = AD_BLOCK_ON

    private val navEntries: Array<NavEntry> = arrayOf(
        NavEntry("Home", "https://rbstudiosmusic.kliv.site/", R.drawable.ic_nav_home),
        NavEntry("Products", "https://rbstudiosmusic.kliv.site/products", R.drawable.ic_nav_grid),
        NavEntry("Contact", "https://rbstudiosmusic.kliv.site/contact", R.drawable.ic_nav_phone)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accentColor = run {
            val idx = prefs.getInt("theme_idx", -1)
            if (idx >= 0 && idx < THEME_PRESETS.size) {
                try { Color.parseColor(THEME_PRESETS[idx].split("|").getOrNull(1) ?: THEME_COLOR) } catch (e: Exception) { themeColorInt }
            } else themeColorInt
        }
        fullScreenOn = FULLSCREEN_ON

        if (KEEP_SCREEN_ON) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        if (FULLSCREEN_ON) {
            supportActionBar?.hide()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }

        filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val callback = fileChooserCallback
            fileChooserCallback = null
            callback?.onReceiveValue(if (uri != null) arrayOf(uri) else null)
        }

        val root = FrameLayout(this)
        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL

        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.max = 100
        progressBar.progressTintList = ColorStateList.valueOf(accentColor)
        progressBar.progressBackgroundTintList = ColorStateList.valueOf(0x22888888)
        content.addView(progressBar, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(3)))

        webView = WebView(this)
        swipeRefresh = SwipeRefreshLayout(this)
        swipeRefresh.addView(webView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        if (PULL_REFRESH) {
            swipeRefresh.setOnRefreshListener { webView.reload() }
            swipeRefresh.setColorSchemeColors(accentColor)
        } else {
            swipeRefresh.isEnabled = false
        }
        content.addView(swipeRefresh, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))

        if (SHOW_NAV) {
            val bar = buildNavBar()
            navBar = bar
            val barLp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM)
            barLp.leftMargin = dp(12)
            barLp.rightMargin = dp(12)
            barLp.bottomMargin = dp(10)
            root.addView(bar, barLp)
        }

        if (SHOW_NAV) {
            webView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                val dy = scrollY - oldScrollY
                if (dy > 8) hideNavBar() else if (dy < -8) showNavBar()
            }
        }

        if (TOOLS_ON) {
            val fab = buildMoreButton()
            val fabLp = FrameLayout.LayoutParams(dp(46), dp(46), Gravity.BOTTOM or Gravity.END)
            fabLp.rightMargin = dp(14)
            fabLp.bottomMargin = if (SHOW_NAV) dp(98) else dp(22)
            root.addView(fab, fabLp)
        }

        if (WHATSAPP_ON) {
            val wa = buildWhatsappButton()
            whatsappFab = wa
            val waLp = FrameLayout.LayoutParams(dp(52), dp(52), Gravity.BOTTOM or Gravity.END)
            waLp.rightMargin = dp(14)
            waLp.bottomMargin = if (TOOLS_ON) dp(156) else if (SHOW_NAV) dp(98) else dp(22)
            root.addView(wa, waLp)
        }

        root.addView(content, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        if (INTRO_ON) {
            val intro = buildIntro()
            introOverlay = intro
            root.addView(intro, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        }

        if (WELCOME_ON) {
            val welcome = buildWelcomeSlider()
            welcomeOverlay = welcome
            welcome.translationY = -resources.displayMetrics.heightPixels.toFloat()
            welcome.alpha = 0f
            root.addView(welcome, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            welcome.postDelayed({ showWelcome() }, if (INTRO_ON) 1950L else 350L)
        }

        setContentView(root)

        setupWebView()
        if (NIGHT_MODE_ON && prefs.getBoolean("night_on", false)) {
            nightOn = true
            applyNight(true)
        }
        if (NIGHT_MODE_ON && AUTO_NIGHT_ON && !nightOn) {
            val hr = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            if (hr >= 19 || hr < 6) {
                nightOn = true
                prefs.edit().putBoolean("night_on", true).apply()
                applyNight(true)
            }
        }
        textZoomLevel = prefs.getInt("text_zoom", 100)
        if (textZoomLevel != 100) webView.settings.textZoom = textZoomLevel
        selectNav(0)
        webView.loadUrl(HOME_URL)
        registerDownloadReceiver()
        webView.addJavascriptInterface(BlobBridge(), "AndroidDownloads")
        if (LONGPRESS_DL_ON) {
            webView.setOnLongClickListener {
                val hit = webView.hitTestResult
                val extra = hit.extra ?: return@setOnLongClickListener false
                val isImage = hit.type == WebView.HitTestResult.IMAGE_TYPE || hit.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE
                if (!isImage) return@setOnLongClickListener false
                if (extra.startsWith("blob:") || extra.startsWith("data:")) {
                    captureBlobDownload(extra)
                } else if (extra.startsWith("http") && DOWNLOADS_ON) {
                    startDownload(extra, webView.settings.userAgentString, "", "image/*")
                }
                true
            }
        }
        if (DESKTOP_VIEW_ON) {
            baseUa = webView.settings.userAgentString
            desktopView = prefs.getBoolean("desktop_view", false)
            if (desktopView) webView.settings.userAgentString = DESKTOP_UA
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val welcome = welcomeOverlay
                if (welcome != null && welcome.visibility == View.VISIBLE) {
                    dismissWelcome()
                    return
                }
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    val now = System.currentTimeMillis()
                    if (now - lastBackAt < 2000L) {
                        finish()
                    } else {
                        lastBackAt = now
                        Toast.makeText(this@MainActivity, "Ek baar aur back dabao — app band ho jayega", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroy() {
        downloadReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                // pehle se hat chuka tha
            }
        }
        downloadReceiver = null
        fileChooserCallback?.onReceiveValue(null)
        fileChooserCallback = null
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
        }
        tts = null
        super.onDestroy()
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun shade(color: Int, factor: Float): Int {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        fun mix(c: Int): Int = if (factor >= 0f) (c + ((255 - c) * factor).toInt()) else (c * (1f + factor)).toInt()
        return Color.rgb(mix(r).coerceIn(0, 255), mix(g).coerceIn(0, 255), mix(b).coerceIn(0, 255))
    }

    private fun buildIntro(): FrameLayout {
        val overlay = FrameLayout(this)
        val bg = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(shade(splashColorInt, -0.45f), splashColorInt, shade(themeColorInt, -0.55f))
        )
        bg.setGradientCenter(0.5f, 0.35f)
        overlay.background = bg

        // — floating soft circles — background me halke se chalte rehte hain
        fun addFloat(sizeDp: Int, color: Int, alphaInt: Int, x: Int, y: Int, driftMs: Long) {
            val dot = View(this)
            val dotBg = GradientDrawable()
            dotBg.shape = GradientDrawable.OVAL
            dotBg.setColor(color)
            dot.background = dotBg
            dot.alpha = alphaInt.toFloat()
            overlay.addView(dot, FrameLayout.LayoutParams(dp(sizeDp), dp(sizeDp)))
            dot.translationX = x.toFloat()
            dot.translationY = y.toFloat()
            val rise = ObjectAnimator.ofFloat(dot, View.TRANSLATION_Y, y.toFloat(), y - dp(46).toFloat(), y.toFloat())
            rise.duration = driftMs
            rise.repeatCount = ObjectAnimator.INFINITE
            rise.startDelay = (driftMs / 3)
            rise.start()
        }
        addFloat(130, shade(themeColorInt, 0.15f), 36, -dp(30), dp(80), 3400)
        addFloat(90, Color.WHITE, 26, dp(230), dp(120), 4200)
        addFloat(160, shade(themeColorInt, 0.4f), 30, dp(40), dp(430), 5000)
        addFloat(70, Color.WHITE, 22, -dp(10), dp(380), 3800)

        val stack = LinearLayout(this)
        stack.orientation = LinearLayout.VERTICAL
        stack.gravity = Gravity.CENTER

        val logoCard = FrameLayout(this)
        val cardBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(Color.WHITE, shade(themeColorInt, 0.72f)))
        cardBg.cornerRadius = dp(34).toFloat()
        cardBg.setStroke(dp(2), 0x66FFFFFF)
        logoCard.background = cardBg
        logoCard.clipToOutline = true
        logoCard.elevation = dp(24).toFloat()
        logoCard.setPadding(dp(14), dp(14), dp(14), dp(14))
        val logo = ImageView(this)
        val fullRes = resources.getIdentifier("ic_brand_full", "drawable", packageName)
        logo.setImageResource(if (fullRes != 0) fullRes else R.mipmap.ic_launcher)
        logoCard.addView(logo, FrameLayout.LayoutParams(dp(104), dp(104), Gravity.CENTER))

        // — shine sweep — logo ke upar se roshani ka pass jata hai
        val shine = View(this)
        val shineBg = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(0x00FFFFFF, 0x73FFFFFF, 0x00FFFFFF))
        shine.background = shineBg
        shine.rotation = 22f
        logoCard.addView(shine, FrameLayout.LayoutParams(dp(30), dp(150), Gravity.CENTER))
        shine.translationX = -dp(80).toFloat()
        val sweep = ObjectAnimator.ofFloat(shine, View.TRANSLATION_X, -dp(80).toFloat(), dp(80).toFloat())
        sweep.duration = 850
        sweep.startDelay = 620
        sweep.interpolator = OvershootInterpolator(0.9f)
        sweep.start()

        val name = TextView(this)
        name.text = APP_NAME
        name.textSize = 22f
        name.setTextColor(Color.WHITE)
        name.typeface = Typeface.DEFAULT_BOLD
        name.gravity = Gravity.CENTER
        name.letterSpacing = 0.08f
        name.setShadowLayer(dp(6).toFloat(), 0f, dp(2).toFloat(), 0x66000000)

        val tagline = TextView(this)
        tagline.text = "Loading ho raha hai..."
        tagline.textSize = 13f
        tagline.setTextColor(0xB3FFFFFF.toInt())
        tagline.gravity = Gravity.CENTER
        tagline.letterSpacing = 0.04f

        val spinner = ProgressBar(this)
        spinner.indeterminateTintList = ColorStateList.valueOf(accentColor)

        val ring = FrameLayout(this)
        val ringBg = GradientDrawable()
        ringBg.shape = GradientDrawable.OVAL
        ringBg.setColor(0x2EFFFFFF)
        ring.background = ringBg
        ring.addView(spinner, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))

        stack.addView(logoCard, LinearLayout.LayoutParams(dp(132), dp(132)))
        val nameLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        nameLp.topMargin = dp(24)
        stack.addView(name, nameLp)
        val tagLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tagLp.topMargin = dp(7)
        stack.addView(tagline, tagLp)
        val spLp = LinearLayout.LayoutParams(dp(46), dp(46))
        spLp.topMargin = dp(20)
        stack.addView(ring, spLp)

        overlay.addView(stack, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        logoCard.scaleX = 0.3f
        logoCard.scaleY = 0.3f
        logoCard.alpha = 0f
        name.alpha = 0f
        name.translationY = dp(16).toFloat()
        tagline.alpha = 0f

        logoCard.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(500).setInterpolator(OvershootInterpolator(1.6f)).start()
        logoCard.postDelayed({
            logoCard.animate().scaleX(1.05f).scaleY(1.05f).setDuration(320).withEndAction {
                logoCard.animate().scaleX(1f).scaleY(1f).setDuration(260).start()
            }.start()
        }, 560)
        logoCard.postDelayed({
            name.animate().alpha(1f).translationY(0f).setDuration(380).start()
            tagline.animate().alpha(1f).setDuration(380).start()
        }, 330)

        overlay.postDelayed({
            overlay.animate().translationY(-overlay.height.toFloat()).alpha(0f).setDuration(450)
                .withEndAction { overlay.visibility = View.GONE }.start()
        }, 1500)

        return overlay
    }

    private fun showWelcome() {
        val ov = welcomeOverlay ?: return
        ov.alpha = 1f
        ov.visibility = View.VISIBLE
        ov.translationY = -dp(110).toFloat()
        ov.animate().translationY(0f).setDuration(640).setInterpolator(DecelerateInterpolator(1.7f)).start()
        welcomeHeader?.let { h ->
            h.alpha = 0f
            h.translationY = -dp(30).toFloat()
            h.animate().alpha(1f).translationY(0f).setDuration(420).setStartDelay(60).setInterpolator(DecelerateInterpolator(1.3f)).start()
        }
        welcomeCenter?.let { c ->
            c.alpha = 0f
            c.scaleX = 0.90f
            c.scaleY = 0.90f
            c.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(560).setStartDelay(160).setInterpolator(OvershootInterpolator(1.06f)).start()
        }
        welcomeBottom?.let { b ->
            b.alpha = 0f
            b.translationY = dp(40).toFloat()
            b.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(300).setInterpolator(DecelerateInterpolator(1.4f)).start()
        }
    }

    private fun dismissWelcome() {
        val ov = welcomeOverlay ?: return
        ov.animate().translationY(-ov.height.toFloat() * 0.45f).alpha(0f).setDuration(400).setInterpolator(AccelerateInterpolator(1.25f))
            .withEndAction { ov.visibility = View.GONE }.start()
    }

    private fun buildWelcomeSlider(): FrameLayout {
        val overlay = FrameLayout(this)
        val bgGrad = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(shade(splashColorInt, -0.30f), shade(accentColor, -0.30f), shade(splashColorInt, -0.55f))
        )
        bgGrad.setGradientCenter(0.5f, 0.3f)
        overlay.background = bgGrad

        fun addFloat(sizeDp: Int, color: Int, alphaInt: Int, x: Int, y: Int, driftMs: Long) {
            val dot = View(this)
            val dotBg = GradientDrawable()
            dotBg.shape = GradientDrawable.OVAL
            dotBg.setColor(color)
            dot.background = dotBg
            dot.alpha = alphaInt.toFloat()
            overlay.addView(dot, FrameLayout.LayoutParams(dp(sizeDp), dp(sizeDp)))
            dot.translationX = x.toFloat()
            dot.translationY = y.toFloat()
            val rise = ObjectAnimator.ofFloat(dot, View.TRANSLATION_Y, y.toFloat(), y - dp(40).toFloat(), y.toFloat())
            rise.duration = driftMs
            rise.repeatCount = ObjectAnimator.INFINITE
            rise.startDelay = (driftMs / 3)
            rise.start()
        }
        addFloat(150, shade(accentColor, 0.25f), 42, -dp(36), dp(60), 3600)
        addFloat(95, Color.WHITE, 30, dp(235), dp(150), 4400)
        addFloat(175, shade(accentColor, 0.45f), 34, dp(30), dp(430), 5200)
        addFloat(66, Color.WHITE, 24, dp(30), dp(64), 3900)

        // — upar: chhota logo + app ka naam + Skip button —
        val header = LinearLayout(this)
        header.orientation = LinearLayout.HORIZONTAL
        header.gravity = Gravity.CENTER_VERTICAL
        header.setPadding(dp(18), dp(16), dp(14), dp(6))

        val logoHolder = FrameLayout(this)
        val logoBg = GradientDrawable()
        logoBg.setColor(Color.WHITE)
        logoBg.cornerRadius = dp(13).toFloat()
        logoHolder.background = logoBg
        logoHolder.clipToOutline = true
        logoHolder.elevation = dp(6).toFloat()
        val logo = ImageView(this)
        logo.setImageResource(R.mipmap.ic_launcher)
        logoHolder.addView(logo, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        header.addView(logoHolder, LinearLayout.LayoutParams(dp(42), dp(42)))

        val headName = TextView(this)
        headName.text = APP_NAME
        headName.textSize = 15f
        headName.setTextColor(Color.WHITE)
        headName.typeface = Typeface.DEFAULT_BOLD
        headName.maxLines = 1
        headName.letterSpacing = 0.03f
        val headNameLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        headNameLp.leftMargin = dp(10)
        header.addView(headName, headNameLp)

        val skip = TextView(this)
        skip.text = "Skip ›"
        skip.setTextColor(Color.WHITE)
        skip.textSize = 13f
        skip.typeface = Typeface.DEFAULT_BOLD
        skip.setPadding(dp(16), dp(7), dp(16), dp(7))
        val skipBg = GradientDrawable()
        skipBg.setColor(0x38FFFFFF)
        skipBg.cornerRadius = dp(18).toFloat()
        skip.background = RippleDrawable(ColorStateList.valueOf(0x40FFFFFF), skipBg, null)
        header.addView(skip)
        overlay.addView(header, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP))
        welcomeHeader = header

        // — beech me: bade gradient bubbles wale slides —
        val center = LinearLayout(this)
        center.orientation = LinearLayout.VERTICAL
        center.gravity = Gravity.CENTER
        welcomeCenter = center

        val flipper = ViewFlipper(this)
        flipper.isAutoStart = true
        flipper.flipInterval = 3800
        val inAnim = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        inAnim.duration = 520
        inAnim.interpolator = DecelerateInterpolator(1.2f)
        val outAnim = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        outAnim.duration = 520
        outAnim.interpolator = AccelerateInterpolator(1.1f)
        flipper.inAnimation = inAnim
        flipper.outAnimation = outAnim

        val slides = listOf(
            Triple("👋", WELCOME_TEXT, "Aapka poora website — ab ek asli app me"),
            Triple("⬇️", "Download & Save", "File, photo, PDF seedha phone ke Downloads folder me"),
            Triple("🖨️", "Print & Share", "⋮ button se page print karo ya PDF bana kar bhejo"),
            Triple("⚡", "Smart Tools", "Night mode, screenshot, WhatsApp — sab ek jagah")
        )
        val palettes = listOf(
            intArrayOf(shade(accentColor, 0.42f), shade(accentColor, -0.15f)),
            intArrayOf(0xFF1B8A3A.toInt(), 0xFF57C863.toInt()),
            intArrayOf(0xFF6D3FC4.toInt(), 0xFF9B7BE8.toInt()),
            intArrayOf(0xFF0B7285.toInt(), 0xFF37B9CE.toInt())
        )

        slides.forEachIndexed { idx, slideData ->
            val slide = LinearLayout(this)
            slide.orientation = LinearLayout.VERTICAL
            slide.gravity = Gravity.CENTER_HORIZONTAL
            slide.setPadding(dp(8), 0, dp(8), 0)

            val bubble = FrameLayout(this)
            val bubbleBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, palettes[idx % palettes.size])
            bubbleBg.shape = GradientDrawable.OVAL
            bubbleBg.setStroke(dp(3), 0x73FFFFFF)
            bubble.background = bubbleBg
            bubble.elevation = dp(16).toFloat()
            val bigIcon = TextView(this)
            bigIcon.text = slideData.first
            bigIcon.textSize = 48f
            bigIcon.gravity = Gravity.CENTER
            bubble.addView(bigIcon, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
            slide.addView(bubble, LinearLayout.LayoutParams(dp(124), dp(124)))

            val title = TextView(this)
            title.text = slideData.second
            title.textSize = 23f
            title.typeface = Typeface.DEFAULT_BOLD
            title.setTextColor(Color.WHITE)
            title.gravity = Gravity.CENTER
            title.letterSpacing = 0.01f
            title.setShadowLayer(dp(8).toFloat(), 0f, dp(2).toFloat(), 0x59000000)
            val titleLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            titleLp.topMargin = dp(22)
            slide.addView(title, titleLp)

            val sub = TextView(this)
            sub.text = slideData.third
            sub.textSize = 14.5f
            sub.setTextColor(0xD9FFFFFF.toInt())
            sub.gravity = Gravity.CENTER
            sub.setPadding(dp(12), 0, dp(12), 0)
            val subLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            subLp.topMargin = dp(8)
            slide.addView(sub, subLp)

            flipper.addView(slide, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        }

        val stage = FrameLayout(this)
        val glow = View(this)
        val glowBg = GradientDrawable()
        glowBg.shape = GradientDrawable.OVAL
        glowBg.setColor(shade(accentColor, 0.45f))
        glow.background = glowBg
        glow.alpha = 0.34f
        stage.addView(glow, FrameLayout.LayoutParams(dp(310), dp(310), Gravity.CENTER))
        val glowPulse = ObjectAnimator.ofFloat(glow, View.ALPHA, 0.26f, 0.48f, 0.26f)
        glowPulse.duration = 2600
        glowPulse.repeatCount = ObjectAnimator.INFINITE
        glowPulse.start()
        val glowScale = ObjectAnimator.ofPropertyValuesHolder(
            glow,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.12f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.12f, 1f)
        )
        glowScale.duration = 3200
        glowScale.repeatCount = ObjectAnimator.INFINITE
        glowScale.start()
        stage.addView(flipper, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))

        center.addView(stage, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(330)))
        overlay.addView(center, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        // — neeche: pill dots + Shuru Karein button + version —
        val bottom = LinearLayout(this)
        bottom.orientation = LinearLayout.VERTICAL
        bottom.gravity = Gravity.CENTER_HORIZONTAL
        bottom.setPadding(dp(26), 0, dp(26), dp(34))
        welcomeBottom = bottom

        val dots = mutableListOf<View>()
        val dotsRow = LinearLayout(this)
        dotsRow.orientation = LinearLayout.HORIZONTAL
        dotsRow.gravity = Gravity.CENTER
        repeat(slides.size) {
            val dot = View(this)
            val dotBg = GradientDrawable()
            dotBg.shape = GradientDrawable.RECTANGLE
            dotBg.cornerRadius = dp(5).toFloat()
            dotBg.setColor(0x80FFFFFF.toInt())
            dot.background = dotBg
            dots.add(dot)
            val dLp = LinearLayout.LayoutParams(dp(9), dp(9))
            dLp.setMargins(dp(4), 0, dp(4), 0)
            dotsRow.addView(dot, dLp)
        }

        fun updateDots(index: Int) {
            dots.forEachIndexed { i, d ->
                val active = i == index
                val lp = d.layoutParams
                lp.width = if (active) dp(28) else dp(9)
                lp.height = dp(9)
                d.layoutParams = lp
                (d.background as GradientDrawable).setColor(if (active) Color.WHITE else 0x80FFFFFF.toInt())
            }
        }

        val startBtn = TextView(this)
        startBtn.text = "Shuru Karein  →"
        startBtn.textSize = 16f
        startBtn.setTextColor(Color.WHITE)
        startBtn.typeface = Typeface.DEFAULT_BOLD
        startBtn.gravity = Gravity.CENTER
        startBtn.letterSpacing = 0.04f
        startBtn.setPadding(dp(38), dp(14), dp(38), dp(14))
        val btnBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(accentColor, 0.38f), accentColor))
        btnBg.cornerRadius = dp(30).toFloat()
        btnBg.setStroke(dp(2), 0x66FFFFFF)
        startBtn.background = RippleDrawable(ColorStateList.valueOf(0x40FFFFFF), btnBg, null)
        startBtn.elevation = dp(14).toFloat()

        val pulse = ObjectAnimator.ofPropertyValuesHolder(
            startBtn,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.04f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.04f, 1f)
        )
        pulse.duration = 1100
        pulse.repeatCount = ObjectAnimator.INFINITE
        pulse.start()

        val ver = TextView(this)
        ver.text = "v" + VERSION_NAME
        ver.textSize = 11f
        ver.setTextColor(0x8CFFFFFF.toInt())
        ver.gravity = Gravity.CENTER
        ver.letterSpacing = 0.06f

        bottom.addView(dotsRow, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        val startLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        startLp.topMargin = dp(24)
        bottom.addView(startBtn, startLp)
        val verLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        verLp.topMargin = dp(14)
        bottom.addView(ver, verLp)
        overlay.addView(bottom, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM))

        val dismiss: () -> Unit = { dismissWelcome() }
        startBtn.setOnClickListener {
            bounce(startBtn)
            dismiss()
        }
        skip.setOnClickListener { dismiss() }
        overlay.setOnClickListener { dismiss() }

        updateDots(0)
        val sync = object : Runnable {
            override fun run() {
                if (overlay.visibility == View.VISIBLE) {
                    updateDots(flipper.displayedChild)
                    flipper.postDelayed(this, 3800)
                }
            }
        }
        flipper.postDelayed(sync, 3800)

        return overlay
    }

    private fun buildWhatsappButton(): FrameLayout {
        val holder = FrameLayout(this)
        val circle = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(Color.parseColor("#3BE477"), Color.parseColor("#25D366"), Color.parseColor("#128C7E")))
        circle.shape = GradientDrawable.OVAL
        holder.background = circle
        holder.elevation = dp(12).toFloat()

        val logo = ImageView(this)
        logo.setImageResource(R.drawable.ic_whatsapp)
        logo.setPadding(dp(12), dp(12), dp(12), dp(12))
        holder.addView(logo, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        val pulse = ObjectAnimator.ofPropertyValuesHolder(
            holder,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.08f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.08f, 1f)
        )
        pulse.duration = 900
        pulse.repeatCount = 1
        pulse.startDelay = 700
        pulse.start()

        holder.setOnClickListener { bounce(holder); openWhatsapp() }
        holder.setOnLongClickListener {
            Toast.makeText(this, "Tap karo — seedha WhatsApp chat khulega", Toast.LENGTH_SHORT).show()
            true
        }
        return holder
    }

    private fun openWhatsapp() {
        try {
            val num = WHATSAPP_NUMBER
            val text = Uri.encode(WHATSAPP_MESSAGE)
            try {
                val appUri = Uri.parse("whatsapp://send?phone=" + num + "&text=" + text)
                startActivity(Intent(Intent.ACTION_VIEW, appUri))
            } catch (e: ActivityNotFoundException) {
                val webUri = Uri.parse("https://api.whatsapp.com/send?phone=" + num + "&text=" + text)
                openExternal(webUri)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp nahi khul paya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyNight(on: Boolean) {
        if (on) {
            webView.settings.forceDark = android.webkit.WebSettings.FORCE_DARK_ON
            if (Build.VERSION.SDK_INT >= 29) {
                val alg = webView.settings
                try {
                    val m = alg.javaClass.getMethod("setAlgorithmicDarkeningAllowed", Boolean::class.javaPrimitiveType)
                    m.invoke(alg, true)
                } catch (e: Exception) {
                    // purane device par forceDark kaafi hai
                }
            }
        } else {
            webView.settings.forceDark = android.webkit.WebSettings.FORCE_DARK_OFF
        }
    }

    private fun toggleNightMode() {
        nightOn = !nightOn
        prefs.edit().putBoolean("night_on", nightOn).apply()
        applyNight(nightOn)
        Toast.makeText(this, if (nightOn) "Night mode ON" else "Night mode OFF", Toast.LENGTH_SHORT).show()
    }

    private fun changeTextSize(delta: Int) {
        textZoomLevel = (textZoomLevel + delta).coerceIn(70, 180)
        prefs.edit().putInt("text_zoom", textZoomLevel).apply()
        webView.settings.textZoom = textZoomLevel
        Toast.makeText(this, "Text size: " + textZoomLevel + "%", Toast.LENGTH_SHORT).show()
    }

    private fun clearAppCache() {
        try {
            webView.clearCache(true)
            webView.clearFormData()
            webView.clearHistory()
            Toast.makeText(this, "Cache clear ho gaya ✓ — page dobara load ho raha hai", Toast.LENGTH_SHORT).show()
            webView.reload()
        } catch (e: Exception) {
            Toast.makeText(this, "Cache clear nahi ho paya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takeScreenshot() {
        try {
            webView.invalidate()
            val w = webView.width.coerceAtLeast(1)
            val h = webView.height.coerceAtLeast(1)
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            webView.draw(canvas)
            val stamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US).format(java.util.Date())
            val safeApp = APP_NAME.replace(Regex("[^A-Za-z0-9_-]"), "_")
            val fileName = safeApp + "_" + stamp + ".png"
            var savedUri: Uri? = null
            if (Build.VERSION.SDK_INT >= 29) {
                val values = android.content.ContentValues()
                values.put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, fileName)
                values.put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/png")
                values.put(android.provider.MediaStore.Images.Media.IS_PENDING, 1)
                val dest = contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: throw IllegalStateException("save fail")
                contentResolver.openOutputStream(dest)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
                val done = android.content.ContentValues()
                done.put(android.provider.MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(dest, done, null, null)
                savedUri = dest
            } else {
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_NAME)
                if (!dir.exists()) dir.mkdirs()
                val outFile = File(dir, fileName)
                outFile.outputStream().use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
                MediaScannerConnection.scanFile(this, arrayOf(outFile.absolutePath), arrayOf("image/png"), null)
                savedUri = Uri.fromFile(outFile)
            }
            Toast.makeText(this, "Screenshot save ho gaya — Gallery me dekho", Toast.LENGTH_SHORT).show()
            try {
                val send = Intent(Intent.ACTION_SEND)
                send.setType("image/png")
                send.putExtra(Intent.EXTRA_STREAM, savedUri)
                send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(send, "Screenshot share karo"))
            } catch (e: Exception) {
                // save ho gaya — share optional hai
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Screenshot nahi le paya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareAppLink() {
        try {
            val send = Intent(Intent.ACTION_SEND)
            send.setType("text/plain")
            send.putExtra(Intent.EXTRA_SUBJECT, APP_NAME)
            send.putExtra(Intent.EXTRA_TEXT, APP_NAME + " app try karo! " + SITE_LABEL)
            startActivity(Intent.createChooser(send, "App share karo"))
        } catch (e: Exception) {
            Toast.makeText(this, "Share nahi ho paya", Toast.LENGTH_SHORT).show()
        }
    }

    // App ki asli APK file share — dost file kholte hi install kar sakta hai
    private fun shareApkNow() {
        try {
            val ai = packageManager.getApplicationInfo(packageName, 0)
            val srcFile = File(ai.sourceDir)
            val safe = APP_NAME.replace(Regex("[^A-Za-z0-9_-]"), "_").ifBlank { "MyApp" }
            val dest = File(cacheDir, safe + ".apk")
            Toast.makeText(this, "APK file taiyaar ho rahi hai...", Toast.LENGTH_SHORT).show()
            Thread {
                try {
                    srcFile.copyTo(dest, overwrite = true)
                    runOnUiThread {
                        try {
                            val uri = FileProvider.getUriForFile(this, packageName + ".fileprovider", dest)
                            val send = Intent(Intent.ACTION_SEND)
                            send.type = "application/vnd.android.package-archive"
                            send.putExtra(Intent.EXTRA_STREAM, uri)
                            send.putExtra(Intent.EXTRA_SUBJECT, APP_NAME + " app install karo")
                            send.putExtra(Intent.EXTRA_TEXT, APP_NAME + " ki app — file kholte hi install ho jayegi")
                            send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            startActivity(Intent.createChooser(send, APP_NAME + " ki APK share karo"))
                        } catch (e: Exception) {
                            shareAppLink()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread { shareAppLink() }
                }
            }.start()
        } catch (e: Exception) {
            shareAppLink()
        }
    }

    // — Padh ke sunao: page ka text Hindi/English voice me bolke padhta hai —
    private fun ensureTts() {
        if (tts == null) {
            tts = TextToSpeech(this) { status ->
                ttsReady = status == TextToSpeech.SUCCESS
                if (ttsReady) {
                    try {
                        val hi = Locale("hi", "IN")
                        if ((tts?.isLanguageAvailable(hi) ?: -2) >= 0) tts?.language = hi else tts?.language = Locale.US
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    private fun readAloud() {
        if (tts?.isSpeaking == true) {
            tts?.stop()
            Toast.makeText(this, "Padhana band ho gaya", Toast.LENGTH_SHORT).show()
            return
        }
        ensureTts()
        Toast.makeText(this, "Page bolke padha rahe hain...", Toast.LENGTH_SHORT).show()
        webView.evaluateJavascript("(document.body ? (document.body.innerText||'') : '').slice(0,4000)") { v ->
            val raw = v ?: ""
            val text = raw.removeSurrounding("\"").replace("\\n", " ").replace("\\t", " ").trim()
            if (!ttsReady || text.isBlank()) {
                Toast.makeText(this, "Awaaz taiyaar nahi hui ya page khaali hai", Toast.LENGTH_SHORT).show()
                return@evaluateJavascript
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "app_page")
        }
    }

    // — Bookmarks: page save karo aur baad me ek tap me kholo —
    private fun bookmarkEntries(): MutableList<String> {
        val raw = prefs.getString("bookmarks", "") ?: ""
        return raw.split("||").filter { it.isNotBlank() }.toMutableList()
    }

    private fun saveBookmarkEntries(list: List<String>) {
        prefs.edit().putString("bookmarks", list.take(30).joinToString("||")).apply()
    }

    private fun bookmarkCurrentPage() {
        val url = webView.url ?: HOME_URL
        webView.evaluateJavascript("(document.title||'').slice(0,80)") { t ->
            val name = (t ?: "").removeSurrounding("\"").replace("|", " ").ifBlank { url }
            val entry = url + "|" + name
            val list = bookmarkEntries()
            if (list.any { it.startsWith(url + "|") }) {
                Toast.makeText(this, "Ye page pehle se bookmark me hai", Toast.LENGTH_SHORT).show()
            } else {
                list.add(0, entry)
                saveBookmarkEntries(list)
                Toast.makeText(this, "Bookmark save ho gaya — Mere Bookmarks me dekho", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showBookmarksSheet() {
        val list = bookmarkEntries()
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        val sheet = LinearLayout(this)
        sheet.orientation = LinearLayout.VERTICAL
        val sheetBg = GradientDrawable()
        sheetBg.setColor(Color.WHITE)
        sheetBg.cornerRadius = dp(28).toFloat()
        sheet.background = sheetBg
        sheet.elevation = dp(18).toFloat()
        sheet.setPadding(dp(18), dp(10), dp(18), dp(18))
        val handle = View(this)
        val handleBg = GradientDrawable()
        handleBg.setColor(0xFFDCE1E8.toInt())
        handleBg.cornerRadius = dp(3).toFloat()
        handle.background = handleBg
        sheet.addView(handle, LinearLayout.LayoutParams(dp(44), dp(6)).also { it.gravity = Gravity.CENTER_HORIZONTAL })
        val bmTitle = TextView(this)
        bmTitle.text = "Mere Bookmarks"
        bmTitle.textSize = 18f
        bmTitle.typeface = Typeface.DEFAULT_BOLD
        bmTitle.setTextColor(0xFF111827.toInt())
        val bmLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        bmLp.topMargin = dp(10)
        sheet.addView(bmTitle, bmLp)
        val bmSub = TextView(this)
        bmSub.text = "jin pages ko tumne star kiya hai — tap karke kholo"
        bmSub.textSize = 11.5f
        bmSub.setTextColor(0xFF6B7280.toInt())
        sheet.addView(bmSub, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        val scroll = ScrollView(this)
        scroll.isVerticalScrollBarEnabled = false
        val listLayout = LinearLayout(this)
        listLayout.orientation = LinearLayout.VERTICAL
        scroll.addView(listLayout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        if (list.isEmpty()) {
            val empty = TextView(this)
            empty.text = "Abhi koi bookmark nahi — kisi page par Bookmark karo dabao"
            empty.textSize = 13f
            empty.setTextColor(0xFF6B7280.toInt())
            val eLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            eLp.topMargin = dp(14)
            listLayout.addView(empty, eLp)
        }
        list.forEach { entry ->
            val parts = entry.split("|", limit = 2)
            val url = parts.getOrNull(0) ?: return@forEach
            val name = parts.getOrNull(1) ?: url
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.gravity = Gravity.CENTER_VERTICAL
            val rowBg = GradientDrawable()
            rowBg.setColor(0xFFF6F8FB.toInt())
            rowBg.cornerRadius = dp(14).toFloat()
            row.background = rowBg
            row.setPadding(dp(12), dp(10), dp(8), dp(10))
            row.foreground = RippleDrawable(ColorStateList.valueOf(0x1F888888), null, null)
            val block = LinearLayout(this)
            block.orientation = LinearLayout.VERTICAL
            val nm = TextView(this)
            nm.text = name
            nm.textSize = 13.5f
            nm.typeface = Typeface.DEFAULT_BOLD
            nm.setTextColor(0xFF111827.toInt())
            nm.maxLines = 1
            block.addView(nm)
            val ur = TextView(this)
            ur.text = friendlyPath(url)
            ur.textSize = 10.5f
            ur.setTextColor(0xFF8A94A6.toInt())
            ur.maxLines = 1
            block.addView(ur)
            row.addView(block, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            val del = TextView(this)
            del.text = "✕"
            del.textSize = 14f
            del.typeface = Typeface.DEFAULT_BOLD
            del.setTextColor(0xFFB91C1C.toInt())
            del.gravity = Gravity.CENTER
            del.setPadding(dp(10), dp(6), dp(10), dp(6))
            del.setOnClickListener {
                saveBookmarkEntries(bookmarkEntries().filterNot { it == entry })
                dialog.dismiss()
                showBookmarksSheet()
            }
            row.addView(del)
            row.setOnClickListener {
                bounce(row)
                dialog.dismiss()
                webView.loadUrl(url)
            }
            val rLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            rLp.topMargin = dp(8)
            listLayout.addView(row, rLp)
        }
        sheet.addView(scroll, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))
        dialog.setContentView(sheet)
        dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.92).toInt(), (resources.displayMetrics.heightPixels * 0.62).toInt())
        dialog.show()
    }

    private fun emailSupportTo(to: String) {
        val target = to.trim()
        if (target.isBlank()) {
            Toast.makeText(this, "Email address khali hai — Builder me apna email daalo", Toast.LENGTH_LONG).show()
            return
        }
        try {
            val gm = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Uri.encode(target)))
            gm.setPackage("com.google.android.gm")
            gm.putExtra(Intent.EXTRA_SUBJECT, APP_NAME + " — app se message")
            gm.putExtra(Intent.EXTRA_TEXT, "Namaste,\n\n")
            startActivity(gm)
            return
        } catch (e: Exception) {
        }
        try {
            val send = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Uri.encode(target)))
            send.putExtra(Intent.EXTRA_EMAIL, arrayOf(target))
            send.putExtra(Intent.EXTRA_SUBJECT, APP_NAME + " — app se message")
            send.putExtra(Intent.EXTRA_TEXT, "Namaste,\n\n")
            startActivity(send)
            return
        } catch (e: Exception) {
        }
        try {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Uri.encode(target))), "Email bhejo"))
        } catch (e: Exception) {
            openExternal(Uri.parse("mailto:" + Uri.encode(target)))
        }
    }

    private fun emailSupport() {
        emailSupportTo(SUPPORT_EMAIL)
    }

    // — Direction: pehle Google Maps app, phir koi Maps app, phir browser — har haal me khulta hai —
    private fun openDirections(query: String, webFallback: Uri?) {
        var q = query.trim()
        if (q.isBlank()) q = MAP_QUERY.trim()
        if (q.isBlank()) {
            if (webFallback != null) openExternal(webFallback)
            else Toast.makeText(this, "Address set nahi hai — Builder me studio ka address daalo", Toast.LENGTH_LONG).show()
            return
        }
        val enc = Uri.encode(q)
        try {
            val maps = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + enc))
            maps.setPackage("com.google.android.apps.maps")
            if (maps.resolveActivity(packageManager) != null) {
                startActivity(maps)
                return
            }
        } catch (e: Exception) {
        }
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + enc)))
            return
        } catch (e: Exception) {
        }
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + enc)))
        } catch (e: Exception) {
            if (webFallback != null) openExternal(webFallback)
            else Toast.makeText(this, "Maps app nahi mila", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openDirectionsTool() {
        openDirections(MAP_QUERY, null)
    }

    // — Website ke Maps links (maps.google.com, maps.app.goo.gl, google.com/maps) ko Maps app me khole —
    private fun openMapsFromWeb(uri: Uri) {
        var q = ""
        try { q = uri.getQueryParameter("q") ?: "" } catch (e: Exception) { }
        if (q.isBlank()) { try { q = uri.getQueryParameter("query") ?: "" } catch (e: Exception) { } }
        if (q.isBlank()) { try { q = uri.lastPathSegment ?: "" } catch (e: Exception) { } }
        if (q.isNotBlank()) openDirections(q, uri)
        else openExternal(uri)
    }

    // — Call: seedha phone dialer khulta hai —
    private fun callSupport() {
        val num = CALL_NUMBER.trim()
        if (num.isBlank()) {
            Toast.makeText(this, "Phone number set nahi hai", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num)))
        } catch (e: Exception) {
            Toast.makeText(this, "Phone app nahi khula", Toast.LENGTH_SHORT).show()
        }
    }

    // — Page translate: site ka page Hindi me padho —
    private fun translatePage() {
        try {
            val cur = webView.url ?: HOME_URL
            val u = Uri.parse(cur)
            val host = u.host ?: throw IllegalStateException("no host")
            val path = u.encodedPath ?: "/"
            val query = u.query ?: ""
            val qs = if (query.isBlank()) "" else "?" + query
            val sep = if (qs.isBlank()) "?" else "&"
            val target = "https://" + host.replace(".", "-") + ".translate.goog" + path + qs + sep + "_x_tr_sl=auto&_x_tr_tl=hi&_x_tr_hl=hi"
            webView.loadUrl(target)
            Toast.makeText(this, "Page Hindi me translate ho raha hai...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Translate nahi ho paya", Toast.LENGTH_SHORT).show()
        }
    }

    // — Mera History: jo pages tumne khole — tap karke wapas jao —
    private fun showHistorySheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        val sheet = LinearLayout(this)
        sheet.orientation = LinearLayout.VERTICAL
        val sheetBg = GradientDrawable()
        sheetBg.setColor(Color.WHITE)
        sheetBg.cornerRadius = dp(28).toFloat()
        sheet.background = sheetBg
        sheet.elevation = dp(18).toFloat()
        sheet.setPadding(dp(18), dp(8), dp(18), dp(16))
        val handle = View(this)
        val handleBg = GradientDrawable()
        handleBg.setColor(0xFFE2E8F0.toInt())
        handleBg.cornerRadius = dp(4).toFloat()
        handle.background = handleBg
        sheet.addView(handle, LinearLayout.LayoutParams(dp(44), dp(5)).apply { gravity = Gravity.CENTER_HORIZONTAL; topMargin = dp(6) })
        val title = TextView(this)
        title.text = "Mera History"
        title.textSize = 20f
        title.typeface = Typeface.DEFAULT_BOLD
        title.setTextColor(0xFF0F172A.toInt())
        sheet.addView(title, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(10) })
        val sub = TextView(this)
        sub.text = "jo pages tumne khole — tap karke wapas jao"
        sub.textSize = 13f
        sub.setTextColor(0xFF64748B.toInt())
        sheet.addView(sub, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(2) })
        val clr = TextView(this)
        clr.text = "History saaf karo"
        clr.textSize = 12.5f
        clr.typeface = Typeface.DEFAULT_BOLD
        clr.setTextColor(0xFFDC2626.toInt())
        val clrBg = GradientDrawable()
        clrBg.setColor(0xFFFEE2E2.toInt())
        clrBg.cornerRadius = dp(12).toFloat()
        clr.background = clrBg
        clr.setPadding(dp(12), dp(6), dp(12), dp(6))
        clr.setOnClickListener {
            webView.clearHistory()
            dialog.dismiss()
            Toast.makeText(this, "History saaf ho gayi", Toast.LENGTH_SHORT).show()
        }
        sheet.addView(clr, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
        val scroll = ScrollView(this)
        scroll.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(10) }
        val list = LinearLayout(this)
        list.orientation = LinearLayout.VERTICAL
        scroll.addView(list)
        val hist = webView.copyBackForwardList()
        val cur = hist.currentIndex
        for (i in 0 until hist.size) {
            val item = hist.getItemAtIndex(i)
            val url = item.url ?: continue
            val name = if (item.title.isNullOrBlank()) friendlyPath(url) else item.title
            val row = LinearLayout(this)
            row.orientation = LinearLayout.VERTICAL
            val rowBg = GradientDrawable()
            rowBg.setColor(if (i == cur) 0xFFE8F1FF.toInt() else 0xFFF6F8FB.toInt())
            rowBg.cornerRadius = dp(14).toFloat()
            row.background = rowBg
            row.setPadding(dp(12), dp(10), dp(12), dp(10))
            val t1 = TextView(this)
            t1.text = name
            t1.textSize = 15f
            t1.typeface = Typeface.DEFAULT_BOLD
            t1.setTextColor(0xFF0F172A.toInt())
            t1.maxLines = 1
            t1.ellipsize = android.text.TextUtils.TruncateAt.END
            row.addView(t1)
            val t2 = TextView(this)
            t2.text = if (i == cur) "● abhi yahin ho" else friendlyPath(url)
            t2.textSize = 12f
            t2.setTextColor(if (i == cur) 0xFF2563EB.toInt() else 0xFF94A3B8.toInt())
            t2.maxLines = 1
            t2.ellipsize = android.text.TextUtils.TruncateAt.END
            row.addView(t2, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(2) })
            row.setOnClickListener {
                dialog.dismiss()
                if (i != cur) webView.goBackOrForward(i - cur)
            }
            list.addView(row, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
        }
        if (hist.size == 0) {
            val empty = TextView(this)
            empty.text = "Abhi koi history nahi bani"
            empty.textSize = 14f
            empty.setTextColor(0xFF94A3B8.toInt())
            list.addView(empty, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(12) })
        }
        sheet.addView(scroll)
        dialog.setContentView(sheet)
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setLayout((resources.displayMetrics.widthPixels * 0.92f).toInt(), (resources.displayMetrics.heightPixels * 0.60f).toInt())
            window.setGravity(Gravity.CENTER)
        }
        dialog.show()
    }

    // — Page navigation: peeche/aage jao + page offline save — Quick Tools menu ke andar —
    private fun goBackPage() {
        if (webView.canGoBack()) webView.goBack()
        else Toast.makeText(this, "Peeche aur kuch nahi hai", Toast.LENGTH_SHORT).show()
    }

    private fun goForwardPage() {
        if (webView.canGoForward()) webView.goForward()
        else Toast.makeText(this, "Aage aur kuch nahi hai", Toast.LENGTH_SHORT).show()
    }

    private fun savePageOffline() {
        val url = webView.url
        if (url.isNullOrBlank() || !url.startsWith("http")) {
            Toast.makeText(this, "Abhi koi page khula nahi hai", Toast.LENGTH_SHORT).show()
            return
        }
        startDownload(url, webView.settings.userAgentString, "", "text/html")
    }
    private fun findInPage() {
        val input = EditText(this)
        input.hint = "kya dhoondna hai?"
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setSingleLine(true)
        val wrap = FrameLayout(this)
        wrap.setPadding(dp(16), dp(10), dp(16), 0)
        wrap.addView(input, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        AlertDialog.Builder(this)
            .setTitle("Page me dhoondo")
            .setView(wrap)
            .setPositiveButton("Dhoondo") { _, _ ->
                val q = input.text.toString().trim()
                if (q.isNotEmpty()) {
                    webView.findAllAsync(q)
                    Toast.makeText(this, "shabd highlight ho gaya — page me dekho", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Band", null)
            .show()
    }

    // — Email: pehle Gmail app, phir koi bhi mail app, phir chooser — seedha message likhne ka screen khulta hai —

    private fun openAppSettings() {
        try {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)))
        } catch (e: Exception) {
            Toast.makeText(this, "App settings nahi khul payi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildMoreButton(): TextView {
        val btn = TextView(this)
        btn.text = "⋮"
        btn.textSize = 20f
        btn.setTextColor(Color.WHITE)
        btn.typeface = Typeface.DEFAULT_BOLD
        btn.gravity = Gravity.CENTER
        val bg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(accentColor, 0.35f), accentColor, shade(accentColor, -0.25f)))
        bg.shape = GradientDrawable.OVAL
        bg.setStroke(dp(2), Color.WHITE)
        btn.background = bg
        btn.elevation = dp(12).toFloat()
        moreBtn = btn
        btn.setOnClickListener { showToolsMenu() }
        return btn
    }

    private fun showToolsMenu() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)

        val sheet = LinearLayout(this)
        sheet.orientation = LinearLayout.VERTICAL
        val sheetBg = GradientDrawable()
        sheetBg.setColor(Color.WHITE)
        sheetBg.cornerRadius = dp(28).toFloat()
        sheet.background = sheetBg
        sheet.elevation = dp(18).toFloat()
        sheet.setPadding(dp(18), dp(10), dp(18), dp(18))

        val handle = View(this)
        val handleBg = GradientDrawable()
        handleBg.setColor(0xFFDCE1E8.toInt())
        handleBg.cornerRadius = dp(3).toFloat()
        handle.background = handleBg
        val handleLp = LinearLayout.LayoutParams(dp(44), dp(6))
        handleLp.gravity = Gravity.CENTER_HORIZONTAL
        sheet.addView(handle, handleLp)

        val strip = View(this)
        val stripBg = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(shade(accentColor, 0.45f), accentColor, shade(accentColor, -0.35f)))
        stripBg.cornerRadius = dp(4).toFloat()
        strip.background = stripBg
        val stripLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(5))
        stripLp.topMargin = dp(8)
        sheet.addView(strip, stripLp)

        // Hero header — gradient banner, white text, round white close
        val header = LinearLayout(this)
        header.orientation = LinearLayout.HORIZONTAL
        header.gravity = Gravity.CENTER_VERTICAL
        val heroBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(accentColor, 0.45f), accentColor, shade(accentColor, -0.35f)))
        heroBg.cornerRadius = dp(20).toFloat()
        header.background = heroBg
        header.elevation = dp(4).toFloat()
        header.setPadding(dp(14), dp(12), dp(12), dp(12))
        val headLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        headLp.topMargin = dp(10)
        headLp.bottomMargin = dp(2)
        sheet.addView(header, headLp)

        val headBlock = LinearLayout(this)
        headBlock.orientation = LinearLayout.VERTICAL
        val sheetTitle = TextView(this)
        sheetTitle.text = "⚡ Quick Tools"
        sheetTitle.textSize = 20f
        sheetTitle.typeface = Typeface.DEFAULT_BOLD
        sheetTitle.setTextColor(Color.WHITE)
        headBlock.addView(sheetTitle)
        val sheetSub = TextView(this)
        sheetSub.text = SITE_LABEL + " ke kaam ke tools"
        sheetSub.textSize = 12f
        sheetSub.setTextColor(0xE6FFFFFF.toInt())
        headBlock.addView(sheetSub)
        header.addView(headBlock, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

        val close = TextView(this)
        close.text = "✕"
        close.textSize = 15f
        close.typeface = Typeface.DEFAULT_BOLD
        close.setTextColor(accentColor)
        close.gravity = Gravity.CENTER
        val closeBg = GradientDrawable()
        closeBg.shape = GradientDrawable.OVAL
        closeBg.setColor(Color.WHITE)
        close.background = closeBg
        close.elevation = dp(2).toFloat()
        close.setOnClickListener { dialog.dismiss() }
        header.addView(close, LinearLayout.LayoutParams(dp(34), dp(34)))

        // — Tool dhoondo box: likhte hi list neeche filter hoti hai —
        val search = EditText(this)
        search.hint = "Tool dhoondo — jaise: night, pdf, scroll"
        search.textSize = 13f
        search.setSingleLine(true)
        search.inputType = InputType.TYPE_CLASS_TEXT
        val searchBg = GradientDrawable()
        searchBg.setColor(0xFFF1F4F9.toInt())
        searchBg.cornerRadius = dp(14).toFloat()
        search.background = searchBg
        search.setPadding(dp(14), dp(10), dp(14), dp(10))
        search.elevation = dp(1).toFloat()
        sheet.addView(search, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(10) })

        // Scrollable area — jitne bhi tools hon, sab yahan scroll karke dikhte hain
        val scroll = ScrollView(this)
        scroll.isVerticalScrollBarEnabled = false
        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        scroll.addView(content, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val items = buildToolItems()
        fun renderTools(filterRaw: String) {
            content.removeAllViews()
            val filter = filterRaw.trim().lowercase()
            val shown = items.filter { filter.isEmpty() || it.label.lowercase().contains(filter) || it.sub.lowercase().contains(filter) || it.section.lowercase().contains(filter) }
            if (shown.isEmpty()) {
                val none = TextView(this)
                none.text = "Koi tool nahi mila — kuch aur likho"
                none.textSize = 12.5f
                none.gravity = Gravity.CENTER
                none.setTextColor(0xFF9CA3AF.toInt())
                none.setPadding(0, dp(18), 0, dp(18))
                content.addView(none, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                return
            }
            var lastSection = ""
            shown.forEachIndexed { index, item ->
                if (item.section != lastSection) {
                    lastSection = item.section
                    val secHead = LinearLayout(this)
                    secHead.orientation = LinearLayout.HORIZONTAL
                    secHead.gravity = Gravity.CENTER_VERTICAL
                    val dot = View(this)
                    val dotBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(accentColor, 0.35f), accentColor))
                    dotBg.shape = GradientDrawable.OVAL
                    dot.background = dotBg
                    secHead.addView(dot, LinearLayout.LayoutParams(dp(7), dp(7)))
                    val st = TextView(this)
                    st.text = lastSection.uppercase()
                    st.textSize = 10.5f
                    st.typeface = Typeface.DEFAULT_BOLD
                    st.letterSpacing = 0.10f
                    st.setTextColor(0xFF4B5563.toInt())
                    secHead.addView(st, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { leftMargin = dp(6) })
                    val cnt = TextView(this)
                    cnt.text = "· " + shown.count { it.section == lastSection }
                    cnt.textSize = 10f
                    cnt.typeface = Typeface.DEFAULT_BOLD
                    cnt.setTextColor(0xFF9CA3AF.toInt())
                    secHead.addView(cnt, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { leftMargin = dp(8) })
                    val stLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    stLp.topMargin = if (index == 0) dp(4) else dp(13)
                    stLp.bottomMargin = dp(7)
                    content.addView(secHead, stLp)
                }
                val toolRow = buildToolRow(dialog, item)
                val rowLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                rowLp.bottomMargin = dp(8)
                content.addView(toolRow, rowLp)
            }
        }
        renderTools("")
        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) { renderTools(s?.toString() ?: "") }
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })
        // Scroll height fix — sheet screen se bahar na jaye, neeche ke options bhi poore dikhen
        val screenH = resources.displayMetrics.heightPixels
        val maxScroll = (screenH * 0.60f).toInt()
        content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val scrollH = kotlin.math.min(content.measuredHeight, maxScroll)
        sheet.addView(scroll, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, scrollH))

        dialog.setContentView(sheet)
        val win = dialog.window
        if (win != null) {
            win.setBackgroundDrawableResource(android.R.color.transparent)
            win.setGravity(Gravity.BOTTOM)
            win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        sheet.translationY = dp(320).toFloat()
        sheet.alpha = 0f
        dialog.show()
        sheet.animate().translationY(0f).alpha(1f).setDuration(300).setInterpolator(OvershootInterpolator(1.05f)).start()
    }

    // — Text copy: poore page ka likha hua text clipboard me —
    private fun jsonUnquote(v: String?): String {
        if (v == null) return ""
        var s = v.trim()
        if (s.length >= 2 && s.startsWith("\"") && s.endsWith("\"")) s = s.substring(1, s.length - 1)
        val sb = StringBuilder()
        var i = 0
        while (i < s.length) {
            if (s[i] == '\\' && i + 1 < s.length) {
                when (s[i + 1]) {
                    'n' -> { sb.append('\n'); i += 2 }
                    't' -> { sb.append('\t'); i += 2 }
                    'r' -> { sb.append('\r'); i += 2 }
                    '"' -> { sb.append('"'); i += 2 }
                    '\\' -> { sb.append('\\'); i += 2 }
                    '/' -> { sb.append('/'); i += 2 }
                    else -> { sb.append(s[i]); i += 1 }
                }
            } else { sb.append(s[i]); i += 1 }
        }
        return sb.toString()
    }

    private fun copyPageText() {
        webView.evaluateJavascript("(function(){return document.body ? document.body.innerText.substring(0, 15000) : ''})()", object : android.webkit.ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                val txt = jsonUnquote(value)
                runOnUiThread {
                    if (txt.isBlank()) {
                        Toast.makeText(this@MainActivity, "Copy karne layak text nahi mila", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            cm.setPrimaryClip(android.content.ClipData.newPlainText("page text", txt))
                            Toast.makeText(this@MainActivity, "Page ka text copy ho gaya", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@MainActivity, "Copy nahi ho paya", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    // — Font style: page ka lekha serif ya bade akshar —
    private fun fontStyleDialog() {
        val styles = arrayOf("Normal style", "Padhai style (serif)", "Bade akshar style")
        AlertDialog.Builder(this)
            .setTitle("Font style badlo")
            .setMessage("Page ke lekhe ka style chuno")
            .setItems(styles) { _, which ->
                val js = when (which) {
                    1 -> "(function(){document.body.style.fontFamily='serif';document.body.style.lineHeight='1.8'})()"
                    2 -> "(function(){document.body.style.fontFamily='sans-serif';document.body.style.lineHeight='1.6';document.body.style.fontSize='1.12em'})()"
                    else -> "(function(){document.body.style.fontFamily='';document.body.style.lineHeight='';document.body.style.fontSize=''})()"
                }
                webView.evaluateJavascript(js, null)
                Toast.makeText(this, "Font style lag gaya", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Band", null)
            .show()
    }

    // — Auto refresh: page har 30 second me khud update —
    private var autoRefreshOn = false
    private var autoRefreshTimer: android.os.Handler? = null

    private fun toggleAutoRefresh() {
        if (autoRefreshOn) {
            autoRefreshTimer?.removeCallbacksAndMessages(null)
            autoRefreshTimer = null
            autoRefreshOn = false
            Toast.makeText(this, "Auto refresh band ho gaya", Toast.LENGTH_SHORT).show()
        } else {
            val h = android.os.Handler(android.os.Looper.getMainLooper())
            val task = object : Runnable {
                override fun run() {
                    webView.reload()
                    h.postDelayed(this, 30000)
                }
            }
            h.postDelayed(task, 30000)
            autoRefreshTimer = h
            autoRefreshOn = true
            Toast.makeText(this, "Auto refresh chalu — har 30 second page khud update hoga", Toast.LENGTH_LONG).show()
        }
    }

    // — Photo save: page par di hui photos me se chun kar download —
    private fun showImagesSheet() {
        webView.evaluateJavascript("(function(){var a=[];var els=document.querySelectorAll('img');for(var i=0;i<els.length&&a.length<14;i++){var u=els[i].currentSrc||els[i].src||'';if(u&&u.indexOf('http')===0){a.push(u)}}return JSON.stringify(a)})()", object : android.webkit.ValueCallback<String> {
            override fun onReceiveValue(value: String?) {
                val raw = jsonUnquote(value)
                runOnUiThread {
                    val urls = try {
                        val l = org.json.JSONArray(raw)
                        (0 until l.length()).map { l.getString(it) }
                    } catch (e: Exception) { emptyList() }
                    if (urls.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Is page par koi photo nahi mili", Toast.LENGTH_SHORT).show()
                    } else {
                        val names = urls.map { u -> u.substringAfterLast('/').substringBefore('?').ifBlank { "photo" } }.toTypedArray()
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Photo save karo")
                            .setMessage("Jo photo chahiye use dabao — download ho jayegi")
                            .setItems(names) { _, which -> startDownload(urls[which], webView.settings.userAgentString, "attachment", "image/*") }
                            .setNegativeButton("Band", null)
                            .show()
                    }
                }
            }
        })
    }

    // — QR banao: is page ka QR — dusre phone se scan karke kholo —
    private fun showQrDialog() {
        val url = webView.url ?: HOME_URL
        val enc = java.net.URLEncoder.encode(url, "UTF-8")
        val iv = ImageView(this)
        iv.adjustViewBounds = true
        iv.maxHeight = dp(360)
        val hint = TextView(this)
        hint.text = "Is page ka QR — scan karke kholo · " + friendlyPath(url)
        hint.textSize = 13f
        hint.setPadding(dp(20), dp(16), dp(20), 0)
        hint.setTextColor(0xFF64748B.toInt())
        Thread {
            try {
                val conn = java.net.URL("https://api.qrserver.com/v1/create-qr-code/?size=420x420&data=" + enc).openConnection() as java.net.HttpURLConnection
                conn.connectTimeout = 12000
                conn.readTimeout = 12000
                val bmp = android.graphics.BitmapFactory.decodeStream(conn.inputStream)
                runOnUiThread { if (bmp != null) iv.setImageBitmap(bmp) else hint.text = "QR load nahi hua — internet check karo" }
            } catch (e: Exception) {
                runOnUiThread { hint.text = "QR load nahi hua — internet check karo" }
            }
        }.start()
        val wrap = LinearLayout(this)
        wrap.orientation = LinearLayout.VERTICAL
        wrap.addView(hint)
        wrap.addView(iv, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        AlertDialog.Builder(this)
            .setTitle("QR banao")
            .setView(wrap)
            .setPositiveButton("Ho gaya", null)
            .show()
    }
    // — Internet bachao: cache se kholo + trackers band — data ka kharch bahut kam —
    private fun toggleDataSaver() {
        dataSaverCacheOn = !dataSaverCacheOn
        webView.settings.cacheMode = if (dataSaverCacheOn) WebSettings.LOAD_CACHE_ELSE_NETWORK else WebSettings.LOAD_DEFAULT
        if (dataSaverCacheOn) webView.evaluateJavascript(DATA_SAVER_JS, null)
        Toast.makeText(this, if (dataSaverCacheOn) "Internet bachao on — cache se khulega, data kam lagega" else "Internet bachao off — normal mode", Toast.LENGTH_SHORT).show()
    }

    // — Video autoplay band: video khud nahi chalega (tool se on/off) —
    private fun toggleVideoBlock() {
        videoBlockOn = !videoBlockOn
        if (videoBlockOn) webView.evaluateJavascript(VIDEO_BLOCK_JS, null)
        else webView.evaluateJavascript("(function(){try{if(window.__rbVidObs){window.__rbVidObs.disconnect();window.__rbVidObs=null}}catch(e){}})()", null)
        Toast.makeText(this, if (videoBlockOn) "Video autoplay band — video khud nahi chalega" else "Video autoplay chalu", Toast.LENGTH_SHORT).show()
    }

    // — Ads band karo: app me hi on/off —
    private fun toggleAdBlock() {
        adBlockOn = !adBlockOn
        if (adBlockOn) injectAdBlock(webView)
        else webView.evaluateJavascript("(function(){try{document.querySelectorAll('[data-w2a-ad]').forEach(function(e){e.style.display=''})}catch(e){}})()", null)
        Toast.makeText(this, if (adBlockOn) "Ads band on — banner ads chhupenge" else "Ads dikhne lagenge", Toast.LENGTH_SHORT).show()
    }

    // — Page share: khula page WhatsApp/link par bhejo —
    private fun shareCurrentPage() {
        val url = webView.url ?: HOME_URL
        val send = Intent(Intent.ACTION_SEND)
        send.type = "text/plain"
        send.putExtra(Intent.EXTRA_TEXT, APP_NAME + " — " + SITE_LABEL + "\n" + url)
        try {
            startActivity(Intent.createChooser(send, "Page share karo"))
        } catch (e: Exception) {
            Toast.makeText(this, "Share karne wali app nahi mili", Toast.LENGTH_SHORT).show()
        }
    }

    // Saare Quick Tools — sections me grouped: Padhai sabse upar, phir Screen, Page, Files, App, Madad
    private fun buildToolItems(): List<ToolItem> {
        val items = mutableListOf<ToolItem>()
        if (FIND_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_find, "Page me dhoondo", "shabd turant milenge", 0xFF0891B2.toInt(), "Padhai ke Tools") { findInPage() })
        if (READ_ALOUD_ON) items.add(ToolItem(R.drawable.ic_tool_speaker, "Padh ke sunao", "page bolke padhega", 0xFF6D28D9.toInt(), "Padhai ke Tools") { readAloud() })
        if (TTS_SPEED_ON) items.add(ToolItem(R.drawable.ic_tool_speed, "Sunne ki raftaar", "awaaz tez/dheemi", 0xFF4F46E5.toInt(), "Padhai ke Tools") { ttsSpeedDialog() })
        if (TRANSLATE_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_translate, "Hindi me padho", "page translate", 0xFF0F766E.toInt(), "Padhai ke Tools") { translatePage() })
        if (DICT_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_dict, "Shabd ka matlab", "meaning turant", 0xFF7C2D12.toInt(), "Padhai ke Tools") { dictDialog() })
        if (SITE_SEARCH_ON) items.add(ToolItem(R.drawable.ic_tool_sitesearch, "Site me dhoondo", "Google se isi site me", 0xFF0E7490.toInt(), "Padhai ke Tools") { siteSearchDialog() })
        if (COPY_TEXT_ON) items.add(ToolItem(R.drawable.ic_tool_copy, "Text copy karo", "poora page ka likha", 0xFF4338CA.toInt(), "Padhai ke Tools") { copyPageText() })
        if (FONT_STYLE_ON) items.add(ToolItem(R.drawable.ic_tool_fontstyle, "Font style badlo", "lekhe ka style", 0xFF9F1239.toInt(), "Padhai ke Tools") { fontStyleDialog() })
        items.add(ToolItem(R.drawable.ic_tool_textgrow, "Text bada karo", "padhna aasan", 0xFF0EA5E9.toInt(), "Padhai ke Tools") { changeTextSize(15) })
        items.add(ToolItem(R.drawable.ic_tool_textgrow, "Text chhota karo", "compact view", 0xFF38BDF8.toInt(), "Padhai ke Tools") { changeTextSize(-15) })
        if (READING_MODE_ON) items.add(ToolItem(R.drawable.ic_tool_bookopen, if (readingModeOn) "Padhai mode band" else "Padhai mode on", "sirf text, aaram se", 0xFFB45309.toInt(), "Padhai ke Tools") { toggleReadingMode() })
        if (BOOKMARKS_ON) items.add(ToolItem(R.drawable.ic_tool_star, "Bookmark karo", "page save karo", 0xFFEAB308.toInt(), "Padhai ke Tools") { bookmarkCurrentPage() })
        if (BOOKMARKS_ON) items.add(ToolItem(R.drawable.ic_tool_book, "Mere Bookmarks", "save kiye page", 0xFFD97706.toInt(), "Padhai ke Tools") { showBookmarksSheet() })
        if (OFFLINE_SAVE_ON) items.add(ToolItem(R.drawable.ic_tool_save, "Page save karo", "offline padho baad me", 0xFF0369A1.toInt(), "Padhai ke Tools") { savePageOffline() })
        if (AUTOSCROLL_ON) items.add(ToolItem(R.drawable.ic_tool_autoscroll, "Auto scroll karo", "page khud chalega", 0xFF92400E.toInt(), "Padhai ke Tools") { autoScrollDialog() })
        if (KEEPCR_ON) items.add(ToolItem(R.drawable.ic_tool_screenon, if ((window.attributes.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0) "Screen band hone do" else "Screen band na ho", "padhai/video me jalta rahe", 0xFF0D9488.toInt(), "Screen ke Tools") { toggleKeepScreenOn() })
        if (BRIGHTNESS_ON) items.add(ToolItem(R.drawable.ic_tool_brightness, "Roshni set karo", "screen ki brightness", 0xFFCA8A04.toInt(), "Screen ke Tools") { showBrightnessDialog() })
        items.add(ToolItem(if (nightOn) R.drawable.ic_tool_sun else R.drawable.ic_tool_moon, if (nightOn) "Day mode karo" else "Night mode karo", "aankhon ko aaram", 0xFF1E293B.toInt(), "Screen ke Tools") { toggleNightMode() })
        if (MUTE_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_mute, if (muted) "Aawaz chalu karo" else "Aawaz band karo", "site ki awaaz", 0xFFDB2777.toInt(), "Screen ke Tools") { toggleMute() })
        if (DATA_SAVE_ON) items.add(ToolItem(R.drawable.ic_tool_datasave, if (dataSaveOn) "Photo chalu karo" else "Data save karo", "photo band, data bacho", 0xFF059669.toInt(), "Screen ke Tools") { toggleDataSave() })
        if (VIDEO_BLOCK_ON) items.add(ToolItem(R.drawable.ic_tool_videoblock, if (videoBlockOn) "Video autoplay chalu" else "Video autoplay band", "video khud na chale, data bacho", 0xFF0F766E.toInt(), "Screen ke Tools") { toggleVideoBlock() })
        if (ADBLOCK_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_adblock, if (adBlockOn) "Ads chalu karo" else "Ads band karo", "banner ads chhupao", 0xFFB91C1C.toInt(), "Screen ke Tools") { toggleAdBlock() })
        if (SCREENSHOT_ON) items.add(ToolItem(R.drawable.ic_tool_camera, "Screenshot lo", "page ki photo", 0xFFDC2626.toInt(), "Screen ke Tools") { takeScreenshot() })
        if (FULLSCREEN_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_fullscreen, if (fullScreenOn) "Full screen band" else "Full screen karo", "poora screen app ka", 0xFF7C3AED.toInt(), "Screen ke Tools") { toggleFullScreen() })
        if (DESKTOP_VIEW_ON) items.add(ToolItem(R.drawable.ic_tool_monitor, if (desktopView) "Mobile view karo" else "Desktop view karo", "poori site desktop mode", 0xFF6366F1.toInt(), "Screen ke Tools") { toggleDesktopView() })
        if (THEME_PICKER_ON) items.add(ToolItem(R.drawable.ic_tool_palette, "Theme badlo", "rang turant badlo", 0xFFEC4899.toInt(), "Screen ke Tools") { showThemeSheet() })
        if (GO_TOP_ON) items.add(ToolItem(R.drawable.ic_tool_arrowup, "Top par jao", "seedha page ke upar", 0xFFF59E0B.toInt(), "Screen ke Tools") { goToTop() })
        items.add(ToolItem(R.drawable.ic_tool_back, "Peeche jao", "pichla page", 0xFF334155.toInt(), "Page ke Tools") { goBackPage() })
        items.add(ToolItem(R.drawable.ic_tool_forward, "Aage jao", "agla page", 0xFF475569.toInt(), "Page ke Tools") { goForwardPage() })
        items.add(ToolItem(R.drawable.ic_tool_refresh, "Refresh page", "dobara load", 0xFF2563EB.toInt(), "Page ke Tools") { webView.reload() })
        items.add(ToolItem(R.drawable.ic_tool_home, "Home page", "shuruati page", 0xFF111827.toInt(), "Page ke Tools") { webView.loadUrl(HOME_URL) })
        if (URL_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_globe, "Kholo (URL likho)", "seedha page kholo", 0xFF0284C7.toInt(), "Page ke Tools") { openUrlDialog() })
        if (AUTO_REFRESH_ON) items.add(ToolItem(R.drawable.ic_tool_autorefresh, if (autoRefreshOn) "Auto refresh band" else "Auto refresh karo", "har 30s update", 0xFF3F6212.toInt(), "Page ke Tools") { toggleAutoRefresh() })
        if (QR_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_qr, "QR banao", "page ka QR code", 0xFF374151.toInt(), "Page ke Tools") { showQrDialog() })
        if (PAGE_SHARE_ON) items.add(ToolItem(R.drawable.ic_tool_share, "Page share karo", "page ka link bhejo", 0xFF2563EB.toInt(), "Page ke Tools") { shareCurrentPage() })
        if (HISTORY_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_history, "Mera History", "khole hue pages", 0xFF64748B.toInt(), "Page ke Tools") { showHistorySheet() })
        items.add(ToolItem(R.drawable.ic_tool_print, "Print / PDF", "page ya PDF banao", 0xFF111827.toInt(), "Files aur Print") { printPage() })
        if (DL_LIST_ON) items.add(ToolItem(R.drawable.ic_tool_download, "Mere Downloads", "app ki hi list", 0xFF10B981.toInt(), "Files aur Print") { showDownloadsSheet() })
        items.add(ToolItem(R.drawable.ic_tool_folder, "Downloads folder", "phone ka folder", 0xFF059669.toInt(), "Files aur Print") { openDownloads() })
        if (IMAGE_SAVE_ON) items.add(ToolItem(R.drawable.ic_tool_images, "Photo save karo", "page ki photo chuno", 0xFF86198F.toInt(), "Files aur Print") { showImagesSheet() })
        if (SHARE_APP_ON) items.add(ToolItem(R.drawable.ic_tool_share, "App share karo", "asli APK file bhejo", 0xFF8B5CF6.toInt(), "App") { shareApkNow() })
        if (DATA_SAVER_ON) items.add(ToolItem(R.drawable.ic_tool_internetbachao, if (dataSaverCacheOn) "Internet bachao band" else "Internet bachao", "cache se kholo, data kam", 0xFF15803D.toInt(), "App") { toggleDataSaver() })
        if (CLEAR_CACHE_ON) items.add(ToolItem(R.drawable.ic_tool_broom, "Cache clear", "speed badhao", 0xFFF97316.toInt(), "App") { clearAppCache() })
        if (COOKIES_CLEAR_ON) items.add(ToolItem(R.drawable.ic_tool_cookie, "Cookies clear", "login data saaf", 0xFFB45309.toInt(), "App") { clearCookiesNow() })
        if (APPINFO_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_info, "App ki jaankari", "version aur settings", 0xFF475569.toInt(), "Madad") { openAppSettings() })
        return items
    }



    // — Tool row: poora chauda row — icon chip + naam + sub + arrow. Poora naam ek line me — text kabhi nahi katta —
    private fun buildToolRow(dialog: Dialog, item: ToolItem): View {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL
        row.setPadding(dp(10), dp(9), dp(12), dp(9))
        val rowBg = GradientDrawable()
        rowBg.setColor(0xFFF7F9FC.toInt())
        rowBg.cornerRadius = dp(16).toFloat()
        rowBg.setStroke(dp(1), shade(item.accent, 0.90f))
        row.background = RippleDrawable(ColorStateList.valueOf(shade(item.accent, 0.86f)), rowBg, null)
        row.elevation = dp(2).toFloat()

        val chip = FrameLayout(this)
        val chipBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(item.accent, 0.30f), item.accent))
        chipBg.cornerRadius = dp(13).toFloat()
        chip.background = chipBg
        chip.elevation = dp(3).toFloat()
        val icon = ImageView(this)
        icon.setImageResource(item.icon)
        chip.addView(icon, FrameLayout.LayoutParams(dp(21), dp(21), Gravity.CENTER))
        row.addView(chip, LinearLayout.LayoutParams(dp(42), dp(42)))

        val textBlock = LinearLayout(this)
        textBlock.orientation = LinearLayout.VERTICAL
        val label = TextView(this)
        label.text = item.label
        label.textSize = 13.5f
        label.typeface = Typeface.DEFAULT_BOLD
        label.setTextColor(0xFF111827.toInt())
        label.maxLines = 1
        textBlock.addView(label, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        val sub = TextView(this)
        sub.text = item.sub
        sub.textSize = 11f
        sub.setTextColor(0xFF6B7280.toInt())
        sub.maxLines = 1
        textBlock.addView(sub, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        row.addView(textBlock, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply { leftMargin = dp(11) })

        val arrow = TextView(this)
        arrow.text = "›"
        arrow.textSize = 18f
        arrow.typeface = Typeface.DEFAULT_BOLD
        arrow.setTextColor(shade(item.accent, 0.55f))
        row.addView(arrow, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { leftMargin = dp(4) })

        row.setOnClickListener {
            bounce(row)
            dialog.dismiss()
            item.action()
        }
        return row
    }


    // — Screen band na ho: padhai/video dekhte waqt screen jalta rahe —
    private fun toggleKeepScreenOn() {
        val on = (window.attributes.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0
        if (on) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            Toast.makeText(this, "Screen ab wapas band ho sakta hai", Toast.LENGTH_SHORT).show()
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            Toast.makeText(this, "Screen jalta rahega — band nahi hoga", Toast.LENGTH_SHORT).show()
        }
    }

    // — Roshni (brightness): slider ghumao, aankhon ko aaram —
    private fun showBrightnessDialog() {
        val cur = window.attributes.screenBrightness
        val start = ((if (cur < 0f) 0.6f else cur) * 100).toInt().coerceIn(5, 100)
        val seek = android.widget.SeekBar(this)
        seek.max = 100
        seek.progress = start
        seek.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: android.widget.SeekBar?, p: Int, fromUser: Boolean) {
                val lp = window.attributes
                lp.screenBrightness = (p / 100f).coerceAtLeast(0.06f)
                window.attributes = lp
            }
            override fun onStartTrackingTouch(s: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(s: android.widget.SeekBar?) {}
        })
        val wrap = LinearLayout(this)
        wrap.orientation = LinearLayout.VERTICAL
        wrap.setPadding(dp(20), dp(6), dp(20), 0)
        wrap.addView(seek, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        AlertDialog.Builder(this)
            .setTitle("Roshni set karo")
            .setMessage("Slider ghumao — screen ki roshni turant badlegi")
            .setView(wrap)
            .setPositiveButton("Ho gaya", null)
            .show()
    }

    // — Auto scroll: page khud dheere-dheere chalega, aaram se padho —
    private fun autoScrollDialog() {
        val speeds = arrayOf("Dheema — aaram se", "Normal", "Tez — fast")
        AlertDialog.Builder(this)
            .setTitle("Auto scroll")
            .setMessage("Speed chuno — page khud scroll karega")
            .setItems(speeds) { _, which ->
                val px = intArrayOf(2, 3, 6)[which]
                webView.evaluateJavascript("(function(){if(window.__rbScroll){clearInterval(window.__rbScroll)}window.__rbScroll=setInterval(function(){window.scrollBy(0," + px + ");if((window.innerHeight+window.scrollY)>=document.body.scrollHeight){clearInterval(window.__rbScroll);window.__rbScroll=null}},50)})()", null)
                Toast.makeText(this, "Auto scroll chalu — rokne ke liye dobara yahi tool dabao", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Scroll roko") { _, _ ->
                webView.evaluateJavascript("(function(){if(window.__rbScroll){clearInterval(window.__rbScroll);window.__rbScroll=null}})()", null)
                Toast.makeText(this, "Auto scroll band ho gaya", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
    // — URL se sirf path — Bookmarks/History me site ka domain (jaise rbstudiosmusic.kliv.site) kabhi nahi dikhta —
    private fun friendlyPath(raw: String): String {
        var p = raw
        val scheme = p.indexOf("://")
        if (scheme >= 0) p = p.substring(scheme + 3)
        val slash = p.indexOf("/")
        val path = if (slash >= 0) p.substring(slash) else "/"
        val clean = path.substringBefore('?').substringBefore('#').trimEnd('/')
        return if (clean.isBlank()) "Home page" else clean
    }

    // — Site me dhoondo: Google se sirf is site ke andar search —
    private fun siteSearchDialog() {
        val input = EditText(this)
        input.hint = "kya dhoondna hai is site me?"
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setSingleLine(true)
        val wrap = FrameLayout(this)
        wrap.setPadding(dp(16), dp(10), dp(16), 0)
        wrap.addView(input, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        AlertDialog.Builder(this)
            .setTitle("Site me dhoondo")
            .setView(wrap)
            .setPositiveButton("Dhoondo") { _, _ ->
                val q = input.text.toString().trim()
                if (q.isNotEmpty()) {
                    val host = Uri.parse(HOME_URL).host ?: ""
                    webView.loadUrl("https://www.google.com/search?q=" + java.net.URLEncoder.encode(q, "UTF-8") + "&as_sitesearch=" + host)
                }
            }
            .setNegativeButton("Band", null)
            .show()
    }

    // — Shabd ka matlab: kisi bhi shabd ka Hindi meaning —
    private fun dictDialog() {
        val input = EditText(this)
        input.hint = "shabd likho — matlab milega"
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setSingleLine(true)
        val wrap = FrameLayout(this)
        wrap.setPadding(dp(16), dp(10), dp(16), 0)
        wrap.addView(input, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        AlertDialog.Builder(this)
            .setTitle("Shabd ka matlab")
            .setView(wrap)
            .setPositiveButton("Dekho") { _, _ ->
                val q = input.text.toString().trim()
                if (q.isNotEmpty()) {
                    webView.loadUrl("https://www.google.com/search?q=" + java.net.URLEncoder.encode(q + " meaning in hindi", "UTF-8"))
                }
            }
            .setNegativeButton("Band", null)
            .show()
    }

    // — Sunne ki raftaar: Padh ke sunao ki awaaz dheemi/tez —
    private fun ttsSpeedDialog() {
        val speeds = arrayOf("Dheemi — 0.75x", "Normal — 1x", "Tez — 1.25x", "Bahut tez — 1.5x")
        AlertDialog.Builder(this)
            .setTitle("Sunne ki raftaar")
            .setMessage("Padh ke sunao wali awaaz ki speed chuno")
            .setItems(speeds) { _, which ->
                val rate = floatArrayOf(0.75f, 1f, 1.25f, 1.5f)[which]
                tts?.setSpeechRate(rate)
                Toast.makeText(this, "Awaaz ki raftaar set ho gayi", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    // — Cookies clear: login data saaf karke privacy —
    private fun clearCookiesNow() {
        try {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
            Toast.makeText(this, "Cookies saaf ho gaye — privacy lock lag gaya", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Cookies clear nahi ho paye", Toast.LENGTH_SHORT).show()
        }
    }

    // — Padhai mode: sirf text — image, video, ad sab chhup jaate hain — aaram se padho —
    private fun toggleReadingMode() {
        readingModeOn = !readingModeOn
        if (readingModeOn) {
            webView.evaluateJavascript("(function(){var s=document.getElementById('rb-read');if(!s){s=document.createElement('style');s.id='rb-read';s.textContent='img,video,iframe,svg,canvas{display:none!important}body{background:#FFFFFF!important}body,body *{background-image:none!important;color:#222222!important;line-height:1.85!important;font-family:Georgia,serif!important}';document.head.appendChild(s)}})()", null)
            Toast.makeText(this, "Padhai mode on — sirf text dikhega", Toast.LENGTH_SHORT).show()
        } else {
            webView.evaluateJavascript("(function(){var s=document.getElementById('rb-read');if(s){s.remove()}})()", null)
            Toast.makeText(this, "Padhai mode band", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleMute() {
        muted = !muted
        if (muted) {
            webView.evaluateJavascript("(function(){var m=function(){try{document.querySelectorAll('video,audio').forEach(function(e){e.muted=true})}catch(_){}};m();window.__rbMuteObs=new MutationObserver(function(){m()});window.__rbMuteObs.observe(document.documentElement,{childList:true,subtree:true})})()", null)
            Toast.makeText(this, "Site ki aawaz band ho gayi", Toast.LENGTH_SHORT).show()
        } else {
            webView.evaluateJavascript("(function(){try{if(window.__rbMuteObs){window.__rbMuteObs.disconnect();window.__rbMuteObs=null}}catch(_){}try{document.querySelectorAll('video,audio').forEach(function(e){e.muted=false})}catch(_){}})()", null)
            Toast.makeText(this, "Aawaz wapas chalu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleDataSave() {
        dataSaveOn = !dataSaveOn
        webView.settings.blockNetworkImage = dataSaveOn
        webView.reload()
        Toast.makeText(this, if (dataSaveOn) "Data save on — photo band, page halka" else "Photo wapas dikhenge", Toast.LENGTH_SHORT).show()
    }

    private fun openUrlDialog() {
        val input = EditText(this)
        input.hint = "https://example.com"
        input.inputType = InputType.TYPE_TEXT_VARIATION_URI
        input.setSingleLine(true)
        val wrap = LinearLayout(this)
        wrap.orientation = LinearLayout.VERTICAL
        wrap.setPadding(dp(18), dp(8), dp(18), 0)
        wrap.addView(input, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        AlertDialog.Builder(this)
            .setTitle("Kya kholna hai?")
            .setMessage("Website ka pura address likho")
            .setView(wrap)
            .setPositiveButton("Kholo") { _, _ ->
                var u = input.text.toString().trim()
                if (u.isNotBlank()) {
                    if (!u.startsWith("http")) u = "https://" + u
                    webView.loadUrl(u)
                }
            }
            .setNegativeButton("Rehne do", null)
            .show()
    }

    private fun toggleFullScreen() {
        fullScreenOn = !fullScreenOn
        if (fullScreenOn) {
            supportActionBar?.hide()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            Toast.makeText(this, "Full screen on ho gaya", Toast.LENGTH_SHORT).show()
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            Toast.makeText(this, "Full screen band ho gaya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToTop() {
        webView.evaluateJavascript("window.scrollTo({top:0,behavior:'smooth'})", null)
    }

    private fun toggleRotation() {
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            Toast.makeText(this, "Screen ghumane ke liye unlock ho gayi", Toast.LENGTH_SHORT).show()
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Toast.makeText(this, "Portrait lock ho gaya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showThemeSheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)

        val sheet = LinearLayout(this)
        sheet.orientation = LinearLayout.VERTICAL
        val sheetBg = GradientDrawable()
        sheetBg.setColor(Color.WHITE)
        sheetBg.cornerRadius = dp(28).toFloat()
        sheet.background = sheetBg
        sheet.elevation = dp(18).toFloat()
        sheet.setPadding(dp(18), dp(10), dp(18), dp(18))

        val handle = View(this)
        val handleBg = GradientDrawable()
        handleBg.setColor(0xFFDCE1E8.toInt())
        handleBg.cornerRadius = dp(3).toFloat()
        handle.background = handleBg
        val handleLp = LinearLayout.LayoutParams(dp(44), dp(6))
        handleLp.gravity = Gravity.CENTER_HORIZONTAL
        sheet.addView(handle, handleLp)

        val title = TextView(this)
        title.text = "Theme chuno"
        title.textSize = 18f
        title.typeface = Typeface.DEFAULT_BOLD
        title.setTextColor(0xFF111827.toInt())
        val titleLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        titleLp.topMargin = dp(10)
        sheet.addView(title, titleLp)
        val subT = TextView(this)
        subT.text = "rang turant badalta hai — phone me save rehta hai"
        subT.textSize = 11.5f
        subT.setTextColor(0xFF6B7280.toInt())
        sheet.addView(subT, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        val scroll = ScrollView(this)
        scroll.isVerticalScrollBarEnabled = false
        val grid = LinearLayout(this)
        grid.orientation = LinearLayout.VERTICAL
        scroll.addView(grid, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val currentHex = String.format("#%06X", 0xFFFFFF and accentColor)
        var row: LinearLayout? = null
        THEME_PRESETS.forEachIndexed { idx, entry ->
            if (idx % 3 == 0) {
                row?.let { g -> grid.addView(g, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)) }
                val nr = LinearLayout(this)
                nr.orientation = LinearLayout.HORIZONTAL
                row = nr
            }
            val parts = entry.split("|")
            val name = parts.getOrNull(0) ?: ""
            val hex = parts.getOrNull(1) ?: ""
            val cell = LinearLayout(this)
            cell.orientation = LinearLayout.VERTICAL
            cell.gravity = Gravity.CENTER_HORIZONTAL
            cell.setPadding(dp(4), dp(10), dp(4), dp(6))

            val circle = FrameLayout(this)
            var base = accentColor
            try { base = Color.parseColor(hex) } catch (e: Exception) { }
            val cBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(base, 0.30f), base))
            cBg.shape = GradientDrawable.OVAL
            val active = hex.equals(currentHex, true)
            cBg.setStroke(dp(3), if (active) Color.WHITE else 0x33000000)
            circle.background = cBg
            circle.elevation = if (active) dp(6).toFloat() else dp(3).toFloat()
            if (active) {
                val check = TextView(this)
                check.text = "✓"
                check.textSize = 18f
                check.typeface = Typeface.DEFAULT_BOLD
                check.setTextColor(Color.WHITE)
                check.gravity = Gravity.CENTER
                circle.addView(check, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            }
            cell.addView(circle, LinearLayout.LayoutParams(dp(52), dp(52)))

            val tName = TextView(this)
            tName.text = name
            tName.textSize = 10.5f
            tName.setTextColor(0xFF374151.toInt())
            tName.gravity = Gravity.CENTER
            tName.maxLines = 1
            val tLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            tLp.topMargin = dp(5)
            cell.addView(tName, tLp)

            cell.setOnClickListener {
                bounce(cell)
                try {
                    applyAccent(Color.parseColor(hex), idx)
                } catch (e: Exception) {
                }
                dialog.dismiss()
                Toast.makeText(this, name + " theme lag gayi", Toast.LENGTH_SHORT).show()
            }
            val r = row
            if (r != null) {
                r.addView(cell, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            }
        }
        row?.let { g -> grid.addView(g, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)) }
        sheet.addView(scroll, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        dialog.setContentView(sheet)
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setGravity(Gravity.BOTTOM)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        sheet.translationY = dp(320).toFloat()
        sheet.alpha = 0f
        dialog.show()
        sheet.animate().translationY(0f).alpha(1f).setDuration(300).setInterpolator(OvershootInterpolator(1.05f)).start()
    }

    private fun printPage() {
        try {
            val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
            printManager.print(APP_NAME, webView.createPrintDocumentAdapter(APP_NAME), null)
        } catch (e: Exception) {
            Toast.makeText(this, "Print option is phone par nahi mila", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openDownloads() {
        try {
            startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
        } catch (e: Exception) {
            Toast.makeText(this, "Downloads folder nahi khul paya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startDownload(url: String, userAgent: String, contentDisposition: String, mimeType: String) {
        if (url.startsWith("blob:") || url.startsWith("data:")) {
            captureBlobDownload(url)
            return
        }
        if (!url.startsWith("http")) {
            openExternal(Uri.parse(url))
            return
        }
        try {
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("User-Agent", userAgent)
            request.addRequestHeader("Referer", HOME_URL)
            val cookies = CookieManager.getInstance().getCookie(url)
            if (!cookies.isNullOrBlank()) request.addRequestHeader("Cookie", cookies)
            request.setTitle(fileName)
            request.setDescription(APP_NAME)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setAllowedOverMetered(true)
            request.setAllowedOverRoaming(true)
            try {
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            } catch (e: Exception) {
                // koi device public folder allow nahi karta — default destination bhi Downloads hi hai
            }
            lastDownloadId = (getSystemService(DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
            downloadIds.add(lastDownloadId)
            downloadMimes[lastDownloadId] = mimeType
            Toast.makeText(this, "Download shuru — " + fileName + " (Downloads folder me save hoga)", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            openExternal(Uri.parse(url))
        }
    }

    private fun jsonQuote(value: String): String {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
    }

    private fun captureBlobDownload(url: String) {
        Toast.makeText(this, "Download shuru ho raha hai...", Toast.LENGTH_SHORT).show()
        val js = "(function(){var u=" + jsonQuote(url) + ",f=window.__appbanaoGrab;if(f){f(u,'download.bin');}else{setTimeout(function(){window.__appbanaoGrab&&window.__appbanaoGrab(u,'download.bin');},700);}})()"
        webView.evaluateJavascript(js, null)
    }

    inner class BlobBridge {
        @JavascriptInterface
        fun blobSaveStart(name: String, mime: String) {
            try {
                blobBuffer.reset()
                blobName = name.ifBlank { "download.bin" }
                blobMime = mime.ifBlank { "application/octet-stream" }
            } catch (e: Exception) {
            }
        }

        @JavascriptInterface
        fun blobSaveChunk(b64: String) {
            try {
                val bytes = Base64.decode(b64, Base64.DEFAULT)
                synchronized(blobBuffer) { blobBuffer.write(bytes) }
            } catch (e: Exception) {
            }
        }

        @JavascriptInterface
        fun blobSaveDone() {
            runOnUiThread { finishBlobSave() }
        }

        @JavascriptInterface
        fun blobSaveFail() {
            runOnUiThread { Toast.makeText(this@MainActivity, "Ye download app me nahi ho paya — link browser me try karo", Toast.LENGTH_LONG).show() }
        }
    }

    private fun finishBlobSave() {
        val bytes = synchronized(blobBuffer) { blobBuffer.toByteArray() }
        if (bytes.isEmpty()) {
            Toast.makeText(this, "File khali thi — download cancel", Toast.LENGTH_SHORT).show()
            return
        }
        val safeName = blobName.replace(Regex("[^A-Za-z0-9 ._()-]"), "_").ifBlank { "download.bin" }
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                val values = android.content.ContentValues()
                values.put(android.provider.MediaStore.Downloads.DISPLAY_NAME, safeName)
                values.put(android.provider.MediaStore.Downloads.MIME_TYPE, blobMime)
                values.put(android.provider.MediaStore.Downloads.IS_PENDING, 1)
                val dest = contentResolver.insert(android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: throw IllegalStateException("save fail")
                contentResolver.openOutputStream(dest)?.use { it.write(bytes) }
                val done = android.content.ContentValues()
                done.put(android.provider.MediaStore.Downloads.IS_PENDING, 0)
                contentResolver.update(dest, done, null, null)
                rememberSavedBlob(safeName, dest.toString())
                Toast.makeText(this, "Download complete — " + safeName + " Downloads folder me save ho gaya", Toast.LENGTH_LONG).show()
            } else {
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "")
                if (!dir.exists()) dir.mkdirs()
                val outFile = File(dir, safeName)
                outFile.writeBytes(bytes)
                MediaScannerConnection.scanFile(this, arrayOf(outFile.absolutePath), arrayOf(blobMime), null)
                rememberSavedBlob(safeName, "file://" + outFile.absolutePath)
                Toast.makeText(this, "Download complete — " + safeName, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            try {
                val dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: filesDir
                val outFile = File(dir, safeName)
                outFile.writeBytes(bytes)
                rememberSavedBlob(safeName, "file://" + outFile.absolutePath)
                Toast.makeText(this, "Download complete — " + safeName + " (app ke folder me save)", Toast.LENGTH_LONG).show()
            } catch (e2: Exception) {
                Toast.makeText(this, "File save nahi ho payi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun rememberSavedBlob(name: String, uri: String) {
        val cur = prefs.getString("saved_blobs", "") ?: ""
        val next = (cur.split("|@|").filter { it.isNotBlank() } + (name + "@@" + uri)).takeLast(30)
        prefs.edit().putString("saved_blobs", next.joinToString("|@|")).apply()
    }

    private fun savedBlobEntries(): List<Pair<String, String>> {
        val raw = prefs.getString("saved_blobs", "") ?: ""
        return raw.split("|@|").filter { it.contains("@@") }.map { entry ->
            val parts = entry.split("@@")
            Pair(parts[0], parts[1])
        }
    }

    private fun openSavedBlob(uriStr: String) {
        try {
            val uri = if (uriStr.startsWith("file://")) Uri.fromFile(File(uriStr.removePrefix("file://"))) else Uri.parse(uriStr)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, blobMime)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Is file ko kholne wala app nahi mila", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "File nahi khul payi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildSavedRow(dialog: Dialog, name: String, uriStr: String): View {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL
        row.setPadding(dp(12), dp(10), dp(10), dp(10))
        val card = GradientDrawable()
        card.setColor(0xFFEDF7FF.toInt())
        card.cornerRadius = dp(14).toFloat()
        row.background = card
        row.foreground = RippleDrawable(ColorStateList.valueOf(0x1F888888), null, null)
        val rowLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        rowLp.topMargin = dp(8)
        row.layoutParams = rowLp

        val info = LinearLayout(this)
        info.orientation = LinearLayout.VERTICAL
        val label = TextView(this)
        label.text = name
        label.textSize = 14f
        label.typeface = Typeface.DEFAULT_BOLD
        label.setTextColor(0xFF1B1F24.toInt())
        label.maxLines = 2
        info.addView(label)
        val meta = TextView(this)
        meta.text = "App me download — tap karke kholo"
        meta.textSize = 11f
        meta.setTextColor(0xFF6B7280.toInt())
        info.addView(meta)
        row.addView(info, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        row.setOnClickListener { bounce(row); openSavedBlob(uriStr) }
        return row
    }

    private fun toggleDesktopView() {
        desktopView = !desktopView
        prefs.edit().putBoolean("desktop_view", desktopView).apply()
        webView.settings.userAgentString = if (desktopView) DESKTOP_UA else baseUa
        Toast.makeText(this, if (desktopView) "Desktop view ON — poori site load ho rahi hai" else "Mobile view ON", Toast.LENGTH_SHORT).show()
        webView.reload()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 4001) {
            val pending = pendingDownload
            pendingDownload = null
            if (pending != null && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload(pending[0], pending[1], pending[2], pending[3])
            } else {
                Toast.makeText(this, "Storage permission chahiye file save karne ke liye", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerDownloadReceiver() {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                if (id != -1L && id == lastDownloadId) {
                    val media = isMediaMime(downloadMimes[id])
                    if (GALLERY_ON && media) saveToGalleryIfMedia(id)
                    val msg = if (GALLERY_ON && media) "Download complete — Gallery aur Downloads folder me save ho gaya" else "Download complete — Downloads folder me save ho gaya"
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
        downloadReceiver = receiver
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, filter)
        }
    }

    private fun isMediaMime(mime: String?): Boolean {
        val m = (mime ?: "").lowercase()
        return m.startsWith("image/") || m.startsWith("video/")
    }

    private fun downloadTitle(id: Long): String {
        return try {
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = dm.query(DownloadManager.Query().setFilterById(id))
            cursor?.use {
                val tIdx = it.getColumnIndex(DownloadManager.COLUMN_TITLE)
                if (it.moveToFirst() && tIdx >= 0) return it.getString(tIdx) ?: "file"
            }
            "file"
        } catch (e: Exception) {
            "file"
        }
    }

    private fun saveToGalleryIfMedia(id: Long) {
        val mime = (downloadMimes[id] ?: "").lowercase()
        if (!mime.startsWith("image/") && !mime.startsWith("video/")) return
        try {
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val source = dm.getUriForDownloadedFile(id) ?: return
            val fileName = downloadTitle(id)
            if (Build.VERSION.SDK_INT >= 29) {
                val values = android.content.ContentValues()
                values.put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                values.put(android.provider.MediaStore.MediaColumns.MIME_TYPE, mime)
                values.put(android.provider.MediaStore.MediaColumns.IS_PENDING, 1)
                val collection = if (mime.startsWith("video/")) {
                    android.provider.MediaStore.Video.Media.getContentUri(android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY)
                } else {
                    android.provider.MediaStore.Images.Media.getContentUri(android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY)
                }
                val dest = contentResolver.insert(collection, values) ?: return
                contentResolver.openInputStream(source)?.use { input ->
                    contentResolver.openOutputStream(dest)?.use { output -> input.copyTo(output) }
                }
                val done = android.content.ContentValues()
                done.put(android.provider.MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(dest, done, null, null)
            } else {
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_NAME)
                if (!dir.exists()) dir.mkdirs()
                val destFile = File(dir, fileName)
                contentResolver.openInputStream(source)?.use { input ->
                    destFile.outputStream().use { output -> input.copyTo(output) }
                }
                MediaScannerConnection.scanFile(this, arrayOf(destFile.absolutePath), arrayOf(mime), null)
            }
        } catch (e: Exception) {
            // Gallery copy fail — file phir bhi Downloads folder me safe hai
        }
    }

    private fun humanSize(bytes: Long): String {
        if (bytes <= 0) return ""
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        return when {
            mb >= 1 -> String.format("%.1f MB", mb)
            kb >= 1 -> String.format("%.0f KB", kb)
            else -> bytes.toString() + " B"
        }
    }

    private fun openDownloadedFile(dm: DownloadManager, id: Long) {
        try {
            val uri = dm.getUriForDownloadedFile(id)
            if (uri == null) {
                Toast.makeText(this, "File nahi mili — shayad delete ho chuki hai", Toast.LENGTH_SHORT).show()
                return
            }
            val intent = Intent(Intent.ACTION_VIEW)
            val mime = downloadMimes[id]
            if (mime.isNullOrBlank()) intent.data = uri else intent.setDataAndType(uri, mime)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Is file ko kholne wala app nahi mila", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "File nahi khul payi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareDownloadedFile(dm: DownloadManager, id: Long, name: String) {
        try {
            val uri = dm.getUriForDownloadedFile(id) ?: return
            val send = Intent(Intent.ACTION_SEND)
            val mime = downloadMimes[id]
            send.setType(if (mime.isNullOrBlank()) "*/*" else mime)
            send.putExtra(Intent.EXTRA_STREAM, uri)
            send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(send, "Share karo — " + name))
        } catch (e: Exception) {
            Toast.makeText(this, "Share nahi ho payi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildDownloadRow(dialog: Dialog, dm: DownloadManager, id: Long, name: String, size: Long): View {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL
        row.setPadding(dp(12), dp(10), dp(10), dp(10))
        val card = GradientDrawable()
        card.setColor(0xFFF3F5F7.toInt())
        card.cornerRadius = dp(14).toFloat()
        row.background = card
        row.foreground = RippleDrawable(ColorStateList.valueOf(0x1F888888), null, null)
        val rowLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        rowLp.topMargin = dp(8)
        row.layoutParams = rowLp

        val info = LinearLayout(this)
        info.orientation = LinearLayout.VERTICAL

        val label = TextView(this)
        label.text = name
        label.textSize = 14f
        label.typeface = Typeface.DEFAULT_BOLD
        label.setTextColor(0xFF1B1F24.toInt())
        label.maxLines = 2
        info.addView(label)

        val meta = TextView(this)
        val sizeText = humanSize(size)
        meta.text = if (sizeText.isBlank()) "Tap karke kholo" else sizeText + " • tap karke kholo"
        meta.textSize = 11f
        meta.setTextColor(0xFF6B7280.toInt())
        info.addView(meta)

        val share = TextView(this)
        share.text = "Share"
        share.textSize = 12f
        share.typeface = Typeface.DEFAULT_BOLD
        share.setTextColor(accentColor)
        val shareBg = GradientDrawable()
        shareBg.setColor(shade(accentColor, 0.88f))
        shareBg.cornerRadius = dp(12).toFloat()
        share.background = shareBg
        share.setPadding(dp(12), dp(6), dp(12), dp(6))
        share.setOnClickListener {
            bounce(share)
            shareDownloadedFile(dm, id, name)
        }

        row.addView(info, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        val shareLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        shareLp.leftMargin = dp(8)
        row.addView(share, shareLp)

        row.setOnClickListener { openDownloadedFile(dm, id) }
        row.setOnLongClickListener {
            if (downloadIds.contains(id)) {
                try {
                    dm.remove(id)
                    downloadIds.remove(id)
                    Toast.makeText(this, "Delete ho gayi — " + name, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    showDownloadsSheet()
                } catch (e: Exception) {
                    Toast.makeText(this, "Delete nahi ho payi", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                Toast.makeText(this, "Ye file kisi aur app ne download ki hai — delete nahi ho sakti", Toast.LENGTH_SHORT).show()
                true
            }
        }
        return row
    }

    private fun showDownloadsSheet() {
        val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val dialog = Dialog(this)
        val sheet = LinearLayout(this)
        sheet.orientation = LinearLayout.VERTICAL
        val pad = dp(16)
        sheet.setPadding(pad, pad, pad, dp(12))
        val bg = GradientDrawable()
        bg.setColor(Color.WHITE)
        bg.cornerRadius = dp(22).toFloat()
        sheet.background = bg

        val title = TextView(this)
        title.text = "Mere Downloads"
        title.textSize = 18f
        title.typeface = Typeface.DEFAULT_BOLD
        title.setTextColor(0xFF1B1F24.toInt())
        sheet.addView(title)

        val sub = TextView(this)
        sub.text = "Tap = kholo • Share bhejo • dabaye rakho = delete"
        sub.textSize = 12f
        sub.setTextColor(0xFF6B7280.toInt())
        val subLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        subLp.topMargin = dp(2)
        sheet.addView(sub, subLp)

        val scroll = ScrollView(this)
        val list = LinearLayout(this)
        list.orientation = LinearLayout.VERTICAL
        scroll.addView(list, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        var rows = 0
        try {
            val cursor = dm.query(DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL))
            cursor?.use {
                val iIdx = it.getColumnIndex(DownloadManager.COLUMN_ID)
                val tIdx = it.getColumnIndex(DownloadManager.COLUMN_TITLE)
                val sIdx = it.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                while (it.moveToNext()) {
                    if (iIdx < 0) break
                    val id = it.getLong(iIdx)
                    val name = if (tIdx >= 0) (it.getString(tIdx) ?: "file") else "file"
                    val size = if (sIdx >= 0 && !it.isNull(sIdx)) it.getLong(sIdx) else 0L
                    list.addView(buildDownloadRow(dialog, dm, id, name, size))
                    rows++
                }
            }
        } catch (e: Exception) {
            // list na mile to neeche empty message dikhega
        }
        val saved = savedBlobEntries()
        saved.forEach { entry ->
            list.addView(buildSavedRow(dialog, entry.first, entry.second))
            rows++
        }
        if (rows == 0) {
            val empty = TextView(this)
            empty.text = "Abhi koi download nahi hui — website se koi file download karo, wo yahan dikhegi"
            empty.textSize = 13f
            empty.setTextColor(0xFF6B7280.toInt())
            val eLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            eLp.topMargin = dp(14)
            list.addView(empty, eLp)
        }
        sheet.addView(scroll, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))

        val openFolder = TextView(this)
        openFolder.text = "Downloads folder kholo"
        openFolder.textSize = 14f
        openFolder.typeface = Typeface.DEFAULT_BOLD
        openFolder.gravity = Gravity.CENTER
        openFolder.setTextColor(Color.WHITE)
        val btnBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(accentColor, 0.35f), accentColor))
        btnBg.cornerRadius = dp(14).toFloat()
        openFolder.background = btnBg
        openFolder.setPadding(dp(14), dp(11), dp(14), dp(11))
        openFolder.setOnClickListener {
            bounce(openFolder)
            dialog.dismiss()
            openDownloads()
        }
        val btnLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        btnLp.topMargin = dp(12)
        sheet.addView(openFolder, btnLp)

        dialog.setContentView(sheet)
        dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.92).toInt(), (resources.displayMetrics.heightPixels * 0.72).toInt())
        dialog.show()
    }

    private fun buildNavBar(): LinearLayout {
        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.gravity = Gravity.CENTER
        bar.setPadding(dp(6), dp(6), dp(6), dp(6))
        val barBg = GradientDrawable()
        barBg.setColor(Color.WHITE)
        barBg.cornerRadius = dp(32).toFloat()
        barBg.setStroke(dp(1), 0x18808080)
        bar.background = barBg
        bar.elevation = dp(18).toFloat()

        navEntries.forEachIndexed { index, entry ->
            val item = LinearLayout(this)
            item.orientation = LinearLayout.VERTICAL
            item.gravity = Gravity.CENTER
            item.setPadding(dp(2), dp(4), dp(2), dp(4))
            item.foreground = RippleDrawable(ColorStateList.valueOf(0x1F888888), null, null)

            val pillHolder = FrameLayout(this)
            val pill = GradientDrawable()
            pill.cornerRadius = dp(19).toFloat()
            pillHolder.background = pill
            pillBackgrounds.add(pill)
            pillHolders.add(pillHolder)

            val icon = ImageView(this)
            icon.setImageResource(entry.icon)
            icon.setColorFilter(0xFF8A8F98.toInt())
            pillHolder.addView(icon, FrameLayout.LayoutParams(dp(24), dp(24), Gravity.CENTER))

            val label = TextView(this)
            label.text = entry.label
            label.textSize = 10f
            label.maxLines = 1
            label.gravity = Gravity.CENTER
            label.letterSpacing = 0.02f
            label.setTextColor(0xFF8A8F98.toInt())

            item.addView(pillHolder, LinearLayout.LayoutParams(dp(62), dp(38)))
            item.addView(label)
            item.setOnClickListener {
                selectNav(index)
                bounce(pillHolder)
                webView.loadUrl(entry.url)
            }
            navIcons.add(icon)
            navLabels.add(label)
            bar.addView(item, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        }
        return bar
    }

    private fun bounce(view: View) {
        view.animate().scaleX(1.22f).scaleY(1.22f).setDuration(120).setInterpolator(OvershootInterpolator(1.4f)).withEndAction {
            view.animate().scaleX(1f).scaleY(1f).setDuration(140).start()
        }.start()
    }

    private fun hideNavBar() {
        navBar?.animate()?.translationY((dp(90)).toFloat())?.setDuration(220)?.start()
    }

    private fun showNavBar() {
        navBar?.animate()?.translationY(0f)?.setDuration(220)?.start()
    }

    private fun selectNav(index: Int) {
        if (!SHOW_NAV) return
        currentNavIndex = index
        navIcons.forEachIndexed { i, icon -> icon.setColorFilter(if (i == index) Color.WHITE else 0xFF8A8F98.toInt()) }
        navLabels.forEachIndexed { i, label ->
            label.setTextColor(if (i == index) accentColor else 0xFF8A8F98.toInt())
            label.typeface = if (i == index) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
        pillBackgrounds.forEachIndexed { i, pill ->
            pill.orientation = GradientDrawable.Orientation.TL_BR
            if (i == index) {
                pill.setColors(intArrayOf(shade(accentColor, 0.35f), accentColor))
            } else {
                pill.setColor(0x00000000)
            }
        }
        pillHolders.forEachIndexed { i, holder -> holder.elevation = if (i == index) dp(6).toFloat() else 0f }
    }

    private fun updateNavTheme() {
        selectNav(currentNavIndex)
    }

    private fun applyAccent(color: Int, presetIdx: Int) {
        accentColor = color
        prefs.edit().putInt("theme_idx", presetIdx).apply()
        try {
            window.statusBarColor = shade(color, -0.45f)
        } catch (e: Exception) {
        }
        progressBar.progressTintList = ColorStateList.valueOf(color)
        swipeRefresh.setColorSchemeColors(color)
        moreBtn?.let { b ->
            val bg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(color, 0.35f), color, shade(color, -0.25f)))
            bg.shape = GradientDrawable.OVAL
            bg.setStroke(dp(2), Color.WHITE)
            b.background = bg
        }
        updateNavTheme()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(PINCH_ZOOM)
        webView.settings.builtInZoomControls = PINCH_ZOOM
        webView.settings.displayZoomControls = false
        webView.settings.mediaPlaybackRequiresUserGesture = VIDEO_BLOCK_ON
        webView.settings.cacheMode = if (DATA_SAVER_ON) WebSettings.LOAD_CACHE_ELSE_NETWORK else WebSettings.LOAD_DEFAULT
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                val scheme = uri.scheme?.lowercase() ?: return false
                // Email link — seedha message likhne ka screen khulta hai (jo address link me ho)
                if (scheme == "mailto") {
                    emailSupportTo(uri.toString().removePrefix("mailto:").substringBefore('?'))
                    return true
                }
                // Maps / location link — seedha Maps app me khulta hai
                if (scheme == "geo") {
                    var gq = ""
                    try { gq = uri.getQueryParameter("q") ?: "" } catch (e: Exception) { }
                    if (gq.isBlank()) { try { gq = uri.schemeSpecificPart.substringBefore('?') } catch (e: Exception) { } }
                    openDirections(gq, uri)
                    return true
                }
                // Call / SMS / WhatsApp / intent links — sab direct apni app me khulte hain
                if (scheme == "tel" || scheme == "sms" || scheme == "intent" || scheme == "whatsapp") {
                    openExternal(uri)
                    return true
                }
                if (scheme == "http" || scheme == "https") {
                    // Google Maps / directions / email-compose links ko WebView me mat kholo — Maps/Gmail app khule
                    val linkHost = uri.host?.lowercase() ?: ""
                    val linkPath = (uri.path ?: "").lowercase()
                    val isMapsLink = linkHost == "maps.google.com" || linkHost == "maps.app.goo.gl" || (linkHost == "goo.gl" && linkPath.startsWith("/maps")) || ((linkHost == "www.google.com" || linkHost == "google.com" || linkHost.endsWith(".google.com")) && linkPath.startsWith("/maps"))
                    val isGmail = linkHost == "mail.google.com"
                    if (isMapsLink) {
                        openMapsFromWeb(uri)
                        return true
                    }
                    if (isGmail) {
                        openExternal(uri)
                        return true
                    }
                    val host = uri.host?.lowercase() ?: return false
                    val sameSite = host == HOME_HOST || host.endsWith("." + HOME_HOST)
                    if (OPEN_EXTERNAL && !sameSite) {
                        openExternal(uri)
                        return true
                    }
                    return false
                }
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
                if (HIDE_ON) injectHideEngine(view)
                if (REPLACE_JS.isNotEmpty()) view.evaluateJavascript(REPLACE_JS, null)
                if (DATA_SAVER_JS.isNotEmpty() && dataSaverCacheOn) view.evaluateJavascript(DATA_SAVER_JS, null)
                if (VIDEO_BLOCK_JS.isNotEmpty() && videoBlockOn) view.evaluateJavascript(VIDEO_BLOCK_JS, null)
                if (adBlockOn) injectAdBlock(view)
                if (DOWNLOADS_ON || LONGPRESS_DL_ON) {
                    view.evaluateJavascript(BLOB_HOOK_JS, null)
                    view.postDelayed({ view.evaluateJavascript(BLOB_HOOK_JS, null) }, 600)
                }
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                if (OFFLINE_PAGE && request.isForMainFrame) showOffline()
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
                progressBar.visibility = if (newProgress >= 100) View.INVISIBLE else View.VISIBLE
            }

            override fun onShowFileChooser(webView: WebView, callback: ValueCallback<Array<Uri>>, params: FileChooserParams): Boolean {
                fileChooserCallback?.onReceiveValue(null)
                fileChooserCallback = callback
                val accept = params.acceptTypes?.firstOrNull { it.isNotBlank() } ?: "*/*"
                return try {
                    filePicker.launch(accept)
                    true
                } catch (e: Exception) {
                    try {
                        filePicker.launch("*/*")
                        true
                    } catch (e2: Exception) {
                        fileChooserCallback = null
                        Toast.makeText(this@MainActivity, "File choose karne wala app nahi mila", Toast.LENGTH_SHORT).show()
                        false
                    }
                }
            }
        }

        webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
            if (url.startsWith("blob:") || url.startsWith("data:")) {
                captureBlobDownload(url)
                return@DownloadListener
            }
            if (!DOWNLOADS_ON) {
                openExternal(Uri.parse(url))
                return@DownloadListener
            }
            if (Build.VERSION.SDK_INT <= 28 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                pendingDownload = arrayOf(url, userAgent, contentDisposition ?: "", mimeType ?: "")
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 4001)
                return@DownloadListener
            }
            startDownload(url, userAgent, contentDisposition ?: "", mimeType ?: "")
        })
    }

    private fun injectHideEngine(view: WebView) {
        view.evaluateJavascript(HIDE_JS, null)
        view.postDelayed({ view.evaluateJavascript(HIDE_JS, null) }, 400)
        view.postDelayed({ view.evaluateJavascript(HIDE_JS, null) }, 1500)
    }

    private fun injectAdBlock(view: WebView) {
        view.evaluateJavascript(AD_BLOCK_JS, null)
    }

    private fun openExternal(uri: Uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            try {
                // koi direct app na mile to chooser se khulo (Gmail/Chrome/Maps me se chuno)
                val fallback = Intent(Intent.ACTION_VIEW, uri)
                fallback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(Intent.createChooser(fallback, "Kaunsa app khole?"))
            } catch (e2: Exception) {
                Toast.makeText(this, "Ye link is phone par nahi khul paya", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ye link nahi khul paya", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showOffline() {
        val html = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1'><style>body{font-family:sans-serif;background:" + SPLASH_COLOR + ";color:#E8EDF4;display:flex;align-items:center;justify-content:center;height:100vh;margin:0;text-align:center}h2{margin:10px 0 4px}p{opacity:0.7;margin:0 0 6px}button{background-color:" + THEME_COLOR + ";color:#0C0F14;border:0;padding:13px 26px;border-radius:10px;font-size:15px;font-weight:700;margin-top:16px}</style></head><body><div><div style='font-size:52px'>&#128246;</div><h2>No Internet</h2><p>Internet connection check karein aur dobara try karein.</p><button onclick=\"location.href='" + HOME_URL + "'\">Retry</button></div></body></html>"
        webView.loadDataWithBaseURL(HOME_URL, html, "text/html", "utf-8", HOME_URL)
    }
}
