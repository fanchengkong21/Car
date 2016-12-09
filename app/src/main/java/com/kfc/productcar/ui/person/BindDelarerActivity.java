package com.kfc.productcar.ui.person;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.ui.main.QrcodeActivity;
import com.kfc.productcar.ui.main.RegisterActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static com.kfc.productcar.ui.main.RegisterActivity.REQUSET;

public class BindDelarerActivity extends BaseActivity {

    @InjectView(R.id.bind_edt)
    EditText bindEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_delarer);
        initTitle(this, R.id.menu_title, "绑定经销商");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.REQUSET && resultCode == RESULT_OK) {
            if (!TextUtils.isEmpty(data.getStringExtra("code"))) {
                bindEdt.setText(data.getStringExtra("code"));
            }
        }
    }

    @OnClick({R.id.bind_image, R.id.bind_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            //获取二维码
            case R.id.bind_image:
                Intent intent = new Intent(BindDelarerActivity.this, QrcodeActivity.class);
                startActivityForResult(intent, REQUSET);
                break;
            //绑定经销商  只有扫描经销商才能绑定成功
            case R.id.bind_btn:
                AsyncHttpClient client=new AsyncHttpClient();
                RequestParams params=new RequestParams();
                params.put("invitecode",bindEdt.getText().toString());
                client.post("http://118.178.227.119/api.php?" + CarConfig.BINDSELLER, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String string=new String(responseBody);
                        Log.i("绑定经销商",string);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                break;
        }
    }
}
