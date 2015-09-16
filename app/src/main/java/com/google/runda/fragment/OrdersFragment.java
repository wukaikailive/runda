package com.google.runda.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.order.OrderDetailActivity;
import com.google.runda.event.PullOrdersFailEvent;
import com.google.runda.event.PullOrdersSucceedEvent;
import com.google.runda.model.Order;
import com.google.runda.view.XListView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by 凯凯 on 2015/3/20.
 */
public class OrdersFragment extends ListFragment implements XListView.IXListViewListener ,View.OnClickListener{

    XListView xListView;
    ArrayList<com.google.runda.model.Order> orders=new ArrayList<Order>();

    OrdersAdapter ordersAdapter;
    int size=6;
    int moreSize=4;

    LinearLayout mLilaStores;
    LinearLayout mLilaLoading;
    LinearLayout mLilaLoadFail;
    Button mBtnRetry;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_0_3,container,false);
        xListView= (XListView) rootView.findViewById(android.R.id.list);
        mLilaStores= (LinearLayout) rootView.findViewById(R.id.lila_stores);
        mLilaLoading= (LinearLayout) rootView.findViewById(R.id.lila_loading);
        mLilaLoadFail= (LinearLayout) rootView.findViewById(R.id.lila_load_fail);
        mBtnRetry= (Button) rootView.findViewById(R.id.btn_retry);
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
        ordersAdapter =new OrdersAdapter();
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
        new com.google.runda.bll.Order().beginPullOrders();
    }

    @Override
    public void onLoadMore() {
        size+=moreSize;
        new com.google.runda.bll.Order().beginPullOrders();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_retry:
                onRefresh();
                showLoading();
                break;
        }
    }

    private void showLoading(){
        mLilaStores.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.VISIBLE);
    }
    void showOrders(){
        mLilaStores.setVisibility(View.VISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
    }
    void showLoadFail(){
        mLilaStores.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.VISIBLE);
    }
    void hideAll(){
        mLilaStores.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
    }

    class OrdersAdapter extends BaseAdapter{
        private LayoutInflater mInflater= LayoutInflater.from(getActivity());
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
            final com.google.runda.model.Order order= orders.get(position);
            Holder holder=null;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.list_item_order, parent,false);
                holder=new Holder();
                holder.tvStoreName= (TextView) convertView.findViewById(R.id.tv_store_name);
                holder.tvStatus=(TextView)convertView.findViewById(R.id.tv_status);
                holder.ivWater=(ImageView)convertView.findViewById(R.id.iv_water);
                holder.tvDescription= (TextView) convertView.findViewById(R.id.tv_description);
                holder.tvPrice= (TextView) convertView.findViewById(R.id.tv_price);
                holder.tvNum= (TextView) convertView.findViewById(R.id.tv_num);
                holder.tvTotalPrice= (TextView) convertView.findViewById(R.id.tv_total_price);
                holder.tvTotalNum= (TextView) convertView.findViewById(R.id.tv_total_num);
                holder.btnEnsure= (Button) convertView.findViewById(R.id.btn_ensure);
                holder.btnDetail= (Button) convertView.findViewById(R.id.btn_detail);

                convertView.setTag(holder);
            }else{
                holder=(Holder)convertView.getTag();
            }

            holder.tvStoreName.setText(order.WaterStoreName);
            holder.tvStatus.setText(String.valueOf(order.Status));
            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(getActivity(), OrderDetailActivity.class);
                    getActivity().startActivity(intent);
                }
            });
//todo full
            return convertView;
        }

        class Holder{
            TextView tvStoreName;
            TextView tvStatus;
            ImageView ivWater;
            TextView tvDescription;
            TextView tvPrice;
            TextView tvNum;
            TextView tvTotalPrice;
            TextView tvTotalNum;
            Button btnEnsure;
            Button btnDetail;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this.getActivity(),"你点击了第"+(position-1),Toast.LENGTH_SHORT).show();
    }


    /*EventBus - - 响应开始*/

    public void onEventMainThread(PullOrdersSucceedEvent event){
        orders= (ArrayList<com.google.runda.model.Order>) event.getData();
        xListView.stopRefresh();
        xListView.stopLoadMore();
        ordersAdapter.notifyDataSetChanged();
        xListView.setRefreshTime("刚刚");
        //Toast.makeText(this.getActivity(),"数据加载成功",Toast.LENGTH_SHORT).show();
        showOrders();
    }

    public void onEventMainThread(PullOrdersFailEvent event){
        Toast.makeText(this.getActivity(),"数据加载失败 "+event.getMessage(),Toast.LENGTH_SHORT).show();
        showLoadFail();
    }
    /*EventBus - - 响应结束*/
}
