package com.flyzebra.xinyi.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/13.
 */
public class User implements Serializable {
    private static final long serializable = 1L;
    private int userId;
    private String userName;
    private String phoneNum;
    private String password;

    public User(int userId, String userName, String phoneNum) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNum = phoneNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    private void readObject(ObjectInputStream in) {
        try {
            ObjectInputStream.GetField readFields = in.readFields();
            password = (String) readFields.get("password", "");
            password = "pass";//模拟解密,需要获得本地的密钥
            userId=readFields.get("userId",0);
            phoneNum= (String) readFields.get("phoneNum","");
            userName = (String) readFields.get("userName","");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void writeObject(ObjectOutputStream out) {
        try {
            ObjectOutputStream.PutField putFields = out.putFields();
            password = "encryption";//模拟加密
            putFields.put("password", password);
            putFields.put("userId",userId);
            putFields.put("phoneNum",phoneNum);
            putFields.put("userName",userName);
            System.out.println("加密后的密码" + password);
            out.writeFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
