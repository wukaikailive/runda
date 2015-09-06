package com.google.runda.event;

import java.util.List;

/**
 * Created by wukai on 2015/4/25.
 */
public class PullCategoriesSucceedEvent<T> {
    List<T> data;

    public PullCategoriesSucceedEvent(List data){
        this.data =data;
    }
    public List<T> getData(){
        return data;
    }
}
