package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flyzebra.xinyi.R;

/**
 * Created by Administrator on 2016/3/18.
 */
public class LeftMenuFragment extends Fragment{
    private String[] str_arrs;
    private ListView listview;
    private MainActitity activity;
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
        str_arrs = getResources().getStringArray(R.array.left_drawer);
        listview = (ListView) view.findViewById(R.id.left_drawer_lv_01);
        listview.setAdapter(new ArrayAdapter<String>(activity, R.layout.left_drawer_listview_item, R.id.tv_01, str_arrs));
    }
}
