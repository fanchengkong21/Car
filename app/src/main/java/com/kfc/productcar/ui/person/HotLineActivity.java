package com.kfc.productcar.ui.person;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HotLineActivity extends BaseActivity {
    @InjectView(R.id.hotline_listview)
    ListView hotlineListview;
    private Integer[] images = {R.mipmap.sales_re, R.mipmap.person_friend, R.mipmap.person_email, R.mipmap.person_authentication};
    private String[] titles = {"销售顾问", "售后顾问", "给店内留言", "绑定专属顾问"};
    private List<Integer> imagelist;
    private List<String> titlelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_line);
        initTitle(this, R.id.menu_title, "热线电话");
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
        imagelist=new ArrayList<>();
        titlelist=new ArrayList<>();
        for (int i = 0; i <images.length ; i++) {
            imagelist.add(images[i]);
            titlelist.add(titles[i]);
        }
        HotLineAdapter adapter=new HotLineAdapter(imagelist,titlelist,this);
        hotlineListview.setAdapter(adapter);
        hotlineListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position==0){
                    Intent intent = new Intent(HotLineActivity.this, SalesRepActivity.class);
                    intent.putExtra("dealerid",  getIntent().getIntExtra("dealerid",0));
                    intent.putExtra("title", "销售顾问");
                    startActivity(intent);
                }
                if (position==1){
                    Intent intent1 = new Intent(HotLineActivity.this, SalesRepActivity.class);
                    intent1.putExtra("dealerid",  getIntent().getIntExtra("dealerid",0));
                    intent1.putExtra("title", "售后顾问");
                    startActivity(intent1);
                }
                if (position==2){
                    Intent intent2 = new Intent(HotLineActivity.this, LeaveMessageActivity.class);
                    intent2.putExtra("dealerid",  getIntent().getIntExtra("dealerid",0));
                    startActivity(intent2);
                }
                if (position==3){
                    Intent intent3 = new Intent(HotLineActivity.this, BindDelarerActivity.class);
                    //intent2.putExtra("dealerid",  getIntent().getIntExtra("dealerid",0));
                    startActivity(intent3);
                }
            }
        });

    }

    @Override
    protected void initEvents() {

    }

    class HotLineAdapter extends BaseAdapter {
        private List<Integer> list;
        private List<String> title;
        private Context context;

        public HotLineAdapter(List<Integer> list, List<String> title, Context context) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.hotline_item, null);
            }
            viewHolder = getHolder(convertView);
            viewHolder.name.setText(title.get(position));
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
            TextView name;
            ImageView image;

            public ViewHolder(View convertView) {
                name = (TextView) convertView.findViewById(R.id.hotLine_name);
                image = (ImageView) convertView.findViewById(R.id.hotLine_image);

            }
        }
    }
}