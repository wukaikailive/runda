package com.google.runda.interfaces;

import android.view.View;

/**
 * Created by bigface on 2015/9/16.
 */
public interface IHolder {
    /**
     * 初始化View
     */
    void init();//初始化

    /**
     * 隐藏View
     */
    void hide();

    /**
     * 显示View
     */
    void show();

    /**
     * 绑定点击事件
     * @param listener 点击监听对象
     */
    void bindClickEvent(View.OnClickListener listener);
}
