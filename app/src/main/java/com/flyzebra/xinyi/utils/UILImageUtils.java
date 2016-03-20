package com.flyzebra.xinyi.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 初始化
 * Created by Administrator on 2016/2/27.
 */
public class UILImageUtils {

    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.memoryCacheExtraOptions(480, 800);
        config.diskCacheExtraOptions(480, 800, null);
//        config.taskExecutor(...);
//        config.taskExecutorForCachedImages(...);
        config.threadPoolSize(3); // default
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.tasksProcessingOrder(QueueProcessingType.FIFO);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
        config.memoryCacheSize(2 * 1024 * 1024);
        config.memoryCacheSizePercentage(13); // default
        config.diskCache(new UnlimitedDiskCache(cacheDir));
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.diskCacheFileCount(100);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.imageDownloader(new BaseImageDownloader(context)); // default
        config.imageDecoder(new BaseImageDecoder(true)); // default
        config.defaultDisplayImageOptions(DisplayImageOptions.createSimple()); // default
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions getDisplayImageOptions(int res1, int res2, int res3) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(res1)
                .showImageForEmptyUri(res2)
                .showImageOnFail(res3)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }
}
