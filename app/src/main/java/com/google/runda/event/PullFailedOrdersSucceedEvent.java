package com.google.runda.event;

import java.util.List;

/**
 * Created by bigface on 2015/9/20.
 */
public class PullFailedOrdersSucceedEvent<T> {
    List<T> data;

    public PullFailedOrdersSucceedEvent(List data){
        this.data =data;
    }
    public List<T> getData(){
        return data;
    }
}
