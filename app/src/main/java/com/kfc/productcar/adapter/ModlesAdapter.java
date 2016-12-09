package com.kfc.productcar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.Modles;
import com.kfc.productcar.ui.home.HomeIntroduceActivity;

import java.util.List;

import static com.kfc.productcar.R.id.modles_title;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/29  11:34
 * @PackageName com.kfc.productcar.adapter
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class ModlesAdapter extends RecyclerView.Adapter<ModlesAdapter.ViewHolder> {
    private List<Modles> list;
    private Context context;


    public ModlesAdapter(List<Modles> list, Context context) {
        this.list = list;
        this.context = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context,R.layout.modles_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,HomeIntroduceActivity.class);
                intent.putExtra("url",list.get(position).getUrl());
                intent.putExtra("name",list.get(position).getTitle());

                context.startActivity(intent);
            }
        });
        Glide.with(context).load(list.get(position).getImg()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;

        public ViewHolder(View convertView) {
            super(convertView);
            image= ((ImageView) convertView.findViewById(R.id.modles_image));
            title= ((TextView) convertView.findViewById(modles_title));
        }
    }
}
