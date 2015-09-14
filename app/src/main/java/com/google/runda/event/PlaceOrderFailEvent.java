package com.google.runda.event;

/**
 * Created by bigface on 2015/9/15.
 */
public class PlaceOrderFailEvent {

    String message;
    public PlaceOrderFailEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }

}
