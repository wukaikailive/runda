package com.google.runda.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.AboutActivity;
import com.google.runda.activity.feedback.FeedBackActivity;
import com.google.runda.bll.Config;
import com.google.runda.model.FontSize;

/**
 * Created by 凯凯 on 2015/3/20.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox checkBoxIsAllowOrderUpdate;
    private CheckBox checkBoxIsAllowAd;
    private CheckBox checkBoxIsAlwaysNeedLogin;
    private TextView textSize;

    private TableRow page_row_text_setting;
   // private String userName;
   // private String password;

    TableRow tableRowFeedBack;
    TableRow tableRowCheckUpdate;
    TableRow tableRowAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0_4,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    //初始化
    private void init(){
        checkBoxIsAllowOrderUpdate= (CheckBox) getView().findViewById(R.id.checkBoxIsAllowOrderUpdate);
        checkBoxIsAllowAd= (CheckBox) getView().findViewById(R.id.checkBoxIsAllowAd);
        checkBoxIsAlwaysNeedLogin= (CheckBox) getView().findViewById(R.id.checkBoxIsAlwaysNeedLogin);
        textSize= (TextView) getView().findViewById(R.id.textSize);
        page_row_text_setting= (TableRow) getView().findViewById(R.id.page_row_text_setting);
        tableRowFeedBack= (TableRow) getView().findViewById(R.id.more_page_row_feedback);
        tableRowAbout= (TableRow) getView().findViewById(R.id.more_page_row_about);
        tableRowCheckUpdate= (TableRow) getView().findViewById(R.id.more_page_row_check_update);

        com.google.runda.bll.Config bll=new Config();
        com.google.runda.model.Config model=bll.getConfig();
        checkBoxIsAllowOrderUpdate.setChecked(model.isAllowAuto);
        checkBoxIsAllowAd.setChecked(model.isAllowAd);
        checkBoxIsAlwaysNeedLogin.setChecked(model.isAlwaysNeedLogin);
        int size= model.fontSize;
        if(size==FontSize.large.GetValue()){
            textSize.setText("大");
        }else if(size==FontSize.middle.GetValue()){
            textSize.setText("中");
        }else if(size==FontSize.small.GetValue()) {
            textSize.setText("小");
        }

        //
        checkBoxIsAllowOrderUpdate.setOnCheckedChangeListener(this);
        checkBoxIsAllowAd.setOnCheckedChangeListener(this);
        checkBoxIsAlwaysNeedLogin.setOnCheckedChangeListener(this);

        page_row_text_setting.setOnClickListener(this);
        tableRowFeedBack.setOnClickListener(this);
        tableRowCheckUpdate.setOnClickListener(this);
        tableRowAbout.setOnClickListener(this);
    }

    //保存设置
    private void saveSetting(){
        com.google.runda.model.Config model=new com.google.runda.model.Config();

        model.isAllowAuto=checkBoxIsAllowOrderUpdate.isChecked();
        model.isAllowAd=checkBoxIsAllowAd.isChecked();
        model.isAlwaysNeedLogin=checkBoxIsAlwaysNeedLogin.isChecked();
        String ts=textSize.getText().toString();
        if(ts=="大"){
            model.fontSize= FontSize.large.GetValue();
        }else if(ts=="中"){
            model.fontSize= FontSize.middle.GetValue();
        }else if(ts=="小"){
            model.fontSize= FontSize.small.GetValue();
        }
        com.google.runda.bll.Config bll=new Config();
        model.userName=bll.getSetting("name");
        model.phone=bll.getSetting("phone");
        model.password=bll.getSetting("password");
        bll.setConfig(model);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        try{
            saveSetting();
            Toast.makeText(this.getActivity(),"设置已保存",Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this.getActivity(),"设置保存失败,如无法解决，请反馈您的问题给我们。",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.page_row_text_setting:
                final String[] menu=new String[]{"大", "中", "小"};
                new AlertDialog.Builder(this.getActivity()).setTitle("选择文本大小").setItems(
                        menu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                textSize.setText(menu[i]);
                                saveSetting();
                                Toast.makeText(getActivity(),"设置已保存",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;
            case R.id.more_page_row_feedback:
                getActivity().startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.more_page_row_check_update:
                break;
            case R.id.more_page_row_about:
                getActivity().startActivity(new Intent(getActivity(), AboutActivity.class));
            break;
        }
    }
}
