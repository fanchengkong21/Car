package com.kfc.productcar.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;
import com.kfc.productcar.ui.main.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeIntroduceActivity extends BaseActivity {
    public final static String SHARED_PREFERENCES_STATE = "shared_preferences_state";
    public final static String KEY_STATE = "key_state";

    @InjectView(R.id.home_right)
    TextView homeRight;
    @InjectView(R.id.introduce_webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_introduce);
        ButterKnife.inject(this);
        initTitle(this, R.id.menu_title, getIntent().getStringExtra("name"));
        initBtnBack(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        webView.loadUrl(getIntent().getStringExtra("url"));
        initWebview(this, webView);
        //返回键
        ((Button) findViewById(R.id.menu_back))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 如果WebView不是第一个界面,那就返回网页的上一个页面
                        if (webView.canGoBack()) {
                            webView.goBack();
                        }
                        //如果WebView是第一个界面,那就退出这个Activity
                        else {
                            HomeIntroduceActivity.this.finish();
                        }
                    }
                });

    }

    @Override
    protected void initEvents() {
        if (getIntent().getIntExtra("state", -1) == 0) {
            homeRight.setVisibility(View.GONE);
        } else {
            homeRight.setVisibility(View.VISIBLE);
            homeRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeIntroduceActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
        if (share.getInt(KEY_STATE, 1) == 0) {
            homeRight.setVisibility(View.GONE);
        }
    }
    @Override
    // 设置回退
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        } else {
            HomeIntroduceActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
