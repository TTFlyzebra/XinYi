package com.flyzebra.xinyi.openutils;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.flyzebra.xinyi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public static void ShowImageView(String url,NetworkImageView iv,int res1,int res2){
        iv.setDefaultImageResId(res1);
        iv.setErrorImageResId(res2);
        iv.setImageUrl(url,mImageLoader);
    }

    /**
     * 更新ListView
     * @param url
     * @param list
     * @param adapter
     */
    public static void upAdapter(String url, final List<Map<String,Object>> list , final BaseAdapter adapter){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                list.clear();
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("mealinfo");
                    for(int i= 0;i<jsonArray.length();i++){
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        Map<String,Object> map = new HashMap<String,Object>();
                        Iterator<String> iterator = jsonObject2.keys();
                        while(iterator.hasNext()){
                            String json_key = iterator.next();
                            Object json_value = jsonObject2.get(json_key);
                            if(json_value==null){
                                json_value="";
                            }
                            map.put(json_key, json_value);
                        }
                        list.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

}
