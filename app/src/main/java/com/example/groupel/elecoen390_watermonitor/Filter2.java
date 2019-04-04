package com.example.groupel.elecoen390_watermonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Filter2 extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter2);

        webView=findViewById(R.id.filter2);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://crystalquest.com/collections/nitrate-removal/products/nitrate-countertop-water-filter-system");
    }
}
