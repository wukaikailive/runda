package com.google.runda.event;

/**
 * Created by bigface on 2015/9/20.
 */
public class ChangeUserInfoFailEvent {
    String message;
    public ChangeUserInfoFailEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}