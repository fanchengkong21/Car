package com.kfc.productcar.ui.person;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.Random;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class LeaveMessageActivity extends BaseActivity {

    @InjectView(R.id.message_edit)
    EditText messageEdit;
    private Random random;
    private String service_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_message);
        initTitle(this, R.id.menu_title, "给店内留言");
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
        params.put("dealerid", getIntent().getIntExtra("dealerid",0));
        HttpClient.post(CarConfig.GETSELLER, params, new AsyncHttpResponseHandler() {




            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string=new String(responseBody);
                Log.i("随机",string);
                try {
                        JSONObject object=new JSONObject(string);
                        Gson gson=new Gson();
                        random = gson.fromJson(object.toString(), Random.class);
                        service_phone = random.getData().get(0).getService_phone();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        },LeaveMessageActivity.this);


    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.message_btn, R.id.message_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_btn:
                RequestParams params=new RequestParams();
                params.put("fid",getIntent().getStringExtra("dealerid"));
                params.put("message",messageEdit.getText().toString());
                HttpClient.post(CarConfig.MESSAGE, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String string=new String(responseBody);
                        Log.i("留言",string);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                },LeaveMessageActivity.this);
                break;
            case R.id.message_image:
                final AlertDialog.Builder dialog=new AlertDialog.Builder(LeaveMessageActivity.this);
                dialog.setMessage("是否要拔打电话?").setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent();
                        intent.setData(Uri.parse("tel:" + service_phone));
                        intent.setAction(Intent.ACTION_CALL);
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                }).create().show();
                break;
        }
    }
}
