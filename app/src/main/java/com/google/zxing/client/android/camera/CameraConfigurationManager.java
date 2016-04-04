/*
 * Copyright (C) 2010 ZXing authors
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

package com.google.zxing.client.android.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;

import com.flyzebra.xinyi.utils.FlyLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
final class CameraConfigurationManager {

    private static final String TAG = "CameraConfiguration";

    private final Context context;
    private Point screenResolution;
    private Point cameraResolution;

    CameraConfigurationManager(Context context) {
        this.context = context;
    }


    /**
     * Reads, one time, values from the camera that are needed by the app.
     */
    void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point theScreenResolution = new Point();
        display.getSize(theScreenResolution);
        screenResolution = theScreenResolution;
        FlyLog.i("<CameraConfigurationManager>initFromCameraParameters:screenResolution=" + screenResolution);
        cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, screenResolution);
    }

    void setDesiredCameraParameters(Camera camera, boolean safeMode) {
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            FlyLog.i("<CameraConfigurationManager>setDesiredCameraParameters->Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }
        FlyLog.i("<CameraConfigurationManager>setDesiredCameraParameters->Initial camera parameters: " + parameters.flatten());
        if (safeMode) {
            FlyLog.i("<CameraConfigurationManager>setDesiredCameraParameters->In camera config safe mode -- most settings will not be honored");
        }
        //读取系统灯光开关状态并设置
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        initializeTorch(parameters, prefs, safeMode);

        CameraConfigurationUtils.setFocus(parameters, true, true, safeMode);
        if (!safeMode) {
            CameraConfigurationUtils.setBarcodeSceneMode(parameters);
            CameraConfigurationUtils.setVideoStabilization(parameters);
            CameraConfigurationUtils.setFocusArea(parameters);
            CameraConfigurationUtils.setMetering(parameters);
        }

        setDisplayOrientation(camera, 90);

        parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
        FlyLog.i("<CameraConfigurationManager>setDesiredCameraParameters->Final camera parameters: " + parameters.flatten());
        //竖屏添加
        camera.setParameters(parameters);

        Camera.Parameters afterParameters = camera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if (afterSize != null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
            FlyLog.i("<CameraConfigurationManager>setDesiredCameraParameters->Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y +
                    ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
            cameraResolution.x = afterSize.width;
            cameraResolution.y = afterSize.height;
        }
    }

    Point getCameraResolution() {
        return cameraResolution;
    }

    Point getScreenResolution() {
        return screenResolution;
    }

    boolean getTorchState(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters != null) {
                String flashMode = parameters.getFlashMode();
                return flashMode != null &&
                        (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
                                Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
            }
        }
        return false;
    }

    void setTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        doSetTorch(parameters, newSetting, false);
        camera.setParameters(parameters);
    }

    private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode) {
        boolean currentSetting = FrontLightMode.readPref(prefs) == FrontLightMode.ON;
        doSetTorch(parameters, currentSetting, safeMode);
    }

    private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
        CameraConfigurationUtils.setTorch(parameters, newSetting);
        if (!safeMode) {
            CameraConfigurationUtils.setBestExposure(parameters, newSetting);
        }
    }

    /*改变照相机成像的方向的方法*/
    protected void setDisplayOrientation(Camera camera, int angle) {
        Method downPolymorphic = null;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", int.class);
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, angle);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


}
