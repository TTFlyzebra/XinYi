package com.flyzebra.xinyi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/4/10.
 */
public class CardImageView extends ImageView {
    private Paint mPaint;

    public CardImageView(Context context) {
        super(context);
        init(context);
    }

    public CardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
//        mPaint.setAlpha(0);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
//        canvas.drawRoundRect(0,0,width,height,100,100,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
//        canvas.drawRect(0,0,width,height,mPaint);
    }
}
