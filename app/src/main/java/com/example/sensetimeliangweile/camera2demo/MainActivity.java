package com.example.sensetimeliangweile.camera2demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;

import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_CAMERA = 100;

    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private boolean checkPermissionsAndRequestIfNeed() {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsToBeRequested = new ArrayList<>();
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToBeRequested.add(permission);
                    result = false;
                }
            }
            if (!permissionsToBeRequested.isEmpty()) {
                requestPermissions(permissionsToBeRequested.toArray(new String[0]), REQUEST_CODE_FOR_CAMERA);
            }
        }

        return result;
    }

    private CameraManager mCameraManager;
    private String mCameraID;
    private CameraCharacteristics mCharacteristics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkPermissionsAndRequestIfNeed();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String[] cameraIDs = mCameraManager.getCameraIdList();
            int backID = 0;
            for (int i = 0; i < cameraIDs.length; i++) {
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraIDs[i]);
                int facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (CameraCharacteristics.LENS_FACING_BACK == facing) {
                    mCameraID = cameraIDs[i];
                    mCharacteristics = characteristics;
                    break;
                }

            }

            StreamConfigurationMap map = mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] previewSizes = map.getOutputSizes(SurfaceTexture.class);
            for (int i = 0; i < previewSizes.length; i++) {
                Log.d("lwl_texture", previewSizes[i].toString());
            }

            Size[] jpegSizes=map.getOutputSizes(ImageFormat.JPEG);
            for(int i=0;i<jpegSizes.length;i++){
                Log.d("lwl_jpeg", jpegSizes[i].toString());
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
}
