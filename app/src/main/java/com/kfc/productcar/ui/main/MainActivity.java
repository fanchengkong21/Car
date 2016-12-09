package com.kfc.productcar.ui.main;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.City;
import com.kfc.productcar.bean.GroupList;
import com.kfc.productcar.ui.home.DealerDetailsActivity;
import com.kfc.productcar.ui.person.SalesRepActivity;
import com.kfc.productcar.utils.HttpClient;
import com.kfc.productcar.utils.ReadJson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends TabActivity {
    @InjectView(R.id.main_home)
    RadioButton mainHome;
    @InjectView(R.id.main_forum)
    RadioButton mainForum;
    @InjectView(R.id.main_middle)
    RadioButton mainMiddle;
    @InjectView(R.id.main_service)
    RadioButton mainService;
    @InjectView(R.id.main_person)
    RadioButton mainPerson;
    private RadioGroup mTabButtonGroup;
    private TabHost mTabHost;
    //记住密码状态 0记住 1未记住
    public final static String SHARED_PREFERENCES_STATE="shared_preferences_state";
    public final static String KEY_STATE="key_state";
    //关联状态 0无关联其他有关联
    public final static String SHARED_PREFERENCES_GROUP = "shared_preferences_group";
    public final static String KEY_GROUP = "key_group";
    public final static String SHARED_PREFERENCES_DEALERID="shared_preferences_dealerid";
    public final static String KEY_DEALERID="key_dealerid";

    public static final String TAB_A = "A_ACTIVITY";
    public static final String TAB_B = "B_ACTIVITY";
    //public static final String TAB_C = "C_ACTIVITY";
    public static final String TAB_D = "D_ACTIVITY";
    public static final String TAB_E = "E_ACTIVITY";
    private AMapLocationClient mLocationClient;
    private double longitude, latitude;
    private List<City> list;
    private int id;
    private List<GroupList> glist;
    private GroupList groupList;
    private String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_title));
        }

        loadData();
        initViews();
    }

    private void loadData() {
        final SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_GROUP, Context.MODE_PRIVATE);
       final SharedPreferences share1 = getSharedPreferences(
                SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
        Log.i("code",share.getInt(KEY_STATE, -1)+"");

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
                        Log.i("经纬度", longitude + "," + latitude);
                        city = aMapLocation.getCity();
                       // Toast.makeText(MainActivity.this, "定位成功"+city, Toast.LENGTH_SHORT).show();
                        //登陆后无关联,记住密码
                        if (share.getString(KEY_GROUP,"-9").equals("0")&&share1.getInt(KEY_STATE, -1) == 0){
                            Toast.makeText(MainActivity.this, "登录后无关联", Toast.LENGTH_SHORT).show();
                            initFile();
                            //定位成功未登录
                        }else if (getIntent().getIntExtra("state",-1)!=0){
                            Toast.makeText(MainActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                            location();
                            //定位成功无关联
                        }else if (getIntent().getStringExtra("group").equals("0")){
                            Toast.makeText(MainActivity.this, "无关联", Toast.LENGTH_SHORT).show();
                            initFile();
                            //定位成功有关联
                        }else {
                            Toast.makeText(MainActivity.this, "有关联", Toast.LENGTH_SHORT).show();
                            location();
                        }
                        //定位失败
                    } else {
                        // Toast.makeText(MainActivity.this, "定位失败"+city, Toast.LENGTH_SHORT).show();
                        //定位失败未登录
                        if (getIntent().getIntExtra("state",-1)!=0){
                            Toast.makeText(MainActivity.this, "未登录", Toast.LENGTH_SHORT).show();
                            initFile();
                            //定位失败无关联
                        }else if (getIntent().getStringExtra("group").equals("0")){
                            Toast.makeText(MainActivity.this, "无关联", Toast.LENGTH_SHORT).show();
                            initFile();
                            //定位失败有关联
                        }else {
                            Toast.makeText(MainActivity.this, "有关联", Toast.LENGTH_SHORT).show();
                            location();
                        }

                    }
                }
            }
        });
        mLocationClient.startLocation();

    }

    private void location() {
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
                loadList();
            }
        }
    }

    private void initFile() {
        final SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_GROUP, Context.MODE_PRIVATE);
        mTabHost = getTabHost();
        Intent   i_a = new Intent(this, HomeActivity.class);//首页
        i_a.putExtra("state", getIntent().getIntExtra("state", -1));
        i_a.putExtra("locationId",getIntent().getStringExtra("locationId"));
        i_a.putExtra("group",share.getString(KEY_GROUP,"-9"));
        Intent i_b = new Intent(this, ForumActivity.class);//社区
        //Intent i_c = new Intent(this, MiddleActivity.class);//中间
        Intent i_d = new Intent(this, ServiceActivity.class);//服务
        Intent i_e = new Intent(this, PersonActivity.class);//我的
        mTabHost.addTab(mTabHost.newTabSpec(TAB_A).setIndicator(TAB_A)
                .setContent(i_a));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_B).setIndicator(TAB_B)
                .setContent(i_b));
        //mTabHost.addTab(mTabHost.newTabSpec(TAB_C).setIndicator(TAB_C));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_D).setIndicator(TAB_D)
                .setContent(i_d));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_E).setIndicator(TAB_E)
                .setContent(i_e));
        mTabHost.setCurrentTabByTag(TAB_A);
        mTabButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                setBtnTextColor();

                switch (checkedId) {
                    case R.id.main_home:
                        mainHome.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_A);
                        break;
                    case R.id.main_forum:

                        mainForum.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_B);
                        break;
                    case R.id.main_middle:
                        mainMiddle.setChecked(false);
                        //mTabHost.setCurrentTabByTag(TAB_C);
                        View dialogView=View.inflate(MainActivity.this,R.layout.middle_dialog,null);
                        final Dialog dialog = new Dialog(MainActivity.this,R.style.Dialog_Fullscreen);
                        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        Window window = dialog.getWindow();
                        window.setWindowAnimations(R.style.main_menu_animstyle);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        TextView middle_sales = (TextView) dialogView.findViewById(R.id.middle_sales);
                        TextView middle_customer = (TextView) dialogView.findViewById(R.id.middle_customer);
                        TextView middle_post = (TextView) dialogView.findViewById(R.id.middle_post);
                        TextView middle_invitation = (TextView) dialogView.findViewById(R.id.middle_invitation);
                        ImageView middle_delete = (ImageView) dialogView.findViewById(R.id.middler_delete);
                        middle_sales.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(MainActivity.this, SalesRepActivity.class);
                                intent.putExtra("title","销售顾问");
                                startActivity(intent);
                                dialog.dismiss();
                                mainMiddle.setChecked(false);


                            }
                        });
                        middle_customer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(MainActivity.this, SalesRepActivity.class);
                                intent.putExtra("title","售后顾问");
                                startActivity(intent);
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        middle_post.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        middle_invitation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(MainActivity.this, InvitationActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        middle_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.main_service:
                        mainService.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_D);
                        break;
                    case R.id.main_person:
                        mainPerson.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_E);
                        break;

                }
            }
        });
    }

    private void loadList() {
        glist=new ArrayList<>();
        RequestParams params = new RequestParams();
        Log.i("经销商列表", id+"");
        params.put("area", id);
        params.put("page", 1);
        params.put("point", longitude + "," + latitude);
        Log.i("经销商列表", longitude + "," + latitude);
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
                        initEvents();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, MainActivity.this);

    }


    protected void initViews() {
        mTabButtonGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        if (getIntent().getIntExtra("state",-1)==0){
            mTabButtonGroup.setVisibility(View.VISIBLE);
        }else {
            mTabButtonGroup.setVisibility(View.GONE);
        }
       SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_STATE, Context.MODE_PRIVATE);
       Log.i("code",share.getInt(KEY_STATE, -1)+"");
        if (share.getInt(KEY_STATE, -1) == 0) {
            mTabButtonGroup.setVisibility(View.VISIBLE);
        }
    }


    protected void initEvents() {
        mTabHost = getTabHost();
        SharedPreferences share = getSharedPreferences(
                SHARED_PREFERENCES_DEALERID, Context.MODE_PRIVATE);
        Intent   i_a = new Intent(this, DealerDetailsActivity.class);//首页
         i_a.putExtra("name", glist.get(0).getName());
         share.edit().putInt(KEY_DEALERID,Integer.parseInt(glist.get(0).getId())).commit();
         i_a.putExtra("dealerid", Integer.parseInt(glist.get(0).getId()));
         i_a.putExtra("state", getIntent().getIntExtra("state", -1));
         i_a.putExtra("id",getIntent().getStringExtra("id"));
         i_a.putExtra("lat",latitude);
         i_a.putExtra("lng",longitude);
        Intent i_b = new Intent(this, ForumActivity.class);//社区
        //Intent i_c = new Intent(this, MiddleActivity.class);//中间
        Intent i_d = new Intent(this, ServiceActivity.class);//服务
        Intent i_e = new Intent(this, PersonActivity.class);//我的
        mTabHost.addTab(mTabHost.newTabSpec(TAB_A).setIndicator(TAB_A)
                .setContent(i_a));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_B).setIndicator(TAB_B)
                .setContent(i_b));
        //mTabHost.addTab(mTabHost.newTabSpec(TAB_C).setIndicator(TAB_C));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_D).setIndicator(TAB_D)
                .setContent(i_d));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_E).setIndicator(TAB_E)
                .setContent(i_e));
        mTabHost.setCurrentTabByTag(TAB_A);
        mTabButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                setBtnTextColor();

                switch (checkedId) {
                    case R.id.main_home:
                        mainHome.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_A);
                        break;
                    case R.id.main_forum:

                        mainForum.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_B);
                        break;
                    case R.id.main_middle:
                        mainMiddle.setChecked(false);
                        //mTabHost.setCurrentTabByTag(TAB_C);
                        View dialogView=View.inflate(MainActivity.this,R.layout.middle_dialog,null);
                        final Dialog dialog = new Dialog(MainActivity.this,R.style.Dialog_Fullscreen);
                        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        Window window = dialog.getWindow();
                        window.setWindowAnimations(R.style.main_menu_animstyle);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        TextView middle_sales = (TextView) dialogView.findViewById(R.id.middle_sales);
                        TextView middle_customer = (TextView) dialogView.findViewById(R.id.middle_customer);
                        TextView middle_post = (TextView) dialogView.findViewById(R.id.middle_post);
                        TextView middle_invitation = (TextView) dialogView.findViewById(R.id.middle_invitation);
                        ImageView middle_delete = (ImageView) dialogView.findViewById(R.id.middler_delete);
                        middle_sales.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(MainActivity.this, SalesRepActivity.class);
                                intent.putExtra("title","销售顾问");
                                intent.putExtra("dealerid", Integer.parseInt(glist.get(0).getId()));
                                startActivity(intent);
                                dialog.dismiss();
                                mainMiddle.setChecked(false);


                            }
                        });
                        middle_customer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(MainActivity.this, SalesRepActivity.class);
                                intent.putExtra("dealerid", Integer.parseInt(glist.get(0).getId()));
                                intent.putExtra("title","售后顾问");
                                startActivity(intent);
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        middle_post.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        middle_invitation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(MainActivity.this, InvitationActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        middle_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                mainMiddle.setChecked(false);
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.main_service:
                        mainService.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_D);
                        break;
                    case R.id.main_person:
                        mainPerson.setTextColor(getResources().getColor(R.color.main_rb_text_selected));
                        mTabHost.setCurrentTabByTag(TAB_E);
                        break;

                }
            }
        });
    }

    private void setBtnTextColor() {
        mainHome.setTextColor(getResources().getColor(R.color.main_rb_text));
        mainForum.setTextColor(getResources().getColor(R.color.main_rb_text));
        mainService.setTextColor(getResources().getColor(R.color.main_rb_text));
        mainPerson.setTextColor(getResources().getColor(R.color.main_rb_text));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * Double click exit function
     */
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



}
