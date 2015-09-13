package com.google.runda.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.google.runda.R;

/**
 * Created by bigface on 15/9/13.
 * 修改用户信息
 */
public class ChangeInfoActivity extends Activity {

    //todo 修改用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_change_info);
        //todo 加载布局文件
    }
}
