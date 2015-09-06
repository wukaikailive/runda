package com.google.runda.event;

/**
 * Created by wukai on 2015/5/1.
 */
public class LoginFailEvent {
    String message;
    public LoginFailEvent(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
