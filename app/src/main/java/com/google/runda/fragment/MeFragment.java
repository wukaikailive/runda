package com.google.runda.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.user.ChangeInfoActivity;
import com.google.runda.bll.Config;
import com.google.runda.bll.User;
import com.google.runda.event.ExitLoginEvent;
import com.google.runda.event.LetMeRefreshEvent;
import com.google.runda.event.PullUserDataFailEvent;
import com.google.runda.event.PullUserDataSucceedEvent;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.view.floatbutton.FloatingActionButton;

import de.greenrobot.event.EventBus;

/**
 * Created by 凯凯 on 2015/3/20.
 */
public class MeFragment extends android.app.Fragment implements View.OnClickListener {



    Button btnExitLogin;/*退出登录按钮*/

    TextView mTvRegion;
    TextView mTvDetailAddress;
    TextView mTvPhone;
    LinearLayout mLilaFloatMenu;
    FloatingActionButton floatingActionButton;
    boolean floatMenuIsOpen=false;



    private LinearLayout mLilaLoading;
    private LinearLayout mLilaUserInfo;
    private LinearLayout mLilaFloatMenuLa;
    private LinearLayout mLilaLoadFail;

    private Button mBtnRetry;
    private Button mBtnToLogin;
    private Button mBtnEdit;
    private Button mBtnRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0_1_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        hideAll();
        showLoading();
    }

    private void init() {
        //绑定控件
        mLilaFloatMenu= (LinearLayout) getView().findViewById(R.id.lila_float_menu);
        btnExitLogin = (Button) getView().findViewById(R.id.btnExitLogin);
        mTvRegion = (TextView) getView().findViewById(R.id.tv_region);
        mTvPhone = (TextView) getView().findViewById(R.id.tv_phone);
        mTvDetailAddress = (TextView) getView().findViewById(R.id.tv_detail_address);
        floatingActionButton= (FloatingActionButton) getView().findViewById(R.id.button_floating_action);

        mLilaFloatMenuLa= (LinearLayout) getView().findViewById(R.id.lila_float_menu_la);
        mLilaLoading = (LinearLayout)getView(). findViewById(R.id.lila_load_user_info);
        mLilaUserInfo= (LinearLayout) getView().findViewById(R.id.lila_user_info);
        mLilaLoadFail = (LinearLayout) getView().findViewById(R.id.lila_load_user_info_error);
        mBtnRetry= (Button) getView().findViewById(R.id.btn_retry);
        mBtnToLogin= (Button) getView().findViewById(R.id.btn_to_login);
        mBtnEdit= (Button) getView().findViewById(R.id.btnEdit);
        mBtnRefresh= (Button) getView().findViewById(R.id.btnRefresh);

        //注册监听事件
        btnExitLogin.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        mBtnRetry.setOnClickListener(this);
        mBtnToLogin.setOnClickListener(this);
        mBtnEdit.setOnClickListener(this);
        mBtnRefresh.setOnClickListener(this);
        bindUserData();
    }

    private void bindUserData() {
        com.google.runda.bll.User bll = new User();
        bll.beginPullUserInfo("phone");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnExitLogin:
                com.google.runda.bll.Config bll = new Config();
                bll.setConfig(bll.getDefaultConfig());
                EventBus.getDefault().post(new ExitLoginEvent());
                break;
            case R.id.button_floating_action:
                Animation animationPushIn=AnimationUtils.loadAnimation(getActivity(),R.anim.push_in);
                Animation animationPushOut=AnimationUtils.loadAnimation(getActivity(),R.anim.push_out);

                if(floatMenuIsOpen==false){
                    mLilaFloatMenu.setAnimation(animationPushIn);
                    mLilaFloatMenu.setVisibility(View.VISIBLE);
                    floatMenuIsOpen=true;
                }else{
                    mLilaFloatMenu.setAnimation(animationPushOut);
                    mLilaFloatMenu.setVisibility(View.INVISIBLE);
                    floatMenuIsOpen=false;
                }
                break;
            case R.id.btn_retry:
                showLoading();
                new User().beginPullUserInfo("phone");
                break;
            case R.id.btn_to_login:
                EventBus.getDefault().post(new ExitLoginEvent());
                break;
            case R.id.btnEdit:
                getActivity().startActivity(new Intent(getActivity(), ChangeInfoActivity.class));
                break;
            case R.id.btnRefresh:
                hideAll();
                showLoading();
                bindUserData();
                break;
        }
    }


    private void hideAll(){
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaFloatMenuLa.setVisibility(View.INVISIBLE);
        mLilaUserInfo.setVisibility(View.INVISIBLE);
    }
    private void showLoading(){
        mLilaLoading.setVisibility(View.VISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaFloatMenuLa.setVisibility(View.INVISIBLE);
        mLilaUserInfo.setVisibility(View.INVISIBLE);
    }

    private void showLoadFail(){
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.VISIBLE);
        mLilaFloatMenuLa.setVisibility(View.INVISIBLE);
        mLilaUserInfo.setVisibility(View.INVISIBLE);
    }
    private void showUserInfo(){
        mLilaLoading.setVisibility(View.INVISIBLE);
        mLilaLoadFail.setVisibility(View.INVISIBLE);
        mLilaFloatMenuLa.setVisibility(View.VISIBLE);
        mLilaUserInfo.setVisibility(View.VISIBLE);
    }

    /*响应用户数据加载成功事件*/
    public void onEventMainThread(PullUserDataSucceedEvent event) {
        com.google.runda.model.User model = ServerConfig.USER;
        mTvPhone.setText(model.CellPhone);
        mTvRegion.setText("" + model.Province + " " + model.City + " " + model.County);
        mTvDetailAddress.setText(model.DetailAddress);
        showUserInfo();
    }

    /*响应用户数据加载失败事件*/
    public void onEventMainThread(PullUserDataFailEvent event) {
        showLoadFail();
        Toast.makeText(getActivity(), event.getMessage() + "", Toast.LENGTH_SHORT).show();

    }

    public void onEventMainThread(LetMeRefreshEvent event) {
        hideAll();
        showLoading();
        bindUserData();
        Toast.makeText(getActivity(), event.getMessage() + "", Toast.LENGTH_SHORT).show();
    }

}
