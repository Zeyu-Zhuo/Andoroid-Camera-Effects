package com.example.cameraeffects.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import com.example.cameraeffects.BigEyeView;
import com.example.cameraeffects.Utils;
import com.example.cameraeffects.utils.CameraHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class AbstractFilter {
    protected FloatBuffer mVertexBuffer;
    protected FloatBuffer mTextureBuffer;
    protected int mProgram;
    protected int vMatrix;
    protected int vTexture;
    protected int vPosition;
    protected int vCoord;
    protected int mWidth;
    protected int mHeight;
    protected String vFilename;
    protected String fFilename;

    public void onReady(int width,int height){
        this.mWidth = width;
        this.mHeight = height;
    }

    public AbstractFilter(Context context,String vFilename, String fFilename) {
        this.vFilename = vFilename;
        this.fFilename = fFilename;

        mVertexBuffer = ByteBuffer.allocateDirect(4*2*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.clear();
        float VERTEX[] = {
                -1,-1,
                1,-1,
                -1,1,
                1,1
        };
        mVertexBuffer.put(VERTEX);

        mTextureBuffer = ByteBuffer.allocateDirect(4*2*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureBuffer.clear();
        float TEXTURE[] = {
                0,0,
                0,1,
                1,0,
                1,1
        };
        mTextureBuffer.put(TEXTURE);
        initialize(context);
        initCoordinate();
    }

    protected abstract void initCoordinate();

    private void initialize(Context context) {
        String vertex = Utils.loadStringFromAssets(context,vFilename);
        String fragment =Utils.loadStringFromAssets(context,fFilename);
        int program = Utils.LoadProgram(vertex,fragment);

        vPosition = GLES20.glGetAttribLocation(program,"vPosition");
        vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");
    }
    public int onDrawFrame(int textureId){
        GLES20.glViewport(0,0,mWidth,mHeight);
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,mVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,mTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);

        GLES20.glUniform1i(vTexture,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        return textureId;
    }

}
