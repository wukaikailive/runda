package com.google.runda.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.runda.R;
import com.google.runda.event.DoActivityFinishEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by wukai on 2015/4/26.
 * 注册第一步 同意用户协议
 */
public class RegisterActivityStep1 extends Activity implements View.OnClickListener{

    ImageView ivCancel;
    ImageView ivNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_1);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void init(){
        ivCancel= (ImageView) findViewById(R.id.ivCancel);
        ivNext= (ImageView) findViewById(R.id.ivNext);


        ivCancel.setOnClickListener(this);
        ivNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivCancel:
                this.finish();
                break;
            case R.id.ivNext:
                startActivity(new Intent(this,RegisterActivityStep2.class));
                break;
        }
    }
    /*EventBus - - 响应开始*/
    public void onEventMainThread(DoActivityFinishEvent event){
        this.finish();
    }
    /*EventBus - - 响应结束*/
}
