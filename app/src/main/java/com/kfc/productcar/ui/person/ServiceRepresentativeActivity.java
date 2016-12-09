package com.kfc.productcar.ui.person;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceRepresentativeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_representative);
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


    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.sales_layout, R.id.customer_layout, R.id.message_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sales_layout:
                Intent intent=new Intent(ServiceRepresentativeActivity.this,SalesRepActivity.class);
                intent.putExtra("title","销售顾问");
                startActivity(intent);
                break;
            case R.id.customer_layout:
                Intent intent1=new Intent(ServiceRepresentativeActivity.this,SalesRepActivity.class);
                intent1.putExtra("title","售后顾问");
                startActivity(intent1);
                break;
            case R.id.message_layout:
                Intent intent2=new Intent(ServiceRepresentativeActivity.this,LeaveMessageActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
