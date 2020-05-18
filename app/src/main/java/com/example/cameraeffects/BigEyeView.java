package com.example.cameraeffects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class BigEyeView extends GLSurfaceView {

    public BigEyeView(Context context) {
        super(context,null );
    }

    public BigEyeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new CameraEffectsViewRender(this));
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

}
