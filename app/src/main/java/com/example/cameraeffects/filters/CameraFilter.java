package com.example.cameraeffects.filters;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.example.cameraeffects.R;
import com.example.cameraeffects.Utils;

import javax.microedition.khronos.opengles.GL;

public class CameraFilter extends AbstractFilter{
    //FBO
    int[] mFrameBuffer;
    int[] mFrameBufferTextures;

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    private float[] matrix;

    public CameraFilter(Context context) {
        super(context,"camera_vertex.vert","camera_frag.frag");
    }

    @Override
    protected void initCoordinate() {
        mTextureBuffer.clear();
        float[] TEXTURE = {0,0,0,1,1,0,1,1};
        mTextureBuffer.put(TEXTURE);
    }
    public void onReady(int width,int height){
        super.onReady(width,height);
        if(mFrameBuffer!=null){
            destroyFrameBuffers();
        }
        mFrameBuffer = new int[1];
        // 生成fbo
        GLES20.glGenFramebuffers(1,mFrameBuffer,0);
        //实例化一个纹理
        mFrameBufferTextures = new int[1];
        Utils.glGenTextures(mFrameBufferTextures);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mFrameBufferTextures[0]);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,mFrameBuffer[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,mWidth,mHeight,0,GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,null);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,mFrameBufferTextures[0],0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
    }
    private void destroyFrameBuffers(){
        if(mFrameBufferTextures!=null){
            GLES20.glDeleteTextures(1,mFrameBufferTextures,0);
            mFrameBufferTextures = null;
        }
    }
    @Override
    public int onDrawFrame(int textureId) {
        GLES20.glViewport(0,0,mWidth,mHeight);
        //不调用就是默认的操作glsurfaceview中的纹理了，显示到屏幕上
        //此操作让它只渲染到fbo中（缓存）
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,mFrameBuffer[0]);
        //use the program
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,mVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,mTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        //变换矩阵
        GLES20.glUniformMatrix4fv(vMatrix,1,false,matrix,0);
        //激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureId);
        GLES20.glUniform1i(vTexture,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);

        return mFrameBufferTextures[0];
    }
}
