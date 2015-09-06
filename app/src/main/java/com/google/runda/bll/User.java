package com.google.runda.bll;

import android.util.*;
import android.util.Log;

import com.google.runda.event.CheckCheckcodeFailEvent;
import com.google.runda.event.CheckCheckcodeSucceedEvent;
import com.google.runda.event.CheckPhoneFailEvent;
import com.google.runda.event.CheckPhoneSucceedEvent;
import com.google.runda.event.LoginFailEvent;
import com.google.runda.event.LoginSuccessEvent;
import com.google.runda.event.PullUserDataFailEvent;
import com.google.runda.event.PullUserDataSucceedEvent;
import com.google.runda.event.RegisterFailEvent;
import com.google.runda.event.RegisterSucceedEvent;
import com.google.runda.event.RequestLoginOverEvent;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by wukai on 2015/4/16.
 */
public class User {
    private com.google.runda.dal.User dal = new com.google.runda.dal.User();

    /**
     * 获取字符串验证码
     * @return
     * @throws IOException
     */
    private String getCheckCodeString() throws IOException {
        String checkCode = dal.getCheckCodeString(ServerConfig.WAY_CHECK_CODE_STRING);
        return checkCode;
    }

/*登陆 - - 开始*/
    /**
     *  登陆
     * @param loginName 登陆账号 - -用户名 或 电话号码
     * @param password  密码
     * @param checkCode 验证码
     * @param loginType 登陆类型 - - userName 或 phoneNumber
     * @return json字符串
     */
    public String login(String loginName, String password, String checkCode,String loginType) throws IOException {
        Map<String, String> rawParams = new HashMap<String, String>();
        rawParams.put("passWord", password);
        rawParams.put("checkCode", checkCode);
        if(loginType.equals("userName")){
            rawParams.put("userName", loginName);
            return dal.post(ServerConfig.WAY_LOGIN_ROUTE_USERNAME, rawParams);
        }else{
            rawParams.put("phoneNumber", loginName);
            return dal.post(ServerConfig.WAY_LOGIN_ROUTE_PHONE,rawParams);
        }
    }

    LoginThread loginThread = null;

    public void beginLogin(String loginName, String password, String checkCode,String loginType) {
        synchronized (this) {
            if (loginThread != null) {
                return;
            }
            loginThread = new LoginThread();
            loginThread.loginName = loginName;
            loginThread.password = password;
            loginThread.checkCode = checkCode;
            loginThread.loginType=loginType;
            loginThread.start();
        }
    }

    class LoginThread extends Thread {
        public String loginName;
        public String password;
        public String checkCode;
        public String loginType;
        @Override
        public void run() {
            com.google.runda.bll.Config bll = new Config();
            try {
                String jsonString = login(loginName, password, checkCode,loginType);
                Log.e("jsonString", jsonString + " ");
                Map<String, String> result = JsonHelper.toMap(jsonString);
                if (result.get("code").equals("200")) {
                    com.google.runda.model.Config model = bll.getDefaultConfig();
                    if(loginType.equals("userName")){
                        model.userName = loginName;
                    }else{
                        model.phone=loginName;
                    }
                    model.password = password;
                    bll.setConfig(model);
                    EventBus.getDefault().post(new LoginSuccessEvent());
                }else{
                    bll.setDefault();
                    EventBus.getDefault().post(new LoginFailEvent(result.get("message")));
                }
            } catch (Exception e) {
                bll.setDefault();
                EventBus.getDefault().post(new LoginFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (User.this) {
                loginThread = null;
            }
        }
    }
/*登陆 - - 结束*/

/*加载用户信息 - - 开始*/
    public String pullUserInfo() throws IOException {
        return dal.get(ServerConfig.WAY_USER_INFO);
    }

    PullUserInfoThread pullUserInfoThread = null;

    public void beginPullUserInfo(String loginType) {
        synchronized (this) {
            if (pullUserInfoThread != null) {
                return;
            }
            pullUserInfoThread = new PullUserInfoThread();
            pullUserInfoThread.loginType=loginType;
            pullUserInfoThread.start();
        }
    }

    class PullUserInfoThread extends Thread {
        public String loginType;
        @Override
        public void run() {
            try {
                com.google.runda.bll.Config bllConfig = new Config();
                String loginName="";
                if(loginType.equals("userName")){
                    loginName = bllConfig.getSetting("name");
                }else {
                    loginName=bllConfig.getSetting("phone");
                }
                String password = bllConfig.getSetting("password");
                String checkCode = getCheckCodeString();
                String jsonString = login(loginName, password, checkCode,loginType);
                Map<String, String> result = JsonHelper.toMap(jsonString);
                if (result.get("code").equals("200")) {
                    String jsonUser = pullUserInfo();
                    Log.e("jsonUser", jsonUser + " ");
                    Map<String, String> detail = JsonHelper.toMap(jsonUser);
                    Map<String, String> data = JsonHelper.toMap(detail.get("data"));
                    com.google.runda.model.User model = new com.google.runda.model.User();
                    model.ID = Integer.parseInt(data.get("id"));
                    model.Name = data.get("userName");
                    model.NickName = data.get("nickName");
                    model.Sex = data.get("sex");
                    model.RealName = data.get("realName");
                    model.HeadImgUrl = data.get("photo");
                    model.CellPhone = data.get("phoneNumber");
                    model.Email = data.get("email");
                    model.RoleID = Integer.parseInt(data.get("role"));
                    model.IsLock = Boolean.parseBoolean(data.get("isLock"));
                    model.Province = data.get("province");
                    model.City = data.get("city");
                    model.County = data.get("country");
                    model.DetailAddress = data.get("detailAddress");
                    ServerConfig.USER = model;
                    //发送加载成功事件
                    EventBus.getDefault().post(new PullUserDataSucceedEvent());
                } else {
                    ServerConfig.USER=new com.google.runda.model.User();
                    //发送加载失败事件
                    EventBus.getDefault().post(new PullUserDataFailEvent(result.get("message")));
                }
                Log.e("jsonString", jsonString + " ");

            } catch (Exception e) {
                ServerConfig.USER=new com.google.runda.model.User();
                //发送加载失败事件
                EventBus.getDefault().post(new PullUserDataFailEvent(e.getLocalizedMessage()));
            }
            synchronized (User.this) {
                pullUserInfoThread = null;
            }
        }
    }
/*加载用户信息 - - 结束*/

/*注册 - - 开始*/
    public String register(com.google.runda.model.User user) throws IOException {
        Map<String, String> rawParams = new HashMap<String, String>();
        rawParams.put("userName", user.Name);
        rawParams.put("passWord", user.Password);
        rawParams.put("checkCode", user.CheckCode);
        rawParams.put("sex", user.Sex);
        rawParams.put("phoneNumber", user.CellPhone);
        rawParams.put("province", user.Province);
        rawParams.put("city", user.City);
        rawParams.put("country", user.County);
        rawParams.put("detailAddress", user.DetailAddress);
        rawParams.put("nickName","");
        rawParams.put("realName","");
        rawParams.put("email","");
        return dal.post(ServerConfig.WAY_USER_REGISTER, rawParams);

    }

    RegisterThread registerThread=null;
    public void beginRegister(com.google.runda.model.User user) {
        synchronized (this){
            if(registerThread!=null){
                return;
            }
            registerThread=new RegisterThread();
            registerThread.user=user;
            registerThread.start();
        }
    }

    class RegisterThread extends Thread {
        public com.google.runda.model.User user;

        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = register(user);
                Log.e("register jsonString",""+jsonString);
                Map<String,String> result=JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    com.google.runda.bll.Config bllConfig=new Config();
                    bllConfig.setSetting("phone",ServerConfig.USER.CellPhone);
                    bllConfig.setSetting("password",ServerConfig.USER.Password);
                    bllConfig.save();
                    EventBus.getDefault().post(new RegisterSucceedEvent());
                }
                else{
                    EventBus.getDefault().post(new RegisterFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new RegisterFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new RegisterFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }

            synchronized (User.this){
                registerThread=null;
            }
        }
    }

/*注册 - - 结束*/

/*验证手机号 - - 开始*/
    public String checkPhone(String phone) throws IOException {
        return dal.get(ServerConfig.WAY_CHECK_PHONE+phone);
    }
    CheckPhoneThread checkPhoneThread=null;
    public void beginCheckPhone(String phone){
        synchronized (this){
            if(checkPhoneThread!=null){
                return;
            }
            checkPhoneThread=new CheckPhoneThread();
            checkPhoneThread.phone=phone;
            checkPhoneThread.start();
        }
    }
    class CheckPhoneThread extends Thread{
        public String phone;

        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = checkPhone(phone);
                Log.e("check_phone jsonString",""+jsonString);
                Map<String,String> result=JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new CheckPhoneSucceedEvent());
                }
                else{
                    EventBus.getDefault().post(new CheckPhoneFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new CheckPhoneFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new CheckPhoneFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (User.this){
                checkPhoneThread=null;
            }
        }
    }
/*验证手机号 - - 结束*/

/*验证验证码 - - 开始*/
    public String checkCheckcode(String checkcode) throws IOException {
        return dal.get(ServerConfig.WAY_CHECK_CHECKCODE+checkcode);
    }

    CheckCheckcodeThread checkCheckcodeThread=null;
    public void beginCheckCheckcode(String checkcode){
        synchronized (this){
            if(checkCheckcodeThread!=null){
                return;
            }
            checkCheckcodeThread=new CheckCheckcodeThread();
            checkCheckcodeThread.checkcode=checkcode;
            checkCheckcodeThread.start();
        }
    }
    class CheckCheckcodeThread extends Thread{
        public String checkcode;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = checkCheckcode(checkcode);
                Log.e("chcheckcode jsonString",""+jsonString);
                Map<String,String> result=JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new CheckCheckcodeSucceedEvent());
                }
                else{
                    EventBus.getDefault().post(new CheckCheckcodeFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new CheckCheckcodeFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new CheckCheckcodeFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (User.this){
                checkCheckcodeThread=null;
            }
        }
    }
/*验证验证码 - - 结束*/
}
