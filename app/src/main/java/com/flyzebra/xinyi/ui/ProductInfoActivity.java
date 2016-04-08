package com.flyzebra.xinyi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.model.http.IHttp;
import com.flyzebra.xinyi.model.http.MyVolley;

/**
 * Created by FlyZebra on 2016/4/8.
 */
public class ProductInfoActivity extends AppCompatActivity {
    public static final String IMG_URL = "BITMAP";
    public static final String TEXT = "TEXT";
    private IHttp iHttp = MyVolley.getInstance();
    private String HTTPTAG = "Fragment" + Math.random();
    private String imgUrl;
    private String text;

    private ImageView iv01;
    private TextView tv01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productinfo_activity);
        iv01 = (ImageView) findViewById(R.id.pinfo_iv_01);
        tv01 = (TextView) findViewById(R.id.pinfo_tv_01);
        Intent intent = getIntent();
        if (intent != null) {
            imgUrl = intent.getStringExtra(IMG_URL);
            iHttp.upImageView(this, imgUrl, iv01);
            text = intent.getStringExtra(TEXT);
            tv01.setText("深圳市在近日开启了一场史上最严的“禁摩限电”整治活动，" +
                    "一方面对公交、地铁口等地区的非法拉客行为重点打击，另一方面也将查处严重超标" +
                    "和没有牌照的电动车、摩托车。自4月11日起，北京长安街等十条大街禁行电动自行车。" +
                    "对此规定，你怎么看？");
        }
    }
}
