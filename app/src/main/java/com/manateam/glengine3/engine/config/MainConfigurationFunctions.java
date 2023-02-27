package com.manateam.glengine3.engine.config;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.manateam.glengine3.engine.main.camera.CameraSettings;
import com.manateam.glengine3.engine.main.camera.ProjectionMatrixSettings;
import com.manateam.glengine3.engine.main.shaders.Shader;

public class MainConfigurationFunctions {
    private static float[] mProjectionMatrix = new float[16];
    private static float[] mViewMatrix = new float[16];


    public static Context context;

    public static void bindAllMatrix(CameraSettings c, ProjectionMatrixSettings p, float[] mMatrix) {
        applyMatrix(mMatrix);
        applyProjectionMatrix(p);
        applyCameraSettings(c);
    }

    public static void applyProjectionMatrix(ProjectionMatrixSettings p, boolean perspectiveEnabled) {
        //choose wehther use perspective or not
        if (perspectiveEnabled) {
            Matrix.frustumM(mProjectionMatrix, 0, p.left, p.right, p.bottom, p.top, p.near, p.far);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, p.left, p.right, p.bottom, p.top, p.near, p.far);
        }
        glUniformMatrix4fv(Shader.getActiveShader().getAdaptor().getProjectionLocation(), 1, false, mProjectionMatrix, 0);
    }

    public static void applyProjectionMatrix(ProjectionMatrixSettings p) {
        //perspective is always enabled here
        Matrix.frustumM(mProjectionMatrix, 0, p.left, p.right, p.bottom, p.top, p.near, p.far);
        glUniformMatrix4fv(Shader.getActiveShader().getAdaptor().getProjectionLocation(), 1, false, mProjectionMatrix, 0);
    }

    public static void applyCameraSettings(CameraSettings cam) {
        Matrix.setLookAtM(mViewMatrix, 0, cam.eyeX, cam.eyeY, cam.eyeZ, cam.centerX, cam.centerY, cam.centerZ, cam.upX, cam.upY, cam.upZ);
        glUniformMatrix4fv(Shader.getActiveShader().getAdaptor().getCameraLocation(), 1, false, mViewMatrix, 0);
    }

    public static void applyMatrix(float[] mMatrix) {
        glUniformMatrix4fv(Shader.getActiveShader().getAdaptor().getTransformMatrixLocation(), 1, false, mMatrix, 0);
    }

    public static float[] resetTranslateMatrix(float mMatrix[]) {
        Matrix.setIdentityM(mMatrix, 0);
        return mMatrix;
    }
}
