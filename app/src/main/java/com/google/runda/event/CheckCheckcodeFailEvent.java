package com.google.runda.event;

/**
 * Created by wukai on 2015/5/1.
 */
public class CheckCheckcodeFailEvent {
    String message;
    public CheckCheckcodeFailEvent(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
