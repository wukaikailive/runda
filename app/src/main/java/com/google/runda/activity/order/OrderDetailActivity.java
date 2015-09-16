package com.google.runda.activity.order;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.runda.R;

/**
 * Created by bigface on 2015/9/12.
 */
public class OrderDetailActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);

    }


    class DetailOrderHolder{

        LinearLayout lilaBack;//返回上一界面

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

        EnsureOrderBottomHolder ensureOrderBottomHolder;

    }

    class EnsureOrderBottomHolder{

        Button btnCancelOrder;
        Button btnReminderOrder;
        Button btnDelayOrder;
        Button btnEnsureOrder;

    }
    
}
