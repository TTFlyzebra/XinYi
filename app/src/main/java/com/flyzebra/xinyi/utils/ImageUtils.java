package com.flyzebra.xinyi.utils;

import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.openutils.VolleyUtils;

/**
 * Created by Administrator on 2016/3/2.
 */
public class ImageUtils {
    /**
     * 使用UIL库显示加载ImageView的图片
     * @param url 图像路径
     * @param iv 显示图像的ImageView控件
     */
    public static void ShowImageView (String url,ImageView iv){
        //UILIamge方式显示图片
//      ImageLoader.getInstance().displayImage(url,iv,UILImageUtils.getDisplayImageOptions(R.drawable.image, R.drawable.image, R.drawable.image));
        //Volley方式显示图片
        VolleyUtils.ShowImageView(url,iv,R.drawable.image, R.drawable.image);
    }

    public static void ShowImageView (String url,NetworkImageView iv){
        //Volley方式显示图片
        VolleyUtils.ShowImageView(url,iv,R.drawable.image, R.drawable.image);
    }
}
