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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.event.OrderAddressChangedEvent;
import com.google.runda.event.OrderReceiveDateChangeEvent;
import com.google.runda.event.PlaceOrderFailEvent;
import com.google.runda.event.PlaceOrderSucceedEvent;
import com.google.runda.interfaces.IArgModel;
import com.google.runda.interfaces.IHolder;
import com.google.runda.model.Commodity;
import com.google.runda.model.PlaceOrderFormModel;
import com.google.runda.staticModel.ServerConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

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
        EventBus.getDefault().register(this);
        ensureOrderHolder=new EnsureOrderHolder();
        ensureOrderHolder.init();
        ensureOrderHolder.bindClickEvent(this);
        argModel=new ArgModel();
        argModel.init(getIntent());
        argModel.bindValueOfView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class ArgModel implements IArgModel {
        int num;//购买数量
        float totalPrice;//总价
        String storeName;//水站名字
        int storeId;//水站ID
        com.google.runda.model.Commodity commodity;//水的详情
        com.google.runda.model.User user;//用户信息

        @Override
        public void init(Intent intent){
            Bundle bundle=intent.getExtras();
            num=bundle.getInt("num");
            storeName=bundle.getString("waterStoreName");
            storeId=bundle.getInt("storeId");
            commodity= (Commodity) bundle.getSerializable("commodity");
            user= ServerConfig.USER;
            totalPrice=Float.parseFloat(commodity.waterGoodsPrice)*num;
        }
        @Override
        public void bindValueOfView(){
            ensureOrderHolder.tvReceiverName.setText(user.RealName==null?"":user.RealName);
            ensureOrderHolder.tvReceiverPhone.setText(user.CellPhone);
            ensureOrderHolder.tvReceiverDetailAddress.setText(user.DetailAddress);

            ensureOrderHolder.tvStoreName.setText(storeName);
            ensureOrderHolder.tvDescription.setText(commodity.waterGoodsDescript);
            ensureOrderHolder.tvPrice.setText(commodity.waterGoodsPrice);
            ensureOrderHolder.tvNum.setText(String.valueOf(num));

            ensureOrderHolder.tvTotalPrice.setText(String.valueOf(totalPrice));

            Date t=new Date();
            Long time=t.getTime();
            time+=1*60*60*1000;
            Date sendDate=new Date(time);
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ensureOrderHolder.tvSendTime.setText(sf.format(sendDate));
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
                Intent intent=new Intent(this,OrderChangeAddressActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("receiverName",ensureOrderHolder.tvReceiverName.getText().toString());
                bundle.putString("receiverPhone",ensureOrderHolder.tvReceiverPhone.getText().toString());
                bundle.putString("region",argModel.user.Province+argModel.user.City+argModel.user.County);
                bundle.putString("detailAddress",ensureOrderHolder.tvReceiverDetailAddress.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.lila_set_send_time:
                //todo 设置可收货时间
                Intent intent1=new Intent(this, OrderChangeReceiveDateActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_ensure_order:
                //todo 确认收货 记得发送付款方式
                com.google.runda.model.PlaceOrderFormModel model=new PlaceOrderFormModel();
                model.recieverPersonName=ensureOrderHolder.tvReceiverName.getText().toString();
                model.recieverPersonPhone=ensureOrderHolder.tvReceiverPhone.getText().toString();
                model.recieverAddress=ensureOrderHolder.tvReceiverDetailAddress.getText().toString();
                model.waterGoodsID= argModel.commodity.id;
                model.waterGoodsCount= ensureOrderHolder.tvNum.getText().toString();
                model.waterGoodsPrice= ensureOrderHolder.tvPrice.getText().toString();
                model.recieverTime=ensureOrderHolder.tvSendTime.getText().toString();
                model.remark=ensureOrderHolder.edtMessage.getText().toString();
                model.waterStoreID=String.valueOf(argModel.storeId);

                ensureOrderHolder.proBarWaitEnsureOrder.setVisibility(View.VISIBLE);
                ensureOrderHolder.btnEnsureOrder.setEnabled(false);

                //下单开始
                new com.google.runda.bll.Order().beginPlaceOrder(model);
                break;
        }
    }

    /**
     * 订单确认界面的 view holder
     */
    class EnsureOrderHolder implements IHolder {

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
        ProgressBar proBarWaitEnsureOrder;//点击确认按钮后的进度条

        /**
         * find id,bind holder's view
         */
        @Override
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
            proBarWaitEnsureOrder= (ProgressBar) findViewById(R.id.pro_bar_wait_ensure_order);

            proBarWaitEnsureOrder.setVisibility(View.INVISIBLE);
        }

        @Override
        public void hide() {

        }

        @Override
        public void show() {

        }

        @Override
        public void bindClickEvent(View.OnClickListener listener){
            lilaBack.setOnClickListener(listener);
            lilaSerAddress.setOnClickListener(listener);
            lilaSetSendTime.setOnClickListener(listener);
            btnEnsureOrder.setOnClickListener(listener);
        }
    }

    /**
     * 事件处理
     */
    //下订单成功事件
    public void onEventMainThread(PlaceOrderSucceedEvent event){
        Toast.makeText(this,"下订单成功!",Toast.LENGTH_LONG).show();

        this.finish();
    }

    //下订单失败事件
    public void onEventMainThread(PlaceOrderFailEvent event){
        Toast.makeText(this,"下订单失败啦!原因："+event.getMessage(),Toast.LENGTH_LONG).show();
        ensureOrderHolder.proBarWaitEnsureOrder.setVisibility(View.INVISIBLE);
        ensureOrderHolder.btnEnsureOrder.setEnabled(true);
    }

    //修改收货地址事件
    public void onEventMainThread(OrderAddressChangedEvent event){
        ensureOrderHolder.tvReceiverName.setText(event.userName);
        ensureOrderHolder.tvReceiverPhone.setText(event.userPhone);
        ensureOrderHolder.tvReceiverDetailAddress.setText(event.detailAddress);
    }

    public void onEventMainThread(OrderReceiveDateChangeEvent event){
        ensureOrderHolder.tvSendTime.setText(event.date+" "+event.time);
    }

}
