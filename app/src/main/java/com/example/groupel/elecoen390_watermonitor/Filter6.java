package com.example.groupel.elecoen390_watermonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Filter6 extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter6);
        webView=findViewById(R.id.filter6);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.aquapurefilters.com/store/product/200980.201002/cactus-x-12.html");
    }
}
