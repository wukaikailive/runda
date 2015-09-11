package com.google.runda.util;

/**
 * Created by wukai on 2015/4/18.
 */

import com.google.gson.reflect.TypeToken;
import com.google.runda.bll.Store;
import com.google.runda.model.Brand;
import com.google.runda.model.Category;
import com.google.runda.model.Commodity;
import com.google.runda.model.Order;
import com.google.runda.model.Province;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.*;

/**
 * 1:将JavaBean转换成Map、JSONObject
 * 2:将Map转换成Javabean
 * 3:将JSONObject转换成Map、Javabean
 *
 * @author Alexia
 */

public class JsonHelper {

    /**
     * 将Javabean转换为Map
     *
     * @param javaBean javaBean
     * @return Map对象
     */
    public static Map toMap(Object javaBean) {

        Map result = new HashMap();
        Method[] methods = javaBean.getClass().getDeclaredMethods();

        for (Method method : methods) {

            try {

                if (method.getName().startsWith("get")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;

    }

    /**
     * 将Json对象转换成Map
     *
     * @return Map对象
     * @throws JSONException
     */
    public static Map toMap(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;

    }

    /**
     * 将JavaBean转换成JSONObject（通过Map中转）
     *
     * @param bean javaBean
     * @return json对象
     */
    public static JSONObject toJSON(Object bean) {

        return new JSONObject(toMap(bean));

    }

    /**
     * 将Map转换成Javabean
     *
     * @param javabean javaBean
     * @param data     Map数据
     */
    public static Object toJavaBean(Object javabean, Map data) {

        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {

            try {
                if (method.getName().startsWith("set")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(javabean, data.get(field));

                }
            } catch (Exception e) {
            }

        }

        return javabean;

    }

    public static <T> List<T> toBeanList(String jsonString) {
        Gson gson = new Gson();
        try {
            List<T> list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
            return list;
        } catch (com.google.gson.JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List toStoreList(String jsonString) throws com.google.gson.JsonSyntaxException {
        Gson gson = new Gson();
        List<Store> list = gson.fromJson(jsonString, new TypeToken<List<com.google.runda.model.Store>>() {
        }.getType());
        return list;
    }
    public static List toOrderList(String jsonString) throws JsonSyntaxException {
        Gson gson = new Gson();
        List<Order> list = gson.fromJson(jsonString, new TypeToken<List<Order>>() {
        }.getType());
        return list;
    }

    public static List toBrandList(String jsonString) throws JsonSyntaxException {
        Gson gson = new Gson();
        List<Brand> list = gson.fromJson(jsonString, new TypeToken<List<Brand>>() {
        }.getType());
        return list;
    }

    public static List toCategoryList(String jsonString) throws JsonSyntaxException {
        Gson gson = new Gson();
        List<Category> list = gson.fromJson(jsonString, new TypeToken<List<Category>>() {
        }.getType());
        return list;
    }

    public static List toCommodityList(String jsonString) throws JsonSyntaxException{
        Gson gson = new Gson();
        List<Commodity> list = gson.fromJson(jsonString, new TypeToken<List<Commodity>>() {
        }.getType());
        return list;
    }

    public static <T>  T toBean (String jsonString,Class<T> cls) throws JsonSyntaxException{
        Gson gson = new Gson();
        T list = gson.fromJson(jsonString,cls);
        return list;
    }

    /**
     * JSONObject到JavaBean
     *
     * @return json对象
     * @throws ParseException json解析异常
     * @throws JSONException
     */
    public static void toJavaBean(Object javabean, String jsonString)
            throws ParseException, JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        Map map = toMap(jsonObject.toString());

        toJavaBean(javabean, map);

    }

}