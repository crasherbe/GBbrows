package com.example.gbrowser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private WebView webViewku;
    private WebSettings webSettings;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdBlocker.init(this);

        webViewku = (WebView) findViewById(R.id.web_view);
        webViewku.setWebViewClient(new MyBrowser());

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        webViewku.setWebViewClient(new WebViewClient());
        webViewku.loadUrl("https://www.google.com/");
        progressBar.setProgress(0);

        webViewku.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress==100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);

                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
            }
        });


        WebSettings webSettings = webViewku.getSettings();
        webSettings.setJavaScriptEnabled(true);



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
