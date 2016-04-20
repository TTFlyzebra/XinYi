package com.flyzebra.xinyi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/8.
 */
public class BlockTextView extends TextView{
    private Paint mPaint1;
    private Paint mPaint2;
    public BlockTextView(Context context) {
        super(context);
        init();
    }

    public BlockTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlockTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public BlockTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPaint1 = new Paint();
        mPaint1.setColor(Color.BLUE);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint2 = new Paint();
        mPaint2.setColor(Color.YELLOW);
        mPaint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint1);
        canvas.drawRect(1,1,getMeasuredWidth()-1,getMeasuredHeight()-1,mPaint2);
        canvas.save();
        canvas.translate(1,0);
        super.onDraw(canvas);
        canvas.restore();
    }
}
