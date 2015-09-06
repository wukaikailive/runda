package com.google.runda.activity.register;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.bll.User;
import com.google.runda.event.DoActivityFinishEvent;
import com.google.runda.event.DoTransitionToLoginEvent;
import com.google.runda.event.RegisterFailEvent;
import com.google.runda.event.RegisterSucceedEvent;
import com.google.runda.staticModel.ServerConfig;

import de.greenrobot.event.EventBus;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by wukai on 2015/4/26.
 */
public class RegisterActivityStep3 extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
//    private Button mBtnConfirm;
    private Button mBtnChoose;
    private TextView mTvRegion;
    private ImageView mIvOver;
    private ImageView mIvBack;
    private EditText mEtDetailAddress;
//弹出框 - - 地址
    AlertDialog.Builder mRegionBuilder;
    AlertDialog mRegionDialog;
    View mRegionLayout;
//弹出框 - - 确认注册
    View mRegisterEnsureLayout;
    AlertDialog.Builder mRegisterEnsureBuilder;
    AlertDialog mRegisterEnsureDialog;
    TextView mTvEnsurePhone;
    TextView mTvEnsureRegion;
    TextView mTvEnsureDetailAddress;
//弹出框 - - 提示注册中
    View mWaitRegisterLayout;
    AlertDialog.Builder mWaitRegisterBuilder;
    AlertDialog mWaitRegisterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_3);
        EventBus.getDefault().register(this);
        initRegionDialog();
        initRegisterEnsureDialog();
        initWaitRegisterDialog();
        setUpViews();
        setUpListener();
        setUpData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setUpViews() {
        mViewProvince = (WheelView) mRegionLayout.findViewById(R.id.id_province);
        mViewCity = (WheelView) mRegionLayout.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) mRegionLayout.findViewById(R.id.id_district);
        mTvRegion = (TextView) findViewById(R.id.tv_region);
        mIvOver = (ImageView) findViewById(R.id.ivOver);
        mIvBack= (ImageView) findViewById(R.id.ivBack);
        mEtDetailAddress = (EditText) findViewById(R.id.et_detail_address);
//        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnChoose = (Button) findViewById(R.id.btn_choose_region);

        mTvEnsurePhone = (TextView) mRegisterEnsureLayout.findViewById(R.id.tv_ensure_phone);
        mTvEnsureRegion = (TextView) mRegisterEnsureLayout.findViewById(R.id.tv_ensure_region);
        mTvEnsureDetailAddress = (TextView) mRegisterEnsureLayout.findViewById(R.id.tv_ensure_detail_address);

    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
//        mBtnConfirm.setOnClickListener(this);
        mBtnChoose.setOnClickListener(this);

        mIvOver.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(RegisterActivityStep3.this, mProvinceDatas));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_confirm:
//                showSelectedResult();
//                break;
            case R.id.btn_choose_region:
                showRegionDialog();
                break;
            case R.id.ivOver:
                ServerConfig.USER.Province = mCurrentProviceName;
                ServerConfig.USER.City = mCurrentCityName;
                ServerConfig.USER.County = mCurrentDistrictName;
                ServerConfig.USER.DetailAddress = mEtDetailAddress.getText().toString();
                showRegisterEnsureDialog();
                break;
            case R.id.ivBack:
                this.finish();
                break;
            default:
                break;
        }
    }

    private void showSelectedResult() {
        Toast.makeText(RegisterActivityStep3.this, "当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
                + mCurrentDistrictName + "," + mCurrentZipCode, Toast.LENGTH_SHORT).show();
    }


    private void showRegionDialog() {
        mRegionDialog.show();
        Button btnOk = (Button) mRegionLayout.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mRegionLayout.findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvRegion.setText(mCurrentProviceName + "." + mCurrentCityName + "." + mCurrentDistrictName);
                mRegionDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvRegion.setText("");
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

    //初始化确认注册窗口
    private void initRegisterEnsureDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mRegisterEnsureLayout = inflater.inflate(R.layout.layout_register_ensure, null);
        mRegisterEnsureBuilder = new AlertDialog.Builder(this);
        mRegisterEnsureBuilder.setView(mRegisterEnsureLayout);
        mRegisterEnsureBuilder.setCancelable(false);

        mRegisterEnsureDialog = mRegisterEnsureBuilder.create();
    }

    private void initWaitRegisterDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        mWaitRegisterLayout = inflater.inflate(R.layout.wait_layout, null);
        mWaitRegisterBuilder = new AlertDialog.Builder(this,R.style.Dialog_Fullscreen);
        mWaitRegisterBuilder.setView(mWaitRegisterLayout);
        mWaitRegisterBuilder.setCancelable(false);

        mWaitRegisterDialog=mWaitRegisterBuilder.create();
    }
    private void showWaitRegisterDialog(){
        mWaitRegisterDialog.show();
    }

    private void showRegisterEnsureDialog() {
        mRegisterEnsureDialog.show();
        mTvEnsurePhone.setText(ServerConfig.USER.CellPhone);
        mTvEnsureRegion.setText(ServerConfig.USER.Province + "." + ServerConfig.USER.City + "." + ServerConfig.USER.County);
        mTvEnsureDetailAddress.setText(ServerConfig.USER.DetailAddress + "." + ServerConfig.USER.CheckCode + "." + ServerConfig.USER.Name + "." + ServerConfig.USER.Sex);
        Button btnOk = (Button) mRegisterEnsureLayout.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) mRegisterEnsureLayout.findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    mRegisterEnsureDialog.dismiss();
                    showWaitRegisterDialog();
                    //注册用户
                    new User().beginRegister(ServerConfig.USER);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegisterEnsureDialog.dismiss();
            }
        });
    }


    /*- -EventBus事件响应开始- -*/
    //登陆成功 自动登陆
    public void onEventMainThread(RegisterSucceedEvent event) {
        //todo 发送注册成功事件告诉主线程切换页面
        //todo 告诉注册的各步骤activity结束自己
        Log.e("收到注册成功消息：","www");
        //关闭提示注册中窗口
        mWaitRegisterDialog.dismiss();
        //告知注册步骤activity结束
        EventBus.getDefault().post(new DoActivityFinishEvent());
        //告知主线程登陆成功 切换到已登陆页面
        EventBus.getDefault().post(new DoTransitionToLoginEvent());

//        this.finish();
    }


    public void onEventMainThread(DoActivityFinishEvent event){
        //结束自己
        this.finish();
    }

    public void onEventMainThread(RegisterFailEvent event){
        Log.e("收到注册失败消息",""+event.getMessage());
        mWaitRegisterDialog.dismiss();
        Toast.makeText(this,""+event.getMessage(),Toast.LENGTH_LONG).show();
    }
    /*- -EventBus事件响应结束- -*/
}
