package com.google.runda.fragment;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.order.OrderDelayReceiveDateActivity;
import com.google.runda.activity.order.OrderDetailActivity;
import com.google.runda.event.PullFailedOrdersFailEvent;
import com.google.runda.event.PullFailedOrdersSucceedEvent;
import com.google.runda.interfaces.IHolder;
import com.google.runda.model.OrderModel;
import com.google.runda.model.OrderStatus;
import com.google.runda.view.XListView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by bigface on 2015/9/18.
 */
public class FailedOrdersFragment extends ListFragment implements View.OnClickListener, XListView.IXListViewListener {

    int flag=5;
    XListView xListView;
    ArrayList<OrderModel> orders = new ArrayList<OrderModel>();

    OrdersAdapter ordersAdapter;
    int size = 6;
    int moreSize = 4;

    LinearLayout mLilaStores;
    LinearLayout mLilaLoading;
    LinearLayout mLilaLoadFail;
    Button mBtnRetry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_unfinished_orders, container, false);
        xListView = (XListView) rootView.findViewById(android.R.id.list);
        mLilaStores = (LinearLayout) rootView.findViewById(R.id.lila_stores);
        mLilaLoading = (LinearLayout) rootView.findViewById(R.id.lila_loading);
        mLilaLoadFail = (LinearLayout) rootView.findViewById(R.id.lila_load_fail);
        mBtnRetry = (Button) rootView.findViewById(R.id.btn_retry);
        hideAll();
        showLoading();
        mBtnRetry.setOnClickListener(this);
        xListView.setXListViewListener(this);
        xListView.setPullLoadEnable(true);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        ordersAdapter = new OrdersAdapter();
        setListAdapter(ordersAdapter);
        onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh() {
        new com.google.runda.bll.Order().beginPullOrders(flag);
    }

    @Override
    public void onLoadMore() {
        size += moreSize;
        new com.google.runda.bll.Order().beginPullOrders(flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retry:
                onRefresh();
                showLoading();
                break;
        }
    }

    private void showLoading() {
        mLilaStores.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.VISIBLE);
    }

    void showOrders() {
        mLilaStores.setVisibility(View.VISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
    }

    void showLoadFail() {
        mLilaStores.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.VISIBLE);
    }

    void hideAll() {
        mLilaStores.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
    }

    class OrdersAdapter extends BaseAdapter {
        private LayoutInflater mInflater = LayoutInflater.from(getActivity());

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final com.google.runda.model.OrderModel order = orders.get(position);
            Holder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_order, parent, false);
                holder = new Holder();
                holder.init(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.ensureOrderBottomHolder.hide();
            holder.commentOrderBottomHolder.hide();
            holder.commentOrderBottomHolder.bindClickEvent();
            holder.ensureOrderBottomHolder.bindClickEvent();

            holder.tvStoreName.setText(order.waterStoreName==null?"":order.waterStoreName);
            holder.tvStatus.setText(String.valueOf(OrderStatus.values()[Integer.parseInt(order.orderStatue)].getTag()));
            switch (Integer.parseInt(order.orderStatue)){
                case 0:
                case 1:
                case 3:
                    holder.ensureOrderBottomHolder.show();
                    holder.ensureOrderBottomHolder.btnEnsureOrder.setVisibility(View.GONE);
                    break;
                case 2:
                case 5:
                case 8:
                    holder.commentOrderBottomHolder.show();
                    holder.commentOrderBottomHolder.btnCommentOrder.setVisibility(View.GONE);
                    break;
                case 4:
                    holder.ensureOrderBottomHolder.show();
                    break;
                case 6:
                    holder.ensureOrderBottomHolder.show();
                    holder.ensureOrderBottomHolder.btnDelayOrder.setVisibility(View.GONE);
                    holder.ensureOrderBottomHolder.btnReminderOrder.setVisibility(View.GONE);
                    holder.ensureOrderBottomHolder.btnCancelOrder.setVisibility(View.GONE);
                    break;
                case 7:
                    holder.commentOrderBottomHolder.show();
                    break;
                default:
                    break;
            }
            holder.lilaOrderDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", order);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });
            holder.tvDescription.setText(order.waterGoodsDescript);
            holder.tvPrice.setText("￥" + order.waterGoodsPrice);
            holder.tvNum.setText("x" + order.waterGoodsCount);
            holder.tvTotalNum.setText("共" + order.waterGoodsCount + "件商品");
            holder.tvTotalPrice.setText("一共" + Integer.parseInt(order.waterGoodsCount) * Float.parseFloat(order.waterGoodsPrice) + "元");

            //todo 取消订单
            holder.ensureOrderBottomHolder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示基本的AlertDialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            holder.ensureOrderBottomHolder.btnDelayOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), OrderDelayReceiveDateActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.getString("orderId",order.orderID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            //todo 确认订单
            holder.ensureOrderBottomHolder.btnEnsureOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示基本的AlertDialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            holder.ensureOrderBottomHolder.btnReminderOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getActivity())
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
            holder.commentOrderBottomHolder.btnCommentOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View view = inflater.inflate(R.layout.layout_comment_order, null);
                    final EditText edtCommentOrder = (EditText) view.findViewById(R.id.edt_comment_order);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("请输入你的评价")
                            .setView(view)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String comment = edtCommentOrder.getText().toString();
                                    if (comment.trim().length() == 0) {
                                        Toast.makeText(getActivity(),"还没入输入评价呢",Toast.LENGTH_SHORT).show();
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
            holder.commentOrderBottomHolder.btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示基本的AlertDialog
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            return convertView;
        }

        class Holder implements IHolder {

            LinearLayout lilaOrderDetail;

            TextView tvStoreName;
            TextView tvStatus;
            ImageView ivWater;
            TextView tvDescription;
            TextView tvPrice;
            TextView tvNum;
            TextView tvTotalPrice;
            TextView tvTotalNum;

            EnsureOrderBottomHolder ensureOrderBottomHolder=new EnsureOrderBottomHolder();
            CommentOrderBottomHolder commentOrderBottomHolder=new CommentOrderBottomHolder();
            @Override
            public void init() {

            }
            public void init(View convertView){
                lilaOrderDetail= (LinearLayout) convertView.findViewById(R.id.lila_order_detail);
                tvStoreName = (TextView) convertView.findViewById(R.id.tv_store_name);
                tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
                ivWater = (ImageView) convertView.findViewById(R.id.iv_water);
                tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
                tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                tvNum = (TextView) convertView.findViewById(R.id.tv_num);
                tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_total_price);
                tvTotalNum = (TextView) convertView.findViewById(R.id.tv_total_num);

                ensureOrderBottomHolder.init(convertView);
                commentOrderBottomHolder.init(convertView);

            }

            @Override
            public void hide() {

            }

            @Override
            public void show() {

            }

            @Override
            public void bindClickEvent(View.OnClickListener listener) {

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
            public void init() {

            }

            public void init(View view) {
                lilaOrderEnsureBottom = (LinearLayout) view.findViewById(R.id.lila_order_ensure_bottom);
                btnCancelOrder = (Button) view.findViewById(R.id.btn_cancel_order);
                btnReminderOrder = (Button) view.findViewById(R.id.btn_reminder_order);
                btnDelayOrder = (Button) view.findViewById(R.id.btn_delay_order);
                btnEnsureOrder = (Button) view.findViewById(R.id.btn_ensure_order);
            }

            @Override
            public void hide() {
                lilaOrderEnsureBottom.setVisibility(View.INVISIBLE);
                btnCancelOrder.setVisibility(View.INVISIBLE);
                btnReminderOrder.setVisibility(View.INVISIBLE);
                btnDelayOrder.setVisibility(View.INVISIBLE);
                btnEnsureOrder.setVisibility(View.INVISIBLE);
            }

            @Override
            public void show() {
                lilaOrderEnsureBottom.setVisibility(View.VISIBLE);
                btnCancelOrder.setVisibility(View.VISIBLE);
                btnReminderOrder.setVisibility(View.VISIBLE);
                btnDelayOrder.setVisibility(View.VISIBLE);
                btnEnsureOrder.setVisibility(View.VISIBLE);
            }

            @Override
            public void bindClickEvent(View.OnClickListener listener) {
                btnCancelOrder.setOnClickListener(listener);
                btnReminderOrder.setOnClickListener(listener);
                btnDelayOrder.setOnClickListener(listener);
                btnEnsureOrder.setOnClickListener(listener);
            }
            public void bindClickEvent(){
                btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
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

            }

            public void init(View view) {
                lilaOrderCommentBottom = (LinearLayout) view.findViewById(R.id.lila_order_comment_bottom);
                btnDeleteOrder = (Button) view.findViewById(R.id.btn_delete_order);
                btnCommentOrder = (Button) view.findViewById(R.id.btn_comment_order);
            }

            @Override
            public void hide() {
                lilaOrderCommentBottom.setVisibility(View.INVISIBLE);
                btnDeleteOrder.setVisibility(View.INVISIBLE);
                btnCommentOrder.setVisibility(View.INVISIBLE);
            }

            @Override
            public void show() {
                lilaOrderCommentBottom.setVisibility(View.VISIBLE);
                btnDeleteOrder.setVisibility(View.VISIBLE);
                btnCommentOrder.setVisibility(View.VISIBLE);
            }

            @Override
            public void bindClickEvent(View.OnClickListener listener) {
                btnDeleteOrder.setOnClickListener(listener);
                btnCommentOrder.setOnClickListener(listener);
            }
            public void bindClickEvent(){

            }
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this.getActivity(), "你点击了第" + (position - 1), Toast.LENGTH_SHORT).show();
    }


    /*EventBus - - 响应开始*/

    public void onEventMainThread(PullFailedOrdersSucceedEvent event) {
        orders = (ArrayList<com.google.runda.model.OrderModel>) event.getData();
        xListView.stopRefresh();
        xListView.stopLoadMore();
        ordersAdapter.notifyDataSetChanged();
        xListView.setRefreshTime("刚刚");
        //Toast.makeText(this.getActivity(),"数据加载成功",Toast.LENGTH_SHORT).show();
        showOrders();
    }

    public void onEventMainThread(PullFailedOrdersFailEvent event) {
        Toast.makeText(this.getActivity(), "数据加载失败 " + event.getMessage(), Toast.LENGTH_SHORT).show();
        showLoadFail();
    }
    /*EventBus - - 响应结束*/
}
