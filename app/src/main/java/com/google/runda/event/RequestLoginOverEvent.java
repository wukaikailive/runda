package com.google.runda.event;

/**
 * Created by wukai on 2015/4/23.
 */
public class RequestLoginOverEvent {
    private Boolean flag;
    private String message;
    private String exception;

    public Boolean getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
