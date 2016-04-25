package com.flyzebra.xinyi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/4/25.
 */
public class DiskUtils {

    public static void saveData(Context context,String name, String[] keyArr ,Object[] dataArr){
        saveData(context, name, keyArr, dataArr, Activity.MODE_PRIVATE);
    }

    public static void saveData(Context context,String name,String[] keyArr, Object[] dataArr,int mode){
        SharedPreferences sp = context.getSharedPreferences(name,mode);
        SharedPreferences.Editor editor = sp.edit();
        for(int i = 0;i<keyArr.length;i++){
            if(dataArr[i] instanceof Integer){
                editor.putInt(keyArr[i], (int) dataArr[i]);
            }else  if(dataArr[i] instanceof String){
                editor.putString(keyArr[i], (String) dataArr[i]);
            }else  if(dataArr[i] instanceof Boolean){
                editor.putBoolean(keyArr[i], (boolean) dataArr[i]);
            }else  if(dataArr[i] instanceof Float){
                editor.putFloat(keyArr[i], (float) dataArr[i]);
            }else  if(dataArr[i] instanceof Long){
                editor.putLong(keyArr[i], (long) dataArr[i]);
            }
        }
        editor.commit();
    }

    public static void clear(Context context,String name) {
        SharedPreferences sp = context.getSharedPreferences(name,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
