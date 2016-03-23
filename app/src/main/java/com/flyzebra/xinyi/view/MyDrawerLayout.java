package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/3/22.
 */
public class MyDrawerLayout extends DrawerLayout {
    private GestureDetectorCompat mGestureDetector;
    private OnGestureListener mOnGestureListener;
    private boolean isFling = false;
    private boolean isLongPress = false;
    private int MIN_FLING = dp2px(15);
    private int MAX_VELOCITYX = -dp2px(500);

    public MyDrawerLayout(Context context) {
        super(context);
        init();
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mOnGestureListener = new SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                isFling = false;
                isLongPress = false;
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e1.getX() - e2.getX()) > MIN_FLING && velocityX < MAX_VELOCITYX) {
                    isFling = true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                isLongPress = true;
                super.onLongPress(e);
            }
        };
        mGestureDetector = new GestureDetectorCompat(getContext(), mOnGestureListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("com.flyzebra", "ev->" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }
}
