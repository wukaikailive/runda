package com.google.runda.event;

import java.util.List;

/**
 * Created by bigface on 2015/9/10.
 */
public class PullUnfinishedOrdersSucceedEvent<T> {
    List<T> data;

    public PullUnfinishedOrdersSucceedEvent(List data){
        this.data =data;
    }
    public List<T> getData(){
        return data;
    }
}
