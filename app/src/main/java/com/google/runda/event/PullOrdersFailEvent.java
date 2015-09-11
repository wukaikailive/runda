package com.google.runda.event;

/**
 * Created by bigface on 2015/9/10.
 */
public class PullOrdersFailEvent {
    String message;
    public PullOrdersFailEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
