package com.google.runda.activity.order;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.google.runda.R;

/**
 * Created by bigface on 2015/9/12.
 * 订单确认界面
 */
public class OrderEnsureActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_ensure);

    }
}