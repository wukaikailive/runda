package com.google.runda.event;

/**
 * Created by bigface on 2015/9/20.
 */
public class CommentOrderSucceedEvent {
    String message;
    public CommentOrderSucceedEvent(String message) {
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
