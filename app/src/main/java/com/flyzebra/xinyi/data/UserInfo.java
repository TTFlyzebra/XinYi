package com.flyzebra.xinyi.data;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by FlyZebra on 2016/2/28.
 */
public class UserInfo implements Serializable {
    private String userName;
    private String userPhone;
    private String userOpenID;
    private Bitmap userBitmap;
    private String userPhotoUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserOpenID() {
        return userOpenID;
    }

    public void setUserOpenID(String userOpenID) {
        this.userOpenID = userOpenID;
    }

    public Bitmap getUserBitmap() {
        return userBitmap;
    }

    public void setUserBitmap(Bitmap userBitmap) {
        this.userBitmap = userBitmap;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }
}
