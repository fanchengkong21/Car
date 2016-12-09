package com.kfc.productcar.ui.person;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.adapter.ChatAdapter;
import com.kfc.productcar.bean.ChatModel;
import com.kfc.productcar.bean.ItemModel;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class FriendChatActivity extends BaseActivity {
    @InjectView(R.id.recylerView)
    RecyclerView recylerView;
    @InjectView(R.id.input)
    EditText input;
    private ChatAdapter adapter;
    private String content;
    private ArrayList<ItemModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);
        initTitle(this, R.id.menu_title, "我的车友");
        initBtnBack(this);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        initViews();
        initEvents();

    }

    private void loadData() {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString().trim();
            }
        });
    }

    @Override
    protected void initViews() {
        list=new ArrayList<>();
        RequestParams params = new RequestParams();
        params.put("touid", getIntent().getStringExtra("uid"));
        HttpClient.post(CarConfig.PMLISTONE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                Log.i("对话列表", string);
                try {
                    JSONObject object=new JSONObject(string);
                    JSONArray array=object.getJSONArray("data");
                    for (int i = 0; i <array.length() ; i++) {
                        JSONObject json=array.getJSONObject(i);
                        Gson gson=new Gson();
                        ChatModel chatModel = gson.fromJson(json.toString(), ChatModel.class);
                        if (chatModel.getMsgfromid().equals(getIntent().getStringExtra("uid"))){
                            list.add(new ItemModel(1,chatModel));
                        }else {
                            list.add(new ItemModel(0,chatModel));
                        }

                        initData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, FriendChatActivity.this);

    }

    private void initData() {
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recylerView.setAdapter(adapter = new ChatAdapter());
        recylerView.smoothScrollToPosition(list.size()-1);
        adapter.replaceAll(list);


        loadData();
    }

    @Override
    protected void initEvents() {

    }
    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.send)
    public void onClick() {
        RequestParams params=new RequestParams();
        Log.i("发送消息",Integer.parseInt(getIntent().getStringExtra("uid"))+content);
        params.put("touid",Integer.parseInt(getIntent().getStringExtra("uid")));
        params.put("message",content);
        HttpClient.post(CarConfig.SENDMESSAGE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String  string=new String(responseBody);
                Log.i("发送消息",string);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        },FriendChatActivity.this);
        ArrayList<ItemModel> data = new ArrayList<>();
        ChatModel model = new ChatModel();
        model.setAvatar(getIntent().getStringExtra("icon"));
        model.setMessage(content);
        data.add(new ItemModel(0, model));
        adapter.addAll(data);
        //recylerView.smoothScrollToPosition(list.size()+1);
        input.setText("");
        hideKeyBorad(input);

    }
    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
