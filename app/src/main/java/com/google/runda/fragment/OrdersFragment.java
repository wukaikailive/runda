package com.google.runda.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.runda.R;
import com.google.runda.interfaces.IHolder;

/**
 * Created by 凯凯 on 2015/3/20.
 */
public class OrdersFragment extends Fragment implements View.OnClickListener{


    FragmentManager fm;
    FragmentTransaction transaction;

    OrdersHolder ordersHolder;

    private Fragment mFragment_1;
    private Fragment mFragment_2;
    private Fragment mFragment_3;
    private Fragment mFragment_4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0_3, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ordersHolder=new OrdersHolder();
        ordersHolder.init();
        ordersHolder.hide();
        ordersHolder.bindClickEvent(this);
        if(savedInstanceState==null){
            setSelect(1);
        }
    }

    @Override
    public void onClick(View v) {
        ordersHolder.hide();
        switch (v.getId()){
            case R.id.frame_unfinished:
                setSelect(1);
                break;
            case R.id.frame_all_done:
                setSelect(2);
                break;
            case R.id.frame_fail:
                setSelect(3);
                break;
            case R.id.frame_all:
                setSelect(4);
                break;
        }
    }

    private void setSelect(int position){
        fm=getChildFragmentManager();
        transaction=fm.beginTransaction();
        hideFragment();
        switch (position){
            case 1:
                if (mFragment_1 == null) {
                    mFragment_1 = new UnfinishedOrdersFragment();
                    transaction.add(R.id.content, mFragment_1);
                } else {
                    transaction.show(mFragment_1);
                }
                ordersHolder.show(1);
                break;
            case 2:
                if (mFragment_2 == null) {
                    mFragment_2 = new UnfinishedOrdersFragment();
                    transaction.add(R.id.content, mFragment_2);
                } else {
                    transaction.show(mFragment_2);
                }
                ordersHolder.show(2);
                break;
            case 3:
                if (mFragment_3 == null) {
                    mFragment_3 = new UnfinishedOrdersFragment();
                    transaction.add(R.id.content, mFragment_3);
                } else {
                    transaction.show(mFragment_3);
                }
                ordersHolder.show(3);
                break;
            case 4:
                if (mFragment_4 == null) {
                    mFragment_4 = new UnfinishedOrdersFragment();
                    transaction.add(R.id.content, mFragment_4);
                } else {
                    transaction.show(mFragment_4);
                }
                ordersHolder.show(4);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(){
        if (mFragment_1 != null) {
            transaction.hide(mFragment_1);
        }
        if (mFragment_2 != null) {
            transaction.hide(mFragment_2);
        }
        if (mFragment_3 != null) {
            transaction.hide(mFragment_3);
        }
        if (mFragment_4 != null) {
            transaction.hide(mFragment_4);
        }
    }

    class OrdersHolder implements IHolder{


        FrameLayout frameUnfinished;
        FrameLayout frameAllDone;
        FrameLayout frameFail;
        FrameLayout frameAll;

        LinearLayout lilaUnfinished;
        LinearLayout lilaAllDone;
        LinearLayout lilaFail;
        LinearLayout lilaAll;
        FrameLayout content;
        @Override
        public void init() {

            frameUnfinished= (FrameLayout) getView().findViewById(R.id.frame_unfinished);
            frameAllDone= (FrameLayout) getView().findViewById(R.id.frame_all_done);
            frameFail= (FrameLayout) getView().findViewById(R.id.frame_fail);
            frameAll= (FrameLayout) getView().findViewById(R.id.frame_all);

            lilaUnfinished= (LinearLayout) getView().findViewById(R.id.lila_unfinished);
            lilaAllDone= (LinearLayout) getView().findViewById(R.id.lila_all_done);
            lilaFail= (LinearLayout) getView().findViewById(R.id.lila_fail);
            lilaAll= (LinearLayout) getView().findViewById(R.id.lila_all);
            content= (FrameLayout) getView().findViewById(R.id.content);
        }

        @Override
        public void hide() {
            lilaUnfinished.setVisibility(View.INVISIBLE);
            lilaAllDone.setVisibility(View.INVISIBLE);
            lilaFail.setVisibility(View.INVISIBLE);
            lilaAll.setVisibility(View.INVISIBLE);
        }

        @Override
        public void show() {

        }
        public void show(int position){
            switch (position){
                case 1:
                    lilaUnfinished.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    lilaAllDone.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    lilaFail.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    lilaAll.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void bindClickEvent(View.OnClickListener listener) {
            frameUnfinished.setOnClickListener(listener);
            frameAllDone.setOnClickListener(listener);
            frameFail.setOnClickListener(listener);
            frameAll.setOnClickListener(listener);
        }
    }
}
