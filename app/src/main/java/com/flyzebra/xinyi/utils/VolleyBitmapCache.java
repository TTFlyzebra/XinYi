package com.flyzebra.xinyi.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by FlyZebra on 2016/3/2.
 */
public class VolleyBitmapCache implements ImageLoader.ImageCache{
    private LruCache<String, Bitmap> mCache;
    private static final int maxSize = 10*1024*1024;

    public VolleyBitmapCache(){
        this(maxSize);
    }

    public VolleyBitmapCache(int maxSize) {
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String key) {
        return mCache.get(key);
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        mCache.put(key, bitmap);
    }
}
