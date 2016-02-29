package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/2/29.
 */
public class UserActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        base_bt_01.setImageResource(R.drawable.ic_menu_deal);
//        base_bt_02.setImageResource(R.drawable.ic_menu_poi);
        base_bt_03.setImageResource(R.drawable.ic_menu_user_on);
//        base_bt_04.setImageResource(R.drawable.ic_menu_more);
//        base_tv_01.setTextColor(colorStateList);
//        base_tv_02.setTextColor(colorStateList);
        base_tv_03.setTextColor(getResources().getColor(R.color.menu_select_on));
//        base_tv_04.setTextColor(colorStateList);
    }

    @Override
    protected void addView(LinearLayout root) {
        LayoutInflater lf = LayoutInflater.from(this);
        LinearLayout ll = (LinearLayout) lf.inflate(R.layout.user_view, null);
        root.addView(ll);
    }
}
