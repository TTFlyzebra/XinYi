package com.flyzebra.xinyi.openutils;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.flyzebra.xinyi.R;

/**
 * Created by FlyZebra on 2016/3/2.
 */
public class VolleyUtils {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    public static RequestQueue Init(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        return mRequestQueue;
    }

    public static void ShowImageView(String url,ImageView iv,int res1,int res2){
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv,res1,res2);
        mImageLoader.get(url, listener);
    }

}
