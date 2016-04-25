package com.flyzebra.xinyi.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.flyzebra.xinyi.utils.DiskUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/2/28.
 */
public class UserInfo implements Serializable {
    private int id;
    private String loginname;
    private String nickname;
    private String loginword;
    private String phone;
    private String openid;
    private String loginwith;
    private String pictureurl;
    private String address;
    private int level;
    private int logintime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginword() {
        return loginword;
    }

    public void setLoginword(String loginword) {
        this.loginword = loginword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getLoginwith() {
        return loginwith;
    }

    public void setLoginwith(String loginwith) {
        this.loginwith = loginwith;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLogintime() {
        return logintime;
    }

    public void setLogintime(int logintime) {
        this.logintime = logintime;
    }

    public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<>();
        if(loginname!=null){
            map.put("loginname",loginname);
        }
        if(nickname!=null){
            map.put("nickname",nickname);
        }
        if(loginword!=null){
            map.put("loginword",loginword);
        }
        if(phone!=null){
            map.put("phone",phone);
        }
        if(openid!=null){
            map.put("openid",openid);
        }
        if(loginwith!=null){
            map.put("loginwith",loginwith);
        }
        if(pictureurl!=null){
            map.put("pictureurl",pictureurl);
        }
        if(address!=null){
            map.put("address",address);
        }
        return map;
    }
    /**
     * 获取本机保存的登陆帐户信息
     */
    public static UserInfo getLocalUserInfo(Context context) {
        SharedPreferences sp= context.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        int id = sp.getInt("id",-1);
        if(id<=0) return null;
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setNickname(sp.getString("nickname", "xinyi"));
        userInfo.setLevel(sp.getInt("level", 1));
        userInfo.setLogintime(sp.getInt("logintime", 0));
        userInfo.setPictureurl(sp.getString("pictureurl", null));
        return userInfo;
    }

    /**
     * 保存登陆信息到本地
     */
    public void saveLocalUserInfo(Context context) {
        DiskUtils.saveData(context, "userinfo",
                new String[]{"id", "nickname", "level", "logintime", "pictureurl"},
                new Object[]{id, nickname, level, logintime, pictureurl});
    }
}
