package com.kfc.productcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kfc.productcar.R;
import com.kfc.productcar.bean.City;

import java.util.Collections;
import java.util.List;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/18  10:18
 * @PackageName com.kfc.productcar.adapter
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class CityAdapter extends BaseAdapter {

    private List<City> friends;
    private Context context;



    public CityAdapter(List<City> friends, Context context) {
        this.friends = friends;
        this.context = context;
        if (friends != null) {//如果不为空 排序
            Collections.sort(friends);
        }
    }



    @Override
    public int getCount() {
        return friends == null ? 0 : friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.city_item, null);
        }
            viewHolder = getHolder(convertView);
                viewHolder.name.setText(friends.get(position).getName());
                String indexWord = friends.get(position).getFirstchar().charAt(0) + "";
                if (position >0 && indexWord.equals(friends.get(position - 1).getFirstchar().charAt(0) + "")) {//如果不是0 并且当前的索引和上一次的相同,就隐藏
                    viewHolder.index.setVisibility(View.GONE);
                } else {
                    viewHolder.index.setVisibility(View.VISIBLE);
                    viewHolder.index.setText(indexWord);
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
        TextView index;
        TextView name;

        public ViewHolder(View convertView) {
            index = (TextView) convertView.findViewById(R.id.index);
            name = (TextView) convertView.findViewById(R.id.name);
        }
    }


}
