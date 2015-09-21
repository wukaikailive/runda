package com.google.runda.bll;

import android.util.Log;

import com.google.runda.event.CancelOrderFailEvent;
import com.google.runda.event.CancelOrderSucceedEvent;
import com.google.runda.event.CommentOrderFailEvent;
import com.google.runda.event.CommentOrderSucceedEvent;
import com.google.runda.event.DelayOrderFailEvent;
import com.google.runda.event.DelayOrderSucceedEvent;
import com.google.runda.event.DeleteOrderFailEvent;
import com.google.runda.event.DeleteOrderSucceedEvent;
import com.google.runda.event.EnsureOrderFailEvent;
import com.google.runda.event.EnsureOrderSucceedEvent;
import com.google.runda.event.PlaceOrderFailEvent;
import com.google.runda.event.PlaceOrderSucceedEvent;
import com.google.runda.event.PullAllDoneOrdersFailEvent;
import com.google.runda.event.PullAllDoneOrdersSucceedEvent;
import com.google.runda.event.PullAllOrdersFailEvent;
import com.google.runda.event.PullAllOrdersSucceedEvent;
import com.google.runda.event.PullCanceledOrdersFailEvent;
import com.google.runda.event.PullCanceledOrdersSucceedEvent;
import com.google.runda.event.PullFailedOrdersFailEvent;
import com.google.runda.event.PullFailedOrdersSucceedEvent;
import com.google.runda.event.PullUnfinishedOrdersFailEvent;
import com.google.runda.event.PullUnfinishedOrdersSucceedEvent;
import com.google.runda.model.PlaceOrderFormModel;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.BeanHelper;
import com.google.runda.util.HttpUtil;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by bigface on 2015/9/10.
 */
public class Order {


    public String getOrdersJson(int flag) throws IOException {
        switch (flag){
            case 1:
                return HttpUtil.get(ServerConfig.WAY_UNFINISHED_ORDERS);
            case 2:
                return HttpUtil.get(ServerConfig.WAY_ALL_DONE_ORDERS);
            case 3:
                return HttpUtil.get(ServerConfig.WAY_ALL_ORDERS);
            case 4:
                return HttpUtil.get(ServerConfig.WAY_CANCELED_ORDERS);
            case 5:
                return HttpUtil.get(ServerConfig.WAY_FAILED_ORDERS);
        }
        return null;
    }
    PullOrdersThread pullOrdersThread=null;

    public void beginPullOrders(int flag){
        synchronized (this){
            if(pullOrdersThread !=null){
                return ;
            }
            pullOrdersThread =new PullOrdersThread();
            pullOrdersThread.flag=flag;
            pullOrdersThread.start();
        }
    }
    class PullOrdersThread extends Thread{
        public int flag;
        @Override
        public void run() {
            String jsonString="";
            try {
                //todo 需加上参数
                jsonString = getOrdersJson(flag);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.OrderModel> orders=JsonHelper.toOrderModelList(result.get("data"));
                    switch (flag){
                        case 1:
                            EventBus.getDefault().post(new PullUnfinishedOrdersSucceedEvent(orders));
                        case 2:
                            EventBus.getDefault().post(new PullAllDoneOrdersSucceedEvent(orders));
                        case 3:
                            EventBus.getDefault().post(new PullAllOrdersSucceedEvent(orders));
                        case 4:
                            EventBus.getDefault().post(new PullCanceledOrdersSucceedEvent(orders));
                        case 5:
                            EventBus.getDefault().post(new PullFailedOrdersSucceedEvent(orders));
                    }
                }
                else{
                    switch (flag){
                        case 1:
                            EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent(result.get("message")));
                        case 2:
                            EventBus.getDefault().post(new PullAllDoneOrdersFailEvent(result.get("message")));
                        case 3:
                            EventBus.getDefault().post(new PullAllOrdersFailEvent(result.get("message")));
                        case 4:
                            EventBus.getDefault().post(new PullCanceledOrdersFailEvent(result.get("message")));
                        case 5:
                            EventBus.getDefault().post(new PullFailedOrdersFailEvent(result.get("message")));
                    }
                }
            } catch (JSONException e) {
                switch (flag){
                    case 1:
                        EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent("数据解析错误 "+jsonString));
                    case 2:
                        EventBus.getDefault().post(new PullAllDoneOrdersFailEvent("数据解析错误 "+jsonString));
                    case 3:
                        EventBus.getDefault().post(new PullAllOrdersFailEvent("数据解析错误 "+jsonString));
                    case 4:
                        EventBus.getDefault().post(new PullCanceledOrdersFailEvent("数据解析错误 "+jsonString));
                    case 5:
                        EventBus.getDefault().post(new PullFailedOrdersFailEvent("数据解析错误 "+jsonString));
                }
                e.printStackTrace();
            }catch (Exception e){
                switch (flag){
                    case 1:
                        EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent(e.getLocalizedMessage()));
                    case 2:
                        EventBus.getDefault().post(new PullAllDoneOrdersFailEvent(e.getLocalizedMessage()));
                    case 3:
                        EventBus.getDefault().post(new PullAllOrdersFailEvent(e.getLocalizedMessage()));
                    case 4:
                        EventBus.getDefault().post(new PullCanceledOrdersFailEvent(e.getLocalizedMessage()));
                    case 5:
                        EventBus.getDefault().post(new PullFailedOrdersFailEvent(e.getLocalizedMessage()));
                }
                e.printStackTrace();
            }
            synchronized (Order.this){
                pullOrdersThread =null;
            }
        }
    }




    //
    public String getUnfinishedOrdersJson() throws IOException {
        return HttpUtil.get(ServerConfig.WAY_UNFINISHED_ORDERS);
    }
    PullUnfinishedOrdersThread pullUnfinishedOrdersThread =null;

    public void beginPullUnfinishedOrders(){
        synchronized (this){
            if(pullUnfinishedOrdersThread !=null){
                return ;
            }
            pullUnfinishedOrdersThread =new PullUnfinishedOrdersThread();
            pullUnfinishedOrdersThread.start();
        }
    }
    class PullUnfinishedOrdersThread extends Thread{

        @Override
        public void run() {
            String jsonString="";
            try {
                //todo 需加上参数
                jsonString = getUnfinishedOrdersJson();
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.OrderModel> orders=JsonHelper.toOrderModelList(result.get("data"));
                    EventBus.getDefault().post(new PullUnfinishedOrdersSucceedEvent(orders));
                }
                else{
                    EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                pullUnfinishedOrdersThread =null;
            }
        }
    }

    public String getPlaceOrderJson(com.google.runda.model.PlaceOrderFormModel model) throws IllegalAccessException, IOException {
        Map<String,String> rawParams = BeanHelper.objToHash(model);
        return HttpUtil.post(ServerConfig.WAY_PLACE_ORDER, rawParams);
    }

    public void beginPlaceOrder(PlaceOrderFormModel model){

        synchronized (this){
            if(placeOrderThread !=null){
                return ;
            }
            placeOrderThread =new PlaceOrderThread();
            placeOrderThread.model=model;
            placeOrderThread.start();
        }
    }
    PlaceOrderThread placeOrderThread=null;

    class PlaceOrderThread extends Thread{
        public PlaceOrderFormModel model;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = getPlaceOrderJson(model);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    Map<String,String> data=JsonHelper.toMap(result.get("data"));
                    String orderID=data.get("orderID");
                    EventBus.getDefault().post(new PlaceOrderSucceedEvent(orderID));
                }
                else{
                    EventBus.getDefault().post(new PlaceOrderFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PlaceOrderFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PlaceOrderFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                placeOrderThread =null;
            }
        }
    }


    public String getOrderDetailJson(String orderId){
        return orderId;
    }

    PullOrderDetailThread pullOrderDetailThread =null;
    public void beginPullOrderDetail(String orderId){
        synchronized (this){
            if(pullOrderDetailThread!=null){
                pullOrderDetailThread=new PullOrderDetailThread();
                pullOrderDetailThread.orderId=orderId;
                pullOrderDetailThread.start();
            }
        }
    }

    class PullOrderDetailThread extends Thread{
        public String orderId;
        @Override
        public void run() {
            String jsonString="";
            try {
                //todo 需要修改
                jsonString = getUnfinishedOrdersJson();
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.Order> orders=JsonHelper.toOrderList(result.get("data"));
                    EventBus.getDefault().post(new PullUnfinishedOrdersSucceedEvent(orders));
                }
                else{
                    EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullUnfinishedOrdersFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                pullOrderDetailThread =null;
            }
        }
    }


    /**
     * 取消订单
     * @param orderId 订单id
     */
    public void beginCancelOrder(String orderId){
        synchronized (this){
            if(cancelOrderThread==null){
                cancelOrderThread=new CancelOrderThread();
                cancelOrderThread.orderId=orderId;
                cancelOrderThread.start();
            }
        }
    }

    CancelOrderThread cancelOrderThread=null;

    /**
     * 取消订单
     */
    class CancelOrderThread extends Thread{
        public String orderId;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = HttpUtil.get(ServerConfig.WAY_CANCEL_ORDER + "&orderid=" + orderId);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new CancelOrderSucceedEvent(result.get("message")));
                }
                else{
                    EventBus.getDefault().post(new CancelOrderFailEvent(code+" "+result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new CancelOrderFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new CancelOrderFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                cancelOrderThread =null;
            }
        }
    }


    /**
     * s删除订单
     * @param orderId 订单id
     */
    public void beginDeleteOrder(String orderId){
        synchronized (this){
            if(deleteOrderThread==null){
                deleteOrderThread=new DeleteOrderThread();
                deleteOrderThread.orderId=orderId;
                deleteOrderThread.start();
            }
        }
    }

    DeleteOrderThread deleteOrderThread=null;

    /**
     * 删除订单
     */
    class DeleteOrderThread extends Thread{
        public String orderId;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = HttpUtil.get(ServerConfig.WAY_DELETE_ORDER+"&orderid="+orderId);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new DeleteOrderSucceedEvent(result.get("message")));
                }
                else{
                    EventBus.getDefault().post(new DeleteOrderFailEvent(code+" "+result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new DeleteOrderFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new DeleteOrderFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                deleteOrderThread =null;
            }
        }
    }



    /**
     * 订单延期
     * @param orderId 订单id
     * @param time 新的收货时间
     */
    public void beginDelayOrder(String orderId,String time){
        synchronized (this){
            if(delayOrderThread==null){
                delayOrderThread=new DelayOrderThread();
                delayOrderThread.orderId=orderId;
                delayOrderThread.time=time;
                delayOrderThread.start();
            }
        }
    }

    DelayOrderThread delayOrderThread=null;

    /**
     *  订单延期
     */
    class DelayOrderThread extends Thread{
        public String orderId;
        public String time;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = HttpUtil.get(ServerConfig.WAY_DELAY_ORDER+"&orderid="+orderId+"&recieverTime="+time);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new DelayOrderSucceedEvent(result.get("message")));
                }
                else{
                    EventBus.getDefault().post(new DelayOrderFailEvent(code+" "+result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new DelayOrderFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new DelayOrderFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                delayOrderThread =null;
            }
        }
    }


    /**
     * 确认收货
     * @param orderId 订单id
     */
    public void beginEnsureOrder(String orderId){
        synchronized (this){
            if(ensureOrderThread==null){
                ensureOrderThread=new EnsureOrderThread();
                ensureOrderThread.orderId=orderId;
                ensureOrderThread.start();
            }
        }
    }

    EnsureOrderThread ensureOrderThread=null;

    /**
     *  确认收货
     */
    class EnsureOrderThread extends Thread{
        public String orderId;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = HttpUtil.get(ServerConfig.WAY_ENSURE_ORDER+"&orderid="+orderId);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new EnsureOrderSucceedEvent(result.get("message")));
                }
                else{
                    EventBus.getDefault().post(new EnsureOrderFailEvent(code+" "+result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new EnsureOrderFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new EnsureOrderFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                ensureOrderThread =null;
            }
        }
    }





    /**
     * 评价订单
     * @param orderId 订单id
     */
    public void beginCommentOrder(String orderId,String comment){
        synchronized (this){
            if(commentOrderThread==null){
                commentOrderThread=new CommentOrderThread();
                commentOrderThread.orderId=orderId;
                commentOrderThread.comment=comment;
                commentOrderThread.start();
            }
        }
    }

    CommentOrderThread commentOrderThread=null;

    /**
     *  评价订单
     */
    class CommentOrderThread extends Thread{
        public String orderId;
        public String comment;
        @Override
        public void run() {
            String jsonString="";
            try {
                Map<String,String> map =new HashMap<>();
                map.put("orderID",orderId);
                map.put("CommentContent",comment);
                jsonString = HttpUtil.post(ServerConfig.WAY_COMMENT_ORDER,map);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new CommentOrderSucceedEvent(result.get("message")));
                }
                else{
                    EventBus.getDefault().post(new CommentOrderFailEvent(code+" "+result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new CommentOrderFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new CommentOrderFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                commentOrderThread =null;
            }
        }
    }

}
