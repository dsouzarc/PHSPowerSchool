package com.ryan.phspowerschool;

import android.webkit.WebView;

/**
 * Created by Ryan on 1/12/14.
 */
public interface SourceCodeListener {
    public void onSourceCodeAcquired(WebView webView, String html);

    public void onProgressChanged(WebView webView, int progress);
}
