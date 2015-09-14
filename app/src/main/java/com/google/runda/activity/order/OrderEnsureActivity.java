package com.google.runda.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.runda.R;
import com.google.runda.model.Commodity;
import com.google.runda.staticModel.ServerConfig;

/**
 * Created by bigface on 2015/9/12.
 * 订单确认界面
 */
public class OrderEnsureActivity extends Activity implements View.OnClickListener{

    EnsureOrderHolder ensureOrderHolder;
    ArgModel argModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_ensure);
        ensureOrderHolder=new EnsureOrderHolder();
        ensureOrderHolder.init();
        ensureOrderHolder.bindClickEvent(this);
        argModel=new ArgModel();
        argModel.init(getIntent());
        argModel.bindValueOfView();
    }

    class ArgModel{
        int num;//购买数量
        float totalPrice;//总价
        String storeName;//水站名字
        com.google.runda.model.Commodity commodity;//水的详情
        com.google.runda.model.User user;//用户信息

        public void init(Intent intent){
            Bundle bundle=intent.getExtras();
            num=bundle.getInt("num");
            storeName=bundle.getString("waterStoreName");
            commodity= (Commodity) bundle.getSerializable("commodity");
            user= ServerConfig.USER;
            totalPrice=Float.parseFloat(commodity.waterGoodsPrice)*num;
        }

        public void bindValueOfView(){
            ensureOrderHolder.tvReceiverName.setText(user.Name);
            ensureOrderHolder.tvReceiverPhone.setText(user.CellPhone);
            ensureOrderHolder.tvReceiverDetailAddress.setText(user.DetailAddress);

            ensureOrderHolder.tvStoreName.setText(storeName);
            ensureOrderHolder.tvDescription.setText(commodity.waterGoodsDescript);
            ensureOrderHolder.tvPrice.setText(commodity.waterGoodsPrice);
            ensureOrderHolder.tvNum.setText(String.valueOf(num));

            ensureOrderHolder.tvTotalPrice.setText(String.valueOf(totalPrice));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lila_back:
                //返回
                this.finish();
                break;
            case R.id.lila_set_address:
                //todo 设置收货地址
                break;
            case R.id.lila_set_send_time:
                //todo 设置可收货时间
                break;
            case R.id.btn_ensure_order:
                //todo 确认收货 记得发送付款方式
                break;
        }
    }

    /**
     * 订单确认界面的 view holder
     */
    class  EnsureOrderHolder{

        LinearLayout lilaBack;//返回上一界面
        LinearLayout lilaSerAddress;//设置地址
        TextView tvReceiverName;//收货人名字
        TextView tvReceiverPhone;//收货人电话
        TextView tvReceiverDetailAddress;//收货人详细地址
        TextView tvStoreName;//水站名字
        ImageView ivWater;//商品图片
        TextView tvDescription;//商品描述
        TextView tvPrice;//单价
        TextView tvNum;//数量

        LinearLayout lilaSetSendTime;//设置收货时间
        TextView tvSendTime;//收货时间
        EditText edtMessage;//留言

        TextView tvTotalPrice;//总价格
        Button btnEnsureOrder;//确认订单

        /**
         * find id,bind holder's view
         */
        public void init(){

            lilaBack= (LinearLayout) findViewById(R.id.lila_back);
            lilaSerAddress= (LinearLayout) findViewById(R.id.lila_set_address);
            tvReceiverName= (TextView) findViewById(R.id.tv_receiver_name);
            tvReceiverPhone= (TextView)  findViewById(R.id.tv_receiver_phone);
            tvReceiverDetailAddress= (TextView)  findViewById(R.id.tv_receiver_detail_address);
            tvStoreName= (TextView)  findViewById(R.id.tv_store_name);
            ivWater= (ImageView)  findViewById(R.id.iv_water);
            tvDescription= (TextView)  findViewById(R.id.tv_description);
            tvPrice= (TextView)  findViewById(R.id.tv_price);
            tvNum= (TextView)  findViewById(R.id.tv_num);

            lilaSetSendTime= (LinearLayout)  findViewById(R.id.lila_set_send_time);
            tvSendTime= (TextView)  findViewById(R.id.tv_send_time);
            edtMessage= (EditText)  findViewById(R.id.edt_message);

            tvTotalPrice= (TextView)  findViewById(R.id.tv_total_price);
            btnEnsureOrder= (Button)  findViewById(R.id.btn_ensure_order);
        }

        public void bindClickEvent(View.OnClickListener listener){
            lilaBack.setOnClickListener(listener);
            lilaSerAddress.setOnClickListener(listener);
            lilaSetSendTime.setOnClickListener(listener);
            btnEnsureOrder.setOnClickListener(listener);
        }
    }
}
