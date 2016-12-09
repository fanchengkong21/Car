package com.kfc.productcar.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.Login;
import com.kfc.productcar.utils.AlarmExpirationManager;
import com.kfc.productcar.utils.Cook;
import com.kfc.productcar.utils.Encryption;
import com.kfc.productcar.utils.HttpClient;
import com.kfc.productcar.utils.NetWorkUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class LoginActivity extends BaseActivity {
    public final static String SHARED_PREFERENCES_LOGIN = "shared_preferences_login";
    public final static String KEY_LOGIN_USERNAME = "key_login_username";
    public final static String KEY_LOGIN_PASSWORD = "key_login_password";
    //记住密码状态 0记住 1未记住
    public final static String SHARED_PREFERENCES_STATE = "shared_preferences_state";
    public final static String KEY_STATE = "key_state";
    //关联/没有关联状态 0没有关联  大于0关联
    public final static String SHARED_PREFERENCES_GROUP = "shared_preferences_group";
    public final static String KEY_GROUP = "key_group";
    public static boolean mIsLoginSuccessed;
    private NetWorkUtils netWorkUtils;
    private Login login;

    @InjectView(R.id.login_user)
    EditText loginUser;
    @InjectView(R.id.login_password)
    EditText loginPassword;
    @InjectView(R.id.remember_pwd)
    CheckBox rememberPwd;
    @InjectView(R.id.login_btn)
    Button loginBtn;
    @InjectView(R.id.forget_pwd)
    TextView forgetPwd;
    @InjectView(R.id.register)
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initTitle(this, R.id.menu_title, "登录");
        initBtnBack(this);
        /**
         * 隐藏ActionBar，状态栏和导航栏透明
         */
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initViews();

    }

    @Override
    protected void initViews() {
        netWorkUtils = new NetWorkUtils(this);
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
      /*  //用户名
        String userName = share.getString(KEY_LOGIN_USERNAME, "");
        if (!userName.equals("")) {
            userName = Encryption.decrypt(userName);
        }
        //密码
        String password = share.getString(KEY_LOGIN_PASSWORD, "");
        if (!password.equals("")) {
            password = Encryption.decrypt(password);
        }
        share = null;
        if (!TextUtils.isEmpty(userName)) {
            loginUser.setText(userName);
        }
        if (!TextUtils.isEmpty(password)) {
            loginPassword.setText(password);
        }*/
    }

    @Override
    protected void initEvents() {
        SharedPreferences shareGroup = getSharedPreferences(SHARED_PREFERENCES_GROUP, Context.MODE_PRIVATE);
        shareGroup.edit().clear().commit();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        cookieStore.clear();
        RequestParams params = new RequestParams();
        params.put("username", loginUser.getText().toString());
        params.put("password", loginPassword.getText().toString());
        HttpClient.post(CarConfig.LOGIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                //Toast.makeText(LoginActivity.this, "登录成功，cookie=" + getCookieText(), Toast.LENGTH_SHORT).show();
                Log.i("登录", response + Cook.getCookies());
                Cook.setCookies(Cook.getCookies());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Gson gson = new Gson();
                    login = gson.fromJson(jsonObject.toString(),
                            Login.class);
                  /* SharedPreferences share = getSharedPreferences(
                            SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
                    share.edit().putInt(KEY_CODE, user.getCode()).commit();*/
                    if (login.getCode() == 1) {
                        //1.创建对话框
                        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        //2.调用方法设置对话框
                        builder.setTitle("提示").setMessage(login.getMsg()).
                                setCancelable(false);//阻止返回键或点击空白处关闭
                        //3.设置确定按钮
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                builder.create().dismiss();
                            }
                        });
                        builder.create().show();
                    }
                    if (login.getCode() == 0) {
                        loginSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, LoginActivity.this);
    }

    /**
     * 获取标准 Cookie
     */
    private String getCookieText() {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(LoginActivity.this);
        List<Cookie> cookies = myCookieStore.getCookies();
        Log.i("登录", "cookies.size() = " + cookies.size());
        Cook.setCookies(cookies);
        for (Cookie cookie : cookies) {
            Log.i("登录", cookie.getName() + " = " + cookie.getValue());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (!TextUtils.isEmpty(cookieName)
                    && !TextUtils.isEmpty(cookieValue)) {
                sb.append(cookieName + "=");
                sb.append(cookieValue + ";");
            }
        }
        Log.e("cookie", sb.toString());
        return sb.toString();
    }

    private void loginSuccess() {
        saveSharePreferences(true, true);
        Runnable run = new Runnable() {

            @Override
            public void run() {
                mIsLoginSuccessed = true;
                final SharedPreferences share = getSharedPreferences(
                        SHARED_PREFERENCES_GROUP, Context.MODE_PRIVATE);
                AlarmExpirationManager.startAutoPushService(LoginActivity.this
                        .getApplicationContext());
                share.edit().putString(KEY_GROUP,login.getData().getOwngroup()).commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("state", login.getCode());//登录成功的状态
                intent.putExtra("group",login.getData().getOwngroup());//关联状态

                startActivity(intent);
                CarConfig.KEY_AUTHTOKEN_GETNAME = loginUser.getText()
                        .toString().trim();
                CarConfig.KEY_AUTHTOKEN_GETPASS = loginPassword.getText()
                        .toString().trim();
                saveAccountInLocal(loginUser.getText().toString(),
                        loginPassword.getText().toString());
            }
        };
        new Thread(run).start();
    }

    /**
     * Storing local data
     *
     * @param user
     * @param pwd
     */
    private void saveAccountInLocal(String user, String pwd) {
        String userName = Encryption.encrypt(user);
        String password = Encryption.encrypt(pwd);
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        share.edit().putString(KEY_LOGIN_USERNAME, userName).commit();
        share.edit().putString(KEY_LOGIN_PASSWORD, password).commit();
    }

    private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
        String userName = loginUser.getText().toString();
        String password = loginPassword.getText().toString();

        userName = Encryption.encrypt(userName);
        password = Encryption.encrypt(password);
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        if (saveUserName) {
            share.edit().putString(KEY_LOGIN_USERNAME, userName).commit();
        }
        if (savePassword) {
            share.edit().putString(KEY_LOGIN_PASSWORD, password).commit();
        } else {
            share.edit().putString(KEY_LOGIN_PASSWORD, "").commit();

        }

        share = null;
    }

    @OnClick({R.id.remember_pwd, R.id.login_btn, R.id.forget_pwd, R.id.register})
    public void onClick(View view) {
        switch (view.getId()) {
            //记住密码 0记住 1未记住
            case R.id.remember_pwd:
                SharedPreferences share = getSharedPreferences(
                        SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
                share.edit().putInt(KEY_STATE, 0).commit();
                break;
            case R.id.login_btn:
                if (TextUtils.isEmpty(loginUser.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(loginPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (netWorkUtils.getConnectState() == NetWorkUtils.NetWorkState.NONE) {
                    //1.创建对话框
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //2.调用方法设置对话框
                    builder.setTitle("提示").setMessage("网络连接失败，请重试").
                            setCancelable(false);//阻止返回键或点击空白处关闭
                    //3.设置确定按钮
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            builder.create().dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    initEvents();
                }
                break;
            //忘记密码
            case R.id.forget_pwd:
                Intent intent1 = new Intent(this, Forget_PasswordActivity.class);
                startActivity(intent1);
                break;
            //注册
            case R.id.register:
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }




}
