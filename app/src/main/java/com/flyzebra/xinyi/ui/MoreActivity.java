package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/2/29.
 */
public class MoreActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_more.setImageResource(R.drawable.ic_menu_more_on);
        base_tv_more.setTextColor(getResources().getColor(R.color.menu_select_on));
    }

    @Override
    protected void onCreateAndaddView(RelativeLayout root) {

    }
}
