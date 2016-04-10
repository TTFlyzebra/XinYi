package com.flyzebra.xinyi.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyzebra.xinyi.model.http.MyVolley;


/**
 * Created by Administrator on 2016/4/11.
 */
public class Play3DImages extends FrameLayout {
    private Context context;
    private String urlArray[];
    private long durationMillis = 1000;
    private Rotate3dAnimation animation[];
    private float degreesArr[];
    private ImageView imageView[];
    private Interpolator interplatro = new LinearInterpolator();

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable playTask = new Runnable() {
        @Override
        public void run() {
            palyAnimtion();
            mHandler.postDelayed(this, durationMillis);
        }
    };

    public Play3DImages(Context context) {
        super(context);
        initContext(context);
    }

    public Play3DImages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContext(context);
    }

    public Play3DImages(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    private void initContext(Context context) {
        this.context = context;
    }

    public Play3DImages setImageUrlArray(String urlArray[], int padding) {
        this.urlArray = new String[urlArray.length];
        System.arraycopy(urlArray, 0, this.urlArray, 0, urlArray.length);
        initImageViews(padding);
        initAnimatios();
        initDegrees();
        return this;
    }

    private void initDegrees() {
        degreesArr = new float[urlArray.length];
        for (int i = 0; i < degreesArr.length; i++) {
            degreesArr[i] = (i * 360f / degreesArr.length) % 360;
        }
    }

    private void initAnimatios() {
        animation = new Rotate3dAnimation[urlArray.length];
        for (int i = 0; i < urlArray.length; i++) {
            animation[i] = new Rotate3dAnimation();
        }
    }

    private void initImageViews(int padding) {
        imageView = new ImageView[urlArray.length];
        for (int i = 0; i < urlArray.length; i++) {
            ImageView iv = new ImageView(context);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setPadding(padding, padding, padding, padding);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView[i] = iv;
            this.addView(iv);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < imageView.length; i++) {
            MyVolley.getInstance().upImageView(context, urlArray[i], imageView[i]);
        }
        mHandler.post(playTask);
    }

    private void palyAnimtion() {
        if (imageView == null) return;
        for (int i = 0; i < imageView.length; i++) {
            animation[i].init(imageView[i], degreesArr[i], imageView.length);
            degreesArr[i] = (degreesArr[i] + 360f / imageView.length) % 360;
            animation[i].setDuration(durationMillis);
            animation[i].setFillAfter(true);
            animation[i].setInterpolator(interplatro);
            imageView[i].startAnimation(animation[i]);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(playTask);
    }

    public Play3DImages setDelayTime(long durationMillis) {
        this.durationMillis = durationMillis;
        return this;
    }

}
