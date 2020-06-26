package com.eastblue.cisuba.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eastblue.cisuba.R;

import butterknife.ButterKnife;


public class PaymentWebActivity extends AppCompatActivity {
    private WebView mWebView;
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_web);

        mWebView = (WebView)findViewById(R.id.pay_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebSettings = mWebView.getSettings();
        //mWebSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://172.30.1.57:8081/mobile/iamport");
        mWebView.setWebViewClient(new WebViewClientClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class WebViewClientClass extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}