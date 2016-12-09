package com.kfc.productcar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kfc.productcar.R;
import com.kfc.productcar.bean.UserInfo;

import java.util.List;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/25  11:35
 * @PackageName com.kfc.productcar.adapter
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class PersonalDataAdapter extends BaseAdapter {
    private Context context;
    private List<UserInfo> datas;

    public PersonalDataAdapter(List<UserInfo> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }
    @Override
    public int getCount() {
        return 11;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolderdata viewHoldercontent=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.personaldata_item,null);
            viewHoldercontent=new ViewHolderdata();
            viewHoldercontent.personaldata_layout= ((LinearLayout) convertView.findViewById(R.id.personaldata_layout));
            viewHoldercontent.personaldata_name=((TextView) convertView.findViewById(R.id.personaldata_name));
            viewHoldercontent.personaldata_value=((TextView) convertView.findViewById(R.id.personaldata_value));
            viewHoldercontent.personal_image= ((ImageView) convertView.findViewById(R.id.personal_image));
            viewHoldercontent.personaldata_image= ((ImageView) convertView.findViewById(R.id.personaldata_image));
            convertView.setTag(viewHoldercontent);
        } else {
            viewHoldercontent= (ViewHolderdata) convertView.getTag();
        }
        if (position==0){
            viewHoldercontent.personal_image.setVisibility(View.GONE);
            viewHoldercontent.personaldata_name.setText("头衔");
            viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getGrouptitle());
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }
        if (position==1){
            viewHoldercontent.personal_image.setVisibility(View.GONE);
            viewHoldercontent.personaldata_name.setText("里程");
            viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getExtcredits1()+"");
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }
        if (position==2){
            viewHoldercontent.personal_image.setVisibility(View.GONE);
            viewHoldercontent.personaldata_name.setText("品币");
            viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getExtcredits2()+"");
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }
        if (position==3){
            viewHoldercontent.personaldata_layout.setVisibility(View.GONE);
        }
        if (position==4){
            viewHoldercontent.personaldata_name.setText("真实姓名");
            viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getRealname());
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }
        if (position==5){
            viewHoldercontent.personaldata_name.setText("性别");
            if (datas.get(0).getData().getSex().equals("1")){
                viewHoldercontent.personaldata_value.setText("女");
            }else{
                viewHoldercontent.personaldata_value.setText("男");
            }
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);

        }
        if (position==6){
            viewHoldercontent.personaldata_name.setText("出生日期");
            viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getBirth());
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }if (position==7){
            viewHoldercontent.personaldata_name.setText("出生城市");
            viewHoldercontent.personaldata_value.setText(datas.get(0).getData().getBirthprovince()
            +" "+datas.get(0).getData().getBirthcity());
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }if (position==8){
            viewHoldercontent.personaldata_name.setText("居住城市");
            viewHoldercontent.personaldata_value.setText(
                    datas.get(0).getData().getResideprovince()+" "+datas.get(0).getData().getResidecity());
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }
        if (position==9){
            viewHoldercontent.personaldata_layout.setVisibility(View.GONE);
        }
        if (position==10){
            viewHoldercontent.personaldata_name.setText("修改密码");
            viewHoldercontent.personaldata_value.setText("");
            viewHoldercontent.personaldata_image.setVisibility(View.GONE);
        }




        return convertView;
    }

    static  class ViewHolderdata {
        LinearLayout personaldata_layout;
        TextView personaldata_name,personaldata_value;
        ImageView personal_image,personaldata_image;
    }
}
