package com.google.runda.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.bll.Config;
import com.google.runda.event.DoChangeCheckcodeEvent;
import com.google.runda.event.DoTransitionToLoginEvent;
import com.google.runda.event.DoTransitionToMeEvent;
import com.google.runda.event.ExitLoginEvent;
import com.google.runda.fragment.LoginFragment;
import com.google.runda.fragment.MeFragment;
import com.google.runda.fragment.OrdersFragment;
import com.google.runda.fragment.SettingsFragment;
import com.google.runda.fragment.WaterFragment;
import com.google.runda.staticModel.ServerConfig;

import de.greenrobot.event.EventBus;

/**
 * Created by 凯凯 on 2015/3/19.
 */
public class UserActivity extends Activity implements View.OnClickListener {

    public int NORMAL=0; /*普通*/
    public int LOGIN=1; /*当用户登录时*/
    public int EXIT_LOGIN=2; /*当用户退出登录时*/

    private LinearLayout mLila_0_1;
    private LinearLayout mLila_0_2;
    private LinearLayout mLila_0_3;
    private LinearLayout mLila_0_4;

    private Fragment mFragment_0_1;
    private Fragment mFragment_0_2;
    private Fragment mFragment_0_3;
    private Fragment mFragment_0_4;


    private boolean isLogin = false;
    private Config mConfigBll = new com.google.runda.bll.Config();

    FragmentManager fm;
    FragmentTransaction  transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);

        initView();
        initEvent();

        if(savedInstanceState==null){
            setSelect(1,NORMAL);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initEvent() {

        mLila_0_1.setOnClickListener(this);
        mLila_0_2.setOnClickListener(this);
        mLila_0_3.setOnClickListener(this);
        mLila_0_4.setOnClickListener(this);

    }

    //初始化View
    private void initView() {
        mLila_0_1 = (LinearLayout) this.findViewById(R.id.lila_0_1);
        mLila_0_2 = (LinearLayout) this.findViewById(R.id.lila_0_2);
        mLila_0_3 = (LinearLayout) this.findViewById(R.id.lila_0_3);
        mLila_0_4 = (LinearLayout) this.findViewById(R.id.lila_0_4);

    }


    private void setSelect(int i,int flag) {
        //
        fm = getFragmentManager();
        transaction = fm.beginTransaction();

        hideFragment(transaction);
        //设置layout背景为亮色
        //显示内容区域
        switch (i) {
            case 1:
                String loginName = mConfigBll.getSetting("phone");
                if (mFragment_0_1 == null) {
                    if (loginName.equals("")) {
                        //登陆
                        mFragment_0_1 = new LoginFragment();

                    } else {
                        //有登陆信息
                        mFragment_0_1 = new MeFragment();
                    }
                    transaction.add(R.id.content, mFragment_0_1);
                }else{
                    if(flag>0){
                        if(flag==LOGIN){
                            mFragment_0_1 = new MeFragment();
                        }
                        else if(flag==EXIT_LOGIN){
                            mFragment_0_1 = new LoginFragment();
                        }
                        transaction.add(R.id.content, mFragment_0_1);
                    }
                    else{
                        transaction.show(mFragment_0_1);

                    }
                }
                mLila_0_1.setBackgroundResource(R.color.grey_shallow);
                break;
            case 2:
                if (mFragment_0_2 == null) {
                    mFragment_0_2 = new WaterFragment();
                    transaction.add(R.id.content, mFragment_0_2);
                } else {
                    transaction.show(mFragment_0_2);
                }
                mLila_0_2.setBackgroundResource(R.color.grey_shallow);
                break;
            case 3:
                if (mFragment_0_3 == null) {
                    mFragment_0_3 = new OrdersFragment();
                    transaction.add(R.id.content, mFragment_0_3);

                } else {
                    transaction.show(mFragment_0_3);
                }
                mLila_0_3.setBackgroundResource(R.color.grey_shallow);
                break;
            case 4:
                if (mFragment_0_4 == null) {
                    mFragment_0_4 = new SettingsFragment();
                    transaction.add(R.id.content, mFragment_0_4);

                } else {
                    transaction.show(mFragment_0_4);
                }
                mLila_0_4.setBackgroundResource(R.color.grey_shallow);
                break;

        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mFragment_0_1 != null) {
            transaction.hide(mFragment_0_1);
        }
        if (mFragment_0_2 != null) {
            transaction.hide(mFragment_0_2);
        }
        if (mFragment_0_3 != null) {
            transaction.hide(mFragment_0_3);
        }
        if (mFragment_0_4 != null) {
            transaction.hide(mFragment_0_4);
        }
    }

    @Override
    public void onClick(View v) {
        if (mConfigBll.getSetting("phone").equals("") || mConfigBll.getSetting("password").equals("")) {
            Toast.makeText(this, "请您先登录或者注册一个账号吧..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ServerConfig.USER==null){
            return;
        }
        if(ServerConfig.USER.CellPhone==null || ServerConfig.USER.CellPhone.equals("")){
            //Toast.makeText(this, "出了些小问题...", Toast.LENGTH_SHORT).show();
            return;
        }
//        try{
//            if (mConfigBll.getSetting("cellPhone").equals("") || mConfigBll.getSetting("province").equals("")
//                    || mConfigBll.getSetting("city").equals("") ||
//                    mConfigBll.getSetting("county").equals("") || mConfigBll.getSetting("detailAddress").equals("")) {
//                Toast.makeText(this, "请先填写联系电话和完整的地址", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }catch (Exception e){
//            Toast.makeText(this, "请先填写联系电话和完整的地址", Toast.LENGTH_SHORT).show();
//            return;
//        }
        resetBackground();
        switch (v.getId()) {
            case R.id.lila_0_1:
                setSelect(1,NORMAL);
                break;
            case R.id.lila_0_2:
                setSelect(2,NORMAL);
                break;
            case R.id.lila_0_3:
                setSelect(3,NORMAL);
                break;
            case R.id.lila_0_4:
                setSelect(4,NORMAL);
                break;
            default:
                break;
        }
    }

    private void resetBackground() {
        mLila_0_1.setBackgroundResource(android.R.drawable.bottom_bar);
        mLila_0_2.setBackgroundResource(android.R.drawable.bottom_bar);
        mLila_0_3.setBackgroundResource(android.R.drawable.bottom_bar);
        mLila_0_4.setBackgroundResource(android.R.drawable.bottom_bar);

    }
    /*EventBus - - 响应开始*/
    /*响应登录成功事件*/
    public void onEventMainThread(DoTransitionToMeEvent event) {

        Log.e("harvic", "onEventMainThread收到了消息：DoTransitionToMeEvent");
        mConfigBll=new Config();
        resetBackground();
        setSelect(1,LOGIN);
    }

    /*响应退出登录事件*/
    public void onEventMainThread(ExitLoginEvent event) {

        Log.e("harvic", "onEventMainThread收到了消息：ExitLoginEvent");
        mConfigBll=new Config();
        resetBackground();
        setSelect(1,EXIT_LOGIN);
    }

    public void onEventMainThread(DoTransitionToLoginEvent event){
        Toast.makeText(this,"注册成功,请登录",Toast.LENGTH_LONG).show();
        //mConfigBll=new Config();
        //resetBackground();
        //setSelect(1,NORMAL);
        EventBus.getDefault().post(new DoChangeCheckcodeEvent());
    }
    /*EventBus - - 响应结束*/
}
