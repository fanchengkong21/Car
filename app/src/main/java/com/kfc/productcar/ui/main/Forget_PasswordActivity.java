package com.kfc.productcar.ui.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.User;
import com.kfc.productcar.utils.CountDownTimerUtils;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class Forget_PasswordActivity extends BaseActivity {

    @InjectView(R.id.forget_binding)
    EditText forgetBinding;
    @InjectView(R.id.forget_verification)
    EditText forgetVerification;
    @InjectView(R.id.forget_obtainverification)
    TextView forgetObtainverification;
    @InjectView(R.id.forget_newpassword)
    EditText forgetNewpassword;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.inject(this);
        initTitle(this, R.id.menu_title, "重置密码");
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
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.forget_obtainverification, R.id.forget_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            //获取验证码
            case R.id.forget_obtainverification:
                //public CountDownTimer (long millisInFuture, long countDownInterval)
                //第一个参数是被调用的毫秒数,第二个参数是间隔时间,默认计时单位是毫秒
                countDownTimerUtils=new CountDownTimerUtils
                        (forgetObtainverification, 60000, 1000);
                countDownTimerUtils.start();
                RequestParams params=new RequestParams();
                params.put("mobile",forgetBinding.getText().toString());
                HttpClient.post(CarConfig.SENDSMS, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String send=new String(responseBody);
                        Log.i("发送验证码",send);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                },this);
                break;
            //重置密码
            case R.id.forget_sure:
                RequestParams params1=new RequestParams();
                params1.put("mobile",forgetBinding.getText().toString());
                params1.put("verifycode",forgetVerification.getText().toString());
                params1.put("password",forgetNewpassword.getText().toString());
                HttpClient.post(CarConfig.RESETPASSWORD, params1, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String reset=new String(responseBody);
                        Log.i("重置密码",reset);
                        try {
                            JSONObject object=new JSONObject(reset);
                            Gson gson=new Gson();
                            User send = gson.fromJson(object.toString(), User.class);
                            if (send.getCode()==0){
                                //1.创建对话框
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Forget_PasswordActivity.this);
                                //2.调用方法设置对话框
                                builder.setTitle("提示").setMessage("密码重置成功").
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
                            }else {
                                //1.创建对话框
                                final AlertDialog.Builder builder = new AlertDialog.Builder(Forget_PasswordActivity.this);
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
                },this);
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
