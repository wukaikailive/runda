package com.google.runda.event;

import java.util.List;

/**
 * Created by bigface on 2015/9/20.
 */
public class PullCanceledOrdersSucceedEvent<T> {
    List<T> data;

    public PullCanceledOrdersSucceedEvent(List data){
        this.data =data;
    }
    public List<T> getData(){
        return data;
    }
}