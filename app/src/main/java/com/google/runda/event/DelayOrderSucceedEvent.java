package com.google.runda.event;

/**
 * Created by bigface on 2015/9/20.
 */
public class DelayOrderSucceedEvent {
    String message;
    public DelayOrderSucceedEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
