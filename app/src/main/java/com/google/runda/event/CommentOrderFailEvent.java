package com.google.runda.event;

/**
 * Created by bigface on 2015/9/20.
 */
public class CommentOrderFailEvent {
    String message;
    public CommentOrderFailEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
