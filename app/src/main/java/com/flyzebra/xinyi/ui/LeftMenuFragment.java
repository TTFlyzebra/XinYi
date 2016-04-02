package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.UserInfo;
import com.flyzebra.xinyi.utils.ResUtils;

/**
 * Created by FlyZebra on 2016/3/18.
 */
public class LeftMenuFragment extends Fragment{
    private String[] menuarr;
    private ListView listview;
    private MainActitity activity;
    private ImageView left_drawer_iv_01;
    private TextView left_drawer_tv_01;
    public LeftMenuFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActitity) getActivity();
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
        }
        menuarr = ResUtils.getStringArray(activity, R.array.left_drawer);
        listview = (ListView) view.findViewById(R.id.left_drawer_lv_01);
        listview.setAdapter(new ArrayAdapter<String>(activity, R.layout.left_drawer_listview_item, R.id.tv_01, menuarr));
    }
}
