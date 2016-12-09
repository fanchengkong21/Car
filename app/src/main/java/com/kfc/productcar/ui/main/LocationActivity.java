package com.kfc.productcar.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;
import com.kfc.productcar.adapter.CityAdapter;
import com.kfc.productcar.bean.City;
import com.kfc.productcar.utils.ReadJson;
import com.kfc.productcar.utils.StringUtils;
import com.kfc.productcar.view.IndexBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LocationActivity extends BaseActivity implements IndexBar.OnIndexBarSelectListener {


    @InjectView(R.id.quickbar)
    IndexBar quickbar;
    @InjectView(R.id.suoyin)
    TextView suoyin;
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.tv_located_city)
    TextView tvLocatedCity;
    private List<City> list;
    private AMapLocationClient mLocationClient;
    private CityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.inject(this);
        initTitle(this, R.id.menu_title, "城市选择");
        initBtnBack(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }
        quickbar.setOnIndexBarSelectListener(this);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
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
                    loadDatas();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadDatas() {
        adapter = new CityAdapter(list, LocationActivity.this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(LocationActivity.this,MainActivity.class);
                intent.putExtra("locationId",list.get(position).getId());
                startActivity(intent);
                finish();
               /* Toast.makeText(LocationActivity.this, "你点击的城市Id是:"+list.get(position).getId()+"=="+position, Toast.LENGTH_SHORT).show();
                Log.i("城市","你点击的城市Id是:"+list.get(position).getId());
                finish();*/
                Toast.makeText(LocationActivity.this, "你点击的城市Id是", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initEvents() {
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
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        Log.e("城市", "city: " + city);
                        Log.e("城市", "district: " + district);
                        String location = StringUtils.extractLocation(city, district);
                        tvLocatedCity.setText(location);
                    } else {
                        //定位失败
                        tvLocatedCity.setText("定位失败");
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    //索引发生变化时候的回调,用于 listview 处理
    @Override
    public void onIndexBarSelect(String word) {
        for (int i = 0; i < list.size(); i++) {
            City city = list.get(i);
            if (word.equals(city.getFirstchar().charAt(0) + "")) {//如果当前的索引和这个对象拼音的首字母一致,则代表是第一个
                listview.setSelection(i);
                showSuoyin(word);
                return;
            }
        }
    }

    private Handler handler = new Handler();

    /**
     * 显示索引
     *
     * @param word
     */
    private void showSuoyin(String word) {
        suoyin.setText(word);
        suoyin.setVisibility(View.VISIBLE);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeSuoyin();
            }
        }, 2000);
    }


    /**
     * 关闭索引
     */
    private void closeSuoyin() {
        suoyin.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
