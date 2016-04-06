package com.flyzebra.xinyi.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.flyzebra.xinyi.IAidlManager;

/**
 * Created by FlyZebra on 2016/4/6.
 */
public class MainService extends Service {
    private Binder mBinder = new IAidlManager.Stub() {
        @Override
        public void getNews(String news) throws RemoteException {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
