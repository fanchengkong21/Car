package com.kfc.productcar.ui.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.FriendList;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class CarFriendsActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @InjectView(R.id.friend_riders)
    TextView friendRiders;
    @InjectView(R.id.riders_view)
    View ridersView;
    @InjectView(R.id.friend_apply)
    TextView friendApply;
    @InjectView(R.id.apply_view)
    View applyView;
    @InjectView(R.id.friend_listview)
    ListView friendListview;
    @InjectView(R.id.friend_refresh)
    SwipeRefreshLayout friendRefresh;
    private List<FriendList> list;
    private FriendListAdapter adapter;
    private RequestListAdapter radapter;
    private List<FriendList> rlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_friends);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        friendRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        list = new ArrayList<>();
        rlist=new ArrayList<>();
        initViews();
        initEvents();

        friendRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //停止刷新,圆圈消失
                        friendRefresh.setRefreshing(false);
                    }
                },500);
            }
        });
    }

    @Override
    protected void initViews() {

        RequestParams params = new RequestParams();
        //params.put("page",1);
        HttpClient.post(CarConfig.FRIENDLIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                Log.i("我的车友",string);
                try {
                    JSONObject object = new JSONObject(string);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Gson gson = new Gson();
                        FriendList friendList = gson.fromJson(json.toString(), FriendList.class);
                        list.add(friendList);

                        loadData();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, CarFriendsActivity.this);
    }

    private void loadData() {
        adapter = new FriendListAdapter(list, CarFriendsActivity.this);
        friendListview.setAdapter(adapter);
        friendListview.setOnItemLongClickListener(this);
        friendListview.setOnItemClickListener(this);
    }

    //长按事件删除好友
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CarFriendsActivity.this);
        builder.setTitle("提示").setMessage("确定要删除好友?").setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                RequestParams params = new RequestParams();
                params.put("uid", list.get(position).getUid());
                HttpClient.post(CarConfig.DELETEFRIEND, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String string = new String(responseBody);
                        Log.i("删除好友", string);
                        friendRefresh.post(new Runnable() {
                            @Override
                            public void run() {
                                friendRefresh.setRefreshing(true);
                                //停止刷新,圆圈消失
                                adapter.clear();
                                initViews();
                                if (friendRefresh.isRefreshing()) {
                                    friendRefresh.setRefreshing(false);
                                }
                            }
                        });


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                }, CarFriendsActivity.this);
                builder.create().dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                builder.create().dismiss();
            }
        });
        builder.create().show();
        return true;

    }

    //点击事件跳转
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(CarFriendsActivity.this, FriendChatActivity.class);
        intent.putExtra("uid",list.get(position).getUid());
        intent.putExtra("username",list.get(position).getUsername());
        intent.putExtra("icon",getIntent().getStringExtra("icon"));
        startActivity(intent);
    }

    @Override
    protected void initEvents() {
        RequestParams params1 = new RequestParams();
        //params1.put("page",1);
        HttpClient.post(CarConfig.REQUESTLIST, params1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                Log.i("好友申请",string);
                try {
                    JSONObject object = new JSONObject(string);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Gson gson = new Gson();
                        FriendList friendList = gson.fromJson(json.toString(), FriendList.class);
                        rlist.add(friendList);
                        radapter = new RequestListAdapter(rlist, CarFriendsActivity.this);
                        friendListview.setAdapter(radapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, CarFriendsActivity.this);
    }




    @OnClick({R.id.friend_back, R.id.friend_riders, R.id.friend_apply, R.id.add_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friend_back:
                finish();
                break;
            case R.id.friend_riders:
                friendRiders.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                ridersView.setVisibility(View.VISIBLE);
                friendApply.setTextColor(getResources().getColor(R.color.status_color));
                applyView.setVisibility(View.GONE);

                friendRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        friendRefresh.setRefreshing(true);
                        //停止刷新,圆圈消失
                        adapter.clear();
                        initViews();
                        if (friendRefresh.isRefreshing()) {
                            friendRefresh.setRefreshing(false);
                        }
                    }
                });
                adapter = new FriendListAdapter(list, CarFriendsActivity.this);
                friendListview.setAdapter(adapter);
                break;
            case R.id.friend_apply:

                friendApply.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                applyView.setVisibility(View.VISIBLE);
                friendRiders.setTextColor(getResources().getColor(R.color.status_color));
                ridersView.setVisibility(View.GONE);

                friendRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        friendRefresh.setRefreshing(true);
                        //停止刷新,圆圈消失
                        radapter.clear();
                        initEvents();
                        if (friendRefresh.isRefreshing()) {
                            friendRefresh.setRefreshing(false);
                        }
                    }
                });
                radapter = new RequestListAdapter(rlist, CarFriendsActivity.this);
                friendListview.setAdapter(radapter);
                break;
            case R.id.add_friend:
                Intent intent = new Intent(CarFriendsActivity.this, AddFriendActivity.class);
                startActivity(intent);
                break;
        }
    }



    //好友列表
    class FriendListAdapter extends BaseAdapter {
        private List<FriendList> list;
        private Context context;

        public FriendListAdapter(List<FriendList> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.friendlist, null);
            }
            viewHolder = getHolder(convertView);
            Glide.with(context).load(list.get(position).getAvatar()).into(viewHolder.image);
            viewHolder.name.setText(list.get(position).getUsername());
            return convertView;
        }

        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
        public void clear(){
            list.clear();
            notifyDataSetChanged();
        }

        class ViewHolder {
            TextView name;
            ImageView image;

            public ViewHolder(View convertView) {
                name = (TextView) convertView.findViewById(R.id.friendlist_name);
                image = (ImageView) convertView.findViewById(R.id.friendlist_image);

            }
        }
    }
    //好友申请
    class RequestListAdapter extends BaseAdapter {
        private List<FriendList> rlist;
        private Context context;

        public RequestListAdapter(List<FriendList> rlist, Context context) {
            this.rlist = rlist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return rlist == null ? 0 : rlist.size();
        }

        @Override
        public Object getItem(int position) {
            return rlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.requestlist, null);
            }
            viewHolder = getHolder(convertView);
            Glide.with(context).load(rlist.get(position).getAvatar()).into(viewHolder.image);
            viewHolder.name.setText(rlist.get(position).getUsername());
            viewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestParams params = new RequestParams();
                    params.put("uid", rlist.get(position).getUid());
                    HttpClient.post(CarConfig.REQUESTSURE, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String string = new String(responseBody);
                            Log.i("确认好友请求", string);
                            friendRefresh.post(new Runnable() {
                                @Override
                                public void run() {
                                    friendRefresh.setRefreshing(true);
                                    //停止刷新,圆圈消失
                                    radapter.clear();
                                    initEvents();
                                    friendListview.setAdapter(radapter);
                                    if (friendRefresh.isRefreshing()) {
                                        friendRefresh.setRefreshing(false);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    }, CarFriendsActivity.this);
                }
            });
            return convertView;
        }

        private ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
        public void clear(){
            rlist.clear();
            notifyDataSetChanged();
        }

        class ViewHolder {
            TextView name,tv;
            ImageView image;

            public ViewHolder(View convertView) {
                name = (TextView) convertView.findViewById(R.id.friendlist_name);
                image = (ImageView) convertView.findViewById(R.id.friendlist_image);
                tv= ((TextView) convertView.findViewById(R.id.friendlist_tv));

            }
        }
    }
}
