package com.google.runda.model;

/**
 * Created by pinipala on 2015/3/26.
 */
public enum FontSize {
    small(1),
    middle(2),
    large(3);
    private int value;
    FontSize(int value){
        this.value=value;
    }
    public int GetValue(){
        return value;
    }
}
