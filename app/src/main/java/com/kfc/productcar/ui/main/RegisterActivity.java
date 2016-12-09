package com.kfc.productcar.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.Login;
import com.kfc.productcar.utils.CountDownTimerUtils;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends BaseActivity {
    //用户名
    @InjectView(R.id.register_user)
    EditText registerUser;
    //密码
    @InjectView(R.id.register_password)
    EditText registerPassword;
    //确认密码
    @InjectView(R.id.register_again_password)
    EditText registerAgainPassword;
    //手机号
    @InjectView(R.id.register_phone)
    EditText registerPhone;
    //验证码
    @InjectView(R.id.register_verification)
    EditText registerVerification;
    //获取手机验证码
    @InjectView(R.id.code_layout)
    PercentRelativeLayout codeLayout;
    //邀请码
    @InjectView(R.id.register_verificationcode)
    EditText registerVerificationcode;
    @InjectView(R.id.register_obtainverification)
    TextView registerObtainverification;
    private CountDownTimerUtils countDownTimerUtils;
    public static final int REQUSET = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        initTitle(this, R.id.menu_title, "注册");
        initBtnBack(this);
       /* *//**
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
        initEvents();
    }

    @Override
    protected void initViews() {

        registerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                codeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.REQUSET && resultCode == RESULT_OK) {
            if (!TextUtils.isEmpty(data.getStringExtra("code"))) {
                registerVerificationcode.setText(data.getStringExtra("code"));
            }
        }
    }

    @OnClick({R.id.register_obtainverification, R.id.register_verificationImage, R.id.register_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            //获取手机验证码
            case R.id.register_obtainverification:
                countDownTimerUtils = new CountDownTimerUtils
                        (registerObtainverification, 60000, 1000);
                countDownTimerUtils.start();
                RequestParams params = new RequestParams();
                params.put("mobile", registerPhone.getText().toString());
                HttpClient.post(CarConfig.SENDSMS, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String send = new String(responseBody);
                        Log.i("发送验证码", send);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                }, this);
                break;
            case R.id.register_verificationImage:
                Intent intent = new Intent(RegisterActivity.this, QrcodeActivity.class);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.register_btn:
                if (registerPassword.getText().toString().equals(registerAgainPassword.getText().toString())) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params1 = new RequestParams();
                    params1.put("username", registerUser.getText().toString());
                    params1.put("password", registerPassword.getText().toString());
                    //手机号
                    params1.put("mobile", registerPhone.getText().toString());
                    //验证码
                    params1.put("verifycode", registerVerification.getText().toString());
                    //邀请码
                    params1.put("invitecode", registerVerificationcode.getText().toString());
                    client.post("http://118.178.227.119/api.php?" + CarConfig.REGISTER, params1, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String register = new String(responseBody);
                            Log.i("注册", register);
                            try {
                                JSONObject object = new JSONObject(register);
                                Gson gson = new Gson();
                                Login send = gson.fromJson(object.toString(), Login.class);
                                if (send.getCode() == 0) {
                                    //1.创建对话框
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    //2.调用方法设置对话框
                                    builder.setTitle("提示").setMessage(send.getMsg()).
                                            setCancelable(false);//阻止返回键或点击空白处关闭
                                    //3.设置确定按钮
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            builder.create().dismiss();
                                            finish();
                                        }
                                    });
                                    builder.create().show();
                                } else {
                                    //1.创建对话框
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    //2.调用方法设置对话框
                                    builder.setTitle("提示").setMessage(send.getMsg()).
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    /**
     * 取消倒计时
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimerUtils!=null) {
            countDownTimerUtils.cancel();
        }
    }

}
