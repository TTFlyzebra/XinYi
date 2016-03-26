package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyzebra.xinyi.fly.FlyLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by FlyZebra on 2016/3/25.
 */
public class ChildView extends FrameLayout implements IChildView {
    protected Context context;
    protected List<Map<String, Object>> list;

    public ChildView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ChildView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ChildView(Context context, ViewGroup parent) {
        super(context);
        parent.addView(this);
        init(context);
    }

    public View setContentView(Context context, @LayoutRes int ResId) {
        View view = LayoutInflater.from(context).inflate(ResId, null);
        this.addView(view);
        return view;
    }

    private void init(Context context) {
        this.context = context;
        SetLayoutParams();
        onCreate();
    }

    public void SetLayoutParams() {

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

    }

    @Override
    public List<Map<String, Object>> getData() {
        return list;
    }

    @Override
    public void setData(List<Map<String, Object>> data) {
        if (data == null) {
            FlyLog.i("<ChildView>setData:data is null");
            return;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.clear();
        list.addAll(data);
    }

    @Override
    public void addData(List<Map<String, Object>> data) {
        if (data == null) {
            FlyLog.i("<ChildView>setData:data is null");
            return;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(data);
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
