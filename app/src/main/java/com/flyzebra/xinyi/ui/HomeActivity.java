package com.flyzebra.xinyi.ui;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/2/28.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView home_bt_01;
    private ImageView home_bt_02;
    private ImageView home_bt_03;
    private ImageView home_bt_04;
    private TextView home_tv_01;
    private TextView home_tv_02;
    private TextView home_tv_03;
    private TextView home_tv_04;
    private LinearLayout home_ll_01;
    private LinearLayout home_ll_02;
    private LinearLayout home_ll_03;
    private LinearLayout home_ll_04;
    
    private ColorStateList colorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        colorStateList=getResources().getColorStateList(R.color.color_select);
        home_bt_01 = (ImageView) findViewById(R.id.home_bt_01);
        home_bt_02 = (ImageView) findViewById(R.id.home_bt_02);
        home_bt_03 = (ImageView) findViewById(R.id.home_bt_03);
        home_bt_04 = (ImageView) findViewById(R.id.home_bt_04);
        home_tv_01 = (TextView) findViewById(R.id.home_tv_01);
        home_tv_02 = (TextView) findViewById(R.id.home_tv_02);
        home_tv_03 = (TextView) findViewById(R.id.home_tv_03);
        home_tv_04 = (TextView) findViewById(R.id.home_tv_04);
        home_ll_01 = (LinearLayout) findViewById(R.id.home_ll_01);
        home_ll_02 = (LinearLayout) findViewById(R.id.home_ll_02);
        home_ll_03 = (LinearLayout) findViewById(R.id.home_ll_03);
        home_ll_04 = (LinearLayout) findViewById(R.id.home_ll_04);
        home_ll_01.setOnClickListener(this);
        home_ll_02.setOnClickListener(this);
        home_ll_03.setOnClickListener(this);
        home_ll_04.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_ll_01:
                home_bt_01.setImageResource(R.drawable.ic_menu_deal_on);
                home_bt_02.setImageResource(R.drawable.ic_menu_poi);
                home_bt_03.setImageResource(R.drawable.ic_menu_user);
                home_bt_04.setImageResource(R.drawable.ic_menu_more);
                home_tv_01.setTextColor(getResources().getColor(R.color.menu_select_on));
                home_tv_02.setTextColor(colorStateList);
                home_tv_03.setTextColor(colorStateList);
                home_tv_04.setTextColor(colorStateList);
                break;
            case R.id.home_ll_02:
                home_bt_01.setImageResource(R.drawable.ic_menu_deal);
                home_bt_02.setImageResource(R.drawable.ic_menu_poi_on);
                home_bt_03.setImageResource(R.drawable.ic_menu_user);
                home_bt_04.setImageResource(R.drawable.ic_menu_more);
                home_tv_01.setTextColor(colorStateList);
                home_tv_02.setTextColor(getResources().getColor(R.color.menu_select_on));
                home_tv_03.setTextColor(colorStateList);
                home_tv_04.setTextColor(colorStateList);
                break;
            case R.id.home_ll_03:
                home_bt_01.setImageResource(R.drawable.ic_menu_deal);
                home_bt_02.setImageResource(R.drawable.ic_menu_poi);
                home_bt_03.setImageResource(R.drawable.ic_menu_user_on);
                home_bt_04.setImageResource(R.drawable.ic_menu_more);               
                home_tv_01.setTextColor(colorStateList);
                home_tv_02.setTextColor(colorStateList);
                home_tv_03.setTextColor(getResources().getColor(R.color.menu_select_on));
                home_tv_04.setTextColor(colorStateList);
                break;
            case R.id.home_ll_04:
                home_bt_01.setImageResource(R.drawable.ic_menu_deal);
                home_bt_02.setImageResource(R.drawable.ic_menu_poi);
                home_bt_03.setImageResource(R.drawable.ic_menu_user);
                home_bt_04.setImageResource(R.drawable.ic_menu_more_on);
                home_tv_01.setTextColor(colorStateList);
                home_tv_02.setTextColor(colorStateList);
                home_tv_03.setTextColor(colorStateList);
                home_tv_04.setTextColor(getResources().getColor(R.color.menu_select_on));
                break;
        }
    }
}
