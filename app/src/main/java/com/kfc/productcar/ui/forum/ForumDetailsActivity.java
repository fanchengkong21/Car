package com.kfc.productcar.ui.forum;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.ForumList;
import com.kfc.productcar.ui.main.ForumActivity;
import com.kfc.productcar.ui.main.LocationActivity;
import com.kfc.productcar.ui.main.LoginActivity;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Ly on 2016/11/29.
 */

public class ForumDetailsActivity extends BaseActivity {
    @InjectView(R.id.btn_forum_zan)
    TextView ForumZan;
    @InjectView(R.id.btn_forum_collect)
    TextView ForumCollect;
    @InjectView(R.id.btn_forum_link)
    ImageView ForumLink;
    @InjectView(R.id.web_forum)
    WebView webForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_details);
        ButterKnife.inject(this);
        initBtnBack(this);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {
        initWebview(this, webForum);
        webForum.loadUrl(getIntent().getStringExtra("url"));
        ForumZan.setText(getIntent().getStringExtra("recommend"));
        ForumCollect.setText(getIntent().getStringExtra("favtimes"));
    }

    @OnClick({R.id.btn_forum_zan, R.id.btn_forum_collect, R.id.btn_forum_link})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forum_zan:
                addDataNumber(CarConfig.MISC, getIntent().getStringExtra("tid"), "tid");
                break;
            case R.id.btn_forum_collect:
                addDataNumber(CarConfig.SPACECP, getIntent().getStringExtra("tid"), "id");
                break;
            case R.id.btn_forum_link:

                break;
        }
    }

    private void addDataNumber(final String config, final String tid, final String key) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RequestParams params = new RequestParams();
                        params.put(key, tid);
                        HttpClient.post(config, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String string = new String(responseBody);
                                Log.e("点赞", string);
                                try {
                                    JSONObject object = new JSONObject(string);
                                    if (object.getInt("code") == 0) {
                                        ForumZan.setText(ForumZan.getText());
                                        NumberDialogWindow(object.getString("msg"));
                                    } else if (object.getInt("code") == 1) {
                                        NumberDialogWindow(object.getString("msg"));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        }, ForumDetailsActivity.this);

                    }
                });

            }
        }, 1000);


    }

}
