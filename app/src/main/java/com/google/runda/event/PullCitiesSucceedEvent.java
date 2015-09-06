package com.google.runda.event;

import java.util.Map;

/**
 * Created by wukai on 2015/4/25.
 */
public class PullCitiesSucceedEvent {
    Map<String, String> data;

    public PullCitiesSucceedEvent(Map<String, String> map){
        data =map;
    }
    public Map<String,String> getData(){
        return data;
    }
}
