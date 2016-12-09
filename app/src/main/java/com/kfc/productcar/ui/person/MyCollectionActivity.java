package com.kfc.productcar.ui.person;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.CarConfig;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.Collection;
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

public class MyCollectionActivity extends BaseActivity {

    @InjectView(R.id.listview)
    ListView listview;
    private Collection myCollectionActivity;
    private List<Collection> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        initTitle(this, R.id.menu_title, "我的收藏");
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
        list = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.put("page", 1);
        HttpClient.post(CarConfig.FAVORITELIST, params, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(string);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Gson gson = new Gson();
                        myCollectionActivity = gson.fromJson(json.toString(), Collection.class);
                        list.add(myCollectionActivity);
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

    private void loadData() {
        MyCollectionAdapter adapter = new MyCollectionAdapter(list, this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
    }

    @Override
    protected void initEvents() {

    }

    class MyCollectionAdapter extends BaseAdapter {
        private List<Collection> list;
        private Context context;

        public MyCollectionAdapter(List<Collection> list, Context context) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.collection_item, null);
            }
            viewHolder = getHolder(convertView);
            viewHolder.title.setText(list.get(position).getTitle());
            viewHolder.name.setText(list.get(position).getForumname());

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

        class ViewHolder {
            TextView title, name, time;

            public ViewHolder(View convertView) {
                title = (TextView) convertView.findViewById(R.id.coll_title);
                name = (TextView) convertView.findViewById(R.id.coll_name);
                time = (TextView) convertView.findViewById(R.id.coll_time);
            }
        }

    }
}
