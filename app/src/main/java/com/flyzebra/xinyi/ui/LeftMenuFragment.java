package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.utils.DisplayUtils;
import com.flyzebra.xinyi.view.StarLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FlyZebra on 2016/3/18.
 */
public class LeftMenuFragment extends Fragment implements View.OnClickListener {
    private MainActivity activity;
    private ImageView left_drawer_iv_01;
    private TextView left_drawer_tv_01;
    private StarLevel left_drawer_starlevel;

    private int[] ResID = {R.id.left_drawer_menu_news, R.id.left_drawer_menu_order, R.id.left_drawer_menu_favorite,
            R.id.left_drawer_menu_history,/* R.id.left_drawer_menu_image_switch, R.id.left_drawer_menu_day_switch,*/
            R.id.left_drawer_menu_delete, R.id.left_drawer_menu_exit};
    private List<LinearLayout> listMenu = new ArrayList<>();

    public LeftMenuFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.left_drawer_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        left_drawer_iv_01 = (ImageView) view.findViewById(R.id.left_drawer_iv_01);
        left_drawer_tv_01 = (TextView) view.findViewById(R.id.left_drawer_tv_01);
        left_drawer_starlevel = (StarLevel) view.findViewById(R.id.left_drawer_starlevel);

        UserInfo userInfo = activity.userInfo;
        if (userInfo != null) {
            String name = userInfo.getUserName();
            String url = userInfo.getUserPhotoUrl();
            if (name != null) {
                left_drawer_tv_01.setText(userInfo.getUserName());
            }
            if (url != null) {
                activity.iHttp.upImageView(activity, userInfo.getUserPhotoUrl(), left_drawer_iv_01);
            }
            int size = DisplayUtils.dip2px(activity, 24);
            left_drawer_starlevel.setStarSize(size, size);
            left_drawer_starlevel.setLevel(9);
        }

        for (int i = 0; i < ResID.length; i++) {
            listMenu.add((LinearLayout) view.findViewById(ResID[i]));
            listMenu.get(i).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_drawer_menu_news:
                StartActivity(NoDevelopActivity.class);
                break;
            case R.id.left_drawer_menu_order:
                StartActivity(NoDevelopActivity.class);
                break;
            case R.id.left_drawer_menu_favorite:
                StartActivity(NoDevelopActivity.class);
                break;
            case R.id.left_drawer_menu_history:
                StartActivity(NoDevelopActivity.class);
                break;
            case R.id.left_drawer_menu_exit:
                activity.finish();
                break;
        }
    }


    private void StartActivity(Class cls) {
        Intent intent = new Intent(activity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
