package site.rbstudiosmusic

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.URLUtil
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class NavEntry(val label: String, val url: String, val icon: Int)

class MainActivity : AppCompatActivity() {

    companion object {
        const val HOME_URL = "https://rbstudiosmusic.kliv.site/"
        const val HOME_HOST = "rbstudiosmusic.kliv.site"
        const val THEME_COLOR = "#3DDC84"
        const val SPLASH_COLOR = "#0C0F14"
        const val HIDE_ON = true
        const val HIDE_CSS = "footer{display:none !important;}.footer{display:none !important;}#footer{display:none !important;}.site-footer{display:none !important;}#powered-by{display:none !important;}.powered-by{display:none !important;}#credit{display:none !important;}[data-kliv-badge]{display:none !important;}.kliv-badge{display:none !important;}#kliv-badge{display:none !important;}[class*=\"kliv-badge\"]{display:none !important;}[id*=\"kliv-badge\"]{display:none !important;}a[href*=\"kliv.site\"]{display:none !important;}a[href*=\"kliv.com\"]{display:none !important;}a[href*=\"kliv.dev\"]{display:none !important;}[data-kliv-footer]{display:none !important;}[class*=\"kliv-footer\"]{display:none !important;}[id*=\"kliv-footer\"]{display:none !important;}"
        const val HIDE_JS = "(function(){\nif(window.__web2appHide){window.__web2appHide();return;}\nvar CSS=\"footer{display:none !important;}.footer{display:none !important;}#footer{display:none !important;}.site-footer{display:none !important;}#powered-by{display:none !important;}.powered-by{display:none !important;}#credit{display:none !important;}[data-kliv-badge]{display:none !important;}.kliv-badge{display:none !important;}#kliv-badge{display:none !important;}[class*=\\\"kliv-badge\\\"]{display:none !important;}[id*=\\\"kliv-badge\\\"]{display:none !important;}a[href*=\\\"kliv.site\\\"]{display:none !important;}a[href*=\\\"kliv.com\\\"]{display:none !important;}a[href*=\\\"kliv.dev\\\"]{display:none !important;}[data-kliv-footer]{display:none !important;}[class*=\\\"kliv-footer\\\"]{display:none !important;}[id*=\\\"kliv-footer\\\"]{display:none !important;}\";\nvar PATTERNS=[\"created with kliv\",\"made with kliv\",\"powered by kliv\",\"built with kliv\",\"made with wix\",\"created with wix\",\"this site was made with wix\",\"powered by wix\",\"powered by wordpress\",\"proudly powered by wordpress\",\"powered by wordpress.com\",\"built on godaddy\",\"created with godaddy\",\"powered by shopify\",\"made in webflow\",\"made with webflow\",\"made with carrd\",\"made on carrd\",\"powered by squarespace\",\"powered by weebly\",\"powered by jimdo\",\"made with tilda\",\"built on tilda\",\"powered by blogger\",\"website created with\",\"website made with\",\"this site was created with\",\"this website was created with\",\"created by kliv\",\"made by kliv\",\"built by kliv\",\"designed by kliv\",\"website by kliv\",\"site by kliv\",\"hosted on kliv\",\"kliv.site\"];\nvar MAX=200;\nfunction applyCss(){\n var s=document.getElementById('web2app-hide-css');\n if(!s){s=document.createElement('style');s.id='web2app-hide-css';(document.head||document.documentElement).appendChild(s);}\n s.textContent=CSS;\n}\nfunction hit(t){for(var i=0;i<PATTERNS.length;i++){if(t.indexOf(PATTERNS[i])!==-1){return true;}}return false;}\nfunction fullText(e){return (e.textContent||'').replace(/\\s+/g,' ').trim().toLowerCase();}\nfunction hideEl(e){e.setAttribute('data-web2app-hidden','1');e.style.setProperty('display','none','important');\n var p=e.parentElement,k=0;\n while(p&&p!==document.body&&k<4){var pt=fullText(p);\n  if(p.children.length<=2&&pt&&pt.length<=MAX&&hit(pt)){p.setAttribute('data-web2app-hidden','1');p.style.setProperty('display','none','important');p=p.parentElement;k++;}else{break;}}}\nfunction hideByText(){if(!PATTERNS.length){return;}\n var n=document.querySelectorAll('a,div,span,p,small,li,section,aside,footer,i,b,em,strong,label,h1,h2,h3,h4,h5,h6,button');\n for(var i=0;i<n.length;i++){var e=n[i];\n  if(e.getAttribute('data-web2app-hidden')){continue;}\n  var t=fullText(e);\n  if(t&&t.length<=MAX&&hit(t)){hideEl(e);}\n }}\nfunction run(){try{applyCss();hideByText();}catch(err){}}\nwindow.__web2appHide=run;\nrun();\nvar tmr=null;\ntry{\n new MutationObserver(function(){if(tmr){clearTimeout(tmr);}tmr=setTimeout(run,150);}).observe(document.documentElement||document.body,{childList:true,subtree:true});\n}catch(err){}\nwindow.addEventListener('load',function(){run();});\n})();"
        const val SHOW_NAV = true
        const val OPEN_EXTERNAL = true
        const val OFFLINE_PAGE = true
        const val PULL_REFRESH = true
    }

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var splashOverlay: FrameLayout
    private val navIcons = mutableListOf<ImageView>()
    private val navLabels = mutableListOf<TextView>()
    private val themeColorInt: Int by lazy { Color.parseColor(THEME_COLOR) }

    private val navEntries: Array<NavEntry> = arrayOf(
        NavEntry("Home", "https://example.com/", R.drawable.ic_nav_home),
        NavEntry("Products", "https://example.com/products", R.drawable.ic_nav_user),
        NavEntry("Contact", "https://example.com/contact", R.drawable.ic_nav_phone)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            content.addView(buildNavBar(), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
        }

        root.addView(content, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        splashOverlay = buildSplash()
        root.addView(splashOverlay, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

        setContentView(root)

        setupWebView()
        selectNav(0)
        webView.loadUrl(HOME_URL)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) webView.goBack() else finish()
            }
        })

        swipeRefresh.postDelayed({
            splashOverlay.animate().alpha(0f).setDuration(350).withEndAction { splashOverlay.visibility = View.GONE }
        }, 650)
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun buildSplash(): FrameLayout {
        val overlay = FrameLayout(this)
        overlay.setBackgroundColor(Color.parseColor(SPLASH_COLOR))
        val icon = ImageView(this)
        icon.setImageResource(R.mipmap.ic_launcher)
        overlay.addView(icon, FrameLayout.LayoutParams(dp(96), dp(96), Gravity.CENTER))
        return overlay
    }

    private fun buildNavBar(): LinearLayout {
        val bar = LinearLayout(this)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.setBackgroundColor(Color.WHITE)
        navEntries.forEachIndexed { index, entry ->
            val item = LinearLayout(this)
            item.orientation = LinearLayout.VERTICAL
            item.gravity = Gravity.CENTER
            item.setPadding(dp(4), dp(7), dp(4), dp(7))

            val icon = ImageView(this)
            icon.setImageResource(entry.icon)
            icon.setColorFilter(0xFF757575.toInt())
            icon.layoutParams = LinearLayout.LayoutParams(dp(24), dp(24))

            val label = TextView(this)
            label.text = entry.label
            label.textSize = 11f
            label.setTextColor(0xFF757575.toInt())

            item.addView(icon)
            item.addView(label)
            item.setOnClickListener { selectNav(index); webView.loadUrl(entry.url) }
            navIcons.add(icon)
            navLabels.add(label)
            bar.addView(item, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        }
        return bar
    }

    private fun selectNav(index: Int) {
        if (!SHOW_NAV) return
        navIcons.forEachIndexed { i, icon -> icon.setColorFilter(if (i == index) themeColorInt else 0xFF757575.toInt()) }
        navLabels.forEachIndexed { i, label -> label.setTextColor(if (i == index) themeColorInt else 0xFF757575.toInt()) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.setSupportZoom(false)
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
        }

        webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
            try {
                val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
                val request = DownloadManager.Request(Uri.parse(url))
                request.setMimeType(mimeType)
                request.addRequestHeader("User-Agent", userAgent)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                (getSystemService(DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
                Toast.makeText(this@MainActivity, "Download shuru...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                openExternal(Uri.parse(url))
            }
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
