package com.example.gbrowser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private WebView webViewku;
    private WebSettings webSettings;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdBlocker.init(this);

        webViewku = (WebView) findViewById(R.id.web_view);
        webViewku.setWebChromeClient(new WebChromeClient());
        webViewku.setWebViewClient(new MyBrowser());

        WebSettings webSettings = webViewku.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webViewku.loadUrl("https://www.google.com/");

    }

    int counter = 0;

    @Override
    public void onBackPressed() {
        if (webViewku.canGoBack()) {
            webViewku.goBack();
        } else {
            counter++;
            if (counter == 2)
                super.onBackPressed();
        }
    }
        private class MyBrowser extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            private Map<String, Boolean> loadedUrls = new HashMap<>();

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                boolean ad;
                if (!loadedUrls.containsKey(url)) {
                    ad = AdBlocker.isAd(url);
                    loadedUrls.put(url, ad);
                } else {
                    ad = loadedUrls.get(url);
                }
                return ad ? AdBlocker.createEmptyResource() :
                        super.shouldInterceptRequest(view, url);
            }

        }
    }
