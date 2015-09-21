package com.google.runda.activity.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.interfaces.IArgModel;
import com.google.runda.interfaces.IHolder;
import com.google.runda.model.OrderModel;
import com.google.runda.model.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bigface on 2015/9/12.
 */
public class OrderDetailActivity extends Activity{

    DetailOrderHolder detailOrderHolder;
    LoadingHolder loadingHolder;
    LoadFailHolder loadFailHolder;
    ArgModel argModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);
        detailOrderHolder=new DetailOrderHolder();
        detailOrderHolder.init();
        detailOrderHolder.hide();
        loadingHolder=new LoadingHolder();
        loadingHolder.init();
//        loadingHolder.hide();
        loadFailHolder =new LoadFailHolder();
        loadFailHolder.init();
        loadFailHolder.hide();

        argModel=new ArgModel();
        argModel.init(getIntent());
        argModel.bindValueOfView();
        loadingHolder.hide();
        detailOrderHolder.show();
    }


    class ArgModel implements IArgModel{
        OrderModel order;
        @Override
        public void init(Intent intent) {
            Bundle bundle=intent.getExtras();
            order = (OrderModel) bundle.getSerializable("order");
        }

        @Override
        public void bindValueOfView() {

            Date t=new Date(Long.parseLong(order.orderSubmitTime));
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            detailOrderHolder.tvSubmitTime.setText(sf.format(t));
            detailOrderHolder.tvStatus.setText(String.valueOf(OrderStatus.values()[Integer.parseInt(order.orderStatue)].getTag()));
            detailOrderHolder.tvTotalPrice.setText("￥"+Integer.parseInt(order.waterGoodsCount)*Float.parseFloat(order.waterGoodsPrice));

            detailOrderHolder.tvReceiverName.setText(order.recieverPersonName==null?"":order.recieverPersonName);
            detailOrderHolder.tvReceiverPhone.setText(order.recieverPersonPhone==null?"":order.recieverPersonPhone);
            detailOrderHolder.tvReceiverDetailAddress.setText(order.recieverAddress==null?"":order.recieverAddress);

            detailOrderHolder.tvStoreName.setText(order.waterStoreName);
            detailOrderHolder.tvDescription.setText(order.waterGoodsDescript);
            detailOrderHolder.tvPrice.setText("￥"+order.waterGoodsPrice);
            detailOrderHolder.tvNum.setText(order.waterGoodsCount);
            detailOrderHolder.tvSendTime.setText(order.recieverTime);

            detailOrderHolder.tvMessage.setText(order.remark);
            detailOrderHolder.tvOrderId.setText("订单id:"+order.orderID);

            switch (Integer.parseInt(order.orderStatue)){
                case 0:
                case 1:
                case 3:
                    detailOrderHolder.ensureOrderBottomHolder.btnEnsureOrder.setVisibility(View.GONE);
                    detailOrderHolder.ensureOrderBottomHolder.show();
                    break;
                case 2:
                case 5:
                case 8:
                    detailOrderHolder.commentOrderBottomHolder.btnCommentOrder.setVisibility(View.GONE);
                    detailOrderHolder.commentOrderBottomHolder.show();
                    break;
                case 4:
                    detailOrderHolder.ensureOrderBottomHolder.show();
                    break;
                case 6:
                    detailOrderHolder.ensureOrderBottomHolder.btnDelayOrder.setVisibility(View.GONE);
                    detailOrderHolder.ensureOrderBottomHolder.btnReminderOrder.setVisibility(View.GONE);
                    detailOrderHolder.ensureOrderBottomHolder.btnCancelOrder.setVisibility(View.GONE);
                    detailOrderHolder.ensureOrderBottomHolder.show();
                    break;
                case 7:
                    detailOrderHolder.commentOrderBottomHolder.show();
                    break;
                default:
                    break;
            }

            //todo 取消订单
            detailOrderHolder.ensureOrderBottomHolder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示基本的AlertDialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                    builder.setMessage("你确认要取消订单吗？如果送水工已经在路上建议您等一等...！id="+order.orderID);
                    builder.setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    new com.google.runda.bll.Order().beginCancelOrder(order.orderID);
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                    builder.show();
                }
            });
            //todo 延迟订单
            detailOrderHolder.ensureOrderBottomHolder.btnDelayOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(OrderDetailActivity.this, OrderDelayReceiveDateActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.getString("orderId",order.orderID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            //todo 确认订单
            detailOrderHolder.ensureOrderBottomHolder.btnEnsureOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示基本的AlertDialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                    builder.setMessage("请确认已经收到货，否则可能人财两空！id="+order.orderID);
                    builder.setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    new com.google.runda.bll.Order().beginEnsureOrder(order.orderID);
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                    builder.show();
                }
            });
            //todo 催单
            detailOrderHolder.ensureOrderBottomHolder.btnReminderOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(OrderDetailActivity.this)
                            .setTitle("拨打电话")
                            .setSingleChoiceItems(new String[] {"水站："+order.waterStoreTellPhone}, 0,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.waterStoreTellPhone));
                                            startActivity(intent);
                                        }
                                    }
                            )
                            .setNegativeButton("取消", null)
                            .show();

                }
            });
            //todo 评价订单
            detailOrderHolder.commentOrderBottomHolder.btnCommentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(OrderDetailActivity.this);
                    View view = inflater.inflate(R.layout.layout_comment_order, null);
                    final EditText edtCommentOrder = (EditText) view.findViewById(R.id.edt_comment_order);
                    new AlertDialog.Builder(OrderDetailActivity.this)
                            .setTitle("请输入你的评价")
                            .setView(view)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String comment = edtCommentOrder.getText().toString();
                                    if (comment.trim().length() == 0) {
                                        Toast.makeText(OrderDetailActivity.this, "还没入输入评价呢", Toast.LENGTH_SHORT).show();
                                    }else{
                                        new com.google.runda.bll.Order().beginCommentOrder(order.orderID,comment);
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });
            //todo 删除订单
            detailOrderHolder.commentOrderBottomHolder.btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示基本的AlertDialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                    builder.setMessage("你确定要删除id="+order.orderID+"订单吗？此操作不可恢复！");
                    builder.setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    new com.google.runda.bll.Order().beginDeleteOrder(order.orderID);
                                }
                            });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                    builder.show();

                }
            });

            detailOrderHolder.btnContactStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(OrderDetailActivity.this)
                            .setTitle("拨打电话")
                            .setSingleChoiceItems(new String[] {"水站："+order.waterStoreTellPhone}, 0,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.waterStoreTellPhone));
                                            startActivity(intent);
                                        }
                                    }
                            )
                            .setNegativeButton("取消", null)
                            .show();
                }
            });

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
