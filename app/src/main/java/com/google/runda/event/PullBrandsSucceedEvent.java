package com.google.runda.event;

import java.util.List;

/**
 * Created by wukai on 2015/4/25.
 */
public class PullBrandsSucceedEvent<T> {
    List<T> data;

    public PullBrandsSucceedEvent(List data){
        this.data =data;
    }
    public List<T> getData(){
        return data;
    }
}
