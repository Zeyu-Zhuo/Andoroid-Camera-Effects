package com.example.cameraeffects;

import android.content.Context;
import android.opengl.GLES20;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String loadStringFromAssets(Context context,String filename){
        try {
            InputStream is = context.getAssets().open(filename);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] b = new byte[2048];
            while((len=is.read(b))!=-1){
                bos.write(b,0,len);
            }
            String s = new String(bos.toByteArray());
            bos.close();
            is.close();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int LoadProgram(String vSource,String fSource){
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader, vSource);
        GLES20. glCompileShader(vertexShader);
        //2、创建片元着色器
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fSource);
        GLES20.glCompileShader(fragmentShader);

        //3、建着色器程序
        int program=GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        //连接着色器
        GLES20.glLinkProgram(program);
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
        return program;
    }

    public static void glGenTextures(int[] texture) {
        GLES20.glGenTextures(texture.length,texture,0);
        for(int i = 0;i<texture.length;i++){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[i]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //纹理环绕方向
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);


            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        }
    }
}
