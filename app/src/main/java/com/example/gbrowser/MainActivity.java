package com.example.gbrowser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import static com.example.gbrowser.R.id.search_bar;

public class MainActivity extends AppCompatActivity{
    WebView webViewku;
    WebSettings Settings;
    ProgressBar progressBar;
    SearchView searchView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webViewku = findViewById(R.id.WebKU);
        progressBar = findViewById(R.id.progressBar);



        WebSettings Settings = webViewku.getSettings();
        Settings.setDisplayZoomControls(false);
        Settings.supportZoom();
        Settings.setSupportZoom(true);
        Settings.setJavaScriptEnabled(true);
        Settings.setBuiltInZoomControls(true);
        Settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        Settings.setLoadWithOverviewMode(true);
        Settings.setUseWideViewPort(true);

        webViewku.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                invalidateOptionsMenu();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                invalidateOptionsMenu();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });






        AdBlocker.init(this);

        progressBar.setMax(100);
        webViewku.setWebViewClient(new WebViewClient());
        webViewku.setWebViewClient(new MyBrowser());

        progressBar.setProgress(0);

        webViewku.loadUrl("https://www.google.com");

        webViewku.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);

                super.onProgressChanged(view, newProgress);

            }

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
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

    private class MyBrowser extends WebViewClient{
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        if (webViewku.canGoForward())
        {
            menu.getItem(1).setEnabled(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        invalidateOptionsMenu();
        if (id==R.id.rfs)
        {
            webViewku.reload();
        }
        if (id==R.id.forward)
        {
            if (webViewku.canGoForward())
            {
                webViewku.goBack();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}




