package com.flyzebra.xinyi.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyzebra.xinyi.model.http.MyVolley;
import com.flyzebra.xinyi.utils.FlyLog;


/**
 * Created by Administrator on 2016/4/11.
 */
public class Play3DImages extends FrameLayout {
    private Context context;
    private String urlArray[];
    private long durationMillis = 1000;
    private long showMillis = 0;
    private Rotate3dAnimation animation[];
    private float degreesArr[];
    private ImageView imageView[];
    private Interpolator interplatro = new LinearInterpolator();
    private float imageAlpha = 1.0f;
    private int imagePadding = 100;
    private boolean isNext = true;
    private boolean isPasue = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable playTask = new Runnable() {
        @Override
        public void run() {
            if (!isPasue) {
                if (isNext) {
                    playToNextImage(durationMillis);
                } else {
                    playToFroeImage(durationMillis);
                }
                mHandler.postDelayed(this, durationMillis + showMillis);
            }
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

    public boolean isPasue() {
        return isPasue;
    }

    public Play3DImages setPasue(boolean isPasue) {
        if (this.isPasue == false) {
            if (isNext) {
                for (int i = 0; i < imageView.length; i++) {
                    degreesArr[i] = (degreesArr[i] - 360f / imageView.length) % 360;
                    animation[i].init(imageView[i], degreesArr[i], imageView.length);
                    animation[i].setDuration(300);
                    animation[i].setFillAfter(true);
                    animation[i].setInterpolator(interplatro);
                    imageView[i].startAnimation(animation[i]);
                }
            } else {
                for (int i = 0; i < imageView.length; i++) {
                    degreesArr[i] = (degreesArr[i] + 360f / imageView.length) % 360;
                    animation[i].init(imageView[i], degreesArr[i], imageView.length);
                    animation[i].setDuration(300);
                    animation[i].setFillAfter(true);
                    animation[i].setInterpolator(interplatro);
                    imageView[i].startAnimation(animation[i]);
                }
            }
        }
        this.isPasue = isPasue;
        if (isPasue == true) {
            mHandler.removeCallbacks(playTask);
        } else {
            mHandler.post(playTask);
        }
        return this;
    }

    private void initContext(Context context) {
        this.context = context;
    }

    public Play3DImages
    setImageUrlArray(String urlArray[]) {
        this.urlArray = new String[urlArray.length];
        System.arraycopy(urlArray, 0, this.urlArray, 0, urlArray.length);
        return this;
    }

    public Play3DImages setImagePadding(int padding) {
        imagePadding = padding;
        return this;
    }

    public void Init() {
        initImageViews();
        initAnimatios();
        initDegrees();
    }

    public Play3DImages setImageAlpha(float imageAlpha) {
        this.imageAlpha = imageAlpha;
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

    private void initImageViews() {
        imageView = new ImageView[urlArray.length];
        for (int i = 0; i < urlArray.length; i++) {
            ImageView iv = new ImageView(context);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setTag(i);
            iv.setAlpha(imageAlpha);
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

    public void playToNextImage(long durationMillis) {
        if (imageView == null) return;
        for (int i = 0; i < imageView.length; i++) {
            degreesArr[i] = (degreesArr[i] + 360f / imageView.length) % 360;
            isNext = true;
            animation[i].init(imageView[i], degreesArr[i] - 360f / imageView.length, degreesArr[i], imageView.length);
            animation[i].setDuration(durationMillis);
            animation[i].setFillAfter(true);
            animation[i].setInterpolator(interplatro);
            imageView[i].startAnimation(animation[i]);
        }
    }

    public void playToFroeImage(long durationMillis) {
        if (imageView == null) return;
        for (int i = 0; i < imageView.length; i++) {
            degreesArr[i] = (degreesArr[i] - 360f / imageView.length) % 360;
            isNext = false;
            animation[i].init(imageView[i], degreesArr[i] + 360f / imageView.length, degreesArr[i], imageView.length);
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

    public Play3DImages setDuration(long durationMillis) {
        this.durationMillis = durationMillis;
        return this;
    }

    public Play3DImages setShowMillis(long showMillis) {
        this.showMillis = showMillis;
        return this;
    }

    public class Rotate3dAnimation extends Animation {
        private float mFromDegrees;
        private float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private float mDepthZ;
        private Camera mCamera;
        private View mView;
        private int mNums;
        private float currentDegress;
        private float new_x = 0;
        private float new_z = 0;

        public Rotate3dAnimation(View view, float fromDegrees, float toDegrees, int nums) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mCenterX = view.getWidth() / 2;
            mCenterY = view.getHeight() / 2;
            mDepthZ = (float) (mCenterX / (Math.tan(((180 / nums) / 180f) * Math.PI)));
            FlyLog.i("<Rotate3dAnimation>mCenterX=" + mCenterX + ",mDepthZ=" + mDepthZ);
            mView = view;
            mNums = nums;
        }

        public Rotate3dAnimation() {
        }

        public void Rotate3dAnimation(View view, float fromDegrees, float toDegrees, int nums) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mCenterX = view.getWidth() / 2;
            mCenterY = view.getHeight() / 2;
            mDepthZ = (float) (mCenterX / (Math.tan(((180 / nums) / 180f) * Math.PI)));
            mView = view;
            mNums = nums;
        }

        public void init(View view, float fromDegrees, float toDegrees, int nums) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mCenterX = view.getWidth() / 2;
            mCenterY = view.getHeight() / 2;
            mDepthZ = (float) (mCenterX / (Math.tan(((180 / nums) / 180f) * Math.PI)));
            mView = view;
            mNums = nums;
        }

        public void init(View view, float toDegrees, int nums) {
            mFromDegrees = currentDegress;
            mToDegrees = toDegrees;
            mCenterX = view.getWidth() / 2;
            mCenterY = view.getHeight() / 2;
            mDepthZ = (float) (mCenterX / (Math.tan(((180 / nums) / 180f) * Math.PI)));
            mView = view;
            mNums = nums;
        }


        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            currentDegress = mFromDegrees + ((mToDegrees - mFromDegrees) * (interpolatedTime));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                float Zorder = (currentDegress + 360) % 360;
                Zorder = Zorder > 180 ? 180 - Zorder % 180 : Zorder;
                mView.setTranslationZ(180f - Zorder);
            } else {
                float mdegrees = currentDegress % 360;
                if (mdegrees > 360 - 360 / mNums) {
                    mView.bringToFront();
                }
            }
            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final Matrix matrix = t.getMatrix();
            camera.save();
            new_x = (float) Math.sin(Math.PI * currentDegress / 180) * mDepthZ;
            new_z = -(float) Math.cos(Math.PI * currentDegress / 180) * mDepthZ;
            camera.translate(new_x, 0.0f, new_z + mDepthZ);
            camera.rotateY(currentDegress);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }


    }
}
