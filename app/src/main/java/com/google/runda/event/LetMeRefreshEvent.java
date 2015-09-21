package com.google.runda.event;

/**
 * Created by bigface on 2015/9/20.
 */
public class LetMeRefreshEvent {
    String message;
    public LetMeRefreshEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
