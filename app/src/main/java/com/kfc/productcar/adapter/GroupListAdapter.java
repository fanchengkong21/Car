package com.kfc.productcar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.GroupList;

import java.util.List;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/23  11:49
 * @PackageName com.kfc.productcar.adapter
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    private List<GroupList> list;
    private Context context;

    public GroupListAdapter(List<GroupList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context,R.layout.grouplist_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.group_name.setText(list.get(position).getName());
        holder.group_distance.setText(list.get(position).getDistance());
        Glide.with(context).load(list.get(position).getImg()).into(holder.group_image);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public  static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView group_image;
        private TextView group_name,group_distance;

        public ViewHolder(View convertView) {
            super(convertView);
            group_image= ((ImageView) convertView.findViewById(R.id.group_image));
            group_name= ((TextView) convertView.findViewById(R.id.group_name));
            group_distance= ((TextView) convertView.findViewById(R.id.group_distance));
        }
    }

}
