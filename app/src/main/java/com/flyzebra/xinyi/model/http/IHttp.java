package com.flyzebra.xinyi.model.http;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/20.
 */
public interface IHttp {
    void getString(String url, Object tag, HttpResult result);

    void postString(String url, Map<String,String> map , Object tag, HttpResult result);

    void upImageView(Context context, String url, ImageView iv);

    void upImageView(Context context, String url, ImageView iv, int LoadResId);

    void upListView(String url, HttpAdapter adapter, String jsonKey, Object tag);

    void upListView(String url, HttpAdapter adapter, String jsonKey, boolean isAdd, Object tag);

    void upListView(String url, HttpAdapter adapter, String jsonKey, Object tag, HttpResult result);

    void upListView(String url, HttpAdapter adapter, String jsonKey, boolean isAdd, Object tag, HttpResult result);

    void upRecyclerViewData(String url, List list, HttpAdapter adapter, Object tag);

    void upRecyclerViewData(String url, List list, HttpAdapter adapter, Object tag, HttpResult result);

    /**
     * NOTE:磁盘缓存的内容必须为JSONArray
     * 功能：从磁盘读取缓存数据并转换成list
     * @param url
     * @return
     */
    List<Map<String,Object>> readListFromCache(String url);

    void execute(Builder builder);

    void cancelAll(Object tag);

    interface HttpResult {
        void succeed(Object object);
        void failed(Object object);
    }

    interface Method {
        int DEPRECATED_GET_OR_POST = -1;
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    /**
     * Created by Administrator on 2016/4/3.
     */
    interface HttpAdapter<T extends List> {
        T getList();

        void notifyDataSetChanged();
    }

    class Builder {
        public String url;
        public String jsonKey;
        public Map<String, String> params;
        public Object tag;
        public List list;
        public HttpAdapter adapter;
        public View view;
        public boolean retry = true;
        public int method = Method.GET;
        public HttpResult result;
        private boolean isAdd;

        public static Builder getInstance() {
            return new Builder();
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setJsonKey(String jsonKey) {
            this.jsonKey = jsonKey;
            return this;
        }

        public Builder setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder setList(List list) {
            this.list = list;
            return this;
        }

        public Builder setAdapter(HttpAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setRetry(boolean retry) {
            this.retry = retry;
            return this;
        }

        public Builder setParams(String... strings) {
            this.params = new HashMap<String, String>();
            for (int i = 0; i < strings.length; i = i + 2) {
                params.put(strings[i], strings[i + 1]);
            }
            return this;
        }

        public Builder setMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder setResult(HttpResult result) {
            this.result = result;
            return this;
        }

        public Builder setIsAdd(boolean isAdd) {
            this.isAdd = isAdd;
            return this;
        }
    }
}
