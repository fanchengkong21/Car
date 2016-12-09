package com.kfc.productcar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kfc.productcar.ui.forum.ForumDetailsActivity;

/**
 * @author fancheng.kong
 * @CreateTime 2016/8/5 22:32
 * @PackageName com.kfc.computercollected
 * @ProjectName ComputerCollected
 * @Email kfc1301478241@163.com
 */

public abstract class BaseActivity extends AppCompatActivity {
    private TextView tvTitle;
    private Button btnBack;
    public static Bitmap bitmap;
    protected void initTitle(Activity mActivity, int i_title_id, String contentTitle) {
        tvTitle = (TextView) mActivity.findViewById(i_title_id);
        if (tvTitle != null) {
            tvTitle.setText(contentTitle);
        } else {
            Log.e("Title->", "NULL");
        }
    }


    /**
     * Initial return function
     */
    protected void initBtnBack(final Activity mActivity) {
        btnBack = (Button) mActivity.findViewById(R.id.menu_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    BaseActivity.this.finish();
                }
            });
        } else {
            Log.e("button->", "NULL");
        }
    }
    /** 初始化视图 **/
    protected abstract void initViews();

    /** 初始化事件 **/
    protected abstract void initEvents();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebview(final BaseActivity activity, WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setTextZoom(80);
        webView.requestFocus();
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //activity.showLoadingDialog("页面加载中, 请稍候...");
                Log.d("zheng", "onPageStarted");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                System.out.println("url" + url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //activity.dismissLoadingDialog();
                Log.d("zheng", "onPageFinished:" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });
    }
    public void NumberDialogWindow(String msg) {
        LayoutInflater inflaterDl = LayoutInflater
                .from(BaseActivity.this);
        final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                R.layout.dialog_window, null);
        final Dialog dialog = new android.app.AlertDialog.Builder(BaseActivity.this)
                .create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        ((TextView) layout.findViewById(R.id.dialog_title)).setText(msg);

        ((Button) layout.findViewById(R.id.dialog_btn))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

    }


}
