/**
 * Royal Banjara Studio — Windows + Mac desktop app (Electron).
 * Website ko asli desktop app window me chalata hai.
 */
const { app, BrowserWindow, Menu, shell } = require("electron");
const path = require("path");

const SITE_URL = "https://rbstudiosmusic.kliv.site/";
const APP_NAME = "Royal Banjara Studio";
const BG_COLOR = "#06202E";

function createWindow() {
  const win = new BrowserWindow({
    width: 1280,
    height: 832,
    minWidth: 360,
    minHeight: 480,
    backgroundColor: BG_COLOR,
    title: APP_NAME,
    icon: path.join(__dirname, "icon.png"),
    autoHideMenuBar: true,
    show: false,
    webPreferences: {
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: true,
    },
  });

  Menu.setApplicationMenu(null);

  // Payment / social links default browser me khulen — app window me site chalti rahe
  win.webContents.setWindowOpenHandler(({ url }) => {
    if (/^https?:/i.test(url)) shell.openExternal(url);
    return { action: "deny" };
  });
  win.webContents.on("will-navigate", (event, url) => {
    if (!/^https?:/i.test(url)) {
      event.preventDefault();
      shell.openExternal(url);
    }
  });

  // Downloads (PDF, file) seedha Downloads folder me save
  win.webContents.session.on("will-download", (event, item) => {
    const fileName = item.getFilename() || "download";
    item.setSavePath(path.join(app.getPath("downloads"), fileName));
  });

  win.once("ready-to-show", () => win.show());
  win.loadURL(SITE_URL);
  win.on("closed", () => {});
}

app.whenReady().then(() => {
  createWindow();
  app.on("activate", () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") app.quit();
});
