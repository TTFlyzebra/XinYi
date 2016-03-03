package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义控件，根据了了控件的宽度，按比例缩放自身的高度 *
 * 如用于ViewPager图片轮播，请保持所有的图像尺寸大小一致
 * Created by FlyZebra on 2016/3/1.
 */
public class AutoSizeWithChildViewPager extends ViewPager{
//    private final String TAG = "com.flyzebra";
    public AutoSizeWithChildViewPager(Context context) {
        super(context);
    }

    public AutoSizeWithChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int p_width = getDefaultSize(0, widthMeasureSpec);
        int p_height = getDefaultSize(0, heightMeasureSpec);
        int c_height = 0;
        int c_width = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int w = child.getMeasuredWidth();
            if (w > c_width) //采用最大的view的宽度。
                c_width = w;
            int h = child.getMeasuredHeight();
            if (h > c_height) //采用最大的view的高度。
                c_height = h;
        }
//        Log.i(TAG, "AutoImageViewViewPager->onMeasure->p_width=" + p_width + ",p_height=" + p_height + ",c_width=" + c_width + ",c_height=" + c_height);
        if(p_width!=0&&c_width!=0){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(c_height*p_width/c_width,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
