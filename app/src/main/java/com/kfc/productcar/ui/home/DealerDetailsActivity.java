package com.kfc.productcar.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.adapter.OtherMessageAdapter;
import com.kfc.productcar.bean.Carousel;
import com.kfc.productcar.bean.Modles;
import com.kfc.productcar.bean.OtherMessage;
import com.kfc.productcar.bean.Weather;
import com.kfc.productcar.ui.main.LoginActivity;
import com.kfc.productcar.ui.person.HotLineActivity;
import com.kfc.productcar.ui.person.SalesRepActivity;
import com.kfc.productcar.utils.GlideImageLoader;
import com.kfc.productcar.utils.HttpClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

import static com.kfc.productcar.R.id.modles_title;

public class DealerDetailsActivity extends BaseActivity implements OnBannerClickListener {
    //记住密码状态 0记住 1未记住
    public final static String SHARED_PREFERENCES_STATE = "shared_preferences_state";
    public final static String KEY_STATE = "key_state";
    //关联状态 0无关联其他有关联
    public final static String SHARED_PREFERENCES_GROUP = "shared_preferences_group";
    public final static String KEY_GROUP = "key_group";
    //经销商ID
    public final static String SHARED_PREFERENCES_DEALERID = "shared_preferences_dealerid";
    public final static String KEY_DEALERID = "key_dealerid";

    @InjectView(R.id.banner)
    Banner banner;
    @InjectView(R.id.dealer_center)
    RecyclerView dealerCenter;
    @InjectView(R.id.dealer_bottom)
    GridView dealerBottom;
    @InjectView(R.id.menu_back)
    Button menuBack;
    @InjectView(R.id.menu_right)
    TextView menuRight;
    @InjectView(R.id.weather_pic)
    ImageView weatherPic;
    @InjectView(R.id.weather_layout)
    LinearLayout weatherLayout;
    @InjectView(R.id.weather_cool)
    TextView weatherCool;
    private Weather weather;
    private List<String> urllist;
    private List<String> imageslist;
    private List<Modles> list;
    private List<OtherMessage> otherlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_details);
        ButterKnife.inject(this);
        initTitle(this, R.id.menu_title, getIntent().getStringExtra("name"));
        initBtnBack(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        imageslist = new ArrayList<String>();
        list = new ArrayList<>();
        otherlist = new ArrayList<>();
        urllist = new ArrayList<>();
        RequestParams params = new RequestParams();
        Log.i("轮播图",getIntent().getIntExtra("dealerid",0)+"");
        params.put("dealerid", getIntent().getIntExtra("dealerid",0));
        //经销商首页轮播图
        HttpClient.post(CarConfig.GETDEALERSLIDE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                Log.i("轮播图", string);
                try {
                    JSONObject object = new JSONObject(string);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Gson gson = new Gson();
                        Carousel carousel = gson.fromJson(json.toString(), Carousel.class);
                        imageslist.add(carousel.getImg());
                        urllist.add(carousel.getUrl());
                        loadCarousel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, this);
        //经销商首页车型列表
        HttpClient.post(CarConfig.GETDEALECARS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                Log.i("车型列表", string);
                try {
                    JSONObject object = new JSONObject(string);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Gson gson = new Gson();
                        Modles modles = gson.fromJson(json.toString(), Modles.class);
                        list.add(modles);
                        loadModels();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, this);
        //经销商首页其他信息
        HttpClient.post(CarConfig.GETINDEXINFO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                Log.i("其他信息", string);
                try {
                    JSONObject object = new JSONObject(string);
                    Gson gson = new Gson();
                    OtherMessage otherMessage = gson.fromJson(object.toString(), OtherMessage.class);
                    otherlist.add(otherMessage);
                    loadOther();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, this);


    }


    //轮播图
    private void loadCarousel() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageslist);
        //设置轮播时间
        banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置点击事件
        banner.setOnBannerClickListener(this);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    //车型列表
    private void loadModels() {
        ModlesAdapter adapter = new ModlesAdapter(list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dealerCenter.setLayoutManager(linearLayoutManager);
        dealerCenter.setAdapter(adapter);
    }

    //其他信息
    private void loadOther() {
        OtherMessageAdapter adapter = new OtherMessageAdapter(otherlist, this);
        dealerBottom.setAdapter(adapter);
        dealerBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    Intent intent = new Intent(DealerDetailsActivity.this, HomeIntroduceActivity.class);
                    intent.putExtra("url", otherlist.get(0).getData().getBottom_left_link1());
                    intent.putExtra("name", otherlist.get(0).getData().getBottom_left_title1());
                    intent.putExtra("state", getIntent().getIntExtra("state", -1));
                    startActivity(intent);
                }
                if (position == 1) {
                    final SharedPreferences shareGroup = getSharedPreferences(
                            SHARED_PREFERENCES_GROUP, Context.MODE_PRIVATE);
                    final SharedPreferences shareState = getSharedPreferences(
                            SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
                    if (getIntent().getIntExtra("state", -1) !=-1&&getIntent().getIntExtra("state", -1) !=0){
                        Intent intent = new Intent(DealerDetailsActivity.this, SalesRepActivity.class);
                        intent.putExtra("title","销售顾问");
                        intent.putExtra("dealerid", getIntent().getIntExtra("dealerid",0));
                        startActivity(intent);
                    }else {
                        if (shareGroup.getString(KEY_GROUP, "").equals("0")){
                            if (getIntent().getIntExtra("state", -1) == -1&&shareState.getInt(KEY_STATE, -1)==-1){
                                Intent intent = new Intent(DealerDetailsActivity.this, SalesRepActivity.class);
                                intent.putExtra("title","销售顾问");
                                intent.putExtra("dealerid", getIntent().getIntExtra("dealerid",0));
                                startActivity(intent);
                            }else {
                                Toast.makeText(DealerDetailsActivity.this, "无关联", Toast.LENGTH_SHORT).show();
                            }
                        }else if (!shareGroup.getString(KEY_GROUP, "").equals("0")){
                            if (getIntent().getIntExtra("state", -1) == -1&&shareState.getInt(KEY_STATE, -1)==-1){
                                Intent intent = new Intent(DealerDetailsActivity.this, SalesRepActivity.class);
                                intent.putExtra("title","销售顾问");
                                intent.putExtra("dealerid", getIntent().getIntExtra("dealerid",0));
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(DealerDetailsActivity.this, HotLineActivity.class);
                                intent.putExtra("dealerid", getIntent().getIntExtra("dealerid",0));
                                startActivity(intent);
                            }

                        }else if(shareState.getInt(KEY_STATE, -1)==0&&
                                shareGroup.getString(KEY_GROUP, "").equals("0")){
                            Toast.makeText(DealerDetailsActivity.this, "无关联", Toast.LENGTH_SHORT).show();
                        }else if (shareState.getInt(KEY_STATE, -1)==0&&
                                !shareGroup.getString(KEY_GROUP, "").equals("0")){
                            Intent intent = new Intent(DealerDetailsActivity.this, HotLineActivity.class);
                            intent.putExtra("dealerid", getIntent().getIntExtra("dealerid",0));
                            startActivity(intent);
                        }
                    }
                }
                if (position == 2) {
                    Intent intent = new Intent(DealerDetailsActivity.this, HomeIntroduceActivity.class);
                    intent.putExtra("url", otherlist.get(0).getData().getBottom_left_link3());
                    intent.putExtra("name", otherlist.get(0).getData().getBottom_left_title3());
                    intent.putExtra("state", getIntent().getIntExtra("state", -1));
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void initEvents() {
        //登录成功
        if (getIntent().getIntExtra("state", -1) == 0) {
            menuRight.setVisibility(View.GONE);
            menuBack.setVisibility(View.GONE);
            initWeather();
            //未登录
        } else  {
            menuBack.setVisibility(View.GONE);
            menuRight.setVisibility(View.VISIBLE);
            menuRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DealerDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
        final SharedPreferences share1 = getSharedPreferences(
                SHARED_PREFERENCES_GROUP, Context.MODE_PRIVATE);
        //登录成功没有关联并且没有记住密码
        if (share1.getString(KEY_GROUP, "").equals("0")&&getIntent().getIntExtra("state", -1) == 0){
            menuBack.setVisibility(View.VISIBLE);
        }
        ////登录成功没有关联并且没有记住密码
       else if (share1.getString(KEY_GROUP, "").equals("0")&&share.getInt(KEY_STATE, 1) == 0){
            menuBack.setVisibility(View.VISIBLE);
            menuRight.setVisibility(View.GONE);
        }else if (share.getInt(KEY_STATE, 1) == 0&&!share1.getString(KEY_GROUP, "").equals("0")) {
            menuRight.setVisibility(View.GONE);
            menuBack.setVisibility(View.GONE);
            initWeather();
        }
    }
    private void initWeather() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "APPCODE 18258472a22548ef8a7320fc0bafec9f");
        RequestParams params = new RequestParams();
        params.put("from", "5");
        params.put("lat", getIntent().getDoubleExtra("lat",0));
        params.put("lng", getIntent().getDoubleExtra("lng",0));
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
                    Intent intent = new Intent(DealerDetailsActivity.this, WeatherActivity.class);
                    intent.putExtra("lat",getIntent().getDoubleExtra("lat",0));
                    intent.putExtra("lng",getIntent().getDoubleExtra("lng",0));
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
                    Intent intent = new Intent(DealerDetailsActivity.this, WeatherActivity.class);
                    intent.putExtra("lat",getIntent().getDoubleExtra("lat",0));
                    intent.putExtra("lng",getIntent().getDoubleExtra("lng",0));
                    startActivity(intent);
                    overridePendingTransition(R.anim.photo_dialog_out_anim,0);
                }
            });
        }

    }

    //注意轮播图的位置是从1开始的,所以position-1
    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent(this, HomeIntroduceActivity.class);
        Log.i("地址", position + "");
        intent.putExtra("url", urllist.get(position - 1));
        intent.putExtra("state", getIntent().getIntExtra("state", -1));
        startActivity(intent);
    }

    //车型列表适配器(中间部分)
    class ModlesAdapter extends RecyclerView.Adapter<ModlesAdapter.ViewHolder> {
        private List<Modles> list;
        private Context context;


        public ModlesAdapter(List<Modles> list, Context context) {
            this.list = list;
            this.context = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(context, R.layout.modles_item, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.title.setText(list.get(position).getTitle());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, HomeIntroduceActivity.class);
                    intent.putExtra("url", list.get(position).getUrl());
                    intent.putExtra("name", list.get(position).getTitle());
                    intent.putExtra("state", getIntent().getIntExtra("state", -1));
                    context.startActivity(intent);
                }
            });
            Glide.with(context).load(list.get(position).getImg()).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView image;
            private TextView title;

            public ViewHolder(View convertView) {
                super(convertView);
                image = ((ImageView) convertView.findViewById(R.id.modles_image));
                title = ((TextView) convertView.findViewById(modles_title));
            }
        }
    }
}
