package com.google.runda.activity.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.register.BaseActivity;
import com.google.runda.event.ChangeUserInfoFailEvent;
import com.google.runda.event.ChangeUserInfoSucceedEvent;
import com.google.runda.event.LetMeRefreshEvent;
import com.google.runda.interfaces.IArgModel;
import com.google.runda.interfaces.IHolder;
import com.google.runda.staticModel.ServerConfig;

import de.greenrobot.event.EventBus;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by bigface on 15/9/13.
 * 修改用户信息
 */
public class ChangeInfoActivity extends BaseActivity implements View.OnClickListener,OnWheelChangedListener {

    //todo 修改用户信息

    ChangeUserInfoHolder changeUserInfoHolder;
    ArgModel argModel;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    AlertDialog.Builder mRegionBuilder;
    AlertDialog mRegionDialog;
    View mRegionLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_change_info);
        EventBus.getDefault().register(this);
        changeUserInfoHolder=new ChangeUserInfoHolder();
        changeUserInfoHolder.init();
        changeUserInfoHolder.bindClickEvent(this);
        argModel=new ArgModel();
        argModel.bindValueOfView();
        initRegionDialog();
        setUpViews();
        setUpListener();
        setUpData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lila_back:
                this.finish();
                break;
            case R.id.btn_choose_region:
                showRegionDialog();
                break;
            case R.id.btn_ensure_change:
                String realName=changeUserInfoHolder.edtUserName.getText().toString();
                String detailAddress=changeUserInfoHolder.edtDetailAddress.getText().toString();
                new com.google.runda.bll.User().beginChangeUserInfo(realName,mCurrentProviceName,mCurrentCityName,mCurrentDistrictName,detailAddress);
                break;
        }
    }


    private void setUpViews() {
        mViewProvince = (WheelView) mRegionLayout.findViewById(R.id.id_province);
        mViewCity = (WheelView) mRegionLayout.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) mRegionLayout.findViewById(R.id.id_district);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(ChangeInfoActivity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = areas[0];
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    class ArgModel implements IArgModel{

        @Override
        public void init(Intent intent) {

        }

        @Override
        public void bindValueOfView() {
            changeUserInfoHolder.tvRegion.setText(ServerConfig.USER.Province+ServerConfig.USER.City+ServerConfig.USER.County);
            changeUserInfoHolder.edtDetailAddress.setText(ServerConfig.USER.DetailAddress);
            changeUserInfoHolder.edtUserName.setText(ServerConfig.USER.RealName==null?"":ServerConfig.USER.RealName);
        }
    }
    /**
     * 修改用户信息页面的holder
     */
    class ChangeUserInfoHolder implements IHolder{

        LinearLayout lilaBack;
        EditText edtUserName;
        Button btnChooseRegion;
        TextView tvRegion;
        EditText edtDetailAddress;
        Button btnEnsureChange;

        @Override
        public void init() {

            lilaBack= (LinearLayout) findViewById(R.id.lila_back);
            edtUserName= (EditText) findViewById(R.id.edt_user_name);
            btnChooseRegion= (Button) findViewById(R.id.btn_choose_region);
            tvRegion= (TextView) findViewById(R.id.tv_region);
            edtDetailAddress= (EditText) findViewById(R.id.edt_detail_address);
            btnEnsureChange= (Button) findViewById(R.id.btn_ensure_change);

        }

        @Override
        public void hide() {

        }

        @Override
        public void show() {

        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {
            lilaBack.setOnClickListener(listener);
            btnChooseRegion.setOnClickListener(listener);
            btnEnsureChange.setOnClickListener(listener);
        }
    }

    private void showRegionDialog() {
        mRegionDialog.show();
        Button btnOk = (Button) mRegionLayout.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mRegionLayout.findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUserInfoHolder.tvRegion.setText(mCurrentProviceName + "." + mCurrentCityName + "." + mCurrentDistrictName);
                mRegionDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                changeUserInfoHolder.tvRegion.setText("");
                mRegionDialog.dismiss();
            }
        });
    }
    //初始化地址窗口
    private void initRegionDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mRegionLayout = inflater.inflate(R.layout.layout_region_dialog, null);
        mRegionBuilder = new AlertDialog.Builder(this);
        mRegionBuilder.setView(mRegionLayout);
        mRegionBuilder.setCancelable(false);

        mRegionDialog = mRegionBuilder.create();
    }

    public void onEventMainThread(ChangeUserInfoSucceedEvent event){
        //结束自己
        EventBus.getDefault().post(new LetMeRefreshEvent("更新信息成功!"));
        this.finish();
    }
    public void onEventMainThread(ChangeUserInfoFailEvent event){
        Toast.makeText(this,"修改信息失败!原因"+event.getMessage(),Toast.LENGTH_LONG).show();
    }

}
