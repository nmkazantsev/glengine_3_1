package com.manateam.glengine3.engine.main.animator;

import static com.manateam.glengine3.OpenGLRenderer.pageMillis;

import android.widget.AdapterView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.manateam.glengine3.engine.main.engine_object.sealObj;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class Animator {

    // indexes for constructing animation via template constructor
    public static final int
            SHIFT = 0,
            ROTATION = 1,
            PIVOT_ROTATION = 2,
            LINEAR = 0,
            SIGMOID = 1;
    private Hashtable<Long, Animation> animQueue;
    private long lastUnusedIndex;

    // template constructor itself, uses predefined indexes instead of manual function specifying
    public void addAnimation(int tfType, float[] args, int vfType, float duration, float vfa, long st) {
        Function<Animation, float[]> tf = null;
        Function<float[], Float> vf = null;

        // tfType - defines witch attribute of EnObject is affected by animation (posMatrix = 0; rotMatrix = 1, combined = 2)
        switch (tfType) {
            case 0:
                tf = FC::shift;
                break;
            case 1:
                tf = FC::rotate;
                break;
            case 2:
                tf = FC::pivotRotation;
                break;
        }

        switch (vfType) {
            case 0:
                vf = FC::linear;
                break;
            case 1:
                vf = FC::sigmoid;
                break;
        }

        listAnimation(new Animation(tf, args, vf, duration, vfa, st, lastUnusedIndex));
    }

    // adds animation without templates, every function has to be specified by hand
    /*
    Example of using specific functions.
    First argument is an EnObject instance that is being target of the animation.
    Second argument is a function that takes an instance of Animation and returns an array of 6 floats,
    first 3 are position, second 3 defines rotation
    (mention this are is not deltas, this are changed attribute).
    Third argument is a function that defines rate of affect on attributes, it takes array
    containing value from 0 to 1 (0 is the beginning of the animation, 1 is the very last moment)
    and some argument, the function must return value from 0 to 1 as well, as was mentioned before
    0 is fist position of the animation, 1 is the very last.
    Then goes duration, single velocity function attribute and start timing.
    Example of full function call from EnObject class:
    addAnimation(
        this,
        (Animator.Animation animation) -> {
            float[] attrs = animation.getAttrs();
            float[] args = animation.getArgs();
            return attrs;
        },
        new float[3],
        (float[] f) -> {
            float k = f[0];
            float a = f[1];
            return f[0];
        },
        1000,
        1.0f,
        5000
    );
     */
    public void addAnimation(Function<Animation, float[]> tf, float[] args, Function<float[], Float> vf, float duration, float vfa, long st) {
        listAnimation(new Animation(tf, args, vf, duration, vfa, st, lastUnusedIndex));
    }

    private void listAnimation(Animation animation) {
        if (animQueue == null) {
            animQueue = new Hashtable<>();
            animQueue.put(lastUnusedIndex, animation);
            lastUnusedIndex++;
            return;
        }
        animQueue.put(lastUnusedIndex, animation);
        lastUnusedIndex++;
    }

    public void animate(sealObj target) {
        // getting targets space attributes
        float[] b = target.getSpaceAttrs();
        // getting array related to the object and going though it
        Enumeration<Long> keys = animQueue.keys();
        while(keys.hasMoreElements()) {
            Long key = keys.nextElement();
            Animation animation = animQueue.get(key);
            if (!animation.isDead) {
                // giving attributes to the animator and getting computation result
                animation.setAttrs(b);
                b = animation.getAnimMatrix();
            } else {
                // deleting "dead" animation
                animQueue.remove(key);
            }
        }
        // writing affected attributes back
        target.setSpaceAttrs(b);
    }

    public static class Animation {
        private boolean isActive; // false only in case animation has not achieved start timing yet
        private boolean isDead; // become true if animation worked out and ready to be deleted
        private final Function<Animation, float[]> tf; // transmission function
        private final Function<float[], Float> vf; // velocity function
        private final float[] args; // additional arguments
        private final float duration; // total duration
        // velocity function argument
        private final float vfa; // velocity function argument
        private final long startTiming; // global start timing in millis
        private float dtBuffer, dt; // buffer for proper dt computing and dt itself (can't be local)
        private float[] attrs; // attributes like position and rotation
        private final long index;

        private Animation(Function<Animation, float[]> tf, float[] args, Function<float[], Float> vf, float duration, float vfa, long st, long i) {
            long c = pageMillis();
            if (st <= c) {
                startTiming = c;
                isActive = true;
            } else {
                startTiming = st;
                isActive = false;
            }
            this.tf = tf;
            this.vf = vf;
            this.args = args;
            this.duration = duration;
            this.vfa = vfa;
            this.dtBuffer = 0;
            isDead = false;
            index = i;
        }

        public float[] getAttrs() {
            return attrs.clone();
        }

        public void setAttrs(float[] attrs) {
            this.attrs = attrs;
        }

        public float[] getArgs() {
            return args.clone();
        }

        public float getDeltaT() {
            return dt;
        }

        // function that returns changes in attributes according to current time and arguments
        public float[] getAnimMatrix() {
            if (!isActive) {
                if (startTiming <= pageMillis()) {
                    isActive = true;
                    return getAnimMatrix();
                }
                return attrs;
            }
            float gt = (pageMillis() - startTiming) / duration; // global timing (linear from 0 to 1)
            float t = vf.apply(new float[]{gt, vfa}); // velocity function output for gt
            dt = t - dtBuffer; // difference in current and previous vf output (shift delta)
            dtBuffer = t;
            if (gt >= 1) isDead = true; // completion
            return tf.apply(this);
        }
    }
}
