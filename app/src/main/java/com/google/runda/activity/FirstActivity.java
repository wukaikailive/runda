package com.google.runda.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.text.Html;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.google.runda.R;
import com.google.runda.staticModel.ServerConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstActivity extends Activity implements View.OnClickListener{
    Button mBtnReserve;
    Button mBtnSend;
    Button mBtnExit;
    Button mBtnOk;
    EditText mEtServerAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first_new_1);
        init();
    }


    private void init() {
        mBtnReserve= (Button) findViewById(R.id.btn_reserve);
        mBtnSend= (Button) findViewById(R.id.btn_send);
        mBtnExit= (Button) findViewById(R.id.btn_exit);
        mBtnOk= (Button) findViewById(R.id.btn_ok);
        mEtServerAddress= (EditText) findViewById(R.id.et_server_address);

        mBtnExit.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnReserve.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);

        mEtServerAddress.setText(ServerConfig.WAY_ROOT);
    }

    /**
     * 启动activity
     *
     * @param packageContext
     * @param cls
     */
    private void startActivity(Context packageContext, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(packageContext, cls);
        this.startActivity(intent);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_reserve:
                startActivity(this,UserActivity.class);
                break;
            case R.id.btn_send:
                Toast.makeText(FirstActivity.this,"此功能将在晚一些推出",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_exit:
                this.finish();
                break;
            case R.id.btn_ok:
                String serverAddress=mEtServerAddress.getText().toString().trim();
                //ServerConfig.WAY_ROOT=serverAddress;
                Toast.makeText(FirstActivity.this,"设置成功!"+ServerConfig.WAY_ROOT,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
