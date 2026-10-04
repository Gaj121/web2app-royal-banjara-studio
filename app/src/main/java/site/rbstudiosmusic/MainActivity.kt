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
        const val SUPPORT_EMAIL = "support@rbstudiosmusic.site"
        const val READ_ALOUD_ON = true
        const val BOOKMARKS_ON = true
        const val AD_BLOCK_JS = "(function(){try{var s=document.createElement('style');s.id='appbanao-adblock';s.textContent=\"ins.adsbygoogle,.adsbygoogle,[id^='google_ads'],[id^='div-gpt-ad'],[id^='taboola'],[class^='popunder'],iframe[src*='doubleclick.net'],iframe[src*='googlesyndication'],iframe[src*='adserver'],.ad-banner,.ad-banner-top,.ad-container,.ad-wrapper,.ad-slot,.advert,.advertisement,.google-ad,.sidebar-ad,.sticky-ad{display:none !important;visibility:hidden !important;}\";(document.head||document.documentElement).appendChild(s);}catch(e){}})()"
        val THEME_PRESETS = arrayOf("Royal Blue|#2563EB", "Midnight Black|#111827", "Emerald Green|#10B981", "Ocean Cyan|#0EA5E9", "Sunset Orange|#F97316", "Grape Purple|#8B5CF6", "Rose Pink|#EC4899", "Royal Gold|#D4AF37", "Teal Fresh|#14B8A6", "Deep Indigo|#6366F1", "Crimson Red|#DC2626", "Amber Glow|#F59E0B", "Lime Punch|#84CC16", "Sky Light|#38BDF8", "Chocolate Brown|#92400E", "Slate Grey|#475569", "Neon Violet|#7C3AED", "Magenta Rush|#E11D48", "Forest Green|#15803D", "Deep Navy|#1E40AF", "Coral Peach|#FF7F50", "Mint Aqua|#06D6A0", "Jade Stone|#00A896", "Bronze Copper|#B87333", "Orchid Pink|#DA70D6", "Plum Velvet|#7E22CE", "Steel Blue|#4682B4", "Ruby Red|#E0115F", "Arctic Ice|#22D3EE", "Coffee Dark|#6F4E37", "Saffron Desi|#FF9933", "Peacock Blue|#0288D1", "Henna Maroon|#800000", "Banana Yellow|#FBC02D", "Grapefruit|#FF6347", "Lavender Soft|#9575CD", "Olive Green|#6B8E23", "Turquoise Sea|#40E0D0", "Fuchsia Flash|#D500F9", "Graphite Steel|#37474F")
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
            send.putExtra(Intent.EXTRA_TEXT, APP_NAME + " app try karo! " + HOME_URL)
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

    private fun emailSupport() {
        val to = SUPPORT_EMAIL
        if (to.isBlank()) {
            shareAppLink()
            return
        }
        try {
            val uri = Uri.parse("mailto:" + to + "?subject=" + Uri.encode(APP_NAME + " sawaal") + "&body=" + Uri.encode("Namaste,\n\n"))
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO, uri), "Email bhejo"))
        } catch (e: Exception) {
            openExternal(Uri.parse("mailto:" + to))
        }
    }

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
        val stripBg = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(shade(accentColor, 0.4f), accentColor, shade(accentColor, -0.3f)))
        stripBg.cornerRadius = dp(4).toFloat()
        strip.background = stripBg
        val stripLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(4))
        stripLp.topMargin = dp(8)
        sheet.addView(strip, stripLp)

        val header = LinearLayout(this)
        header.orientation = LinearLayout.HORIZONTAL
        header.gravity = Gravity.CENTER_VERTICAL
        val headLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        headLp.topMargin = dp(8)
        headLp.bottomMargin = dp(2)
        sheet.addView(header, headLp)

        val headBlock = LinearLayout(this)
        headBlock.orientation = LinearLayout.VERTICAL
        val sheetTitle = TextView(this)
        sheetTitle.text = "Quick Tools"
        sheetTitle.textSize = 19f
        sheetTitle.typeface = Typeface.DEFAULT_BOLD
        sheetTitle.setTextColor(0xFF111827.toInt())
        headBlock.addView(sheetTitle)
        val sheetSub = TextView(this)
        sheetSub.text = APP_NAME + " ke kaam ke tools"
        sheetSub.textSize = 12f
        sheetSub.setTextColor(0xFF6B7280.toInt())
        headBlock.addView(sheetSub)
        header.addView(headBlock, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

        val close = TextView(this)
        close.text = "✕"
        close.textSize = 15f
        close.typeface = Typeface.DEFAULT_BOLD
        close.setTextColor(0xFF374151.toInt())
        close.gravity = Gravity.CENTER
        val closeBg = GradientDrawable()
        closeBg.shape = GradientDrawable.OVAL
        closeBg.setColor(0xFFF1F3F6.toInt())
        close.background = closeBg
        close.setOnClickListener { dialog.dismiss() }
        header.addView(close, LinearLayout.LayoutParams(dp(34), dp(34)))

        // Scrollable area — jitne bhi tools hon, sab yahan scroll karke dikhte hain
        val scroll = ScrollView(this)
        scroll.isVerticalScrollBarEnabled = false
        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        scroll.addView(content, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        val items = buildToolItems()
        val cards = mutableListOf<View>()
        var lastSection = ""
        var row: LinearLayout? = null
        items.forEachIndexed { index, item ->
            if (row == null || item.section != lastSection) {
                row?.let { r -> content.addView(r, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)) }
                lastSection = item.section
                val st = TextView(this)
                st.text = lastSection.uppercase()
                st.textSize = 10.5f
                st.typeface = Typeface.DEFAULT_BOLD
                st.letterSpacing = 0.10f
                st.setTextColor(0xFF98A1B3.toInt())
                val stLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                stLp.topMargin = dp(14)
                stLp.bottomMargin = dp(6)
                content.addView(st, stLp)
                val nr = LinearLayout(this)
                nr.orientation = LinearLayout.HORIZONTAL
                row = nr
            }
            val r = row
            if (r != null) {
                val card = buildToolCard(dialog, item)
                cards.add(card)
                val cardLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                if (r.childCount % 2 == 0) cardLp.rightMargin = dp(5) else cardLp.leftMargin = dp(5)
                r.addView(card, cardLp)
            }
        }
        row?.let { r -> content.addView(r, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)) }
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

    // Saare Quick Tools ki list — sections ke saath; icons premium white vector icons hain
    private fun buildToolItems(): List<ToolItem> {
        val items = mutableListOf<ToolItem>()
        if (FIND_TOOL_ON) items.add(ToolItem(R.drawable.ic_tool_find, "Page me dhoondo", "shabd turant milenge", 0xFF0891B2.toInt(), "Screen aur Padhai") { findInPage() })
        if (READ_ALOUD_ON) items.add(ToolItem(R.drawable.ic_tool_speaker, "Padh ke sunao", "page bolke padhega", 0xFF6D28D9.toInt(), "Screen aur Padhai") { readAloud() })
        items.add(ToolItem(R.drawable.ic_tool_print, "Print / PDF", "page ya PDF banao", 0xFF111827.toInt(), "Files aur Print") { printPage() })
        items.add(ToolItem(R.drawable.ic_tool_camera, "Screenshot lo", "page ki photo", 0xFFDC2626.toInt(), "Files aur Print") { takeScreenshot() })
        items.add(ToolItem(R.drawable.ic_tool_download, "Mere Downloads", "app ki hi list", 0xFF10B981.toInt(), "Files aur Print") { showDownloadsSheet() })
        items.add(ToolItem(R.drawable.ic_tool_folder, "Downloads folder", "phone ka folder", 0xFF059669.toInt(), "Files aur Print") { openDownloads() })
        if (BOOKMARKS_ON) items.add(ToolItem(R.drawable.ic_tool_star, "Bookmark karo", "page save karo", 0xFFEAB308.toInt(), "Save kiye hue") { bookmarkCurrentPage() })
        if (BOOKMARKS_ON) items.add(ToolItem(R.drawable.ic_tool_book, "Mere Bookmarks", "save kiye page", 0xFFD97706.toInt(), "Save kiye hue") { showBookmarksSheet() })
        items.add(ToolItem(if (nightOn) R.drawable.ic_tool_sun else R.drawable.ic_tool_moon, if (nightOn) "Day mode karo" else "Night mode karo", "aankhon ko aaram", 0xFF1E293B.toInt(), "Screen aur Padhai") { toggleNightMode() })
        items.add(ToolItem(R.drawable.ic_tool_textgrow, "Text bada karo", "padhna aasan", 0xFF0EA5E9.toInt(), "Screen aur Padhai") { changeTextSize(15) })
        items.add(ToolItem(R.drawable.ic_tool_textgrow, "Text chhota karo", "compact view", 0xFF0EA5E9.toInt(), "Screen aur Padhai") { changeTextSize(-15) })
        items.add(ToolItem(R.drawable.ic_tool_fullscreen, if (fullScreenOn) "Full screen band" else "Full screen karo", "poora screen app ka", 0xFF7C3AED.toInt(), "Screen aur Padhai") { toggleFullScreen() })
        items.add(ToolItem(R.drawable.ic_tool_monitor, if (desktopView) "Mobile view karo" else "Desktop view karo", "poori site desktop mode", 0xFF6366F1.toInt(), "Screen aur Padhai") { toggleDesktopView() })
        items.add(ToolItem(R.drawable.ic_tool_palette, "Theme badlo", "rang turant badlo", 0xFFEC4899.toInt(), "Screen aur Padhai") { showThemeSheet() })
        items.add(ToolItem(R.drawable.ic_tool_arrowup, "Top par jao", "seedha page ke upar", 0xFFF59E0B.toInt(), "Screen aur Padhai") { goToTop() })
        items.add(ToolItem("🔄", "Ghumao", "portrait ↔ landscape", 0xFF14B8A6.toInt(), "Screen aur Padhai") { toggleRotation() })
        items.add(ToolItem(R.drawable.ic_tool_refresh, "Refresh page", "dobara load", 0xFF2563EB.toInt(), "App") { webView.reload() })
        items.add(ToolItem(R.drawable.ic_tool_home, "Home page", "shuruati page", 0xFF111827.toInt(), "App") { webView.loadUrl(HOME_URL) })
        items.add(ToolItem(R.drawable.ic_tool_share, "App share karo", "asli APK file bhejo", 0xFF8B5CF6.toInt(), "App") { shareApkNow() })
        items.add(ToolItem(R.drawable.ic_tool_broom, "Cache clear", "speed badhao", 0xFFF97316.toInt(), "App") { clearAppCache() })
        items.add(ToolItem("🚪", "App band karo", "seedha close", 0xFFDC2626.toInt(), "App") { finishAffinity() })
        if (SUPPORT_EMAIL.isNotBlank()) items.add(ToolItem(R.drawable.ic_tool_mail, "Email karo", "seedha humein likho", 0xFFEA580C.toInt(), "Madad") { emailSupport() })
        items.add(ToolItem(R.drawable.ic_tool_info, "App ki jaankari", "version aur settings", 0xFF475569.toInt(), "Madad") { openAppSettings() })
        return items
    }

    private fun buildToolCard(dialog: Dialog, item: ToolItem): View {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.gravity = Gravity.CENTER_HORIZONTAL
        card.setPadding(dp(10), dp(14), dp(10), dp(12))
        val cardBg = GradientDrawable()
        cardBg.setColor(0xFFF8FAFC.toInt())
        cardBg.cornerRadius = dp(22).toFloat()
        cardBg.setStroke(dp(1), shade(item.accent, 0.86f))
        card.background = cardBg
        card.foreground = RippleDrawable(ColorStateList.valueOf(shade(item.accent, 0.82f)), null, null)
        card.elevation = dp(3).toFloat()

        val chip = FrameLayout(this)
        val chipBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(item.accent, 0.30f), item.accent))
        chipBg.cornerRadius = dp(17).toFloat()
        chip.background = chipBg
        chip.elevation = dp(4).toFloat()
        val icon = ImageView(this)
        icon.setImageResource(item.icon)
        chip.addView(icon, FrameLayout.LayoutParams(dp(26), dp(26), Gravity.CENTER))
        card.addView(chip, LinearLayout.LayoutParams(dp(50), dp(50)))

        val label = TextView(this)
        label.text = item.label
        label.textSize = 12.5f
        label.typeface = Typeface.DEFAULT_BOLD
        label.setTextColor(0xFF111827.toInt())
        label.gravity = Gravity.CENTER
        label.maxLines = 2
        val labelLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        labelLp.topMargin = dp(9)
        card.addView(label, labelLp)

        val sub = TextView(this)
        sub.text = item.sub
        sub.textSize = 10.5f
        sub.setTextColor(0xFF6B7280.toInt())
        sub.gravity = Gravity.CENTER
        sub.maxLines = 1
        card.addView(sub, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        card.setOnClickListener {
            bounce(card)
            dialog.dismiss()
            item.action()
        }
        return card
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
        val bar = navBar ?: return
        bar.animate().translationY((bar.height + dp(12)).toFloat()).setDuration(220).start()
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
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                val scheme = uri.scheme?.lowercase() ?: return false
                // Email / call / WhatsApp / Maps links — sab direct apni app me khulte hain
                if (scheme == "mailto" || scheme == "tel" || scheme == "sms" || scheme == "intent" || scheme == "whatsapp" || scheme == "geo") {
                    openExternal(uri)
                    return true
                }
                if (scheme == "http" || scheme == "https") {
                    // Google Maps / directions / email-compose links ko WebView me mat kholo — Maps/Gmail app khule
                    val linkHost = uri.host?.lowercase() ?: ""
                    val isMaps = linkHost == "maps.google.com" || linkHost.endsWith(".google.com") && (linkHost.startsWith("maps") || linkHost.startsWith("www.google.com")) && (uri.query ?: "").contains("directions")
                    val isGmail = linkHost == "mail.google.com"
                    if (isMaps || isGmail) {
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
                if (AD_BLOCK_ON) injectAdBlock(view)
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
