package com.flyzebra.xinyi.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/2/28.
 */
public class HomeActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.home_activity);
    }
}
