package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.fly.TvIvAdapter;
import com.flyzebra.xinyi.model.Http;
import com.flyzebra.xinyi.model.IHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/22.
 */

public class BuyActivity extends BaseActivity{
    private IHttp iHttpUpdata = Http.getInstance();
    private List<Map<String,Object>> list;
    private ListView listView;
    private TvIvAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base_bt_buy.setImageResource(R.drawable.ic_menu_buy_on);
        base_tv_buy.setTextColor(getResources().getColor(R.color.menu_select_on));
    }

    @Override
    protected void onCreateAndaddView(RelativeLayout root) {
        LayoutInflater lf = LayoutInflater.from(this);
        RelativeLayout view = (RelativeLayout) lf.inflate(R.layout.buy_fragment, null);
        root.addView(view);
        listView = (ListView) findViewById(R.id.buy_lv_01);
        if(list==null){
            list = new ArrayList<Map<String,Object>>();
        }else{
            list.clear();
        }
        adapter = new TvIvAdapter(this, list, R.layout.home_listview,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"mealname", "mealprice"},
                new int[]{R.id.iv01},
                new String[]{"mealimage"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                iHttpUpdata.upImageView(BuyActivity.this,"http://192.168.1.88/ordermeal" + url, iv);
            }
        });
        listView.setAdapter(adapter);
        iHttpUpdata.upListView("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo", list, "mealinfo", adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        iHttpUpdata.cancelAll("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo");
    }

}
