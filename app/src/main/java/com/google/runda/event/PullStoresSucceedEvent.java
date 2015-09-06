package com.google.runda.event;

import java.util.List;
import java.util.Map;

/**
 * Created by wukai on 2015/4/25.
 */
public class PullStoresSucceedEvent<T> {
    List<T> data;

    public PullStoresSucceedEvent(List data){
        this.data =data;
    }
    public List<T> getData(){
        return data;
    }
}
