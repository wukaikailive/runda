package com.google.runda.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.runda.R;
import com.google.runda.interfaces.IArgModel;
import com.google.runda.interfaces.IHolder;

/**
 * Created by bigface on 2015/9/12.
 */
public class OrderDetailActivity extends Activity implements View.OnClickListener{

    DetailOrderHolder detailOrderHolder;
    LoadingHolder loadingHolder;
    LoadFailHolder loadFailHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);
        detailOrderHolder=new DetailOrderHolder();
        detailOrderHolder.init();
        detailOrderHolder.bindClickEvent(this);
        detailOrderHolder.hide();
        loadingHolder=new LoadingHolder();
        loadingHolder.init();
        loadingHolder.bindClickEvent(this);
//        loadingHolder.hide();
        loadFailHolder =new LoadFailHolder();
        loadFailHolder.init();
        loadFailHolder.bindClickEvent(this);
        loadFailHolder.hide();
    }

    @Override
    public void onClick(View v) {

    }

    class ArgModel implements IArgModel{

        @Override
        public void init(Intent intent) {
            Bundle bundle=intent.getExtras();

        }

        @Override
        public void bindValueOfView() {

        }
    }

    /**
     * 订单详情页
     */
    class DetailOrderHolder implements IHolder {

        LinearLayout lilaBack;//返回上一界面

        ScrollView scrollOrderDetail;//订单详情

        TextView tvSubmitTime;//提交时间
        TextView tvStatus;//订单状态
        TextView tvTotalPrice;//订单总价

        TextView tvReceiverName;//收货人名字
        TextView tvReceiverPhone;//收货人电话
        TextView tvReceiverDetailAddress;//收货人详细地址
        TextView tvStoreName;//水站名字
        ImageView ivWater;//商品图片
        TextView tvDescription;//商品描述
        TextView tvPrice;//单价
        TextView tvNum;//数量

        TextView tvSendTime;//用户可收货时间
        TextView tvMessage;//用户留言

        Button btnContactStore;//联系水站按钮
        TextView tvOrderId;//订单ID

        EnsureOrderBottomHolder ensureOrderBottomHolder=new EnsureOrderBottomHolder();//如果订单未完成这显示
        CommentOrderBottomHolder commentOrderBottomHolder=new CommentOrderBottomHolder();//如果订单已完成，则显示

        @Override
        public void init() {
            lilaBack= (LinearLayout) findViewById(R.id.lila_back);

            scrollOrderDetail= (ScrollView) findViewById(R.id.scroll_order_detail);

            tvSubmitTime= (TextView) findViewById(R.id.tv_submit_time);
            tvStatus= (TextView) findViewById(R.id.tv_status);
            tvTotalPrice= (TextView) findViewById(R.id.tv_total_price);
            tvReceiverName= (TextView) findViewById(R.id.tv_receiver_name);
            tvReceiverPhone= (TextView) findViewById(R.id.tv_receiver_phone);
            tvReceiverDetailAddress= (TextView) findViewById(R.id.tv_receiver_detail_address);
            tvStoreName= (TextView) findViewById(R.id.tv_store_name);
            ivWater= (ImageView) findViewById(R.id.iv_water);
            tvDescription= (TextView) findViewById(R.id.tv_description);
            tvPrice= (TextView) findViewById(R.id.tv_price);
            tvNum= (TextView) findViewById(R.id.tv_num);
            tvSendTime= (TextView) findViewById(R.id.tv_send_time);
            tvMessage= (TextView) findViewById(R.id.tv_message);
            btnContactStore= (Button) findViewById(R.id.btn_contact_store);
            tvOrderId= (TextView) findViewById(R.id.tv_order_id);
            ensureOrderBottomHolder.init();
            commentOrderBottomHolder.init();
        }

        @Override
        public void hide() {
            scrollOrderDetail.setVisibility(View.INVISIBLE);
            ensureOrderBottomHolder.hide();
            commentOrderBottomHolder.hide();
        }

        @Override
        public void show() {
            scrollOrderDetail.setVisibility(View.VISIBLE);
            ensureOrderBottomHolder.show();
            commentOrderBottomHolder.show();
        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {
            lilaBack.setOnClickListener(listener);
            btnContactStore.setOnClickListener(listener);//联系水站
            ensureOrderBottomHolder.bindClickEvent(listener);
            commentOrderBottomHolder.bindClickEvent(listener);
        }
    }

    /**
     * 未完成订单底部按钮栏
     */
    class EnsureOrderBottomHolder implements IHolder {

        LinearLayout lilaOrderEnsureBottom;

        Button btnCancelOrder;
        Button btnReminderOrder;
        Button btnDelayOrder;
        Button btnEnsureOrder;

        @Override
        public void init(){
            lilaOrderEnsureBottom= (LinearLayout) findViewById(R.id.lila_order_ensure_bottom);
            btnCancelOrder= (Button) findViewById(R.id.btn_cancel_order);
            btnReminderOrder= (Button) findViewById(R.id.btn_reminder_order);
            btnDelayOrder= (Button) findViewById(R.id.btn_delay_order);
            btnEnsureOrder= (Button) findViewById(R.id.btn_ensure_order);
        }

        @Override
        public void hide() {
            lilaOrderEnsureBottom.setVisibility(View.INVISIBLE);
        }

        @Override
        public void show() {
            lilaOrderEnsureBottom.setVisibility(View.VISIBLE);
        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {
            btnCancelOrder.setOnClickListener(listener);
            btnReminderOrder.setOnClickListener(listener);
            btnDelayOrder.setOnClickListener(listener);
            btnEnsureOrder.setOnClickListener(listener);
        }


    }

    /**
     * 已完成订单底部按钮栏
     */
    class CommentOrderBottomHolder implements IHolder {

        LinearLayout lilaOrderCommentBottom;
        Button btnDeleteOrder;
        Button btnCommentOrder;

        @Override
        public void init() {
            lilaOrderCommentBottom= (LinearLayout) findViewById(R.id.lila_order_comment_bottom);
            btnDeleteOrder= (Button) findViewById(R.id.btn_delete_order);
            btnCommentOrder= (Button) findViewById(R.id.btn_comment_order);
        }

        @Override
        public void hide() {
            lilaOrderCommentBottom.setVisibility(View.INVISIBLE);
        }

        @Override
        public void show() {
            lilaOrderCommentBottom.setVisibility(View.VISIBLE);
        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {
            btnDeleteOrder.setOnClickListener(listener);
            btnCommentOrder.setOnClickListener(listener);
        }
    }

    /**
     * 正在加载的提示界面
     */
    class LoadingHolder implements IHolder{

        LinearLayout lilaLoading;
        @Override
        public void init() {
            lilaLoading= (LinearLayout) findViewById(R.id.lila_loading);
        }

        @Override
        public void hide() {
            lilaLoading.setVisibility(View.INVISIBLE);
        }

        @Override
        public void show() {
            lilaLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {

        }
    }

    /**
     * 加载失败的提示界面
     */
    class LoadFailHolder implements IHolder{

        LinearLayout lilaLoadFail;
        Button btnRetry;
        @Override
        public void init() {
            lilaLoadFail= (LinearLayout) findViewById(R.id.lila_load_fail);
            btnRetry= (Button) findViewById(R.id.btn_retry);
        }

        @Override
        public void hide() {
            lilaLoadFail.setVisibility(View.INVISIBLE);
        }

        @Override
        public void show() {
            lilaLoadFail.setVisibility(View.VISIBLE);
        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {
            btnRetry.setOnClickListener(listener);
        }
    }


}
