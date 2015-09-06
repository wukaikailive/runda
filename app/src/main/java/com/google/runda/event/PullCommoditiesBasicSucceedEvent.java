package com.google.runda.event;

import java.util.List;

/**
 * Created by wukai on 2015/4/25.
 */
public class PullCommoditiesBasicSucceedEvent<T> {
    List<T> data;

    public PullCommoditiesBasicSucceedEvent(List data){
        this.data =data;
    }
    public List<T> getData(){
        return data;
    }
}
