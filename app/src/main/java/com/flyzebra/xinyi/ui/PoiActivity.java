package com.flyzebra.xinyi.ui;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.MyApp;
import com.flyzebra.xinyi.R;

import java.util.List;

/**
 * Created by FlyZebra on 2016/2/29.
 */
public class PoiActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<ApplicationInfo> list;
    private PoiRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_poi.setImageResource(R.drawable.ic_menu_poi_on);
        base_tv_poi.setTextColor(getResources().getColor(R.color.menu_select_on));
    }

    @Override
    protected void onCreateAndaddView(LinearLayout root) {
        LayoutInflater lf = LayoutInflater.from(this);
        LinearLayout view = (LinearLayout) lf.inflate(R.layout.poi_view, null);
        root.addView(view);

        recyclerView = (RecyclerView) view.findViewById(R.id.poi_rv01);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = getPackageManager().getInstalledApplications(PackageManager.PERMISSION_GRANTED );
        adapter = new PoiRVAdapter(this,list,R.layout.poi_recyclerview);
        recyclerView.setAdapter(adapter);

        recyclerView.post(new Runnable() {
            @Override

            public void run() {
                recyclerView.scrollTo(MyApp.poi_rv_x,MyApp.poi_rv_y);
                Log.i(TAG," recyclerView.scrollTo(MyApp.poi_rv_x,MyApp.poi_rv_y)"+MyApp.poi_rv_x+","+MyApp.poi_rv_y);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApp.poi_rv_x = recyclerView.getScrollX();
        MyApp.poi_rv_y = recyclerView.getScrollY();
        Log.i(TAG,"PoiActitivy->onStop->recyclerView->x,y"+MyApp.poi_rv_x+","+MyApp.poi_rv_y);
    }

}
