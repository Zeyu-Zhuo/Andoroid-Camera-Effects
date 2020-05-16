package com.example.cameraeffects.utils;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;

public class CameraHelper implements Camera.PreviewCallback{
    private static final String TAG = "CameraHelper";
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    public int getmCameraId() { return mCameraId; }
    public CameraHelper(int mCameraId) { this.mCameraId = mCameraId; }



    private int mCameraId;
    private Camera mCamera;
    private byte[] buffer;
    private Camera.PreviewCallback mPreviewCallback;
    private SurfaceTexture mSurfaceTexture;

    public void switchCamera(){
        if(mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else{
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        stopPreview();
        startPreview(mSurfaceTexture);
    }

    private void stopPreview() {
        if(mCamera!=null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void startPreview(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
        try{
            mCamera = Camera.open(mCameraId);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setPreviewSize(WIDTH,HEIGHT);
            mCamera.setParameters(parameters);
            buffer = new byte[WIDTH*HEIGHT*3/2];
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPreviewCallback(Camera.PreviewCallback previewCallback){
        mPreviewCallback = previewCallback;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(null != mPreviewCallback){
            mPreviewCallback.onPreviewFrame(data, camera);
        }
        camera.addCallbackBuffer(buffer);

    }
}
