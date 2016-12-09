package com.kfc.productcar.ui.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.Login;
import com.kfc.productcar.ui.main.MainActivity;
import com.kfc.productcar.utils.GlideCacheUtil;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class FunctionSettingActivity extends BaseActivity {
    public final static String SHARED_PREFERENCES_LOGIN = "shared_preferences_login";
    public final static String SHARED_PREFERENCES_STATE = "shared_preferences_state";
    public final static String SHARED_PREFERENCES_GROUP = "shared_preferences_group";
    public final static String SHARED_PREFERENCES_DEALERID="shared_preferences_dealerid";
    @InjectView(R.id.cachesize)
    TextView cachesize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_setting);
        initTitle(this, R.id.menu_title, "功能设置");
        initBtnBack(this);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        cachesize.setText(GlideCacheUtil.getInstance().getCacheSize(this));

    }

    @Override
    protected void initEvents() {
        int i = GlideCacheUtil.px2dip(this, 675);
        Log.i("长度",i+"");

    }


    @OnClick({R.id.clear_layout, R.id.exit_login})
    public void onClick(View view) {
        switch (view.getId()) {
            //清除缓存
            case R.id.clear_layout:
                final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle("提示").setMessage("您确定要清除缓存?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.create().dismiss();
                    }
                }) .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GlideCacheUtil.getInstance().clearImageAllCache(FunctionSettingActivity.this);
                        cachesize.setText(GlideCacheUtil.getInstance().getCacheSize(FunctionSettingActivity.this));
                    }
                }).create().show();

                break;
            case R.id.exit_login:
                RequestParams params = new RequestParams();
                PersistentCookieStore cookieStore = new PersistentCookieStore(this);
                cookieStore.clear();
                HttpClient.post(CarConfig.LOGOUT, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String string = new String(responseBody);
                        Log.i("退出登录", string);
                        try {
                            JSONObject object = new JSONObject(string);
                            Gson gson = new Gson();
                            Login exitlogin = gson.fromJson(object.toString(), Login.class);
                            if (exitlogin.getCode() == 0) {
                                Intent intent = new Intent(FunctionSettingActivity.this, MainActivity.class);
                                startActivity(intent);
                                SharedPreferences shareLogin = getSharedPreferences(SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
                                shareLogin.edit().clear().commit();
                                SharedPreferences shareState = getSharedPreferences(SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
                                shareState.edit().clear().commit();
                                SharedPreferences shareGroup = getSharedPreferences(SHARED_PREFERENCES_GROUP, Context.MODE_PRIVATE);
                                shareGroup.edit().clear().commit();
                                SharedPreferences shareDealerid= getSharedPreferences(SHARED_PREFERENCES_DEALERID, Context.MODE_PRIVATE);
                                shareDealerid.edit().clear().commit();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                },this);
                break;
        }
    }
}
