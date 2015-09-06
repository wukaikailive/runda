package com.google.runda.event;

/**
 * Created by wukai on 2015/4/25.
 */
public class IOExceptionEvent {
    String localizedMessage;
    public IOExceptionEvent(String localizedMessage) {
        this.localizedMessage=localizedMessage;
    }
    public String getMessage(){
        return localizedMessage;
    }
}
