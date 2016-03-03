package com.flyzebra.xinyi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.View;

/**
 * ViewPager轮播用的导航条用来指示ViewPager当前显示的页面ViewPager
 * Created by FlyZebra on 2016/3/1.
 */
public class CountItemForViewPager extends View {
//    private final String TAG = "com.flyzebra";
    private Paint select_paint;
    private Paint un_select_paint;
    private int width;
    private int height;
    //总页数
    private int sumItem = 10;
    //当前页
    private int currentItem = 5;

    public CountItemForViewPager(Context context) {
        super(context);
    }

    public CountItemForViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountItemForViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > 0 && height > 0) {
            this.width = width;
            this.height = height;
        }
//        Log.i(TAG, "ViewPagerCountImage->onMeasure->width=" + width);
    }

    public void setSumItem(int sumItem) {
        this.sumItem = sumItem;
        postInvalidate();
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        float c_size = height/2.0f;
        float x = c_size+(width-height*(sumItem*2-1))/2;
        float y = height/2.0f;
        for(int i=1;i<=sumItem;i++){
            if(i==currentItem){
                canvas.drawCircle(x+(i-1)*height*2, y, c_size-3, select_paint);
            }else{
                canvas.drawCircle(x+(i-1)*height*2, y, c_size-3, un_select_paint);
            }
        }

    }

    private void initPaint() {
        if (un_select_paint == null) {
            un_select_paint = new Paint();// 实例化Paint
            un_select_paint.setAntiAlias(true);
            un_select_paint.setStrokeWidth(1);// 设置笔画粗细
            un_select_paint.setColor(Color.BLACK);// 设置颜色
            un_select_paint.setStyle(Paint.Style.FILL);// 设置样式
            un_select_paint.setAlpha(100);
        }
        if (select_paint == null) {
            select_paint = new Paint();
            select_paint.setAntiAlias(true);
            select_paint.setStrokeWidth(1);// 设置笔画粗细
            select_paint.setStyle(Paint.Style.FILL);
            select_paint.setColor(Color.GREEN);
            select_paint.setAlpha(255);
        }
    }
}
