package com.example.groupel.elecoen390_watermonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Filter5 extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter5);
        webView=findViewById(R.id.filter5);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.aquapurefilters.com/store/product/200304.200293/apuv8.html");
    }
}
