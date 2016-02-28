package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/2/28.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView home_bt_01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        home_bt_01 = (ImageView) findViewById(R.id.home_bt_01);
        home_bt_01.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_bt_01:
                SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                et.clear();
                et.commit();
                break;
        }
    }
}
