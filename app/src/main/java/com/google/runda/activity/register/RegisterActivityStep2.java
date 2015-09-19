package com.google.runda.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.runda.R;
import com.google.runda.bll.User;
import com.google.runda.event.CheckCheckcodeFailEvent;
import com.google.runda.event.CheckCheckcodeSucceedEvent;
import com.google.runda.event.CheckPhoneFailEvent;
import com.google.runda.event.CheckPhoneSucceedEvent;
import com.google.runda.event.DoActivityFinishEvent;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.LoadImageTask;

import java.util.Date;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by wukai on 2015/4/26.
 */
public class RegisterActivityStep2 extends Activity implements View.OnClickListener {
    /**
     * 如果用户验证登录成功，则让继续下一步
     */
    private boolean flag = false;

    ImageView mIvNext;
    ImageView mIvBack;
    Button mBtnCheck;
    EditText mEtPhone;
    LinearLayout mLilaPassword;
    View mPasswordLayout;
    EditText mEtUserName;
    EditText mEtPassword;
    EditText mEtPasswordEnsure;
    EditText mEtCheckCode;
    ImageView mIvCheckCode;
    TextView mTvSummaryPasswordEnsure;
    TextView mTvSummaryCheckCode;
    ProgressBar mProBarCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_2);
        EventBus.getDefault().register(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        mPasswordLayout = inflater.inflate(R.layout.layout_password, null);
        init();
        //将next图标隐藏掉
        mIvNext.setVisibility(View.INVISIBLE);
        //将验证手机号进度条隐藏掉
        mProBarCheck.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void init() {
        mIvNext = (ImageView) findViewById(R.id.ivNext);
        mBtnCheck = (Button) findViewById(R.id.btn_check);
        mProBarCheck = (ProgressBar) findViewById(R.id.pro_bar_check);
        mIvBack = (ImageView) findViewById(R.id.ivBack);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mLilaPassword = (LinearLayout) findViewById(R.id.lila_password);

        mIvNext.setOnClickListener(this);
        mBtnCheck.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        mEtUserName = (EditText) mPasswordLayout.findViewById(R.id.et_user_name);
        mEtPassword = (EditText) mPasswordLayout.findViewById(R.id.et_password);
        mEtPasswordEnsure = (EditText) mPasswordLayout.findViewById(R.id.et_password_ensure);
        mEtCheckCode = (EditText) mPasswordLayout.findViewById(R.id.et_check_code);
        mIvCheckCode = (ImageView) mPasswordLayout.findViewById(R.id.iv_check_code);
        mTvSummaryCheckCode = (TextView) mPasswordLayout.findViewById(R.id.tv_summary_check_code);
        mTvSummaryPasswordEnsure = (TextView) mPasswordLayout.findViewById(R.id.tv_summary_password_ensure);

        //设置点击事件
        mIvCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCheckCode();
            }
        });


        //设置焦点改变事件
        mEtPasswordEnsure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String password = mEtPassword.getText().toString();
                    String password2 = mEtPasswordEnsure.getText().toString();
                    String reg = "^[^\\s]{6,16}$";
                    if (password.matches(reg)) {
                        if (password.equals(password2)) {
                            mTvSummaryPasswordEnsure.setText("ok");
                        } else {
                            mTvSummaryPasswordEnsure.setText("    ");
                        }
                    } else {
                        Toast.makeText(RegisterActivityStep2.this, "密码的格式不正确哦", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //设置文本改变事件
        mEtCheckCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //隐藏下一步按钮
                mIvNext.setVisibility(View.INVISIBLE);
                if (editable.length() == 4) {
                    // 异步验证验证码是否正确
                    new User().beginCheckCheckcode(mEtCheckCode.getText().toString());
                }
            }
        });

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvSummaryCheckCode.setText("");
                //隐藏密码输入框
                removePasswordLayout();
                //隐藏下一步按钮
                mIvNext.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivNext:
                ServerConfig.USER.CellPhone = mEtPhone.getText().toString();
                ServerConfig.USER.Sex = "保密";
                ServerConfig.USER.Name = mEtUserName.getText().toString()+UUID.randomUUID();
                ServerConfig.USER.Password = mEtPassword.getText().toString();
                ServerConfig.USER.CheckCode = mEtCheckCode.getText().toString();
                ServerConfig.USER.RealName=mEtUserName.getText().toString();

                if (ServerConfig.USER.Name.trim().length() == 0) {
                    Toast.makeText(this, "你还没有输入你的名字哦", Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(RegisterActivityStep2.this, RegisterActivityStep3.class));
                }
                break;
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.btn_check:
                //异步验证手机号
                String ex = "^0{0,1}(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$";
                String phone = mEtPhone.getText().toString();

                if (phone.matches(ex)) {
                    //显示进度条
                    mProBarCheck.setVisibility(View.VISIBLE);
                    //设置验证按钮不可用
                    mBtnCheck.setEnabled(false);
                    new User().beginCheckPhone(phone);
                } else {
                    Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    //加载验证码
    private void loadCheckCode() {
        mIvCheckCode.setImageResource(R.drawable.check_code_default);
        LoadImageTask task = new LoadImageTask(mIvCheckCode, ServerConfig.WAY_CHECK_CODE, 90, 30);
        task.execute();
    }

    private void removePasswordLayout() {
        if (mPasswordLayout.getParent() == mLilaPassword) {
            mLilaPassword.removeView(mPasswordLayout);
        }
    }

    private void addPasswordLayout() {
        mLilaPassword.addView(mPasswordLayout);
    }

    /*EventBus - - 响应开始*/
    public void onEventMainThread(DoActivityFinishEvent event) {
        this.finish();
    }

    public void onEventMainThread(CheckPhoneSucceedEvent event) {
        Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
        //隐藏进度条
        mProBarCheck.setVisibility(View.INVISIBLE);
        //设置验证按钮可用
        mBtnCheck.setEnabled(true);
        removePasswordLayout();
        addPasswordLayout();
        loadCheckCode();
    }

    public void onEventMainThread(CheckPhoneFailEvent event) {
        //隐藏进度条
        mProBarCheck.setVisibility(View.INVISIBLE);
        //设置验证按钮可用
        mBtnCheck.setEnabled(true);
        removePasswordLayout();
        Toast.makeText(this, "" + event.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void onEventMainThread(CheckCheckcodeSucceedEvent event) {
        //  Toast.makeText(this, "验证码验证成功", Toast.LENGTH_SHORT).show();
        Log.e("onEventMainThread", "验证码验证成功");
        mTvSummaryCheckCode.setText("Ok!");
        //显示下一步按钮
        mIvNext.setVisibility(View.VISIBLE);

    }

    public void onEventMainThread(CheckCheckcodeFailEvent event) {
        mTvSummaryCheckCode.setText("");
        //隐藏下一步按钮
        mIvNext.setVisibility(View.INVISIBLE);
    }
    /*EventBus - - 响应结束*/
}
