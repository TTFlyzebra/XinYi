package com.flyzebra.xinyi.view;/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.flyzebra.xinyi.utils.FlyLog;

/**
 * An animation that rotates the view on the Y axis between two specified angles.
 * This animation also adds a translation on the Z axis (depth) to improve the effect.
 */
public class Rotate3dAnimation extends Animation {
    private float mFromDegrees;
    private float mToDegrees;
    private float mCenterX;
    private float mCenterY;
    private float mDepthZ;
    private boolean mReverse;
    private Camera mCamera;
    private View mView;
    private int mNums;

    private float old_x = 0;
    private float old_z = 0;
    private float new_x = 0;
    private float new_z = 0;

    /**
     * Creates a new 3D rotation on the Y axis. The rotation is defined by its
     * start angle and its end angle. Both angles are in degrees. The rotation
     * is performed around a center point on the 2D space, definied by a pair
     * of X and Y coordinates, called centerX and centerY. When the animation
     * starts, a translation on the Z axis (depth) is performed. The length
     * of the translation can be specified, as well as whether the translation
     * should be reversed in time.
     *
     * @param fromDegrees the start angle of the 3D rotation
     * @param toDegrees   the end angle of the 3D rotation
     * @param reverse     true if the translation should be reversed, false otherwise
     */
    public Rotate3dAnimation(View view, float fromDegrees, float toDegrees, int nums, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = view.getWidth() / 2;
        mCenterY = view.getHeight() / 2;
        mDepthZ = (float) (mCenterX / (Math.tan(((180 / nums) / 180f) * Math.PI)));
        FlyLog.i("<Rotate3dAnimation>mCenterX=" + mCenterX + ",mDepthZ=" + mDepthZ);
        mReverse = reverse;
        mView = view;
        mNums = nums;
    }

    public Rotate3dAnimation() {
    }

    public void Rotate3dAnimation(View view, float degrees, int nums) {
        mFromDegrees = degrees;
        mToDegrees = degrees + 360f / nums;
        mCenterX = view.getWidth() / 2;
        mCenterY = view.getHeight() / 2;
        mDepthZ = (float) (mCenterX / (Math.tan(((180 / nums) / 180f) * Math.PI)));
        mView = view;
        mNums = nums;
    }

    public void init(View view, float degrees, int nums) {
        mFromDegrees = degrees;
        mToDegrees = degrees + 360f / nums;
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
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float Zorder = degrees % 360;
            Zorder = Zorder > 180 ? 180 - Zorder % 180 : Zorder;
            mView.setTranslationZ(180f - Zorder);
        } else {
            float mdegrees = degrees % 360;
            if (mdegrees > 360 - 360 / mNums) {
                mView.bringToFront();
            }
        }

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;
        final Matrix matrix = t.getMatrix();
        camera.save();
        new_x = (float) Math.sin(Math.PI * degrees / 180) * mDepthZ;
        new_z = -(float) Math.cos(Math.PI * degrees / 180) * mDepthZ;
        camera.translate(new_x - old_x, 0.0f, new_z - old_z + mDepthZ);
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
