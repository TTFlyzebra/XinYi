package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/2/29.
 */
public class UserActivity extends BaseActivity {
    private Button bt_clear_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_03.setImageResource(R.drawable.ic_menu_user_on);
        base_tv_03.setTextColor(getResources().getColor(R.color.menu_select_on));
    }

    @Override
    protected void onCreateAndaddView(LinearLayout root) {
        LayoutInflater lf = LayoutInflater.from(this);
        LinearLayout ll = (LinearLayout) lf.inflate(R.layout.user_view, null);
        root.addView(ll);
        bt_clear_user = (Button) ll.findViewById(R.id.clear_user);
        bt_clear_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
            }
        });
    }
}