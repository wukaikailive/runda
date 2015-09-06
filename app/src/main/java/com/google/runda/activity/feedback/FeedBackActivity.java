package com.google.runda.activity.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.runda.R;
import com.google.runda.event.AddFeedBackFailEvent;
import com.google.runda.event.AddFeedBackSucceedEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by bigface on 2015/7/24.
 */
public class FeedBackActivity extends Activity implements View.OnClickListener {

    /*反馈的问题类型*/
    RadioGroup mRGFeedbackType;
    /*反馈者邮箱*/
    EditText mEditEmail;
    /*反馈内容*/
    EditText mEditContent;
    /*提交反馈*/
    Button mBtnSubmit;
    /*退出反馈*/
    Button mBtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init(){
        mRGFeedbackType= (RadioGroup) findViewById(R.id.feedback_type);
        mEditEmail= (EditText) findViewById(R.id.editEmail);
        mEditContent= (EditText) findViewById(R.id.editContent);
        mBtnSubmit= (Button) findViewById(R.id.submit);
        mBtnBack= (Button) findViewById(R.id.back);

        mBtnBack.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mBtnSubmit.setEnabled(true);
                } else {
                    mBtnSubmit.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.submit:
                com.google.runda.model.FeedBack model=new com.google.runda.model.FeedBack();
                switch (mRGFeedbackType.getCheckedRadioButtonId()){
                    case R.id.radio_question:
                        model.type=false;
                        break;
                    case R.id.radio_suppose:
                        model.type=true;
                        break;
                    default:
                        model.type=false;
                        break;
                }
                model.email=mEditEmail.getText().toString();
                model.content=mEditContent.getText().toString();
                com.google.runda.bll.FeedBack bll=new com.google.runda.bll.FeedBack();
                bll.beginAddFeedBack(model);
                break;
            default:
                break;
        }
    }


    public void onEventMainThread(AddFeedBackSucceedEvent event){
        startActivity(new Intent(this, FeedBackSuccessActivity.class));
        //结束自己
        this.finish();
    }

    public void onEventMainThread(AddFeedBackFailEvent event){
        Log.e("收到反馈失败消息", "" + event.getMessage());
        Toast.makeText(this, "" + event.getMessage(), Toast.LENGTH_LONG).show();
    }
}
