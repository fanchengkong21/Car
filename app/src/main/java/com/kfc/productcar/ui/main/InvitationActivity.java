package com.kfc.productcar.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

public class InvitationActivity extends BaseActivity {
    private String string;

    @InjectView(R.id.code_image)
    ImageView codeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        initTitle(this, R.id.menu_title, "获取邀请码");
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
        RequestParams params=new RequestParams();
        HttpClient.post(CarConfig.USERQRCODE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将字符串转换成Bitmap类型
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeByteArray(responseBody, 0,
                            responseBody.length);
                    Log.i("Bitmap",bitmap.toString());
                    codeImage.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadData();



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        },InvitationActivity.this);

    }

    private void loadData() {

    }

    @Override
    protected void initEvents() {

    }
}
