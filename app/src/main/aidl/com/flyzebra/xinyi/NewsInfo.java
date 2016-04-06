package com.flyzebra.xinyi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by FlyZebra on 2016/4/6.
 */
public class NewsInfo implements Parcelable {
    public static final Creator<NewsInfo> CREATOR = new Creator<NewsInfo>() {
        @Override
        public NewsInfo createFromParcel(Parcel in) {
            return new NewsInfo(in);
        }

        @Override
        public NewsInfo[] newArray(int size) {
            return new NewsInfo[size];
        }
    };
    private String newsString;

    protected NewsInfo(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
