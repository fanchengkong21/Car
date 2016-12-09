package com.kfc.productcar.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.City;
import com.kfc.productcar.bean.GroupList;
import com.kfc.productcar.bean.Weather;
import com.kfc.productcar.ui.home.DealerDetailsActivity;
import com.kfc.productcar.ui.home.WeatherActivity;
import com.kfc.productcar.utils.HttpClient;
import com.kfc.productcar.utils.ReadJson;
import com.kfc.productcar.utils.StringUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class HomeActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    @InjectView(R.id.menu_left)
    TextView menuLeft;
    @InjectView(R.id.pullLoadMoreRecyclerView)
    PullLoadMoreRecyclerView pullLoadMoreRecyclerView;
    //记住密码状态 0记住 1未记住
    public final static String KEY_STATE = "key_state";
    public final static String SHARED_PREFERENCES_STATE = "shared_preferences_state";
    //经销商ID
    public final static String SHARED_PREFERENCES_DEALERID = "shared_preferences_dealerid";
    public final static String KEY_DEALERID = "key_dealerid";
    @InjectView(R.id.menu_right)
    TextView menuRight;
    @InjectView(R.id.weather_pic)
    ImageView weatherPic;
    @InjectView(R.id.weather_layout)
    LinearLayout weatherLayout;
    @InjectView(R.id.weather_cool)
    TextView weatherCool;


    private AMapLocationClient mLocationClient;
    private double longitude, latitude;
    private GroupListAdapter adapter;
    //用于记录当前是第几页
    private int curPage = 1;
    private RecyclerView recyclerView;
    private Weather weather;
    private List<City> list;
    private int id;
    private List<GroupList> glist;
    private GroupList groupList;
    private String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initTitle(this, R.id.menu_title, "首页");
        initBtnBack(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        recyclerView = pullLoadMoreRecyclerView.getRecyclerView();
        //recyclerView.setVerticalScrollBarEnabled(true);
        //pullLoadMoreRecyclerView.setRefreshing(false);
        pullLoadMoreRecyclerView.setPullRefreshEnable(false);
        pullLoadMoreRecyclerView.setPushRefreshEnable(true);
        pullLoadMoreRecyclerView.setFooterViewText("疯狂加载中……");
        //设置上拉刷新文字颜色
        pullLoadMoreRecyclerView.setFooterViewTextColor(R.color.gray);
        pullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        pullLoadMoreRecyclerView.setFooterViewBackgroundColor(R.color.main_title);
        pullLoadMoreRecyclerView.setLinearLayout();
        initEvents();
        initViews();
    }

    @Override
    protected void initViews() {
        glist=new ArrayList<>();
        mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        longitude = aMapLocation.getLongitude();
                        latitude = aMapLocation.getLatitude();
                        Log.i("经销商列表", longitude + "," + latitude);
                         city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        Log.e("城市", "city: " + city);
                        Log.e("城市", "district: " + district);
                        String location = StringUtils.extractLocation(city, district);
                        if (getIntent().getIntExtra("state", -1) == 0) {
                            menuLeft.setText(city);
                            initWeather();
                            loadlocation();
                        } else {
                            menuLeft.setText(city);
                            initWeather();
                            loadlocation();
                        }

                    } else {
                        //定位失败
                        menuLeft.setText("定位失败");
                    }
                }
            }
        });
        mLocationClient.startLocation();

    }

    private void loadlocation() {
        list = new ArrayList<>();
        String json = ReadJson.getJson(this, "city.json");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONArray city = object.getJSONArray("city");
                for (int j = 0; j < city.length(); j++) {
                    JSONObject objectcity = city.getJSONObject(j);
                    Gson gson = new Gson();
                    City indexcity = gson.fromJson(objectcity.toString(), City.class);
                    list.add(indexcity);

                }
            }
            initId();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initId() {
        for (int i= 0; 0 <list.size() ; i++) {
            //Log.i("城市",city+list.size());
            if (list.get(i).getName().equals(city)){
                id=Integer.parseInt(list.get(i).getId());
                loadData();
            }
        }
    }

    private void initWeather() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "APPCODE 18258472a22548ef8a7320fc0bafec9f");
        RequestParams params = new RequestParams();
        params.put("from", "5");
        params.put("lat", latitude);
        Log.i("经纬度", latitude + "===" + longitude);
        params.put("lng", longitude);
        params.put("need3HourForcast", "0");
        params.put("needAlarm", "0");
        params.put("needHourData", "0");
        params.put("needIndex", "0");
        params.put("needMoreDay", "0");
        client.get("http://ali-weather.showapi.com/gps-to-weather",
                params, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String string = new String(responseBody);
                        Log.i("天气", string);
                        try {
                            JSONObject object = new JSONObject(string);
                            Gson gson = new Gson();
                            weather = gson.fromJson(object.toString(), Weather.class);
                            weathers();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

    }

    private void weathers() {
        if (getIntent().getIntExtra("state", -1) == 0) {
            menuRight.setVisibility(View.GONE);
            weatherCool.setVisibility(View.VISIBLE);
            weatherCool.setText(weather.getShowapi_res_body().getNow().getTemperature() + "°");
            weatherPic.setVisibility(View.VISIBLE);
            Glide.with(this).load(weather.getShowapi_res_body().getNow().getWeather_pic())
                    .into(weatherPic);
            weatherLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
                    intent.putExtra("lat",latitude);
                    intent.putExtra("lng",longitude);
                    startActivity(intent);
                    overridePendingTransition(R.anim.photo_dialog_out_anim,0);
                }
            });
        }
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
        if (share.getInt(KEY_STATE, 1) == 0) {
            weatherCool.setVisibility(View.VISIBLE);
            weatherCool.setText(weather.getShowapi_res_body().getNow().getTemperature() + "°");
            weatherPic.setVisibility(View.VISIBLE);
            Glide.with(this).load(weather.getShowapi_res_body().getNow().getWeather_pic())
                    .into(weatherPic);
            weatherLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
                    intent.putExtra("lat",latitude+"");
                    intent.putExtra("lng",longitude+"");
                    startActivity(intent);
                    overridePendingTransition(R.anim.photo_dialog_out_anim,0);
                }
            });
        }

    }

    @Override
    protected void initEvents() {
        if (getIntent().getIntExtra("state", -1) == 0) {
            menuRight.setVisibility(View.GONE);
        } else {
            menuRight.setVisibility(View.VISIBLE);
        }
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
        if (share.getInt(KEY_STATE, 1) == 0) {
            menuRight.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getIntent().getStringExtra("locationId") != null) {
                    Toast.makeText(HomeActivity.this, "不为空", Toast.LENGTH_SHORT).show();
                    RequestParams params = new RequestParams();
                    params.put("area", getIntent().getIntExtra("id", -1));
                    params.put("page", curPage);
                           /*params.put("point", longitude + "," + latitude);
                            Log.i("经销商列表", longitude + "," + latitude);*/
                    HttpClient.post(CarConfig.GROUPLIST, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String string = new String(responseBody);
                            Log.i("经销商列表", string);
                            try {
                                JSONObject object = new JSONObject(string);
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject objectString = array.getJSONObject(i);
                                    Gson gson = new Gson();
                                    groupList = gson.fromJson(objectString.toString(), GroupList.class);
                                    glist.add(groupList);
                                    adapter = new GroupListAdapter(glist, HomeActivity.this);
                                    pullLoadMoreRecyclerView.setAdapter(adapter);


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    }, HomeActivity.this);
                } else {
                    Toast.makeText(HomeActivity.this, "空", Toast.LENGTH_SHORT).show();
                    //登录后无关联
                    if (getIntent().getStringExtra("group").equals("0")){
                        RequestParams params = new RequestParams();
                        params.put("area", id);
                        params.put("page", curPage);
                        Log.i("次数", curPage + "");
                        HttpClient.post(CarConfig.GROUPLIST, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String string = new String(responseBody);
                                Log.i("经销商列表", string);
                                try {
                                    JSONObject object = new JSONObject(string);
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objectString = array.getJSONObject(i);
                                        Gson gson = new Gson();
                                        groupList = gson.fromJson(objectString.toString(), GroupList.class);
                                        glist.add(groupList);
                                        adapter = new GroupListAdapter(glist, HomeActivity.this);
                                        pullLoadMoreRecyclerView.setAdapter(adapter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        }, HomeActivity.this);
                    }else {
                        //定位失败
                        RequestParams params = new RequestParams();
                        params.put("page", curPage);
                        HttpClient.post(CarConfig.GROUPLIST, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String string = new String(responseBody);
                                Log.i("经销商列表", string);
                                try {
                                    JSONObject object = new JSONObject(string);
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject objectString = array.getJSONObject(i);
                                        Gson gson = new Gson();
                                        groupList = gson.fromJson(objectString.toString(), GroupList.class);
                                        glist.add(groupList);
                                        adapter = new GroupListAdapter(glist, HomeActivity.this);
                                        pullLoadMoreRecyclerView.setAdapter(adapter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        }, HomeActivity.this);
                    }



                    pullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                }
            }


        }, 1000);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }


    @OnClick({R.id.menu_left, R.id.menu_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_left:
                Intent intent1 = new Intent(this, LocationActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_right:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        curPage = curPage + 1;
        loadData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }


    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 1500);
        } else {
            finish();
            System.exit(0);
        }
    }

    public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

        private List<GroupList> list;
        private Context context;


        public GroupListAdapter(List<GroupList> list, Context context) {
            this.list = list;
            this.context = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(context, R.layout.grouplist_item, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.group_name.setText(list.get(position).getName());
            holder.group_distance.setText(list.get(position).getDistance());
            holder.group_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences share = getSharedPreferences(
                            SHARED_PREFERENCES_DEALERID, Context.MODE_PRIVATE);
                    share.edit().putString(KEY_DEALERID, list.get(position).getId()).commit();
                    Intent intent = new Intent(HomeActivity.this, DealerDetailsActivity.class);
                    intent.putExtra("name", list.get(position).getName());
                    intent.putExtra("dealerid", Integer.parseInt(list.get(position).getId()));
                    intent.putExtra("state", getIntent().getIntExtra("state", -1));
                    startActivity(intent);
                }
            });
            Glide.with(context).load(list.get(position).getImg()).into(holder.group_image);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView group_image;
            private TextView group_name, group_distance;

            public ViewHolder(View convertView) {
                super(convertView);
                group_image = ((ImageView) convertView.findViewById(R.id.group_image));
                group_name = ((TextView) convertView.findViewById(R.id.group_name));
                group_distance = ((TextView) convertView.findViewById(R.id.group_distance));
            }
        }

    }
}

