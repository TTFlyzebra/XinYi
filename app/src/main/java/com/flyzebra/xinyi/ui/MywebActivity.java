package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.flyzebra.xinyi.R;


/**
 * Created by Administrator on 2016/5/31.
 */
public class MywebActivity extends AppCompatActivity {

    private WebView mywebWv01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myweb);

        mywebWv01 = (WebView) findViewById(R.id.myweb_wv_01);

        mywebWv01.getSettings().setJavaScriptEnabled(true);
        mywebWv01.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = mywebWv01.getSettings();
        webSettings.setAllowFileAccess(false);
//        webSettings.setBuiltInZoomControls(true);//缩小放大按钮
        mywebWv01.loadUrl("http://flyzebra.wicp.net/yuxiang");
        mywebWv01.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
