package com.kfc.productcar.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ServiceActivity extends BaseActivity {
    private Integer[] images={R.mipmap.pet,R.mipmap.tour,R.mipmap.driv,R.mipmap.medical};
    private String[] titles={"宠物服务","旅游服务","自驾出行","医疗服务"};
    private List<Integer> imagelist;
    private List<String> titlelist;
    private String[] url={"https://mercedes-benz.tmall.com",
    "http://182.92.6.207:8081/imgad/20161114/top_ly.html",
    "http://119.254.66.154:8081/FVAPP/WeChat/selectedTrip_wx.html",
    "http://182.92.6.207:8081/imgad/20161114/top_yl.html"};


    @InjectView(R.id.service_listview)
    ListView serviceListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initTitle(this, R.id.menu_title, "生态圈");
        ButterKnife.inject(this);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        imagelist=new ArrayList<>();
        titlelist=new ArrayList<>();
        for (int i = 0; i <images.length ; i++) {
            imagelist.add(images[i]);
            titlelist.add(titles[i]);
        }
        ServiceAdapter adapter=new ServiceAdapter(imagelist,titlelist,this);
        serviceListview.setAdapter(adapter);
        serviceListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(ServiceActivity.this, url[position], Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void initEvents() {

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
    class ServiceAdapter extends BaseAdapter {
        private List<Integer> list;
        private List<String> title;
        private Context context;

        public ServiceAdapter(List<Integer> list, List<String> title, Context context) {
            this.list = list;
            this.title = title;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.service_item, null);
            }
            viewHolder = getHolder(convertView);
            viewHolder.title.setText(title.get(position));
            Glide.with(context).load(list.get(position)).into(viewHolder.image);


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
            ImageView image;
            TextView title;

            public ViewHolder(View convertView) {
                title = (TextView) convertView.findViewById(R.id.service_title);
                image = (ImageView) convertView.findViewById(R.id.service_image);
            }
        }
    }
}
