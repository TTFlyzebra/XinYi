package com.flyzebra.xinyi.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class UserInfoTest implements Parcelable {
    public static final Creator<UserInfoTest> CREATOR = new Creator<UserInfoTest>() {
        @Override
        public UserInfoTest createFromParcel(Parcel in) {
            return new UserInfoTest(in);
        }

        @Override
        public UserInfoTest[] newArray(int size) {
            return new UserInfoTest[size];
        }
    };
    private int userId;
    private String userName;
    private String phoneNum;
    private String password;

    public UserInfoTest(int userId, String userName, String phoneNum) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNum = phoneNum;
    }

    protected UserInfoTest(Parcel in) {
        userId = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
    }
}
