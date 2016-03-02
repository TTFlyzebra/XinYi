package com.flyzebra.xinyi.data;

import android.widget.ImageView;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.openutils.UILImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

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
        ImageLoader.getInstance().displayImage(url,iv,
                UILImageUtils.getDisplayImageOptions(R.drawable.image, R.drawable.image, R.drawable.image));
    }
}
