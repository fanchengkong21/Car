package com.kfc.productcar.ui.person;

import android.content.DialogInterface;
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

public class NewPasswordActivity extends BaseActivity {

    @InjectView(R.id.new_phone)
    EditText newPhone;
    @InjectView(R.id.new_code)
    EditText newCode;
    @InjectView(R.id.new_password)
    EditText newPassword;
    @InjectView(R.id.new_obtain)
    TextView newObtain;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        initTitle(this, R.id.menu_title, "修改密码");
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

    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.new_obtain, R.id.new_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_obtain:
                countDownTimerUtils = new CountDownTimerUtils
                        (newObtain, 60000, 1000);
                countDownTimerUtils.start();
                RequestParams params = new RequestParams();
                params.put("mobile", newPhone.getText().toString());
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
            case R.id.new_sure:
                RequestParams params1=new RequestParams();
                params1.put("mobile",newPhone.getText().toString());
                params1.put("verifycode",newCode.getText().toString());
                params1.put("password",newPassword.getText().toString());
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
                                final AlertDialog.Builder builder = new AlertDialog.Builder(NewPasswordActivity.this);
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
                                final AlertDialog.Builder builder = new AlertDialog.Builder(NewPasswordActivity.this);
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
}
