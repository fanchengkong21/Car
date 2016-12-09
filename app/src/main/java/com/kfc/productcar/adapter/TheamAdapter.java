package com.kfc.productcar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.ForumList;
import com.kfc.productcar.bean.GroupList;
import com.kfc.productcar.bean.Theam;
import com.kfc.productcar.bean.UserInfo;
import com.kfc.productcar.ui.forum.ForumDetailsActivity;
import com.kfc.productcar.ui.main.ForumActivity;

import java.util.List;

/**
 * Created by Ly on 2016/12/6.
 */

public class TheamAdapter extends BaseAdapter {
    private Context context;
    private List<Theam> listTheam;

    public TheamAdapter(List<Theam> list, Context context) {
        this.listTheam = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listTheam.size();
    }

    @Override
    public Object getItem(int position) {
        return listTheam.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TheamAdapter.ViewHolderdata viewHoldercontent = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.activity_list_item, null);
            viewHoldercontent = new TheamAdapter.ViewHolderdata();
            viewHoldercontent.txt_item = (TextView) convertView.findViewById(R.id.txt_item);
            convertView.setTag(viewHoldercontent);
        } else {
            viewHoldercontent = (TheamAdapter.ViewHolderdata) convertView.getTag();
        }
        viewHoldercontent.txt_item.setText(listTheam.get(position).getName());

        return convertView;
    }

    static class ViewHolderdata {
        TextView txt_item;
    }

}
