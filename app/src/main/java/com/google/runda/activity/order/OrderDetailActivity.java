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

        LinearLayout lilaBack;//������һ����

        TextView tvSubmitTime;//�ύʱ��
        TextView tvStatus;//����״̬
        TextView tvTotalPrice;//�����ܼ�

        TextView tvReceiverName;//�ջ�������
        TextView tvReceiverPhone;//�ջ��˵绰
        TextView tvReceiverDetailAddress;//�ջ�����ϸ��ַ
        TextView tvStoreName;//ˮվ����
        ImageView ivWater;//��ƷͼƬ
        TextView tvDescription;//��Ʒ����
        TextView tvPrice;//����
        TextView tvNum;//����

        TextView tvSendTime;//�û����ջ�ʱ��
        TextView tvMessage;//�û�����

        Button btnContactStore;//��ϵˮվ��ť
        TextView tvOrderId;//����ID

        EnsureOrderBottomHolder ensureOrderBottomHolder;

    }

    class EnsureOrderBottomHolder{

        Button btnCancelOrder;
        Button btnReminderOrder;
        Button btnDelayOrder;
        Button btnEnsureOrder;

    }
    
}
