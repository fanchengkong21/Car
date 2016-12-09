package com.kfc.productcar.ui.person;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.kfc.productcar.bean.User;
import com.kfc.productcar.ui.main.PersonActivity;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class RealNameActivity extends BaseActivity {

    @InjectView(R.id.input_et)
    EditText inputEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name);
        initTitle(this, R.id.menu_title, "真实姓名");
        initBtnBack(this);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        initViews();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @OnClick(R.id.menu_right)
    public void onClick() {
        RequestParams params=new RequestParams();
        params.put("realname",inputEt.getText().toString());
        HttpClient.post(CarConfig.EDITUSERBASEINFO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string=new String(responseBody);
                Log.i("修改用户信息",string);
                try {
                    JSONObject object=new JSONObject(string);
                    Gson gson=new Gson();
                    User user = gson.fromJson(object.toString(), User.class);
                    if (user.getCode()==0){
                        final AlertDialog.Builder dialog=new AlertDialog.Builder(RealNameActivity.this);
                        dialog.setTitle("保存成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.create().dismiss();
                                Intent intent=new Intent(RealNameActivity.this, PersonActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        },RealNameActivity.this);
    }

}
