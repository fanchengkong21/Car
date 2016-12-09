package com.kfc.productcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.OtherMessage;

import java.util.List;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/29  13:14
 * @PackageName com.kfc.productcar.adapter
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class OtherMessageAdapter extends BaseAdapter {
    private List<OtherMessage> list;
    private Context context;

    public OtherMessageAdapter(List<OtherMessage> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
         return 3;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.othermessage_item, null);
        }
        viewHolder = getHolder(convertView);
        if (position==0){
            viewHolder.name.setText(list.get(0).getData().getBottom_left_title1());
            Glide.with(context).load(list.get(0).getData().getBottom_left_img1()).into(viewHolder.image);
        }
        if (position==1){
            viewHolder.name.setText(list.get(0).getData().getBottom_left_title2());
            Glide.with(context).load(list.get(0).getData().getBottom_left_img2()).into(viewHolder.image);
        }
        if (position==2){
            viewHolder.name.setText(list.get(0).getData().getBottom_left_title3());
            Glide.with(context).load(list.get(0).getData().getBottom_left_img3()).into(viewHolder.image);
        }

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

    static class ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            image = (ImageView) convertView.findViewById(R.id.image);
        }
    }
}
