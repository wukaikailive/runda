package com.google.runda.event;

import java.util.List;

/**
 * Created by wukai on 2015/4/25.
 */
public class PullCommodityDetailSucceedEvent<T> {
    T data;

    public PullCommodityDetailSucceedEvent(T data){
        this.data =data;
    }
    public T getData(){
        return data;
    }
}
