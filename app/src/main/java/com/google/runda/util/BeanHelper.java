package com.google.runda.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bigface on 2015/9/15.
 */
public class BeanHelper {

    public static HashMap<String, String> objToHash(Object obj) throws IllegalArgumentException,IllegalAccessException {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        Class clazz = obj.getClass();
        List<Class> clazzs = new ArrayList<Class>();

        do {
            clazzs.add(clazz);
            clazz = clazz.getSuperclass();
        } while (!clazz.equals(Object.class));

        for (Class iClazz : clazzs) {
            Field[] fields = iClazz.getDeclaredFields();
            for (Field field : fields) {
                String objVal = null;
                field.setAccessible(true);
                objVal = (String) field.get(obj);
                hashMap.put(field.getName(), objVal);
            }
        }

        return hashMap;
    }
}
