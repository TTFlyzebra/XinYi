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
    private static RequestQueue mQueue;
    private static ImageLoader mImageLoader;
    public static RequestQueue Init(Context context){
        mQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        return mQueue;
    }

    public static void ShowImageView(String url,ImageView iv){
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.image, R.drawable.image);
        mImageLoader.get(url, listener);
    }

}
