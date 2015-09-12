package com.google.runda.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.order.OrderEnsureActivity;
import com.google.runda.event.PullCommoditiesBasicFailEvent;
import com.google.runda.event.PullCommoditiesBasicSucceedEvent;
import com.google.runda.event.PullStoresFailEvent;
import com.google.runda.event.PullStoresSucceedEvent;
import com.google.runda.model.Commodity;
import com.google.runda.util.LoadImageTask;
import com.google.runda.view.XListView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by 凯凯 on 2015/3/20.
 */
public class WatersActivity extends ListActivity implements XListView.IXListViewListener ,View.OnClickListener{

    XListView xListView;
    ArrayList<Commodity> commodities =new ArrayList<Commodity>();

    CommodityAdapter commodityAdapter;
    int size=6;
    int moreSize=4;

    String storeId;
    String storeName;

    LinearLayout mLilaStores;
    LinearLayout mLilaLoading;
    LinearLayout mLilaLoadFail;
    Button mBtnRetry;
    ImageButton mIbtnClose;
    TextView mTvStoreName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_waters);
        Intent intent=getIntent();
        storeId=intent.getStringExtra("storeId");
        storeName=intent.getStringExtra("storeName");
        init();
        EventBus.getDefault().register(this);
        commodityAdapter =new CommodityAdapter();
        setListAdapter(commodityAdapter);
        onRefresh();
    }

    void init(){
        xListView= (XListView) findViewById(android.R.id.list);
        mLilaStores= (LinearLayout) findViewById(R.id.lila_stores);
        mLilaLoading= (LinearLayout) findViewById(R.id.lila_loading);
        mLilaLoadFail= (LinearLayout) findViewById(R.id.lila_load_fail);
        mBtnRetry= (Button) findViewById(R.id.btn_retry);
        mIbtnClose= (ImageButton) findViewById(R.id.ibtn_close);
        mTvStoreName= (TextView) findViewById(R.id.tv_store_name);
        mTvStoreName.setText(storeName);
        hideAll();
        showLoading();
        mBtnRetry.setOnClickListener(this);
        mIbtnClose.setOnClickListener(this);
        xListView.setXListViewListener(this);
        xListView.setPullLoadEnable(true);
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
        new com.google.runda.bll.Commodity().beginPullCommoditiesBasic(storeId);
    }

    @Override
    public void onLoadMore() {
        //size+=moreSize;
        //new Store().beginPullStores(size);
        new com.google.runda.bll.Commodity().beginPullCommoditiesBasic(storeId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_retry:
                onRefresh();
                showLoading();
                break;
            case R.id.ibtn_close:
                this.finish();
                break;
        }
    }

    private void showLoading(){
        mLilaStores.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.VISIBLE);
    }
    void showCommodities(){
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

    class CommodityAdapter extends BaseAdapter {
        private LayoutInflater mInflater= LayoutInflater.from(WatersActivity.this);
        @Override
        public int getCount() {
            return commodities.size();
        }

        @Override
        public Object getItem(int position) {
            return commodities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Commodity commodity= commodities.get(position);
            Holder holder=null;
            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.list_item_water, parent,false);
                holder=new Holder();
                holder.ivDefault= (ImageView) convertView.findViewById(R.id.iv_default);
                holder.tvWaterName=(TextView) convertView.findViewById(R.id.tv_water_name);
                holder.tvSalesVolume=(TextView) convertView.findViewById(R.id.tv_sales_volume);
                holder.tvPrice=(TextView) convertView.findViewById(R.id.tv_price);
                holder.tvFreeNum=(TextView) convertView.findViewById(R.id.tv_free_num);
                holder.btnSub= (Button) convertView.findViewById(R.id.btn_sub);
                holder.btnPlus= (Button) convertView.findViewById(R.id.btn_plus);
                holder.etNum= (EditText) convertView.findViewById(R.id.et_num);
                holder.btnOk= (Button) convertView.findViewById(R.id.btn_ok);
                holder.btnDetail= (Button) convertView.findViewById(R.id.btn_detail);

                convertView.setTag(holder);

            }else
                holder=(Holder) convertView.getTag();

            holder.tvWaterName.setText(commodity.waterGoodsName);
            holder.tvSalesVolume.setText("销量:"+commodity.salesVolume);
            holder.tvPrice.setText("价格:￥"+commodity.waterGoodsPrice);
            holder.tvFreeNum.setText("库存:"+commodity.waterGoodsInventory+"桶");

//            if(commodity.waterGoodsDefaultImage!=null || commodity.waterGoodsDefaultImage.equals(""))
//            {
//                LoadImageTask loadImageTask=new LoadImageTask(holder.ivDefault,commodity.waterGoodsDefaultImage,80,80);
//                loadImageTask.execute();
//            }

            final Holder finalHolder = holder;
            holder.btnSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num=Integer.parseInt(finalHolder.etNum.getText().toString());
                    num--;
                    if(num<0){
                        num=0;
                    }
                    finalHolder.etNum.setText(String.valueOf(num));
                }
            });
            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num=Integer.parseInt(finalHolder.etNum.getText().toString());
                    int free=Integer.parseInt(commodity.waterGoodsInventory);
                    num++;
                    if(num>free){
                        num=free;
                    }
                    finalHolder.etNum.setText(String.valueOf(num));
                }
            });


            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(WatersActivity.this, WaterDetailActivity.class);
                    intent.putExtra("waterId",commodity.id);
                    intent.putExtra("waterName",commodity.waterGoodsName);
                    startActivity(intent);
                }
            });
            holder.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num=finalHolder.etNum.getText().toString();
                    Intent intent=new Intent(WatersActivity.this, OrderEnsureActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("num",num);
                    bundle.putString("waterStoreName", storeName);
                    bundle.putSerializable("commodity", commodity);
                    Log.e("wukaikai", "启动确认订单界面");
                   startActivity(intent);
                }
            });
            return convertView;
        }

        class Holder{
            ImageView ivDefault;
            TextView tvWaterName;
            TextView tvSalesVolume;
            TextView tvPrice;
            TextView tvFreeNum;
            Button btnSub;
            Button btnPlus;
            EditText etNum;
            Button btnOk;
            Button btnDetail;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(this,"你点击了第"+(position-1),Toast.LENGTH_SHORT).show();
    }


    /*EventBus - - 响应开始*/

    public void onEventMainThread(PullCommoditiesBasicSucceedEvent event){
        commodities = (ArrayList<Commodity>) event.getData();
        xListView.stopRefresh();
        xListView.stopLoadMore();
        commodityAdapter.notifyDataSetChanged();
        xListView.setRefreshTime("刚刚");
        Toast.makeText(this,"数据加载成功",Toast.LENGTH_SHORT).show();
        showCommodities();
    }

    public void onEventMainThread(PullCommoditiesBasicFailEvent event){
        Toast.makeText(this,"数据加载失败 "+event.getMessage(),Toast.LENGTH_SHORT).show();
        showLoadFail();
    }
    /*EventBus - - 响应结束*/
}
