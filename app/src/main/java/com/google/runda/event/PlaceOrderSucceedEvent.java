package com.google.runda.event;

/**
 * Created by bigface on 2015/9/15.
 */
public class PlaceOrderSucceedEvent<T> {

    T data;

    public PlaceOrderSucceedEvent(T data){
        this.data =data;
    }
    public T getData(){
        return data;
    }
}
