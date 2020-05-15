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


public class BigEyeView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private Bitmap mBitmap;
    private int vTexture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private int[] mTextures;
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
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test1);
    }

    public void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
        requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0,0,0,0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        String vertex = Utils.loadStringFromAssets(getContext(),"vertex.vert");
        String fragment =Utils.loadStringFromAssets(getContext(),"fragment.frag");

        //1、创建顶点着色器
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vertex);
        GLES20. glCompileShader(vertexShader);
        //2、创建片元着色器
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragment);
        GLES20.glCompileShader(fragmentShader);

       //3、建着色器程序
        int program=GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        //连接着色器
        GLES20.glLinkProgram(program);
        //使用着色器
        GLES20.glUseProgram(program);

        int vPosition = GLES20.glGetAttribLocation(program,"vPosition");
        int vCoord = GLES20.glGetAttribLocation(program, "vCoord");
        vTexture = GLES20.glGetUniformLocation(program, "vTexture");

        //顶点坐标
        vertexBuffer = ByteBuffer.allocateDirect(VERTEX.length*4).order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(VERTEX);
        vertexBuffer.position(0);
        //传值
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,vertexBuffer);
        //激活
        GLES20.glEnableVertexAttribArray(vPosition);

        //片元坐标数据
        textureBuffer = ByteBuffer.allocateDirect(TEXTURE.length*4).order(ByteOrder.nativeOrder())
                .asFloatBuffer().put(TEXTURE);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,textureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);


        mTextures = new int[1];
        GLES20.glGenTextures(1,mTextures,0);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,mBitmap,0);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glUniform1i(vTexture,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
    }
}
