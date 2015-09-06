package com.google.runda.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.google.runda.R;

/**
 * Created by wukai on 2015/4/22.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);

    }
}
