package com.google.runda.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.WatersActivity;
import com.google.runda.bll.Store;
import com.google.runda.event.PullStoresFailEvent;
import com.google.runda.event.PullStoresSucceedEvent;
import com.google.runda.view.XListView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by 凯凯 on 2015/3/20.
 */
public class WaterFragment extends ListFragment implements XListView.IXListViewListener ,View.OnClickListener{

    XListView xListView;
    ArrayList<com.google.runda.model.Store> stores=new ArrayList<com.google.runda.model.Store>();

    StoresAdapter storesAdapter;
    int size=6;
    int moreSize=4;

    LinearLayout mLilaStores;
    LinearLayout mLilaLoading;
    LinearLayout mLilaLoadFail;
    Button mBtnRetry;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_0_2,container,false);
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
        storesAdapter=new StoresAdapter();
        setListAdapter(storesAdapter);
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
        new Store().beginPullStores(size);
    }

    @Override
    public void onLoadMore() {
        size+=moreSize;
        new Store().beginPullStores(size);
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
    void showStores(){
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

    class StoresAdapter extends BaseAdapter{
        private LayoutInflater mInflater= LayoutInflater.from(getActivity());
        @Override
        public int getCount() {
            return stores.size();
        }

        @Override
        public Object getItem(int position) {
            return stores.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final com.google.runda.model.Store store= stores.get(position);
            Holder holder=null;
            if (convertView==null)
            {
                convertView = mInflater.inflate(R.layout.list_item_store, parent,false);
                holder=new Holder();
                holder.tvStoreName=(TextView) convertView.findViewById(R.id.tvStoreName);
                holder.tvStatus=(TextView) convertView.findViewById(R.id.tvStatus);
                //holder.tvLinkMan=(TextView) convertView.findViewById(R.id.tvLinkMan);
                holder.tvPhone=(TextView) convertView.findViewById(R.id.tvPhone);
                holder.tvDetailAddress=(TextView) convertView.findViewById(R.id.tvDetailAddress);
                holder.lilaTitle= (LinearLayout) convertView.findViewById(R.id.lila_title);
                holder.btnGo= (Button) convertView.findViewById(R.id.btn_go);
                holder.btnDetail= (Button) convertView.findViewById(R.id.btn_detail);
                convertView.setTag(holder);

            }else
                holder=(Holder) convertView.getTag();

            holder.tvStoreName.setText(store.waterStoreName);
            String status=store.waterStoreStatus;
            if(status.equals("0")){
                status="休息中";
                holder.btnGo.setVisibility(View.INVISIBLE);
                holder.lilaTitle.setBackgroundColor(getResources().getColor(R.color.me_yellow));
            }else if(status.equals("1")){
                status="正常营业";
                holder.btnGo.setVisibility(View.VISIBLE);
                holder.lilaTitle.setBackgroundColor(getResources().getColor(R.color.me_green));
            }else{
                status="忙碌中";
                holder.btnGo.setVisibility(View.INVISIBLE);
                holder.lilaTitle.setBackgroundColor(getResources().getColor(R.color.me_red));
            }
            holder.tvStatus.setText(status);
            //holder.tvLinkMan.setText(store.wa);
            holder.tvPhone.setText(store.waterStoreTellPhone);
            holder.tvDetailAddress.setText(store.detailAddress);


            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"this feature will comming soon",Toast.LENGTH_SHORT).show();
                }
            });
            holder.btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), WatersActivity.class);
                    intent.putExtra("storeId",store.id);
                    intent.putExtra("storeName",store.waterStoreName);
                    getActivity().startActivity(intent);
                }
            });
            return convertView;
        }

        class Holder{
            TextView tvStoreName;
            TextView tvStatus;
            //TextView tvLinkMan;
            TextView tvPhone;
            TextView tvDetailAddress;
            Button btnGo;
            Button btnDetail;
            LinearLayout lilaTitle;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this.getActivity(),"你点击了第"+(position-1),Toast.LENGTH_SHORT).show();
    }


    /*EventBus - - 响应开始*/

    public void onEventMainThread(PullStoresSucceedEvent event){
        stores= (ArrayList<com.google.runda.model.Store>) event.getData();
        xListView.stopRefresh();
        xListView.stopLoadMore();
        storesAdapter.notifyDataSetChanged();
        xListView.setRefreshTime("刚刚");
        //Toast.makeText(this.getActivity(),"数据加载成功",Toast.LENGTH_SHORT).show();
        showStores();
    }

    public void onEventMainThread(PullStoresFailEvent event){
        Toast.makeText(this.getActivity(),"数据加载失败 "+event.getMessage(),Toast.LENGTH_SHORT).show();
        showLoadFail();
    }
    /*EventBus - - 响应结束*/
}
