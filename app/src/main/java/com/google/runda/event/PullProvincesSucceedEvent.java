package com.google.runda.event;

import com.google.runda.model.Province;

import java.util.List;
import java.util.Map;

/**
 * Created by wukai on 2015/4/25.
 */
public class  PullProvincesSucceedEvent <T>{
    List<T> data;

    public PullProvincesSucceedEvent(List<T> map){
        data =map;
    }
    public List<T> getData(){
        return data;
    }
}
