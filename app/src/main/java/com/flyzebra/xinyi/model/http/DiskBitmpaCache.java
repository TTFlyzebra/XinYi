package com.flyzebra.xinyi.model.http;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.volley.toolbox.ImageLoader;
import com.flyzebra.xinyi.utils.FlyLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import libcore.io.DiskLruCache;

/**
 * Created by Administrator on 2016/4/3.
 */
public class DiskBitmpaCache implements ImageLoader.ImageCache {
    private final int max_size = 50 * 1024 * 1024;
    private DiskLruCache mDiskLruCache;

    public DiskBitmpaCache(Context context) {
        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            FlyLog.i("<DiskBitmpaCache>save path:" + cacheDir.getParent());
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, max_size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapShot = null;
        try {
            snapShot = mDiskLruCache.get(hashKeyForDisk(url));
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FlyLog.i("<DiskBitmpaCache>getBitmap bitmap:" + bitmap);
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        FlyLog.i("<DiskBitmpaCache>putBitmap start savekey:" + hashKeyForDisk(url));
        OutputStream outputStream = null;
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyForDisk(url));
            if (editor == null) return;
            outputStream = editor.newOutputStream(0);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FlyLog.i("<DiskBitmpaCache>putBitmap finish savekey:" + hashKeyForDisk(url));
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
