package com.kfc.productcar.ui.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.User;
import com.kfc.productcar.bean.UserInfo;
import com.kfc.productcar.utils.HttpClient;
import com.kfc.productcar.utils.ReadJson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cz.msebera.android.httpclient.Header;


public class PersonalDataActivity extends BaseActivity {
    @InjectView(R.id.personal_listview)
    ListView personal_listview;
    private UserInfo userInfo;
    private List<UserInfo> list;
    private String datasel,datayear,datamonth,dataday;
    private String dataprovince, datacity, datacounty;
    private String dataprovince1, datacity1, datacounty1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initTitle(this, R.id.menu_title, "个人资料");
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
        list=new ArrayList<>();
        RequestParams params=new RequestParams();
        HttpClient.get(CarConfig.USERBASEINFO, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string=new String(responseBody);
                Log.i("用户信息",string);
                try {
                    JSONObject object=new JSONObject(string);
                    Gson gson=new Gson();
                    userInfo = gson.fromJson(object.toString(), UserInfo.class);
                    list.add(userInfo);
                    loadData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        },PersonalDataActivity.this);
    }
    @Override
    protected void initEvents() {

    }

    private void loadData() {
        PersonalDataAdapter adapter=new PersonalDataAdapter(list,this);
        personal_listview.setAdapter(adapter);


    }


    @OnClick(R.id.person_save)
    public void onClick() {
        RequestParams params=new RequestParams();
        params.put("sex",datasel);
        params.put("birthyear",datayear);
        params.put("birthmonth",datamonth);
        params.put("birthday",dataday);
        params.put("birthprovince",dataprovince);
        params.put("birthcity",datacity);
        params.put("birthdist",datacounty);
        params.put("resideprovince",dataprovince1);
        params.put("residecity",datacity1);
        params.put("residedist",datacounty1);
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
                        final AlertDialog.Builder dialog=new AlertDialog.Builder(PersonalDataActivity.this);
                        dialog.setTitle("保存成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.create().dismiss();
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
        },PersonalDataActivity.this);


    }
    public class PersonalDataAdapter extends BaseAdapter {
        private Context context;
        private List<UserInfo> datas;

        public PersonalDataAdapter(List<UserInfo> datas, Context context) {
            this.datas = datas;
            this.context = context;
        }
        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
           ViewHolderdata viewHoldercontent=null;
            if (convertView==null){
                convertView=View.inflate(context, R.layout.personaldata_item,null);
                viewHoldercontent=new ViewHolderdata();
                viewHoldercontent.personaldata_layout= ((LinearLayout) convertView.findViewById(R.id.personaldata_layout));
                viewHoldercontent.personaldata_name=((TextView) convertView.findViewById(R.id.personaldata_name));
                viewHoldercontent.personaldata_value=((TextView) convertView.findViewById(R.id.personaldata_value));
                viewHoldercontent.personal_image= ((ImageView) convertView.findViewById(R.id.personal_image));
                viewHoldercontent.personaldata_image= ((ImageView) convertView.findViewById(R.id.personaldata_image));
                viewHoldercontent.data_line= ((View) convertView.findViewById(R.id.data_line));
                convertView.setTag(viewHoldercontent);
            } else {
                viewHoldercontent= (ViewHolderdata) convertView.getTag();
            }
            if (position==0){
                viewHoldercontent.personal_image.setVisibility(View.GONE);
                viewHoldercontent.personaldata_name.setText("头衔");
                viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getGrouptitle());
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
            }
            if (position==1){
                viewHoldercontent.personal_image.setVisibility(View.GONE);
                viewHoldercontent.personaldata_name.setText("里程");
                viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getExtcredits1()+"");
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                viewHoldercontent.data_line.setVisibility(View.GONE);
            }
           /* if (position==2){
                viewHoldercontent.personal_image.setVisibility(View.GONE);
                viewHoldercontent.personaldata_name.setText("品币");
                viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getExtcredits2()+"");
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                viewHoldercontent.data_line.setVisibility(View.GONE);
            }*/
            if (position==2){
                viewHoldercontent.personaldata_layout.setVisibility(View.GONE);
                viewHoldercontent.data_line.setVisibility(View.GONE);
            }
            if (position==3){
                viewHoldercontent.personaldata_name.setText("真实姓名");
                viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getRealname());
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PersonalDataActivity.this, RealNameActivity.class);
                        startActivity(intent);
                    }
                });

            }
            if (position==4){
                viewHoldercontent.personaldata_name.setText("性别");
                if (datas.get(0).getData().getSex().equals("1")){
                    viewHoldercontent.personaldata_value.setText("男");
                }else{
                    viewHoldercontent.personaldata_value.setText("女");
                }
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                final ViewHolderdata finalViewHoldercontent = viewHoldercontent;
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String [] item={"男","女"};
                        AlertDialog.Builder builder=new AlertDialog.Builder(PersonalDataActivity.this);
                        builder.setTitle("请选择性别")
                                .setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        finalViewHoldercontent.personaldata_value.setText(item[which]);
                                        datasel=item[which];
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                });

            }
            if (position==5){
                viewHoldercontent.personaldata_name.setText("出生日期");
                viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getBirth());
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                final ViewHolderdata finalViewHoldercontent1 = viewHoldercontent;
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker picker = new DatePicker(PersonalDataActivity.this, DatePicker.YEAR_MONTH_DAY);
                        picker.setRangeStart(1950, 8, 29);//开始范围
                        picker.setRangeEnd(2020, 1, 1);//结束范围
                        picker.setSelectedItem(2000,1,1);
                        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                            @Override
                            public void onDatePicked(String year, String month, String day) {
                                finalViewHoldercontent1.personaldata_value.setText(year+"."+month+"."+day);
                                datayear=year;
                                datamonth=month;
                                dataday=day;
                                //Toast.makeText(PersonalDataActivity.this, year+month+day, Toast.LENGTH_SHORT).show();
                            }
                        });
                        picker.show();
                    }
                });

            }if (position==6){
                viewHoldercontent.personaldata_name.setText("出生城市");
                viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getBirthprovince()
                        +"-"+datas.get(0).getData().getBirthcity()+"-"+
                        datas.get(0).getData().getBirthdist());
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                final ViewHolderdata finalViewHoldercontent2 = viewHoldercontent;
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Province> data = new ArrayList<Province>();
                        try {
                            String json = ReadJson.getJson(PersonalDataActivity.this,"city2.json");
                            data.addAll(JSON.parseArray(json, Province.class));
                            AddressPicker picker = new AddressPicker(PersonalDataActivity.this, data);

                            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                                @Override
                                public void onAddressPicked(Province province, City city, County county) {
                                    if (county == null) {
                                        finalViewHoldercontent2.personaldata_value.setText(province.getAreaName()+"-"+
                                                city.getAreaName()+"-"+county.getAreaName());
                                    } else {
                                        finalViewHoldercontent2.personaldata_value.setText(province.getAreaName()+"-"+
                                                city.getAreaName()+"-"+county.getAreaName());
                                    }

                                        dataprovince=province.getAreaName();
                                        datacity=city.getAreaName();
                                        datacounty=county.getAreaName();


                                }
                            });
                            picker.show();
                        } catch (Exception e) {
                            Toast.makeText(PersonalDataActivity.this, "", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }if (position==7){
                viewHoldercontent.personaldata_name.setText("居住城市");
                viewHoldercontent.personaldata_value.setText(
                        datas.get(0).getData().getResideprovince()+"-"+
                                datas.get(0).getData().getResidecity()+"-"+
                                datas.get(0).getData().getResidedist());
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                viewHoldercontent.data_line.setVisibility(View.GONE);
                final ViewHolderdata finalViewHoldercontent3 = viewHoldercontent;
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<Province> data = new ArrayList<Province>();
                        try {
                            String json = ReadJson.getJson(PersonalDataActivity.this,"city2.json");
                            data.addAll(JSON.parseArray(json, Province.class));
                            AddressPicker picker = new AddressPicker(PersonalDataActivity.this, data);

                            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                                @Override
                                public void onAddressPicked(Province province1, City city1, County county1) {
                                    if (county1 == null) {
                                        finalViewHoldercontent3.personaldata_value.setText(province1.getAreaName()+"-"+
                                                city1.getAreaName()+"-"+county1.getAreaName());
                                    } else {
                                        finalViewHoldercontent3.personaldata_value.setText(province1.getAreaName()+"-"+
                                                city1.getAreaName()+"-"+county1.getAreaName());
                                    }
                                    dataprovince1=province1.getAreaName();
                                    datacity1=city1.getAreaName();
                                    datacounty1=county1.getAreaName();

                                }
                            });
                            picker.show();
                        } catch (Exception e) {
                            Toast.makeText(PersonalDataActivity.this, "", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
            if (position==8){
                viewHoldercontent.personaldata_layout.setVisibility(View.GONE);
                viewHoldercontent.data_line.setVisibility(View.GONE);
            }
            if (position==9){
                viewHoldercontent.personaldata_name.setText("修改密码");
                viewHoldercontent.personaldata_value.setText("");
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PersonalDataActivity.this, NewPasswordActivity.class);
                        startActivity(intent);
                    }
                });

            }
            if (position==10){
                viewHoldercontent.personaldata_name.setText("绑定手机");
                viewHoldercontent.personaldata_value.setText("");
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PersonalDataActivity.this, BoundPhoneActivity.class);
                        startActivity(intent);
                    }
                });

            }
            if (position==11){
                viewHoldercontent.personaldata_name.setText("绑定车架号");
                viewHoldercontent.personaldata_value.setText("");
                viewHoldercontent.personaldata_image.setVisibility(View.GONE);
                viewHoldercontent.personaldata_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }




            return convertView;
        }



          class ViewHolderdata {
            LinearLayout personaldata_layout;
            TextView personaldata_name,personaldata_value;
            ImageView personal_image,personaldata_image;
              View data_line;
        }
    }
}
