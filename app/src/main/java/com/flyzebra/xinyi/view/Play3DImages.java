package com.flyzebra.xinyi.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flyzebra.xinyi.data.URLS;
import com.flyzebra.xinyi.model.http.GetHttp;
import com.flyzebra.xinyi.utils.FlyLog;

import java.util.List;
import java.util.Map;


/**
 * Created by FlyZebra on 2016/4/11.
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

    private boolean isClockwise = false;
    private boolean isPlay = true;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable playTask = new Runnable() {
        @Override
        public void run() {
            if (isPlay) {
                if (!isClockwise) {
                    playToNextImage(durationMillis);
                } else {
                    playToFroeImage(durationMillis);
                }
                mHandler.postDelayed(this, durationMillis + showMillis);
            }
        }
    };

    //滑动
    private GestureDetectorCompat mGestureDetector;
    private OnGestureListener mGestureListener;
    private int mTouchSlop;
    private boolean isFling;


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
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mGestureListener = new SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                FlyLog.i("<Play3DImages> onDown ");
                return super.onDown(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                FlyLog.i("<Play3DImages> onLongPress ");
                super.onLongPress(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                FlyLog.i("<Play3DImages> onFling X=" + velocityX + ",Y=" + velocityY);
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    isFling = true;
                    if (velocityX < 0) {
                        FlyLog.i("<Play3DImages> pauseShowForeImage X=" + velocityX + ",Y=" + velocityY);
                        playToFroeImage(300);
                        if (isPlay) {
                            mHandler.removeCallbacks(playTask);
                            playAnimition(durationMillis + showMillis);
                        }
                    } else {
                        FlyLog.i("<Play3DImages> pauseShowNextImage X=" + velocityX + ",Y=" + velocityY);
                        playToNextImage(300);
                        if (isPlay) {
                            mHandler.removeCallbacks(playTask);
                            playAnimition(durationMillis + showMillis);
                        }
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        mGestureDetector.onTouchEvent(ev);
//        if(isFling ) return true;
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if(isFling){
//                    return true;
//                }
//                break;
//        }
//        return false;
//    }

    /**
     * 滑动手势判断
     *
     * @param event
     * @return
     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mGestureDetector.onTouchEvent(event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isFling = false;
//                break;
//            case MotionEvent.ACTION_UP:
//                return isFling;
//        }
//        return true;
//    }

    /**
     * 当前播放状态
     *
     * @return
     */
    public boolean isPlay() {
        return isPlay;
    }

    public Play3DImages setPasue(boolean isPlay) {
        this.isPlay = isPlay;
        return this;
    }

    /**
     * 当关播放方向
     *
     * @return
     */
    public boolean isClockwise() {
        return isClockwise;
    }

    /**
     * 设置播放的图片组的网络地址
     *
     * @param urlArray
     * @return
     */

    public Play3DImages setImageUrlArray(String urlArray[]) {
        this.urlArray = new String[urlArray.length];
        System.arraycopy(urlArray, 0, this.urlArray, 0, urlArray.length);
        return this;
    }

    public Play3DImages setImageUrlList(List<Map<String, Object>> list,String key) {
        this.urlArray = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            this.urlArray[i] = URLS.URL + list.get(i).get(key);
        }
        return this;
    }

    /**
     * 设置图片间的间隔
     *
     * @param padding
     * @return
     */
    public Play3DImages setImagePadding(int padding) {
        imagePadding = padding;
        return this;
    }

    /**
     * 设置显示图片的透明度
     *
     * @param imageAlpha
     * @return
     */
    public Play3DImages setImageAlpha(float imageAlpha) {
        this.imageAlpha = imageAlpha;
        return this;
    }

    /**
     * 完成控件的初始化
     */
    public void Init() {
        initImageViews();
        initAnimatios();
        initDegrees();
        for (int i = 0; i < urlArray.length; i++) {
            GetHttp.getIHttp().upImageView(context, urlArray[i], imageView[i]);
        }
    }

    private void initImageViews() {
        this.removeAllViews();
        imageView = new ImageView[urlArray.length];
        for (int i = 0; i < urlArray.length; i++) {
            ImageView iv = new ImageView(context);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setTag(i);
            iv.setAlpha((int) (255 * imageAlpha));
            imageView[i] = iv;
            this.addView(iv);
        }
    }

    private void initAnimatios() {
        animation = new Rotate3dAnimation[urlArray.length];
        for (int i = 0; i < urlArray.length; i++) {
            animation[i] = new Rotate3dAnimation();
        }
    }

    private void initDegrees() {
        degreesArr = new float[urlArray.length];
        for (int i = 0; i < degreesArr.length; i++) {
            degreesArr[i] = (i * 360f / degreesArr.length) % 360;
        }
        playAnimition(0);
    }

    /**
     * 播放下一张图片
     *
     * @param durationMillis
     */
    public void playToNextImage(long durationMillis) {
        if (imageView == null) return;
        for (int i = 0; i < imageView.length; i++) {
            degreesArr[i] = (degreesArr[i] + 360f / imageView.length) % 360;
            isClockwise = false;
            animation[i].init(imageView[i], degreesArr[i] - 360f / imageView.length, degreesArr[i], imageView.length);
            animation[i].setDuration(durationMillis);
            animation[i].setFillAfter(true);
            animation[i].setInterpolator(interplatro);
            imageView[i].startAnimation(animation[i]);
        }
    }

    /**
     * 播放上一张图片
     *
     * @param durationMillis
     */
    public void playToFroeImage(long durationMillis) {
        if (imageView == null) return;
        for (int i = 0; i < imageView.length; i++) {
            degreesArr[i] = (degreesArr[i] - 360f / imageView.length) % 360;
            isClockwise = true;
            animation[i].init(imageView[i], degreesArr[i] + 360f / imageView.length, degreesArr[i], imageView.length);
            animation[i].setDuration(durationMillis);
            animation[i].setFillAfter(true);
            animation[i].setInterpolator(interplatro);
            imageView[i].startAnimation(animation[i]);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(playTask);
    }

    /**
     * 设置转动一张图片所要的时间
     *
     * @param durationMillis
     * @return
     */
    public Play3DImages setDuration(long durationMillis) {
        this.durationMillis = durationMillis;
        return this;
    }

    /**
     * 设置图片播放停止显示图片的时间
     *
     * @param showMillis
     * @return
     */
    public Play3DImages setShowMillis(long showMillis) {
        this.showMillis = showMillis;
        return this;
    }

    /**
     * 开始轮播图片
     *
     * @return
     */
    public Play3DImages playAnimition(long durationMillis) {
        isPlay = true;
        mHandler.postDelayed(playTask, durationMillis);
        return this;
    }

    /**
     * 在指定时间内取消当前播放，延播放轨迹返回
     *
     * @param durationMillis
     */
    public Play3DImages cancleAnimition(long durationMillis) {
        this.isPlay = false;
        mHandler.removeCallbacks(playTask);
        for (int i = 0; i < imageView.length; i++) {
            if (isClockwise) {
                degreesArr[i] = (degreesArr[i] + 360f / imageView.length) % 360;
            } else {
                degreesArr[i] = (degreesArr[i] - 360f / imageView.length) % 360;
            }
            animation[i].init(imageView[i], degreesArr[i], imageView.length);
            animation[i].setDuration(durationMillis);
            animation[i].setFillAfter(true);
            animation[i].setInterpolator(interplatro);
            imageView[i].startAnimation(animation[i]);
        }
        return this;
    }

    /**
     * 在指定时间内完成当前播放
     *
     * @param durationMillis
     */
    public Play3DImages finishAnimition(long durationMillis) {
        this.isPlay = false;
        mHandler.removeCallbacks(playTask);
        for (int i = 0; i < imageView.length; i++) {
            animation[i].init(imageView[i], degreesArr[i], imageView.length);
            animation[i].setDuration(durationMillis);
            animation[i].setFillAfter(true);
            animation[i].setInterpolator(interplatro);
            imageView[i].startAnimation(animation[i]);
        }
        return this;
    }

    /**
     * 停止播放并显示上一张图像
     *
     * @param delayMillis
     * @return
     */
    public Play3DImages pauseShowForeImage(long delayMillis) {
        if (isPlay) {
            if (isClockwise()) {
                playToFroeImage(delayMillis);
            } else {
                cancleAnimition(delayMillis);
            }
        } else {
            playToFroeImage(delayMillis);
        }
        isPlay = false;
        isClockwise = true;
        return this;
    }

    /**
     * 停止播放并显示下一张图像
     *
     * @param delayMillis
     * @return
     */
    public Play3DImages pauseShowNextImage(long delayMillis) {
        if (isPlay) {
            if (!isClockwise()) {
                playToNextImage(delayMillis);
            } else {
                cancleAnimition(delayMillis);
            }
        } else {
            playToNextImage(delayMillis);
        }
        isPlay = false;
        isClockwise = false;
        return this;
    }

    private OnItemClick mOnItemClick;

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        if (this.mOnItemClick == null) {
            for (ImageView iv : imageView) {
//                iv.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mOnItemClick.onItemClick((Integer) v.getTag());
//                        FlyLog.i("<Play3DImages> setOnItemClick position=" + v.getTag());
//                    }
//                });
                iv.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mGestureDetector.onTouchEvent(event);
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                isFling = false;
                                break;
                            case MotionEvent.ACTION_UP:
                                if(!isFling){
                                    mOnItemClick.onItemClick((Integer) v.getTag());
                                }
                        }
                        return true;
                    }
                });
            }
        }
        this.mOnItemClick = onItemClick;
    }


    /**
     * 自定义3D播放动画类
     */
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
                float mDegrees = currentDegress % 360;
                float frontDegrees = 360f / mNums;
                if (mToDegrees % 360 == 0) {
                    if (mDegrees >= 360 - frontDegrees) {
                        mView.bringToFront();
                    } else if (mDegrees < frontDegrees) {
                        mView.bringToFront();
                    }
                }
            }
            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final Matrix matrix = t.getMatrix();
            camera.save();


            new_x = (float) Math.sin(Math.PI * currentDegress / 180) * mDepthZ;
            new_z = (float) Math.cos(Math.PI * currentDegress / 180) * mDepthZ;

            float degress = (currentDegress + 360) % 360;
            float move_x = (float) (Math.cos(Math.PI * degress / 180) * centerX);
            if (degress <= 90) {
                camera.translate(new_x - move_x + mCenterX, 0.0f, mDepthZ - new_z);
            } else if (degress <= 180) {
                camera.translate(new_x + move_x + mCenterX, 0.0f, mDepthZ - new_z);
            } else if (degress <= 270) {
                camera.translate(new_x - move_x - mCenterX, 0.0f, mDepthZ - new_z);
            } else if (degress <= 360) {
                camera.translate(new_x + move_x - mCenterX, 0.0f, mDepthZ - new_z);
            }

            camera.rotateY(currentDegress);

            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
