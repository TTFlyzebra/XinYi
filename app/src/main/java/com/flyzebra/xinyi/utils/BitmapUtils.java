package com.flyzebra.xinyi.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;


public class BitmapUtils {

	public BitmapUtils() {
	}

	/**
	 * 函数功能：把Bitmap转byte[]
	 * @param bitmap
	 * @return
	 */
	public static byte[] BitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		byte[] date = os.toByteArray();
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 函数功能：把byte[]转Bitmap
	 * @param data
	 * @return
	 */
	public static Bitmap ByteArrayToBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	/**
	 * 函数功能：按比例获取图像文件的缩略图
	 *
	 * @param spath  图像文件所有的文件路径
	 * @param max_size  指定所获取的图像的最大长度
	 * @return 成功返回Bitmap对象
	 */
	public static Bitmap GetSmallBitmapFormFile(String spath, int max_size) { // 获取缩略图		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
		opts.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(spath, opts);
		// 这里返回的bmp是null
		// int height = opts.outHeight * width / opts.outWidth;
		int getw = opts.outWidth;
		int geth = opts.outHeight;
		if (geth > getw) {
			opts.outHeight = max_size;
			opts.outWidth = getw * max_size / geth;
		} else {
			opts.outHeight = geth * max_size / getw;
			opts.outWidth = max_size;
		}
		getw = opts.outWidth;
		geth = opts.outHeight;
		opts.inJustDecodeBounds = false;
		int num = 0;
		while (bmp == null && num < 2) {
			try {
				bmp = BitmapFactory.decodeFile(spath, opts);
				bmp = ThumbnailUtils.extractThumbnail(bmp, getw, geth, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			} catch (OutOfMemoryError e) {
				FlyLog.i("<BitmapUtils>OutOfMemoryError"+num+":"+spath);
				System.gc();
				System.runFinalization();
				num++;
			}
		}
		return bmp;
	}

	public static Bitmap getImageThumbnail(String spath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(spath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(spath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,	ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 获取视频文件的缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,	MediaStore.Images.Thumbnails.MICRO_KIND);
		if (bitmap != null) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,	ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}

	public static int getBitmapSize(Bitmap bitmap){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			return bitmap.getAllocationByteCount();
		}else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){
			return bitmap.getByteCount();
		}else{
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}

	public static Bitmap GetSmallBitmapFormFile(String spath, int reqWidth, int reqHeight, boolean normal) { // 获取缩略图		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opts.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(spath, opts);
		// 这里返回的bmp是null
		final int height = opts.outHeight;
		final int width = opts.outWidth;
		if(!normal){
			int inSampleSize = 1;
			if (height > reqHeight || width > reqWidth) {
				// 计算出实际宽高和目标宽高的比率
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
				// 一定都会大于等于目标的宽和高。
				inSampleSize = heightRatio > widthRatio ? heightRatio: widthRatio;
			}
			opts.inSampleSize = inSampleSize;
//			Log.i(TAG, opts.outWidth + ":" + opts.outHeight + ":"+reqWidth+":"+reqHeight+":"+ inSampleSize);
		}
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(spath, opts);
		} catch (OutOfMemoryError e) {
			long maxMemory = Runtime.getRuntime().maxMemory()/1024;
			long totalMemory = Runtime.getRuntime().totalMemory()/1024;
			long freeMemory = Runtime.getRuntime().freeMemory()/1024;
			FlyLog.i("<BitmapUtils>OutOfMemoryError->" +"maxMemory:"+maxMemory+"totalMemory:" + totalMemory+"freeMemory:"+freeMemory);
		}
		return bmp;
	}

}
