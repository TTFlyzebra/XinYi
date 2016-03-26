package com.flyzebra.xinyi.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

/**
 * Created by FlyZebra on 2016/3/25.
 */
public class ResourceUtils {
    private static TypedValue mTmpValue = new TypedValue();

    public static int getXmlDef(Context context, int ResId) {
        synchronized (mTmpValue) {
            TypedValue value = mTmpValue;
            context.getResources().getValue(ResId, value, true);
            return (int) TypedValue.complexToFloat(value.data);
        }
    }

    public static float getDimension(Context context, int ResId) {
        return context.getResources().getDimension(ResId);
    }

    public static ColorStateList getColorStateList(Context context, int ResId) {
        return ContextCompat.getColorStateList(context, ResId);
    }

    public static int getColor(Context context, int ResId) {
        return ContextCompat.getColor(context, ResId);
    }
}
