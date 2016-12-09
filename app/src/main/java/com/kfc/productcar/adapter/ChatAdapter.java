package com.kfc.productcar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kfc.productcar.R;
import com.kfc.productcar.bean.ChatModel;
import com.kfc.productcar.bean.ItemModel;

import java.util.ArrayList;

/**
 * @author fancheng.kong
 * @CreateTime 2016/12/8  16:35
 * @PackageName com.kfc.productcar.adapter
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {

    private ArrayList<ItemModel> dataList = new ArrayList<>();

    public void replaceAll(ArrayList<ItemModel> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ItemModel> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(),list.size());
        }

    }

    @Override
    public ChatAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ChatAViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send, parent, false));
            case 1:
                return new ChatBViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_service, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.BaseAdapter holder, int position) {
        holder.setData(dataList.get(position).object);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(Object object) {

        }
    }

    private class ChatAViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        public ChatAViewHolder(View view) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            Glide.with(itemView.getContext()).load(model.getAvatar()).into(ic_user);
            tv.setText(model.getMessage());
        }
    }
    private class ChatBViewHolder extends BaseAdapter {
        private ImageView ic_user;
        private TextView tv;

        public ChatBViewHolder(View view) {
            super(view);
            ic_user = (ImageView) itemView.findViewById(R.id.ic_user);
            tv = (TextView) itemView.findViewById(R.id.tv);

        }
        @Override
        void setData(Object object) {
            super.setData(object);
            ChatModel model = (ChatModel) object;
            Glide.with(itemView.getContext()).load(model.getAvatar()).into(ic_user);
            tv.setText(model.getMessage());
        }
    }
}
