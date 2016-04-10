package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.utils.ResUtils;
import com.flyzebra.xinyi.view.Play3DImages;

import java.util.List;
import java.util.Map;


/**
 * by FlyZebra on 2016/3/27.
 */
public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_activity);
        Play3DImages paly = (Play3DImages) findViewById(R.id.wel_play3d);
        List<Map<String, Object>> list = TestHttp.getViewPagerList1();
        list.addAll(list);
        list.addAll(list);
        String imageArray[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            imageArray[i] = new String((String) list.get(i).get(IAdapter.P2_IMG_URL));
        }
        paly.setImageUrlArray(imageArray, ResUtils.getMetrices(this).widthPixels / 7).setDelayTime(2000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void goMain() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
