package com.kfc.productcar.ui.person;

import android.content.DialogInterface;
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
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class AddFriendActivity extends BaseActivity {

    @InjectView(R.id.add_edt)
    EditText addEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initTitle(this, R.id.menu_title, "添加好友");
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

    @OnClick(R.id.add_btn)
    public void onClick() {
        RequestParams params=new RequestParams();
        params.put("username",addEdt.getText().toString());
        HttpClient.post(CarConfig.ADDFRIEND, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string=new String(responseBody);
                Log.i("添加好友",string);
                try {
                    JSONObject object=new JSONObject(string);
                    Gson gson=new Gson();
                    User user = gson.fromJson(object.toString(), User.class);
                    if (user.getCode()==0){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
                        builder.setTitle("提示").setMessage(user.getMsg()).setCancelable(false);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                builder.create().dismiss();
                            }
                        });
                        builder.create().show();
                    }else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AddFriendActivity.this);
                        builder.setTitle("提示").setMessage(user.getMsg()).setCancelable(false);
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
        },AddFriendActivity.this);

    }
}
