package com.flyzebra.xinyi.model.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Created by FlyZebra on 2016/3/20.
 */
public class MyVolley implements IHttp {
    private static final int OK = 1;
    private static final int DISK = 2;
    private static final int FAIL = 3;
    public static Set<Object> set_upListView = new HashSet<>();
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static int RETRY_TIME = 10000;
    private static int RETRY_NUM = 0;

    public static MyVolley getInstance() {
        return MyVolleyHolder.sInstance;
    }

    public static RequestQueue Init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        return mRequestQueue;
    }

    public static void getString(final IHttp.Builder info, final HttpResult result) {
        set_upListView.add(info.tag);
        StringRequest stringRequest = new StringRequest(info.method, info.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FlyLog.i("<MyVolley>getString->onResponse:response=" + response);
                result.succeed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (error instanceof NetworkError) {
//                } else if (error instanceof ClientError) {
//                } else if (error instanceof ServerError) {
//                } else if (error instanceof AuthFailureError) {
//                } else if (error instanceof ParseError) {
//                } else if (error instanceof NoConnectionError) {
//                } else if (error instanceof TimeoutError) {
//                }
                FlyLog.i("<MyVolley>getString->onErrorResponse:error=" + error);
                if (info.retry) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (set_upListView.contains(info.tag)) {
                                getString(info, result);
                            }
                        }
                    }, RETRY_TIME);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return info.method == Method.POST ? info.params : super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_TIME, RETRY_NUM, 1f));
        stringRequest.setTag(info.tag);
        mRequestQueue.add(stringRequest);
    }

    public static void upImageView(String url, NetworkImageView iv, int res1, int res2) {
        iv.setDefaultImageResId(res1);
        iv.setErrorImageResId(res2);
        iv.setImageUrl(url, mImageLoader);
    }

    public static void upImageView(String url, ImageView iv) {
        FlyLog.i("<MyVolley>upImageView->url=" + url + ",iv=" + iv.getId());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.image, R.drawable.image);
        mImageLoader.get(url, listener);
    }

    public static void PostUser(final String url, final Map<String, String> Params, final List<Map<String, Object>> list, final String jsonKey, final BaseAdapter adapter, final Object tag) {
        set_upListView.add(tag);
        FlyLog.i("<MyVolley>PostUser->response:url=" + url + ",Params=" + Params.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FlyLog.i("<MyVolley>PostUser->response:response=" + response);
                if (list != null) {
                    try {
                        JsonUtils.getList(list, new JSONObject(response), jsonKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FlyLog.i("<MyVolley>upListView->onErrorResponse:tag=" + tag);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (set_upListView.contains(tag)) {
                            PostUser(url, Params, list, jsonKey, adapter, tag);
                        }
                    }
                }, RETRY_TIME);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return Params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_TIME, RETRY_NUM, 1f));
        stringRequest.setTag(url);
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void upImageView(Context context, String url, ImageView iv) {
        upImageView(url, iv);
    }

    @Override
    public void getString(final String url, final Object tag, final HttpResult result) {
        set_upListView.add(tag);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                FlyLog.i("<MyVolley>getString:response=" + response);
                sendResult(result, response, OK);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String data = readDiskCache(url);
                if (data != null) {
                    sendResult(result, data, DISK);
                    FlyLog.i("<MyVolley>getString->readDiskCache data=" + data);
                } else {
                    sendResult(result, error, FAIL);
                    FlyLog.i("<MyVolley>getString->onErrorResponse:tag=" + tag);
                }
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_TIME, RETRY_NUM, 1f));
        stringRequest.setTag(url);
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void postString(final String url, final Map<String, String> Params, final Object tag, final HttpResult result) {
        set_upListView.add(tag);
        FlyLog.i("<MyVolley>postString->response:url=" + url + ",Params=" + Params.toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sendResult(result, response, OK);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendResult(result, error, FAIL);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return Params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_TIME, RETRY_NUM, 1f));
        stringRequest.setTag(url);
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void execute(Builder builder) {
    }

    //*ListView RecyclearView调用部分
    @Override
    public void upListView(final String url, final HttpAdapter adapter, final String jsonKey, final Object tag) {
        upListView(url, adapter, jsonKey, false, tag);
    }

    @Override
    public void upListView(String url, HttpAdapter adapter, String jsonKey, Object tag, HttpResult result) {
        upListView(url, adapter, jsonKey, false, tag, result);
    }

    @Override
    public void upListView(final String url, final HttpAdapter adapter, final String jsonKey, final boolean isAdd, final Object tag) {
        upListView(url, adapter, jsonKey, isAdd, tag, null);
    }

    @Override
    public void upListView(final String url, final HttpAdapter adapter, final String jsonKey, final boolean isAdd, final Object tag, final HttpResult result) {
        //判断任务是否已取消
        set_upListView.add(tag);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                sendResult(result, jsonObject.toString(), OK);
                notifyData(isAdd, adapter, jsonObject, jsonKey);
                FlyLog.i("<MyVolley>upListView->onResponse:tag=" + tag);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //尝试读取先前保存在硬盘中的数据
                String data = readDiskCache(url);
                if (data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        notifyData(isAdd, adapter, jsonObject, jsonKey);
                        sendResult(result, data, DISK);
                        FlyLog.i("<MyVolley>upListView->readDiskCache data=" + data);
                    } catch (JSONException e) {
                        sendResult(result, volleyError, FAIL);
                    }
                } else {
                    sendResult(result, volleyError, FAIL);
                    FlyLog.i("<MyVolley>upListView->onErrorResponse:tag=" + tag);
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(RETRY_TIME, RETRY_NUM, 1f));
        jsonObjectRequest.setTag(tag);
        mRequestQueue.add(jsonObjectRequest);
    }

    public void cancelAll(Object tag) {
        FlyLog.i("<MyVolley>cancelAll:tag=" + tag);
        set_upListView.remove(tag);
        mRequestQueue.cancelAll(tag);
    }

    public void sendResult(HttpResult result, Object object, int type) {
        if (result != null) {
            switch (type) {
                case OK:
                    result.succeed(object);
                    break;
                case DISK:
                    result.readDiskCache(object);
                    break;
                case FAIL:
                    result.faild(object);
                    break;
            }
        }
    }

    private void notifyData(boolean isAdd, HttpAdapter adapter, JSONObject jsonObject, String jsonKey) {
        if (!isAdd) {
            adapter.getList().clear();
        }
        adapter.getList().addAll(JsonUtils.getList(jsonObject, jsonKey));
        adapter.notifyDataSetChanged();
    }

    public static String readDiskCache(String url) {
        String data = null;
        Cache.Entry entry = mRequestQueue.getCache().get(url);
        if (entry != null) {
            try {
                data = new String(entry.data, HttpHeaderParser.parseCharset(entry.responseHeaders, "UTF-8"));
            } catch (Exception e) {
                FlyLog.i("<MyVolley>upListView->readDiskCache Error");
                return null;
            }
        }
        return data;
    }

    private static class MyVolleyHolder {
        public static final MyVolley sInstance = new MyVolley();
    }

}
