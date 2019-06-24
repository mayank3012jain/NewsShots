package com.oldmonk.newsshots;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends Activity {

    Intent intentThatStarted;
    String urlToSource;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView)findViewById(R.id.webview);
        intentThatStarted = getIntent();
        if (intentThatStarted.hasExtra(AppConfig.JSON_URL_TO_NEWS_SOURCE)){
            urlToSource = intentThatStarted.getStringExtra(AppConfig.JSON_URL_TO_NEWS_SOURCE);
            webView.loadUrl(urlToSource);
        }else{
            Toast.makeText(this, "Url not found", Toast.LENGTH_SHORT).show();
        }
    }


}
