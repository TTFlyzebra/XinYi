package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.Constant;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.utils.JsonUtils;
import com.flyzebra.xinyi.utils.ResUtils;
import com.flyzebra.xinyi.view.Play3DImages;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FlyZebra on 2016/4/8.
 */
public class ProductInfoActivity extends BaseActivity {
    public static final String IMG_URL = "BITMAP";
    public static final String TEXT = "TEXT";
    @Bind(R.id.pinfo_play3d)
    Play3DImages pinfoPlay3d;
    @Bind(R.id.pinfo_iv_01)
    ImageView pinfoIv01;
    @Bind(R.id.pinfo_tv_01)
    TextView pinfoTv01;
    private String imgUrl;
    private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productinfo_activity);
        ButterKnife.bind(this);
        //初始化轮播
        pinfoPlay3d.setDuration(1000)
                .setShowMillis(2000);
//                .setImagePadding(ResUtils.getMetrices(ProductInfoActivity.this).widthPixels / 10);
        iHttp.getString(Constant.URL_HPR, HTTPTAG, new IHttp.HttpResult() {
            @Override
            public void succeed(Object object) {
                try {
                    List<Map<String, Object>> list = JsonUtils.json2List(new JSONArray(object.toString()));
                    List<String> imgList = new ArrayList<String>();
                    for (Map<String, Object> map :list) {
                        imgList.add((String) map.get(IAdapter.PR1_IMGURL));
                    }
                    pinfoPlay3d.setImageUrlList(list,IAdapter.PR1_IMGURL).Init();
                    pinfoPlay3d.setOnItemClick(new Play3DImages.OnItemClick() {
                        @Override
                        public void onItemClick(int position) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Object object) {
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            imgUrl = intent.getStringExtra(IMG_URL);
            iHttp.upImageView(this, Constant.URL + imgUrl, pinfoIv01);
            text = intent.getStringExtra(TEXT);
            pinfoTv01.setText(text);
        }
    }
}
