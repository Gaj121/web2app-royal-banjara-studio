import UIKit
import WebKit
import SafariServices

struct NavEntry {
    let label: String
    let url: String
    let symbol: String
}

class WebViewController: UIViewController, WKNavigationDelegate {
    static let HOME_URL = "https://rbstudiosmusic.kliv.site/"
    static let HOME_HOST = "rbstudiosmusic.kliv.site"
    static let THEME_COLOR = "#EC4899"
    static let SPLASH_COLOR = "#2B0A1D"
    static let SHOW_NAV = true
    static let HIDE_ON = true
    static let HIDE_JS = "(function(){\nif(window.__web2appHide){window.__web2appHide();return;}\nvar CSS=\"footer{display:none !important;}.footer{display:none !important;}#footer{display:none !important;}.site-footer{display:none !important;}#powered-by{display:none !important;}.powered-by{display:none !important;}#credit{display:none !important;}[data-kliv-badge]{display:none !important;}.kliv-badge{display:none !important;}#kliv-badge{display:none !important;}[class*=\\\"kliv-badge\\\"]{display:none !important;}[id*=\\\"kliv-badge\\\"]{display:none !important;}a[href*=\\\"kliv.site\\\"]{display:none !important;}a[href*=\\\"kliv.com\\\"]{display:none !important;}a[href*=\\\"kliv.dev\\\"]{display:none !important;}[data-kliv-footer]{display:none !important;}[class*=\\\"kliv-footer\\\"]{display:none !important;}[id*=\\\"kliv-footer\\\"]{display:none !important;}\";\nvar PATTERNS=[\"created with kliv\",\"made with kliv\",\"powered by kliv\",\"built with kliv\",\"made with wix\",\"created with wix\",\"this site was made with wix\",\"powered by wix\",\"powered by wordpress\",\"proudly powered by wordpress\",\"powered by wordpress.com\",\"built on godaddy\",\"created with godaddy\",\"powered by shopify\",\"made in webflow\",\"made with webflow\",\"made with carrd\",\"made on carrd\",\"powered by squarespace\",\"powered by weebly\",\"powered by jimdo\",\"made with tilda\",\"built on tilda\",\"powered by blogger\",\"website created with\",\"website made with\",\"this site was created with\",\"this website was created with\",\"created by kliv\",\"made by kliv\",\"built by kliv\",\"designed by kliv\",\"website by kliv\",\"site by kliv\",\"hosted on kliv\",\"kliv.site\"];\nvar MAX=200;\nfunction applyCss(){\n var s=document.getElementById('web2app-hide-css');\n if(!s){s=document.createElement('style');s.id='web2app-hide-css';(document.head||document.documentElement).appendChild(s);}\n s.textContent=CSS;\n}\nfunction hit(t){for(var i=0;i<PATTERNS.length;i++){if(t.indexOf(PATTERNS[i])!==-1){return true;}}return false;}\nfunction fullText(e){return (e.textContent||'').replace(/\\s+/g,' ').trim().toLowerCase();}\nfunction hideEl(e){e.setAttribute('data-web2app-hidden','1');e.style.setProperty('display','none','important');\n var p=e.parentElement,k=0;\n while(p&&p!==document.body&&k<4){var pt=fullText(p);\n  if(p.children.length<=2&&pt&&pt.length<=MAX&&hit(pt)){p.setAttribute('data-web2app-hidden','1');p.style.setProperty('display','none','important');p=p.parentElement;k++;}else{break;}}}\nfunction hideByText(){if(!PATTERNS.length){return;}\n var n=document.querySelectorAll('a,div,span,p,small,li,section,aside,footer,i,b,em,strong,label,h1,h2,h3,h4,h5,h6,button');\n for(var i=0;i<n.length;i++){var e=n[i];\n  if(e.getAttribute('data-web2app-hidden')){continue;}\n  var t=fullText(e);\n  if(t&&t.length<=MAX&&hit(t)){hideEl(e);}\n }}\nfunction run(){try{applyCss();hideByText();}catch(err){}}\nwindow.__web2appHide=run;\nrun();\nvar tmr=null;\ntry{\n new MutationObserver(function(){if(tmr){clearTimeout(tmr);}tmr=setTimeout(run,150);}).observe(document.documentElement||document.body,{childList:true,subtree:true});\n}catch(err){}\nwindow.addEventListener('load',function(){run();});\n})();"

    private var webView: WKWebView!
    private let navEntries: [NavEntry] = [
        NavEntry(label: "Home", url: "https://rbstudiosmusic.kliv.site/", symbol: "house"),
        NavEntry(label: "Products", url: "https://rbstudiosmusic.kliv.site/products", symbol: "square.grid.2x2"),
        NavEntry(label: "Contact", url: "https://rbstudiosmusic.kliv.site/contact", symbol: "phone"),
    ]
    private var navIcons: [UIImageView] = []
    private var navLabels: [UILabel] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = color(fromHex: WebViewController.SPLASH_COLOR)
        let content = WKUserContentController()
        if WebViewController.HIDE_ON {
            let endScript = WKUserScript(source: WebViewController.HIDE_JS, injectionTime: .atDocumentEnd, forMainFrameOnly: false)
            let startScript = WKUserScript(source: WebViewController.HIDE_JS, injectionTime: .atDocumentStart, forMainFrameOnly: false)
            content.addUserScript(endScript)
            content.addUserScript(startScript)
        }
        let config = WKWebViewConfiguration()
        config.userContentController = content
        config.allowsInlineMediaPlayback = true
        webView = WKWebView(frame: .zero, configuration: config)
        webView.navigationDelegate = self
        webView.backgroundColor = UIColor.white
        webView.allowsBackForwardNavigationGestures = true
        view.addSubview(webView)
        webView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            webView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            webView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            webView.trailingAnchor.constraint(equalTo: view.trailingAnchor)
        ])
        if WebViewController.SHOW_NAV {
            let wrap = buildNavBar()
            webView.bottomAnchor.constraint(equalTo: wrap.topAnchor).isActive = true
        } else {
            webView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor).isActive = true
        }
        if let url = URL(string: WebViewController.HOME_URL) {
            webView.load(URLRequest(url: url))
        }
    }

    private func buildNavBar() -> UIView {
        let wrap = UIView()
        wrap.backgroundColor = UIColor.white
        view.addSubview(wrap)
        wrap.translatesAutoresizingMaskIntoConstraints = false
        let bar = UIStackView()
        bar.axis = .horizontal
        bar.distribution = .fillEqually
        bar.alignment = .center
        wrap.addSubview(bar)
        bar.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            wrap.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            wrap.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            wrap.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            bar.topAnchor.constraint(equalTo: wrap.safeAreaLayoutGuide.topAnchor),
            bar.leadingAnchor.constraint(equalTo: wrap.leadingAnchor),
            bar.trailingAnchor.constraint(equalTo: wrap.trailingAnchor),
            bar.heightAnchor.constraint(equalToConstant: 54)
        ])
        for (index, entry) in navEntries.enumerated() {
            bar.addArrangedSubview(makeNavItem(entry: entry, index: index))
        }
        selectNav(0)
        return wrap
    }

    private func makeNavItem(entry: NavEntry, index: Int) -> UIStackView {
        let item = UIStackView()
        item.axis = .vertical
        item.alignment = .center
        item.spacing = 2
        item.isUserInteractionEnabled = true
        item.tag = index
        let icon = UIImageView()
        if let image = UIImage(systemName: entry.symbol) {
            icon.image = image.withRenderingMode(.alwaysTemplate)
        }
        icon.contentMode = .scaleAspectFit
        icon.tintColor = UIColor.darkGray
        icon.translatesAutoresizingMaskIntoConstraints = false
        icon.widthAnchor.constraint(equalToConstant: 22).isActive = true
        icon.heightAnchor.constraint(equalToConstant: 22).isActive = true
        let label = UILabel()
        label.text = entry.label
        label.font = UIFont.systemFont(ofSize: 10, weight: .semibold)
        label.textColor = UIColor.darkGray
        item.addArrangedSubview(icon)
        item.addArrangedSubview(label)
        navIcons.append(icon)
        navLabels.append(label)
        let tap = UITapGestureRecognizer(target: self, action: #selector(navTapped(_:)))
        item.addGestureRecognizer(tap)
        return item
    }

    @objc private func navTapped(_ sender: UITapGestureRecognizer) {
        let index = sender.view?.tag ?? 0
        guard navEntries.indices.contains(index) else { return }
        selectNav(index)
        if let url = URL(string: navEntries[index].url) {
            webView.load(URLRequest(url: url))
        }
    }

    private func selectNav(_ index: Int) {
        let tint = color(fromHex: WebViewController.THEME_COLOR)
        for (i, icon) in navIcons.enumerated() { icon.tintColor = i == index ? tint : UIColor.darkGray }
        for (i, label) in navLabels.enumerated() { label.textColor = i == index ? tint : UIColor.darkGray }
    }

    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        guard let url = navigationAction.request.url else { decisionHandler(.allow); return }
        let scheme = url.scheme?.lowercased() ?? ""
        if scheme == "mailto" || scheme == "tel" || scheme == "sms" {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
            decisionHandler(.cancel)
            return
        }
        if scheme == "http" || scheme == "https" {
            let host = url.host?.lowercased() ?? ""
            let sameSite = host == WebViewController.HOME_HOST || host.hasSuffix("." + WebViewController.HOME_HOST)
            if !sameSite {
                present(SFSafariViewController(url: url), animated: true, completion: nil)
                decisionHandler(.cancel)
                return
            }
        }
        decisionHandler(.allow)
    }

    private func color(fromHex hex: String) -> UIColor {
        var value: UInt64 = 0
        Scanner(string: hex.replacingOccurrences(of: "#", with: "")).scanHexInt64(&value)
        return UIColor(
            red: CGFloat((value >> 16) & 0xFF) / 255.0,
            green: CGFloat((value >> 8) & 0xFF) / 255.0,
            blue: CGFloat(value & 0xFF) / 255.0,
            alpha: 1
        )
    }
}
