package com.example.groupel.elecoen390_watermonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Filter3 extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter3);

        webView=findViewById(R.id.filter3);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.aquapurefilters.com/store/product/201216.201238/3mro401.html");
    }
}
