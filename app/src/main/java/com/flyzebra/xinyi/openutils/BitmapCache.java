package com.flyzebra.xinyi.openutils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by FlyZebra on 2016/3/2.
 */
public class BitmapCache implements ImageLoader.ImageCache{
    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {
        int maxSize = 20 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        return mCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        mCache.put(s, bitmap);
    }
}
