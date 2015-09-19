package com.google.runda.interfaces;

import android.content.Intent;

/**
 * Created by bigface on 2015/9/16.
 */
public interface IArgModel {

    /**
     * 初始化model
     * @param intent activity间传递的数据
     */
    void init(Intent intent);

    /**
     * 绑定值到View
     */
    void bindValueOfView();
}
