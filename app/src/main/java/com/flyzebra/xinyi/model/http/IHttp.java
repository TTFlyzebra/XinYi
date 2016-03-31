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
    void upImageView(Context context, String url, ImageView iv);

    /**
     * 使用须知，跟控件view适配的Adapter必须实现HttpAdapter接口
     *
     * @param url
     * @param view
     * @param tag
     * @param <T>
     */
    <T extends View> void upListView(String url, T view, Object tag);

    /**
     * 使用须知，跟控件view适配的Adapter必须实现HttpAdapter接口
     *
     * @param url
     * @param view
     * @param tag
     * @param <T>
     */
    <T extends View> void upListView(String url, T view, String jsonKey, Object tag);

    void execute(Builder builder);

    void cancelAll(Object tag);

    interface Result {
        void succeed(Object object);
        void faild(Object object);
    }

    interface HttpAdapter<T extends List> {
        T getList();

        void notifyDataSetChanged();
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

    class Builder {
        public String url;
        public String jsonKey;
        public Map<String, String> params;
        public Object tag;
        public List list;
        public Object adapter;
        public View view;
        public boolean retry = true;
        public int method = Method.GET;
        public Result result;

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

        public Builder setAdapter(Object adapter) {
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

        public Builder setResult(Result result) {
            this.result = result;
            return this;
        }
    }
}