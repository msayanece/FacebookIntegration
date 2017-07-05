package com.example.admin.facebookintegration;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowPostDetails extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog loading;

    //TODO use this for touch event intent
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Toast.makeText(this, "Don't touch me!", Toast.LENGTH_SHORT).show();
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post_details);
        webView=(WebView)findViewById(R.id.web);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    webView.setVisibility(View.VISIBLE);
                }
                else {

                    webView.setVisibility(View.INVISIBLE);
                }
            }
        });

        webView.setWebViewClient(new myWebView());
        String url = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    private class myWebView extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            loading = ProgressDialog.show(ShowPostDetails.this, "","Loading. Please wait...", true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            loading.dismiss();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loading.dismiss();
        }
    }
}
