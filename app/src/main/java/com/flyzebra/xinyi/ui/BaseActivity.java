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
    public final String TAG = "com.flyzebra";
    protected ImageView base_bt_home;
    protected ImageView base_bt_poi;
    protected ImageView base_bt_user;
    protected ImageView base_bt_more;
    protected TextView base_tv_home;
    protected TextView base_tv_poi;
    protected TextView base_tv_user;
    protected TextView base_tv_more;
    protected ColorStateList colorStateList;

    private LinearLayout base_ll_home;
    private LinearLayout base_ll_poi;
    private LinearLayout base_ll_user;
    private LinearLayout base_ll_more;

    private LinearLayout base_add_view;

    //购物车
    protected ImageView base_bt_buy;
    protected TextView base_tv_buy;
    private LinearLayout base_ll_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        colorStateList = getResources().getColorStateList(R.color.color_select);
        base_bt_home = (ImageView) findViewById(R.id.base_bt_home);
        base_bt_poi = (ImageView) findViewById(R.id.base_bt_poi);
        base_bt_user = (ImageView) findViewById(R.id.base_bt_user);
        base_bt_more = (ImageView) findViewById(R.id.base_bt_more);
        base_tv_home = (TextView) findViewById(R.id.base_tv_home);
        base_tv_poi = (TextView) findViewById(R.id.base_tv_poi);
        base_tv_user = (TextView) findViewById(R.id.base_tv_user);
        base_tv_more = (TextView) findViewById(R.id.base_tv_more);
        base_ll_home = (LinearLayout) findViewById(R.id.base_ll_home);
        base_ll_poi = (LinearLayout) findViewById(R.id.base_ll_poi);
        base_ll_user = (LinearLayout) findViewById(R.id.base_ll_user);
        base_ll_more = (LinearLayout) findViewById(R.id.base_ll_more);
        base_ll_home.setOnClickListener(this);
        base_ll_poi.setOnClickListener(this);
        base_ll_user.setOnClickListener(this);
        base_ll_more.setOnClickListener(this);

        base_bt_buy = (ImageView) findViewById(R.id.base_bt_buy);
        base_tv_buy = (TextView) findViewById(R.id.base_tv_buy);
        base_ll_buy = (LinearLayout) findViewById(R.id.base_ll_buy);
        base_ll_buy.setOnClickListener(this);

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
            case R.id.base_ll_home:
                base_bt_home.setImageResource(R.drawable.ic_menu_deal_on);
                base_bt_poi.setImageResource(R.drawable.ic_menu_poi);
                base_bt_user.setImageResource(R.drawable.ic_menu_user);
                base_bt_more.setImageResource(R.drawable.ic_menu_more);
                base_tv_home.setTextColor(getResources().getColor(R.color.menu_select_on));
                base_tv_poi.setTextColor(colorStateList);
                base_tv_user.setTextColor(colorStateList);
                base_tv_more.setTextColor(colorStateList);
                base_bt_buy.setImageResource(R.drawable.ic_menu_buy);
                base_tv_buy.setTextColor(colorStateList);
                startActivity(new Intent(BaseActivity.this, HomeAcitivy.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.base_ll_poi:
                base_bt_home.setImageResource(R.drawable.ic_menu_deal);
                base_bt_poi.setImageResource(R.drawable.ic_menu_poi_on);
                base_bt_user.setImageResource(R.drawable.ic_menu_user);
                base_bt_more.setImageResource(R.drawable.ic_menu_more);
                base_tv_home.setTextColor(colorStateList);
                base_tv_poi.setTextColor(getResources().getColor(R.color.menu_select_on));
                base_tv_user.setTextColor(colorStateList);
                base_tv_more.setTextColor(colorStateList);
                base_bt_buy.setImageResource(R.drawable.ic_menu_buy);
                base_tv_buy.setTextColor(colorStateList);
                startActivity(new Intent(BaseActivity.this, PoiActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.base_ll_user:
                base_bt_home.setImageResource(R.drawable.ic_menu_deal);
                base_bt_poi.setImageResource(R.drawable.ic_menu_poi);
                base_bt_user.setImageResource(R.drawable.ic_menu_user_on);
                base_bt_more.setImageResource(R.drawable.ic_menu_more);
                base_tv_home.setTextColor(colorStateList);
                base_tv_poi.setTextColor(colorStateList);
                base_tv_user.setTextColor(getResources().getColor(R.color.menu_select_on));
                base_tv_more.setTextColor(colorStateList);
                base_bt_buy.setImageResource(R.drawable.ic_menu_buy);
                base_tv_buy.setTextColor(colorStateList);
                startActivity(new Intent(BaseActivity.this, UserActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.base_ll_more:
                base_bt_home.setImageResource(R.drawable.ic_menu_deal);
                base_bt_poi.setImageResource(R.drawable.ic_menu_poi);
                base_bt_user.setImageResource(R.drawable.ic_menu_user);
                base_bt_more.setImageResource(R.drawable.ic_menu_more_on);
                base_tv_home.setTextColor(colorStateList);
                base_tv_poi.setTextColor(colorStateList);
                base_tv_user.setTextColor(colorStateList);
                base_bt_buy.setImageResource(R.drawable.ic_menu_buy);
                base_tv_buy.setTextColor(colorStateList);
                base_tv_more.setTextColor(getResources().getColor(R.color.menu_select_on));
                startActivity(new Intent(BaseActivity.this, MoreActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.base_ll_buy:
                base_bt_home.setImageResource(R.drawable.ic_menu_deal);
                base_bt_poi.setImageResource(R.drawable.ic_menu_poi);
                base_bt_user.setImageResource(R.drawable.ic_menu_user);
                base_bt_more.setImageResource(R.drawable.ic_menu_more);
                base_tv_home.setTextColor(colorStateList);
                base_tv_poi.setTextColor(colorStateList);
                base_tv_user.setTextColor(colorStateList);
                base_bt_buy.setImageResource(R.drawable.ic_menu_buy_on);
                base_tv_buy.setTextColor(colorStateList);
                base_tv_more.setTextColor(getResources().getColor(R.color.menu_select_on));
                startActivity(new Intent(BaseActivity.this, BuyActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
        }
    }
}
