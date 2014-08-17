package com.ryan.phspowerschool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.LinkedList;

public class WebBrowser extends WebView {

    private final SharedPreferences prefs;
    private boolean loading = false;
    private final LinkedList<SourceCodeListener> sourceCodeListeners = new LinkedList<SourceCodeListener>();

    @SuppressLint("SetJavaScriptEnabled")
    public WebBrowser(Context context) {
        super(context);

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // Inject javascript that forces source code to be printed to the
        // console
        setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String address) {
                // have the page spill its guts, with a secret prefix
                view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
            }
        });

        // Intercept console messages that contain source code
        setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                for (SourceCodeListener listener : sourceCodeListeners) {
                    // Notify all listeners that progress has changed
                    listener.onProgressChanged(view, progress);
                }
                loading = true;
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                // check secret prefix
                if (cmsg.message().startsWith("MAGIC") && !getUrl().contains("Action=Logout")) {
                    String html = cmsg.message().substring(5);
                    try
                    {
                        for (SourceCodeListener listener : sourceCodeListeners) {
                            // Notify all listeners that source code has been
                            // aqcuired
                            listener.onSourceCodeAcquired(WebBrowser.this, html);
                        }
                    }
                    catch (Exception e) { e.printStackTrace(); }
                    loading = false;
                }
                return true;
            }
        });
    }

    public void logout() {
        prefs.edit().putBoolean("signedIn", false).commit();
        clickButton("btnLogout");
        android.webkit.CookieManager.getInstance().removeAllCookie();
    }

    public void addSourceCodeListener(SourceCodeListener listener) {
        sourceCodeListeners.add(listener);
    }

    public void clickButton(String id) {
        loadUrl("javascript:(function(){document.getElementById('" + id + "').click();})()");
    }

    public void passParamToMethod(String methodName, String parameter) {
        try {
            loadUrl("javascript:" + methodName + "('" + parameter + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillInTextField(String id, String text) {
        loadUrl("javascript:(function(){document.getElementById('" + id + "').value='" + text + "';})()");
    }

    public void fillInTextFieldName(String name, String text) {
        loadUrl("javascript:(function(){document.getElementsByName('" + name + "')[0].value='" + text + "';})()");
    }
}
