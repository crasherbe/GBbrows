package com.example.gbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    WebView webViewku;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webViewku = findViewById(R.id.web_view);
        webViewku.setWebChromeClient(new WebChromeClient());
        webViewku.loadUrl("https://www.google.com/");
        webViewku.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webViewku.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webViewku.canGoBack()){
            webViewku.goBack();
        }else {
        super.onBackPressed();
    }
}
    }