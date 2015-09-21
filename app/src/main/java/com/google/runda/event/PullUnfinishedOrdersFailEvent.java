package com.google.runda.event;

/**
 * Created by bigface on 2015/9/10.
 */
public class PullUnfinishedOrdersFailEvent {
    String message;
    public PullUnfinishedOrdersFailEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
