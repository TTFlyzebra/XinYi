package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.TestHttp;
import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.view.GridViewForScrollView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/7.
 */
public class NewsActiviy extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        //--GirdView处理部分
        List<Map<String, Object>> gridview_list = TestHttp.getHotsellsList(); //从HTTP服务器获取GridView显示的数据内容
        GridViewForScrollView gridview = (GridViewForScrollView) findViewById(R.id.news_gv01);
        TvIvAdapter homeGridViewAdapter = new TvIvAdapter(this, gridview_list, R.layout.home_gridview,
                new int[]{R.id.tv01, R.id.tv02},
                new String[]{"name", "price"},
                new int[]{R.id.iv01},
                new String[]{"imagepath"},
                null, new TvIvAdapter.SetImageView() {
            @Override
            public void setImageView(String url, ImageView iv) {
                MyVolley.upImageView(url, iv);
            }
        });
        gridview.setAdapter(homeGridViewAdapter);
    }
}
