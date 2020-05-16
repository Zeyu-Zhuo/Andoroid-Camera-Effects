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
    private Bitmap mBitmap;
    private int vTexture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private int[] mTextures;
    private CameraEffectsViewRender cevr = new CameraEffectsViewRender();
    float VERTEX[] = {
            -1,1,
            -1,-1,
            1,1,
            1,-1
    };
    float TEXTURE[] = {
            0,0,
            0,1,
            1,0,
            1,1
    };

    public BigEyeView(Context context) {
        super(context,null );
    }

    public BigEyeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        cevr.setmBitmap(mBitmap);
        cevr.setContext(context);
        setRenderer(cevr);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test1);
    }

    public void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
        requestRender();
    }

    public CameraEffectsViewRender getCameraEffectsViewRender() {
        return this.cevr;
    }
}
