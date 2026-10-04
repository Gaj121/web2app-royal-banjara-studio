package site.rbstudiosmusic

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
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
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.io.File

class NavEntry(val label: String, val url: String, val icon: Int)

class ToolItem(val icon: String, val label: String, val sub: String, val action: () -> Unit)

class MainActivity : AppCompatActivity() {

    companion object {
        const val HOME_URL = "https://rbstudiosmusic.kliv.site/"
        const val HOME_HOST = "rbstudiosmusic.kliv.site"
        const val APP_NAME = "Royal Banjara Studio"
        const val THEME_COLOR = "#EC4899"
        const val SPLASH_COLOR = "#F8FAFC"
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
    }

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private var navBar: LinearLayout? = null
    private var introOverlay: FrameLayout? = null
    private var welcomeOverlay: FrameLayout? = null
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

    private val navEntries: Array<NavEntry> = arrayOf(
        NavEntry("Home", "https://rbstudiosmusic.kliv.site/", R.drawable.ic_nav_home),
        NavEntry("Login ", "https://rbstudiosmusic.kliv.site/login", R.drawable.ic_nav_grid),
        NavEntry("Contact", "https://rbstudiosmusic.kliv.site/contact", R.drawable.ic_nav_phone),
        NavEntry("Support ", "https://rbstudiosmusic.raiseaticket.com", R.drawable.ic_nav_chat)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        progressBar.progressTintList = ColorStateList.valueOf(themeColorInt)
        progressBar.progressBackgroundTintList = ColorStateList.valueOf(0x22888888)
        content.addView(progressBar, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(3)))

        webView = WebView(this)
        swipeRefresh = SwipeRefreshLayout(this)
        swipeRefresh.addView(webView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        if (PULL_REFRESH) {
            swipeRefresh.setOnRefreshListener { webView.reload() }
            swipeRefresh.setColorSchemeColors(themeColorInt)
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
            intArrayOf(shade(splashColorInt, -0.3f), splashColorInt, shade(themeColorInt, -0.5f))
        )
        overlay.background = bg

        val stack = LinearLayout(this)
        stack.orientation = LinearLayout.VERTICAL
        stack.gravity = Gravity.CENTER

        val logoCard = FrameLayout(this)
        val cardBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(Color.WHITE, shade(themeColorInt, 0.7f)))
        cardBg.cornerRadius = dp(32).toFloat()
        logoCard.background = cardBg
        logoCard.elevation = dp(22).toFloat()
        logoCard.setPadding(dp(12), dp(12), dp(12), dp(12))
        val logo = ImageView(this)
        logo.setImageResource(R.mipmap.ic_launcher)
        logoCard.addView(logo, FrameLayout.LayoutParams(dp(104), dp(104), Gravity.CENTER))

        val name = TextView(this)
        name.text = APP_NAME
        name.textSize = 21f
        name.setTextColor(Color.WHITE)
        name.typeface = Typeface.DEFAULT_BOLD
        name.gravity = Gravity.CENTER
        name.letterSpacing = 0.06f

        val tagline = TextView(this)
        tagline.text = "Loading ho raha hai..."
        tagline.textSize = 13f
        tagline.setTextColor(0xB3FFFFFF.toInt())
        tagline.gravity = Gravity.CENTER

        val spinner = ProgressBar(this)
        spinner.indeterminateTintList = ColorStateList.valueOf(themeColorInt)

        stack.addView(logoCard, LinearLayout.LayoutParams(dp(128), dp(128)))
        val nameLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        nameLp.topMargin = dp(22)
        stack.addView(name, nameLp)
        val tagLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tagLp.topMargin = dp(6)
        stack.addView(tagline, tagLp)
        val spLp = LinearLayout.LayoutParams(dp(38), dp(38))
        spLp.topMargin = dp(18)
        stack.addView(spinner, spLp)

        overlay.addView(stack, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        logoCard.scaleX = 0.35f
        logoCard.scaleY = 0.35f
        logoCard.alpha = 0f
        name.alpha = 0f
        name.translationY = dp(16).toFloat()
        tagline.alpha = 0f

        logoCard.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(480).setInterpolator(OvershootInterpolator(1.5f)).start()
        logoCard.postDelayed({
            name.animate().alpha(1f).translationY(0f).setDuration(380).start()
            tagline.animate().alpha(1f).setDuration(380).start()
        }, 320)

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
        ov.animate().translationY(0f).setDuration(560).setInterpolator(OvershootInterpolator(0.8f)).start()
    }

    private fun dismissWelcome() {
        val ov = welcomeOverlay ?: return
        ov.animate().translationY(-ov.height.toFloat()).alpha(0f).setDuration(400)
            .withEndAction { ov.visibility = View.GONE }.start()
    }

    private fun buildWelcomeSlider(): FrameLayout {
        val overlay = FrameLayout(this)
        val scrim = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(shade(splashColorInt, -0.4f), splashColorInt, shade(splashColorInt, -0.55f))
        )
        overlay.background = scrim

        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.gravity = Gravity.CENTER_HORIZONTAL
        card.setPadding(dp(26), dp(30), dp(26), dp(26))
        val cardBg = GradientDrawable()
        cardBg.setColor(Color.WHITE)
        cardBg.cornerRadius = dp(30).toFloat()
        card.background = cardBg
        card.elevation = dp(24).toFloat()
        card.setOnClickListener { }

        val flipper = ViewFlipper(this)
        flipper.isAutoStart = true
        flipper.flipInterval = 3600
        val inAnim = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        inAnim.duration = 420
        val outAnim = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        outAnim.duration = 420
        flipper.inAnimation = inAnim
        flipper.outAnimation = outAnim

        val slides = listOf(
            Triple("👋", WELCOME_TEXT, "Aapka poora website — ab ek asli app me"),
            Triple("⬇️", "Download & Save", "File, photo, PDF seedha phone ke Downloads folder me save"),
            Triple("🖨️", "Print & Share", "⋮ button se page print karo ya PDF bana kar bhejo")
        )

        slides.forEach { slideData ->
            val slide = LinearLayout(this)
            slide.orientation = LinearLayout.VERTICAL
            slide.gravity = Gravity.CENTER
            val bigIcon = TextView(this)
            bigIcon.text = slideData.first
            bigIcon.textSize = 42f
            bigIcon.gravity = Gravity.CENTER
            val title = TextView(this)
            title.text = slideData.second
            title.textSize = 19f
            title.typeface = Typeface.DEFAULT_BOLD
            title.setTextColor(0xFF17181C.toInt())
            title.gravity = Gravity.CENTER
            title.setPadding(dp(4), 0, dp(4), 0)
            val sub = TextView(this)
            sub.text = slideData.third
            sub.textSize = 13f
            sub.setTextColor(0xFF6B7078.toInt())
            sub.gravity = Gravity.CENTER
            sub.setPadding(dp(10), 0, dp(10), 0)
            slide.addView(bigIcon)
            val titleLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            titleLp.topMargin = dp(10)
            slide.addView(title, titleLp)
            val subLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            subLp.topMargin = dp(4)
            slide.addView(sub, subLp)
            flipper.addView(slide, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        }

        val dots = mutableListOf<TextView>()
        val dotsRow = LinearLayout(this)
        dotsRow.orientation = LinearLayout.HORIZONTAL
        dotsRow.gravity = Gravity.CENTER_HORIZONTAL
        repeat(slides.size) {
            val dot = TextView(this)
            dot.text = "•"
            dot.textSize = 18f
            dot.setTextColor(0xFFC9CDD4.toInt())
            dots.add(dot)
            val dotLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dotLp.setMargins(dp(6), 0, dp(6), 0)
            dotsRow.addView(dot, dotLp)
        }

        fun updateDots(index: Int) {
            dots.forEachIndexed { i, d ->
                d.setTextColor(if (i == index) themeColorInt else 0xFFC9CDD4.toInt())
                d.textSize = if (i == index) 26f else 18f
            }
        }

        val startBtn = TextView(this)
        startBtn.text = "Get Started"
        startBtn.textSize = 15f
        startBtn.setTextColor(Color.WHITE)
        startBtn.typeface = Typeface.DEFAULT_BOLD
        startBtn.gravity = Gravity.CENTER
        startBtn.setPadding(dp(34), dp(13), dp(34), dp(13))
        val btnBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(themeColorInt, 0.3f), themeColorInt))
        btnBg.cornerRadius = dp(26).toFloat()
        startBtn.background = btnBg
        startBtn.elevation = dp(8).toFloat()

        val flipLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(190))
        card.addView(flipper, flipLp)
        val dotsLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dotsLp.topMargin = dp(14)
        card.addView(dotsRow, dotsLp)
        val startLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        startLp.topMargin = dp(20)
        card.addView(startBtn, startLp)

        overlay.addView(card, FrameLayout.LayoutParams(dp(330), FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER))

        val skip = TextView(this)
        skip.text = "Skip"
        skip.setTextColor(Color.WHITE)
        skip.textSize = 13f
        skip.setPadding(dp(12), dp(6), dp(12), dp(6))
        skip.background = RippleDrawable(ColorStateList.valueOf(0x33FFFFFF), null, null)
        val skipLp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP or Gravity.END)
        skipLp.topMargin = dp(20)
        skipLp.rightMargin = dp(18)
        overlay.addView(skip, skipLp)

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
                    flipper.postDelayed(this, 3600)
                }
            }
        }
        flipper.postDelayed(sync, 3600)

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

    private fun buildMoreButton(): TextView {

        val btn = TextView(this)
        btn.text = "⋮"
        btn.textSize = 20f
        btn.setTextColor(Color.WHITE)
        btn.typeface = Typeface.DEFAULT_BOLD
        btn.gravity = Gravity.CENTER
        val bg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(themeColorInt, 0.35f), themeColorInt, shade(themeColorInt, -0.25f)))
        bg.shape = GradientDrawable.OVAL
        bg.setStroke(dp(2), Color.WHITE)
        btn.background = bg
        btn.elevation = dp(12).toFloat()
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

        val header = LinearLayout(this)
        header.orientation = LinearLayout.HORIZONTAL
        header.gravity = Gravity.CENTER_VERTICAL
        val headLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        headLp.topMargin = dp(10)
        headLp.bottomMargin = dp(4)
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

        val items = buildToolItems()
        val grid = LinearLayout(this)
        grid.orientation = LinearLayout.VERTICAL
        var row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        items.forEachIndexed { index, item ->
            if (index > 0 && index % 2 == 0) {
                grid.addView(row, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                row = LinearLayout(this)
                row.orientation = LinearLayout.HORIZONTAL
            }
            val card = buildToolCard(dialog, item)
            val cardLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            if (index % 2 == 0) cardLp.rightMargin = dp(5) else cardLp.leftMargin = dp(5)
            row.addView(card, cardLp)
        }
        if (items.isNotEmpty()) {
            grid.addView(row, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        }
        sheet.addView(grid, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

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
        sheet.animate().translationY(0f).alpha(1f).setDuration(280).setInterpolator(OvershootInterpolator(1.05f)).start()
    }

    private fun buildToolItems(): List<ToolItem> {
        val items = mutableListOf<ToolItem>()
        items.add(ToolItem("🖨️", "Print / PDF", "page ya PDF banao") { printPage() })
        items.add(ToolItem("📥", "Mere Downloads", "app ki hi list") { showDownloadsSheet() })
        items.add(ToolItem("📂", "Downloads folder", "phone ka folder") { openDownloads() })
        items.add(ToolItem("🔄", "Refresh page", "dobara load") { webView.reload() })
        items.add(ToolItem("🏠", "Home page", "shuruati page") { webView.loadUrl(HOME_URL) })
        items.add(ToolItem(if (nightOn) "☀️" else "🌙", if (nightOn) "Day mode karo" else "Night mode karo", "aankhon ko aaram") { toggleNightMode() })
        items.add(ToolItem("A+", "Text bada karo", "padhna aasan") { changeTextSize(15) })
        items.add(ToolItem("A−", "Text chhota karo", "compact view") { changeTextSize(-15) })
        items.add(ToolItem("🧹", "Cache clear", "speed badhao") { clearAppCache() })
        items.add(ToolItem("🚪", "App band karo", "seedha close") { finishAffinity() })
        return items
    }

    private fun buildToolCard(dialog: Dialog, item: ToolItem): View {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.gravity = Gravity.CENTER_HORIZONTAL
        card.setPadding(dp(10), dp(14), dp(10), dp(12))
        val cardBg = GradientDrawable()
        cardBg.setColor(0xFFF4F6FA.toInt())
        cardBg.cornerRadius = dp(18).toFloat()
        card.background = cardBg
        card.foreground = RippleDrawable(ColorStateList.valueOf(0x2258606E), null, null)
        card.elevation = dp(2).toFloat()

        val iconCircle = FrameLayout(this)
        val circleBg = GradientDrawable()
        circleBg.shape = GradientDrawable.OVAL
        circleBg.setColor(shade(themeColorInt, 0.90f))
        iconCircle.background = circleBg
        val icon = TextView(this)
        icon.text = item.icon
        icon.textSize = 21f
        icon.typeface = Typeface.DEFAULT_BOLD
        icon.gravity = Gravity.CENTER
        iconCircle.addView(icon, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        card.addView(iconCircle, LinearLayout.LayoutParams(dp(48), dp(48)))

        val label = TextView(this)
        label.text = item.label
        label.textSize = 12.5f
        label.typeface = Typeface.DEFAULT_BOLD
        label.setTextColor(0xFF111827.toInt())
        label.gravity = Gravity.CENTER
        label.maxLines = 2
        val labelLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        labelLp.topMargin = dp(8)
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
        try {
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("User-Agent", userAgent)
            request.setTitle(fileName)
            request.setDescription(APP_NAME)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            lastDownloadId = (getSystemService(DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
            downloadIds.add(lastDownloadId)
            downloadMimes[lastDownloadId] = mimeType
            Toast.makeText(this, "Download shuru — " + fileName, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            openExternal(Uri.parse(url))
        }
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
        share.setTextColor(themeColorInt)
        val shareBg = GradientDrawable()
        shareBg.setColor(shade(themeColorInt, 0.88f))
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
        val btnBg = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(shade(themeColorInt, 0.35f), themeColorInt))
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
        navIcons.forEachIndexed { i, icon -> icon.setColorFilter(if (i == index) Color.WHITE else 0xFF8A8F98.toInt()) }
        navLabels.forEachIndexed { i, label ->
            label.setTextColor(if (i == index) themeColorInt else 0xFF8A8F98.toInt())
            label.typeface = if (i == index) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
        pillBackgrounds.forEachIndexed { i, pill ->
            pill.orientation = GradientDrawable.Orientation.TL_BR
            if (i == index) {
                pill.setColors(intArrayOf(shade(themeColorInt, 0.35f), themeColorInt))
            } else {
                pill.setColor(0x00000000)
            }
        }
        pillHolders.forEachIndexed { i, holder -> holder.elevation = if (i == index) dp(6).toFloat() else 0f }
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
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                val scheme = uri.scheme?.lowercase() ?: return false
                if (scheme == "mailto" || scheme == "tel" || scheme == "sms" || scheme == "intent" || scheme == "whatsapp") {
                    openExternal(uri)
                    return true
                }
                if (scheme == "http" || scheme == "https") {
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

    private fun openExternal(uri: Uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Koi app nahi mila is link ke liye", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showOffline() {
        val html = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1'><style>body{font-family:sans-serif;background:" + SPLASH_COLOR + ";color:#E8EDF4;display:flex;align-items:center;justify-content:center;height:100vh;margin:0;text-align:center}h2{margin:10px 0 4px}p{opacity:0.7;margin:0 0 6px}button{background-color:" + THEME_COLOR + ";color:#0C0F14;border:0;padding:13px 26px;border-radius:10px;font-size:15px;font-weight:700;margin-top:16px}</style></head><body><div><div style='font-size:52px'>&#128246;</div><h2>No Internet</h2><p>Internet connection check karein aur dobara try karein.</p><button onclick=\"location.href='" + HOME_URL + "'\">Retry</button></div></body></html>"
        webView.loadDataWithBaseURL(HOME_URL, html, "text/html", "utf-8", HOME_URL)
    }
}
