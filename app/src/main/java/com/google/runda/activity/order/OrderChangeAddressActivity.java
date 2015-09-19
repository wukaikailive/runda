package com.google.runda.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.runda.R;
import com.google.runda.event.OrderAddressChangedEvent;
import com.google.runda.interfaces.IArgModel;
import com.google.runda.interfaces.IHolder;

import de.greenrobot.event.EventBus;

/**
 * Created by bigface on 2015/9/17.
 */
public class OrderChangeAddressActivity extends Activity implements View.OnClickListener{

    ChangeAddressOrderHolder changeAddressOrderHolder;
    ArgModel argModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_change_address);
        changeAddressOrderHolder=new ChangeAddressOrderHolder();
        changeAddressOrderHolder.init();
        changeAddressOrderHolder.bindClickEvent(this);
        changeAddressOrderHolder.hide();
        argModel=new ArgModel();
        argModel.init(getIntent());
        argModel.bindValueOfView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lila_back:
                this.finish();
                break;
            case R.id.btn_ensure_change:
                //确认修改
                OrderAddressChangedEvent event=new OrderAddressChangedEvent();
                event.userName=changeAddressOrderHolder.edtUserName.getText().toString();
                event.userPhone=changeAddressOrderHolder.edtUserPhone.getText().toString();
                event.detailAddress=changeAddressOrderHolder.edtDetailAddress.getText().toString();
                EventBus.getDefault().post(event);
                this.finish();
                break;
        }
    }
    class ArgModel implements IArgModel{

        String userName;
        String region;
        String detailAddress;
        String userPhone;

        @Override
        public void init(Intent intent) {
            Bundle bundle=intent.getExtras();
            userName=bundle.getString("receiverName");
            region=bundle.getString("region");
            detailAddress=bundle.getString("detailAddress");
            userPhone =bundle.getString("receiverPhone");
        }

        @Override
        public void bindValueOfView() {
            changeAddressOrderHolder.edtUserName.setText(userName);
            changeAddressOrderHolder.edtUserPhone.setText(userPhone);
            changeAddressOrderHolder.tvRegion.setText(region);
            changeAddressOrderHolder.edtDetailAddress.setText(detailAddress);
        }
    }


    class ChangeAddressOrderHolder implements IHolder {

        LinearLayout lilaBack;
        EditText edtUserName;
        EditText edtUserPhone;
        Button btnChooseRegion;
        TextView tvRegion;
        EditText edtDetailAddress;
        Button btnEnsureChange;
        @Override
        public void init() {
            lilaBack= (LinearLayout) findViewById(R.id.lila_back);
            edtUserName= (EditText) findViewById(R.id.edt_user_name);
            edtUserPhone= (EditText) findViewById(R.id.edt_user_phone);
            btnChooseRegion= (Button) findViewById(R.id.btn_choose_region);
            tvRegion= (TextView) findViewById(R.id.tv_region);
            edtDetailAddress= (EditText) findViewById(R.id.edt_detail_address);
            btnEnsureChange= (Button) findViewById(R.id.btn_ensure_change);
        }

        @Override
        public void hide() {
            btnChooseRegion.setVisibility(View.GONE);
        }

        @Override
        public void show() {

        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {
            lilaBack.setOnClickListener(listener);
            btnChooseRegion.setOnClickListener(listener);
            btnEnsureChange.setOnClickListener(listener);
        }
    }
}
