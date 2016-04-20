package com.flyzebra.xinyi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/8.
 */
public class FlashTextView extends TextView{
    private Paint mPaint;
    private int mViewWidth;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mTranslate;
    public FlashTextView(Context context) {
        super(context);
        init();
    }

    public FlashTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlashTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public FlashTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mViewWidth==0){
            mViewWidth = getMeasuredWidth();
            mLinearGradient = new LinearGradient(-mViewWidth, 0, 2*mViewWidth, 0,
                    new int[]{0xffffffff,0xff00ff00,0xff0000ff,0xffffff00,0xffff00ff,0xff00ffff,0xffff0000,0xffffffff,0xff00ff00,0xff0000ff,0xffffff00,0xffff00ff,0xff00ffff,0xffff0000},
                    null, Shader.TileMode.CLAMP);
            mPaint = getPaint();
            mPaint.setShader(mLinearGradient);
            mGradientMatrix = new Matrix();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mGradientMatrix!=null){
            mTranslate += mViewWidth/5;
            if(mTranslate>mViewWidth){
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate,0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(200);
        }
    }
}
