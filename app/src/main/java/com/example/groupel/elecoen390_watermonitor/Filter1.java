package com.example.groupel.elecoen390_watermonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Filter1 extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter1);

        webView=findViewById(R.id.filter1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://crystalquest.com/collections/nitrate-removal/products/nitrate-removal-smart-single-cartridge-water-filter-system");

    }
}
