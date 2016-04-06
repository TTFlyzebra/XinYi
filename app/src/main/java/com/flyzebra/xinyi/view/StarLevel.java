package com.flyzebra.xinyi.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyzebra.xinyi.utils.FlyLog;

/**
 * Created by Administrator on 2016/4/5.
 */
public class StarLevel extends LinearLayout {
    private int ResID[] = {android.R.drawable.star_big_off, android.R.drawable.star_big_on};
    private int levelSetp = 4;
    private int level = 12;
    private Context context;
    private int starWidth = 0;
    private int starHeight = 0;
    private boolean isAttache;

    public StarLevel(Context context) {
        super(context);
        init(context);
    }

    public StarLevel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StarLevel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    public void setLevelSetpAndrImgaes(int levelStep, @DrawableRes int ResID[]) {
        this.levelSetp = levelStep;
        this.ResID = new int[ResID.length];
        System.arraycopy(ResID, 0, this.ResID, 0, ResID.length);
    }

    public void setLevel(int level) {
        this.level = level;
        drawStars(level);
    }

    public void drawStars(int level) {
        if (!isAttache) return;
        FlyLog.i("<StarLevel>drawStars:level=" + level);
        removeAllViews();
        //进制转换
        int tempLevel = level;
        if (levelSetp < 2) {
            throw new RuntimeException("StartLeve.class, levelSetp is Error number, The number must bigger than 2");
        }
        StringBuffer setpNum = new StringBuffer();
        while (tempLevel / levelSetp > 0) {
            setpNum.append(tempLevel % levelSetp);
            tempLevel = tempLevel / levelSetp;
        }
        setpNum.append(tempLevel);
        FlyLog.i("<StarLevel>drawStars:setpNum=" + setpNum);

        //绘制星星
        //如果等级超过设置能绘制的最大等级，则绘制能绘制最大等级
        if (setpNum.length() > ResID.length) {
            FlyLog.i("<StarLevel>如果等级超过设置能绘制的最大等级，则绘制能绘制最大等级");
            addImageView(ResID[ResID.length - 1], levelSetp - 1);
            postInvalidate();
            return;
        }
        //绘制等级
        int level2 = setpNum.length();
        for (int i = level2; i > 0; i--) {
            FlyLog.i("<StarLevel>ResID=" + (i - 1) + ",Num=" + setpNum.charAt(i - 1) + ",s," + setpNum.toString());
            addImageView(ResID[i - 1], setpNum.charAt(i - 1) - 48);
        }
        postInvalidate();
    }

    private void addImageView(@DrawableRes int ResID, int num) {
        FlyLog.i("<StarLevel>ResID=" + ResID + ",Num=" + num);
        for (int i = 0; i < num; i++) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int width = this.starWidth > 0 ? this.starWidth : LayoutParams.WRAP_CONTENT;
            int height = this.starHeight > 0 ? this.starHeight : LayoutParams.WRAP_CONTENT;
            LayoutParams lp = new LinearLayout.LayoutParams(width, height);
            iv.setLayoutParams(lp);
            addView(iv);
            iv.setImageResource(ResID);
        }
    }

    public void setStarSize(int width, int height) {
        this.starWidth = width;
        this.starHeight = height;
        drawStars(level);
    }

    @Override
    protected void onAttachedToWindow() {
        isAttache = true;
        drawStars(level);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        isAttache = false;
        super.onDetachedFromWindow();
    }
}
