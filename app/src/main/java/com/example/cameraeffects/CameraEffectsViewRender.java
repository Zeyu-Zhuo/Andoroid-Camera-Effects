package com.example.cameraeffects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;


import com.example.cameraeffects.filters.CameraFilter;
import com.example.cameraeffects.filters.ScreenFilter;
import com.example.cameraeffects.utils.CameraHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraEffectsViewRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{

    private int[] mTextures;
    private CameraHelper mCameraHelper;
    private SurfaceTexture mSurfaceTexture;
    private BigEyeView mView;
    private float[] mtx = new float[16];
    CameraFilter mCameraFilter;
    ScreenFilter mScreenFilter;

    public CameraEffectsViewRender(BigEyeView mView) {
        this.mView = mView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //打开摄像头
        mCameraHelper = new CameraHelper(Camera.CameraInfo.CAMERA_FACING_BACK);
        mTextures = new int[1];
        GLES20.glGenTextures(mTextures.length,mTextures,0);
        //摄像头采集数据
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        //矩阵 摄像头不会变形
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter = new CameraFilter(mView.getContext());
        mScreenFilter = new ScreenFilter(mView.getContext());
        mCameraFilter.setMatrix(mtx);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mCameraHelper.startPreview(mSurfaceTexture);

        mCameraFilter.onReady(width,height);
        mScreenFilter.onReady(width,height );
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //摄像头获取一帧数据后回掉此方法
        GLES20.glClearColor(0,0,0,0);
        //执行清空
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter.setMatrix(mtx);
        int id = mCameraFilter.onDrawFrame(mTextures[0]);
        mScreenFilter.onDrawFrame(id);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) { mView.requestRender(); }
}
