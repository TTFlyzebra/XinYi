package com.flyzebra.xinyi.model.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.data.HttpAdapter;
import com.flyzebra.xinyi.utils.FlyLog;
import com.flyzebra.xinyi.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by FlyZebra on 2016/3/20.
 */
public class MyVolley implements IHttp {
    public static Set<Object> set_upListView = new HashSet<>();
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static long RETRY_TIME = 2500;

    public static MyVolley getInstance() {
        return MyVolleyHolder.sInstance;
    }

    public static void getString(final IHttp.Builder info, final Result result) {
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1f));
        stringRequest.setTag(info.tag);
        mRequestQueue.add(stringRequest);
    }

    public static RequestQueue Init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        return mRequestQueue;
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1f));
        stringRequest.setTag(url);
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void upImageView(Context context, String url, ImageView iv) {
        upImageView(url, iv);
    }

    @Override
    public <T extends View> void upListView(String url, T view, Object tag) {
        upListView(url, view, null, tag);
    }

    @Override
    public <T extends View> void upListView(String url, T view, String jsonKey, Object tag) {
        //反射
        HttpAdapter adapter = null;
        try {
            Field field = view.getClass().getDeclaredField("mAdapter");
            field.setAccessible(true);
            field.setAccessible(true);
            adapter = (HttpAdapter) field.get(view);
            FlyLog.i("<MyVolley>upListView->adapter=" + adapter);
        } catch (NoSuchFieldException e) {
            FlyLog.i("<MyVolley>upListView->NoSuchFieldException");
        } catch (IllegalAccessException e) {
            FlyLog.i("<MyVolley>upListView->IllegalAccessException");
        }
        if (adapter != null) {
            getInstance().upListView(url, adapter, adapter.getList(), jsonKey, tag);
        }
    }

    @Override
    public <T extends View> void upListView(String url, HttpAdapter adapter, String jsonKey, Object tag) {
        getInstance().upListView(url, adapter, adapter.getList(), jsonKey, tag);
    }

    @Override
    public void execute(Builder builder) {
        HttpAdapter adapter = null;
        try {
            Field field = builder.view.getClass().getDeclaredField("mAdapter");
            field.setAccessible(true);
            field.setAccessible(true);
            adapter = (HttpAdapter) field.get(builder.view);
        } catch (NoSuchFieldException e) {
            FlyLog.i("<MyVolley>upListView->NoSuchFieldException");
        } catch (IllegalAccessException e) {
            FlyLog.i("<MyVolley>upListView->IllegalAccessException");
        }
        if (adapter != null) {
            if (builder.result != null) {
                getInstance().upListView(builder.result, builder.url, adapter, adapter.getList(), builder.jsonKey, builder.tag);
            } else {
                getInstance().upListView(builder.url, adapter, adapter.getList(), builder.jsonKey, builder.tag);
            }
        }
    }

    private void upListView(final Result result, final String url, final HttpAdapter adapter, final List list, final String jsonKey, final Object tag) {
        //判断任务是否已取消
        set_upListView.add(tag);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (list != null) {
                    list.clear();
                    JsonUtils.getList(list, jsonObject, jsonKey);
                }
                adapter.notifyDataSetChanged();
                if (result != null) {
                    result.succeed(jsonObject);
                }
                FlyLog.i("<MyVolley>upListView->onResponse:tag=" + tag);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (result != null) {
                    result.faild(volleyError);
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (set_upListView.contains(tag)) {
                                upListView(url, adapter, list, jsonKey, tag);
                            }
                        }
                    }, RETRY_TIME);
                    FlyLog.i("<MyVolley>upListView->onErrorResponse:tag=" + tag);
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1f));
        jsonObjectRequest.setTag(tag);
        mRequestQueue.add(jsonObjectRequest);
    }

    private void upListView(final String url, final HttpAdapter adapter, final List list, final String jsonKey, final Object tag) {
        //判断任务是否已取消
        set_upListView.add(tag);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (list != null) {
                    list.clear();
                    JsonUtils.getList(list, jsonObject, jsonKey);
                }
                adapter.notifyDataSetChanged();
                FlyLog.i("<MyVolley>upListView->onResponse:tag=" + tag);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                FlyLog.i("<MyVolley>upListView->onErrorResponse:tag=" + tag);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (set_upListView.contains(tag)) {
                            upListView(url, adapter, list, jsonKey, tag);
                        }
                    }
                }, RETRY_TIME);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(2500, 1, 1f));
        jsonObjectRequest.setTag(tag);
        mRequestQueue.add(jsonObjectRequest);
    }

    public void cancelAll(Object tag) {
        FlyLog.i("<MyVolley>cancelAll:tag=" + tag);
        set_upListView.remove(tag);
        mRequestQueue.cancelAll(tag);
    }

    private static class MyVolleyHolder {
        public static final MyVolley sInstance = new MyVolley();
    }

}
