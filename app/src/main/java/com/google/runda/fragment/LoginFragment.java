package com.google.runda.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.activity.register.RegisterActivityStep1;
import com.google.runda.bll.Config;
import com.google.runda.bll.User;
import com.google.runda.event.DoChangeCheckcodeEvent;
import com.google.runda.event.DoTransitionToMeEvent;
import com.google.runda.event.LoginFailEvent;
import com.google.runda.event.LoginSuccessEvent;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.LoadImageTask;

import de.greenrobot.event.EventBus;

/**
 * Created by wc on 2015/3/30.
 */
public class LoginFragment extends Fragment implements  View.OnClickListener{

    View view;
    EditText userName;
    EditText password;
    EditText checkCode;
    Button login;
    TextView register;
    ImageView ivCheckCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_0_1_phone,container,false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this); //注册EventBus
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); //注销EventBus
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        if(savedInstanceState==null){
            loadCheckCode();
        }
    }

    private void init(){
        userName= (EditText) getView().findViewById(R.id.userName);
        password= (EditText) getView().findViewById(R.id.password);
        checkCode= (EditText) getView().findViewById(R.id.checkCode);
        login = (Button) getView().findViewById(R.id.login);
        register= (TextView) getView().findViewById(R.id.register);
        ivCheckCode= (ImageView) getView().findViewById(R.id.ivCheckCode);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        ivCheckCode.setOnClickListener(this);

    }


    private void loadCheckCode(){
        ivCheckCode.setImageResource(R.drawable.check_code_default);
        LoadImageTask task=new LoadImageTask(ivCheckCode, ServerConfig.WAY_CHECK_CODE,90,30);
        task.execute();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login:
                String name=userName.getText().toString();
                String passwd=password.getText().toString();
                String code=checkCode.getText().toString();
                com.google.runda.bll.User bll=new User();
                bll.beginLogin(name, passwd, code,"phone");
                break;
            case R.id.register:
                getActivity().startActivity(new Intent(getActivity(), RegisterActivityStep1.class));
                break;
            case R.id.ivCheckCode:
                loadCheckCode();
                break;
        }
    }
/*EventBus响应 - - 开始*/

    public void onEventMainThread(LoginSuccessEvent event){
        Toast.makeText(getActivity(),"登陆成功",Toast.LENGTH_LONG).show();
        EventBus.getDefault().post(new DoTransitionToMeEvent());//发送登录成功事件
    }
    public void onEventMainThread(LoginFailEvent event){
        Toast.makeText(getActivity(),"登陆失败"+" "+event.getMessage(),Toast.LENGTH_LONG).show();
        loadCheckCode();
    }

    public void onEventMainThread(DoChangeCheckcodeEvent event){
        loadCheckCode();
        String phone=new Config().getSetting("phone");
        userName.setText(phone);
    }
/*EventBus响应 - - 结束*/
}
