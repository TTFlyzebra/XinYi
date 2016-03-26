package com.flyzebra.xinyi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RotateImage extends ImageView {
    private float mDirection;

    public RotateImage(Context context) {
        this(context, null);
    }

    public RotateImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDirection = 0.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        drawable.setBounds(0, 0, getWidth(), getHeight());
        canvas.save();
        canvas.rotate(mDirection, getWidth() / 2, getHeight() / 2);
        drawable.draw(canvas);
        canvas.restore();
    }

    public void updateDirection(float direction) {
        mDirection = direction;
        invalidate();
    }

}  