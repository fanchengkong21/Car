package com.kfc.productcar.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.Weather;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

public class WeatherActivity extends BaseActivity {
    @InjectView(R.id.weather_day)
    TextView weatherDay;
    @InjectView(R.id.weather_week)
    TextView weatherWeek;
    @InjectView(R.id.weather_month)
    TextView weatherMonth;
    @InjectView(R.id.weather_year)
    TextView weatherYear;
    @InjectView(R.id.weather_image)
    ImageView weatherImage;
    @InjectView(R.id.low)
    TextView low;
    @InjectView(R.id.height)
    TextView height;
    @InjectView(R.id.pm)
    TextView pm;
    @InjectView(R.id.weather_status)
    TextView weatherStatus;
    @InjectView(R.id.weather_bottom)
    RelativeLayout weatherBottom;

    private Weather weather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.inject(this);
        initViews();
        initEvents();

    }

    @Override
    protected void initViews() {
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
        String day = weather.getShowapi_res_body().getF1().getDay().substring(
                weather.getShowapi_res_body().getF1().getDay().length() - 2,
                weather.getShowapi_res_body().getF1().getDay().length());
        weatherDay.setText(day);
        String month= weather.getShowapi_res_body().getF1().getDay().substring(
                weather.getShowapi_res_body().getF1().getDay().length() - 4,
                weather.getShowapi_res_body().getF1().getDay().length()-2);
        weatherMonth.setText(month+"/");
        String year= weather.getShowapi_res_body().getF1().getDay().substring(0,
                weather.getShowapi_res_body().getF1().getDay().length()-4);
        weatherYear.setText(year);
        int week=weather.getShowapi_res_body().getF1().getWeekday();
        if (week == 1) {
            weatherWeek.setText("星期一");
        } else if (week == 2) {
            weatherWeek.setText("星期二");
        } else if (week == 3) {
            weatherWeek.setText("星期三");
        } else if (week == 4) {
            weatherWeek.setText("星期四");
        } else if (week == 5) {
            weatherWeek.setText("星期五");
        } else if (week == 6) {
            weatherWeek.setText("星期六");
        } else if (week == 7) {
            weatherWeek.setText("星期天");
        }
        Glide.with(this).load(weather.getShowapi_res_body().getNow().getWeather_pic()).into(weatherImage);
        low.setText(weather.getShowapi_res_body().getF1().getNight_air_temperature()+"/");
        height.setText(weather.getShowapi_res_body().getF1().getDay_air_temperature()+"℃");
        pm.setText(weather.getShowapi_res_body().getNow().getAqiDetail().getPm2_5()+"");
        weatherStatus.setText(weather.getShowapi_res_body().getNow().getWeather()+","+
        weather.getShowapi_res_body().getNow().getAqiDetail().getQuality()+","+
        weather.getShowapi_res_body().getNow().getWind_direction()+","+
        weather.getShowapi_res_body().getNow().getWind_power());


    }

    @Override
    protected void initEvents() {
        weatherBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
