package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;

/**
 * Created by FlyZebra on 2016/2/29.
 */
public class BuyActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_buy.setImageResource(R.drawable.ic_menu_buy_on);
        base_tv_buy.setTextColor(getResources().getColor(R.color.menu_select_on));
    }

    @Override
    protected void onCreateAndaddView(LinearLayout root) {

    }
}
