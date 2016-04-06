package com.flyzebra.xinyi.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by FlyZebra on 2016/3/31.
 */
public class ChildGridView extends ViewGroup {
    private View titleView;
    private View childView;
    private int column;

    public ChildGridView(Context context) {
        this(context, null);
    }

    public ChildGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    public ChildGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContext(context);
    }

    public ChildGridView(Context context, ViewGroup parent, @LayoutRes int TitleResID, @LayoutRes int ItemResID, int column) {
        super(context);
        titleView = LayoutInflater.from(context).inflate(TitleResID, null);
        this.addView(titleView);
        childView = LayoutInflater.from(context).inflate(ItemResID, null);
        this.addView(childView);
        this.column = column;
        initContext(context);
    }


    private void initContext(Context context) {

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
