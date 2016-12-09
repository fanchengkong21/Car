package com.kfc.productcar.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.kfc.productcar.BaseActivity;
import com.kfc.productcar.R;

public class WeclomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weclome);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }
}
