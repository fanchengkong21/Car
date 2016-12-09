package com.kfc.productcar.ui.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.SalesList;
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
import cz.msebera.android.httpclient.Header;

public class SalesRepActivity extends BaseActivity {


    @InjectView(R.id.sales_listView)
    ListView salesListView;
    private List<SalesList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_rep);
        initTitle(this, R.id.menu_title, getIntent().getStringExtra("title"));
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

        if (getIntent().getStringExtra("title").equals("销售顾问")){
        RequestParams params = new RequestParams();
        params.put("dealerid", getIntent().getIntExtra("dealerid",0));
        HttpClient.post(CarConfig.SELLERLIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                Log.i("销售代表", string);
                try {
                    JSONObject object=new JSONObject(string);
                    JSONArray array=object.getJSONArray("data");
                    for (int i = 0; i <array.length(); i++) {
                        JSONObject json=array.getJSONObject(i);
                        Gson gson=new Gson();
                        SalesList salesList = gson.fromJson(json.toString(), SalesList.class);
                        list.add(salesList);
                        loadData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        }, this);
        }
        if(getIntent().getStringExtra("title").equals("售后顾问")) {
            RequestParams params = new RequestParams();
            params.put("dealerid", getIntent().getIntExtra("dealerid",0));
            params.put("group","售后");
            HttpClient.post(CarConfig.SELLERLIST, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String string = new String(responseBody);
                    Log.i("销售代表", string);
                    try {
                        JSONObject object=new JSONObject(string);
                        JSONArray array=object.getJSONArray("data");
                        for (int i = 0; i <array.length(); i++) {
                            JSONObject json=array.getJSONObject(i);
                            Gson gson=new Gson();
                            SalesList salesList = gson.fromJson(json.toString(), SalesList.class);
                            list.add(salesList);
                            loadData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            }, this);
        }

    }

    private void loadData() {
        SalesListAdapter adapter=new SalesListAdapter(list,this);
        salesListView.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {

    }
    class SalesListAdapter extends BaseAdapter {
        private List<SalesList> list;
        private Context context;

        public SalesListAdapter(List<SalesList> list, Context context) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.salelist_item, null);
            }
            viewHolder = getHolder(convertView);
            if (getIntent().getStringExtra("title").equals("销售顾问")){
                viewHolder.type.setText(getIntent().getStringExtra("title"));
            }
            if (getIntent().getStringExtra("title").equals("售后顾问")){
                viewHolder.type.setText(getIntent().getStringExtra("title"));
            }
            Glide.with(context).load(list.get(position).getAvtar()).into(viewHolder.image);
            viewHolder.name.setText(list.get(position).getUsername());
            viewHolder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder dialog=new AlertDialog.Builder(SalesRepActivity.this);
                    dialog.setMessage("是否要拔打电话?").setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                      }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent=new Intent();
                                    Log.i("电话",list.get(position).getMobile());
                                    intent.setData(Uri.parse("tel:" + list.get(position).getMobile()));
                                    intent.setAction(Intent.ACTION_CALL);
                                    startActivity(intent);
                                    dialogInterface.dismiss();
                                }
                            }).create().show();

                }
            });
            return convertView;
        }
        private SalesListAdapter.ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
         class ViewHolder {
            TextView name,type;
            ImageView image,phone;

            public ViewHolder(View convertView) {
                name = (TextView) convertView.findViewById(R.id.salelist_name);
                type = (TextView) convertView.findViewById(R.id.salelist_type);
                image = (ImageView) convertView.findViewById(R.id.salelist_image);
                phone = (ImageView) convertView.findViewById(R.id.salelist_phone);
            }
        }
    }
}
