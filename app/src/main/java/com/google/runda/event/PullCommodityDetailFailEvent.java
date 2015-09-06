package com.google.runda.event;

/**
 * Created by wukai on 2015/4/25.
 */
public class PullCommodityDetailFailEvent {
    String message;
    public PullCommodityDetailFailEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
