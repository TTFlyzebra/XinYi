package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.HttpUpdata;
import com.flyzebra.xinyi.model.IHttpUpdata;
import com.flyzebra.xinyi.universal.TvIvAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/22.
 */
public class BuyActivity extends AppCompatActivity{
    private IHttpUpdata iHttpUpdata = HttpUpdata.getInstance();
    private List<Map<String,Object>> list;
    private ListView listView;
    private TvIvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_view);
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
        iHttpUpdata.upListView("http://192.168.1.88/ordermeal/table.jsp?get=mealinfo", list, "mealinfo",adapter);
    }
}


