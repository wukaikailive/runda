package com.google.runda.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.event.PullCommoditiesBasicFailEvent;
import com.google.runda.event.PullCommoditiesBasicSucceedEvent;
import com.google.runda.event.PullCommodityDetailFailEvent;
import com.google.runda.event.PullCommodityDetailSucceedEvent;
import com.google.runda.model.Commodity;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * Created by wukai on 2015/5/10.
 */
public class WaterDetailActivity extends Activity implements View.OnClickListener{

    Commodity commodity;

    String waterId;
    String waterName;

    LinearLayout mLilaWater;
    LinearLayout mLilaLoading;
    LinearLayout mLilaLoadFail;
    Button mBtnRetry;
    ImageButton mIbtnClose;
    TextView mTvWaterName;
    TextView mTvBrand;
    TextView mTvCategory;
    TextView mTvGroundingDate;
    TextView mTvDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_water_detail);
        Intent intent=getIntent();
        waterId=intent.getStringExtra("waterId");
        waterName=intent.getStringExtra("waterName");
        init();
        EventBus.getDefault().register(this);
    }

    void init(){
        mLilaWater = (LinearLayout) findViewById(R.id.lila_water);
        mLilaLoading= (LinearLayout) findViewById(R.id.lila_loading);
        mLilaLoadFail= (LinearLayout) findViewById(R.id.lila_load_fail);
        mBtnRetry= (Button) findViewById(R.id.btn_retry);
        mIbtnClose= (ImageButton) findViewById(R.id.ibtn_close);
        mTvWaterName = (TextView) findViewById(R.id.tv_water_name);
        mTvBrand= (TextView) findViewById(R.id.tv_brand);
        mTvCategory= (TextView) findViewById(R.id.tv_category);
        mTvGroundingDate= (TextView) findViewById(R.id.tv_grounding_date);
        mTvDescription= (TextView) findViewById(R.id.tv_description);



        mTvWaterName.setText(waterName);
        hideAll();
        showLoading();
        mBtnRetry.setOnClickListener(this);
        mIbtnClose.setOnClickListener(this);
        //
        new com.google.runda.bll.Commodity().beginPullCommoditiesDetail(waterId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_retry:
                new com.google.runda.bll.Commodity().beginPullCommoditiesDetail(waterId);
                showLoading();
                break;
            case R.id.ibtn_close:
                this.finish();
                break;
        }
    }

    private void showLoading(){
        mLilaWater.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.VISIBLE);
    }
    void showCommodities(){
        mLilaWater.setVisibility(View.VISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
    }
    void showLoadFail(){
        mLilaWater.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.VISIBLE);
    }
    void hideAll(){
        mLilaWater.setVisibility(View.INVISIBLE);
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
    }



    /*EventBus - - 响应开始*/

    public void onEventMainThread(PullCommodityDetailSucceedEvent event){
        commodity = (Commodity) event.getData();
        mTvBrand.setText("品牌:"+commodity.waterBrand);
        mTvCategory.setText("种类:"+commodity.waterCate);
        mTvGroundingDate.setText("上架时间:"+toDateString(Long.parseLong(commodity.groundingDate)));
        mTvDescription.setText(Html.fromHtml(""+commodity.waterGoodsDescript));

        Toast.makeText(this,"数据加载成功",Toast.LENGTH_SHORT).show();
        showCommodities();
    }

    public void onEventMainThread(PullCommodityDetailFailEvent event){
        Toast.makeText(this,"数据加载失败 "+event.getMessage(),Toast.LENGTH_SHORT).show();
        showLoadFail();
    }



    /*EventBus - - 响应结束*/

    private String toDateString(long milli){
        Date date=new Date(milli);
        return date.toString();
    }
}
