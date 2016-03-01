package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;

/**
 * Created by FlyZebra on 2016/2/28.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected ImageView base_bt_01;
    protected ImageView base_bt_02;
    protected ImageView base_bt_03;
    protected ImageView base_bt_04;
    protected TextView base_tv_01;
    protected TextView base_tv_02;
    protected TextView base_tv_03;
    protected TextView base_tv_04;
    protected ColorStateList colorStateList;

    private LinearLayout base_ll_01;
    private LinearLayout base_ll_02;
    private LinearLayout base_ll_03;
    private LinearLayout base_ll_04;
    private LinearLayout base_add_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        colorStateList = getResources().getColorStateList(R.color.color_select);
        base_bt_01 = (ImageView) findViewById(R.id.base_bt_01);
        base_bt_02 = (ImageView) findViewById(R.id.base_bt_02);
        base_bt_03 = (ImageView) findViewById(R.id.base_bt_03);
        base_bt_04 = (ImageView) findViewById(R.id.base_bt_04);
        base_tv_01 = (TextView) findViewById(R.id.base_tv_01);
        base_tv_02 = (TextView) findViewById(R.id.base_tv_02);
        base_tv_03 = (TextView) findViewById(R.id.base_tv_03);
        base_tv_04 = (TextView) findViewById(R.id.base_tv_04);
        base_ll_01 = (LinearLayout) findViewById(R.id.base_ll_01);
        base_ll_02 = (LinearLayout) findViewById(R.id.base_ll_02);
        base_ll_03 = (LinearLayout) findViewById(R.id.base_ll_03);
        base_ll_04 = (LinearLayout) findViewById(R.id.base_ll_04);
        base_ll_01.setOnClickListener(this);
        base_ll_02.setOnClickListener(this);
        base_ll_03.setOnClickListener(this);
        base_ll_04.setOnClickListener(this);

        base_add_view = (LinearLayout) findViewById(R.id.base_add_view);
        onCreateAndaddView(base_add_view);
    }

    /**
     * 在此函数中初始化控件
     * @param root
     */
    protected abstract void onCreateAndaddView(LinearLayout root);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_ll_01:
                base_bt_01.setImageResource(R.drawable.ic_menu_deal_on);
                base_bt_02.setImageResource(R.drawable.ic_menu_poi);
                base_bt_03.setImageResource(R.drawable.ic_menu_user);
                base_bt_04.setImageResource(R.drawable.ic_menu_more);
                base_tv_01.setTextColor(getResources().getColor(R.color.menu_select_on));
                base_tv_02.setTextColor(colorStateList);
                base_tv_03.setTextColor(colorStateList);
                base_tv_04.setTextColor(colorStateList);
                startActivity(new Intent(BaseActivity.this, HomeAcitivy.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.base_ll_02:
                base_bt_01.setImageResource(R.drawable.ic_menu_deal);
                base_bt_02.setImageResource(R.drawable.ic_menu_poi_on);
                base_bt_03.setImageResource(R.drawable.ic_menu_user);
                base_bt_04.setImageResource(R.drawable.ic_menu_more);
                base_tv_01.setTextColor(colorStateList);
                base_tv_02.setTextColor(getResources().getColor(R.color.menu_select_on));
                base_tv_03.setTextColor(colorStateList);
                base_tv_04.setTextColor(colorStateList);
                startActivity(new Intent(BaseActivity.this, PoiActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.base_ll_03:
                base_bt_01.setImageResource(R.drawable.ic_menu_deal);
                base_bt_02.setImageResource(R.drawable.ic_menu_poi);
                base_bt_03.setImageResource(R.drawable.ic_menu_user_on);
                base_bt_04.setImageResource(R.drawable.ic_menu_more);
                base_tv_01.setTextColor(colorStateList);
                base_tv_02.setTextColor(colorStateList);
                base_tv_03.setTextColor(getResources().getColor(R.color.menu_select_on));
                base_tv_04.setTextColor(colorStateList);
                startActivity(new Intent(BaseActivity.this, UserActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.base_ll_04:
                base_bt_01.setImageResource(R.drawable.ic_menu_deal);
                base_bt_02.setImageResource(R.drawable.ic_menu_poi);
                base_bt_03.setImageResource(R.drawable.ic_menu_user);
                base_bt_04.setImageResource(R.drawable.ic_menu_more_on);
                base_tv_01.setTextColor(colorStateList);
                base_tv_02.setTextColor(colorStateList);
                base_tv_03.setTextColor(colorStateList);
                base_tv_04.setTextColor(getResources().getColor(R.color.menu_select_on));
                startActivity(new Intent(BaseActivity.this, MoreActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
        }
    }
}
