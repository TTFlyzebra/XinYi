package com.flyzebra.xinyi.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.utils.JsonUtils;
import com.flyzebra.xinyi.view.ChildGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class NewsActiviy extends BaseActivity {

    private ChildGridView childGridView;
    private List childGridViewList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setEnterTransition(new Explode());
        setContentView(R.layout.news_activity);
        childGridView = (ChildGridView) findViewById(R.id.news_cgv_01);
        childGridView.setTitleImage(R.drawable.ic_hot).setTitle("最新消息").setTitleButtonName("查看更多");
        childGridView.init(R.layout.product_item_01,
                new int[]{R.id.p_item_01_tv_01, R.id.p_item_01_tv_02},
                new String[]{IAdapter.P1_NAME, IAdapter.P1_PRICE},
                new int[]{R.id.p_item_01_iv_01},
                new String[]{IAdapter.P1_IMG_URL});

        childGridView.setColumn(1);
        childGridView.setShowImageSrc(new ChildGridView.ShowImageSrc() {
            @Override
            public void setImageSrcWithUrl(String url, ImageView iv) {
                iHttp.upImageView(NewsActiviy.this, "http://192.168.1.88/ordermeal" + url, iv);
            }
        });

        childGridViewList1 = new ArrayList();
        iHttp.getString(Constant.URL_TABLE_1, HTTPTAG, new IHttp.HttpResult() {
            @Override
            public void succeed(Object object) {
                try {
                    JsonUtils.json2List(childGridViewList1, new JSONObject(object.toString()), "mealinfo");
                    childGridViewList1.addAll(childGridViewList1);
                    childGridView.setData(childGridViewList1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Object object) {
            }
        });
    }
}
