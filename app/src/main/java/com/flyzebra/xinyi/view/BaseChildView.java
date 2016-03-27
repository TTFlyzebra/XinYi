package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.R;
import com.flyzebra.xinyi.utils.FlyLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义子控件基类
 * Created by FlyZebra on 2016/3/25.
 */
public class BaseChildView extends LinearLayout implements IChildView {
    protected Context context;
    protected List<Map<String, Object>> list;
    protected int ResId = R.layout.play_viewpager_autoheight;

    public BaseChildView(Context context) {
        this(context, null);
    }

    public BaseChildView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseChildView(Context context, ViewGroup parent, @LayoutRes int ResId) {
        super(context);
        if (parent != null) {
            parent.addView(this);
        }
        this.ResId = ResId;
        init(context);
    }

    public View setContentView(@LayoutRes int ResId) {
        View view = LayoutInflater.from(context).inflate(ResId, null);
        this.addView(view);
        return view;
    }

    private void init(Context context) {
        this.context = context;
        onCreate();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {
        this.removeAllViews();
        if (list != null) {
            list.clear();
            list = null;
        }
        FlyLog.i("<BaseChildView>onDestory");
    }

    @Override
    public List<Map<String, Object>> getData() {
        return list;
    }

    @Override
    public void setData(List<Map<String, Object>> data) {
        if (data == null) {
            FlyLog.i("<BaseChildView>setData:data is null");
            return;
        }
        this.list = data;
    }

    @Override
    public List<Map<String, Object>> addData(List<Map<String, Object>> data) {
        if (data == null) {
            FlyLog.i("<ChildView>setData:data is null");
            return null;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(data);
        return list;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onStart();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onStop();
    }
}
