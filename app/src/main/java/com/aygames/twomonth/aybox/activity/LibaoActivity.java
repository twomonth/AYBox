package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aygames.twomonth.aybox.R;

/**
 * Created by wf05 on 2017/9/25.
 */

public class LibaoActivity extends Activity {
    private String pathString="http://www.ofwan.com/Home/Wap/gamegift/gid/";
    String gameID;
    private WebView lblqy;
    private TextView lblq_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_libao);
        gameID = getIntent().getStringExtra("gameid");
        lblqy=(WebView) findViewById(R.id.webview_libao);
        lblq_back=(TextView) findViewById(R.id.lblq_back);
        lblqy.getSettings().setJavaScriptEnabled(true);
        lblqy.loadUrl(pathString+gameID);
        lblqy.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, pathString+gameID);
            }
        });

        lblq_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });


    }

}
