package com.seal.gl_engine.engine.main.engine_object;

import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyMatrix;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.resetTranslateMatrix;
import static com.seal.gl_engine.engine.main.animator.Animator.addAnimation;
import static com.seal.gl_engine.utils.Utils.contactArray;


import android.opengl.Matrix;

import com.seal.gl_engine.engine.main.animator.Animator;
import com.seal.gl_engine.engine.main.verticles.Shape;
import com.seal.gl_engine.utils.Utils;

import java.util.Arrays;

public class EnObject {
    private final Shape shape;
    private float[] posMatrix;
    private float[] rotMatrix;

    public float[] getSpaceAttrs() {
        return Utils.contactArray(posMatrix, rotMatrix);
    }

    public void setSpaceAttrs(float[] attrs) {
        posMatrix = Arrays.copyOfRange(attrs, 0, 3);
        rotMatrix = Arrays.copyOfRange(attrs, 3, 6);
    }

    public EnObject(Shape shape) {
        this.shape = shape;
        posMatrix = new float[3];
        rotMatrix = new float[3];
    }

    public void animMotion(float x, float y, float z, float duration, long startTiming) {
        Animator.addAnimation(this,
                Animator.SHIFT,
                new float[]{x, y, z},
                Animator.LINEAR,
                duration,
                0,
                startTiming);
    }

    public void animRotation(float x, float y, float z, float duration, long startTiming) {
        Animator.addAnimation(this,
                Animator.ROTATION,
                new float[]{x, y, z},
                Animator.LINEAR,
                duration,
                0,
                startTiming);
    }

    public void animPivotRotation(float x, float y, float z, float vx, float vy, float vz, float duration, long startTiming) {
        Animator.addAnimation(this,
                Animator.PIVOT_ROTATION,
                new float[]{x, y, z, vx, vy, vz},
                Animator.LINEAR,
                duration,
                0,
                startTiming);
    }

    public void prepareAndDraw() {
        Animator.animate(this);
        float[] b = new float[16];
        resetTranslateMatrix(b);
        Matrix.translateM(b, 0, posMatrix[0], posMatrix[1], posMatrix[2]);
        Matrix.rotateM(b, 0, rotMatrix[0], 1, 0, 0);
        Matrix.rotateM(b, 0, rotMatrix[1], 0, 1, 0);
        Matrix.rotateM(b, 0, rotMatrix[2], 0, 0, 1);
        applyMatrix(b);
        shape.prepareAndDraw();
    }
}