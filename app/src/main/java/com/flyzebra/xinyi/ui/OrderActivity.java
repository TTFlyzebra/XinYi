package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.view.ChildGridView;

/**
 * Created by Administrator on 2016/4/7.
 */
public class OrderActivity extends AppCompatActivity {
    private LinearLayout order_ll_01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(new Slide());
        setContentView(R.layout.order_activity);
//        ChildGridView childGridView = (ChildGridView) findViewById(R.id.order_cgv_01);
//        childGridView.setData(TestHttp.getViewPagerList());
        ChildGridView childGridView = new ChildGridView(this);
        order_ll_01 = (LinearLayout) findViewById(R.id.order_ll_01);
        order_ll_01.addView(childGridView);
        childGridView.setData(TestHttp.getViewPagerList());

    }
}
