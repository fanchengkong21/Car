package com.kfc.productcar.ui.person;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.utils.CountDownTimerUtils;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class BoundPhoneActivity extends BaseActivity {

    @InjectView(R.id.bound_phone)
    EditText boundPhone;
    @InjectView(R.id.bound_code)
    EditText boundCode;
    @InjectView(R.id.bount_obtain)
    TextView bountObtain;
    private CountDownTimerUtils countDownTimerUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_phone);
        initTitle(this, R.id.menu_title, "绑定手机");
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


    @OnClick({R.id.bount_obtain, R.id.bound_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bount_obtain:
                countDownTimerUtils = new CountDownTimerUtils
                        (bountObtain, 60000, 1000);
                countDownTimerUtils.start();
                RequestParams params = new RequestParams();
                params.put("mobile", boundPhone.getText().toString());
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
            case R.id.bound_sure:
                RequestParams params1 = new RequestParams();
                params1.put("mobile",boundPhone.getText().toString());
                params1.put("verifycode",boundCode.getText().toString());
                HttpClient.post(CarConfig.REALNAME, params1, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String send = new String(responseBody);
                        Log.i("绑定手机", send);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                }, this);
                break;
        }
    }
}
