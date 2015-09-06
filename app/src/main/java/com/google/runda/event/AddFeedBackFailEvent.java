package com.google.runda.event;

/**
 * Created by bigface on 2015/7/25.
 */
public class AddFeedBackFailEvent {
    String message;
    public AddFeedBackFailEvent(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
