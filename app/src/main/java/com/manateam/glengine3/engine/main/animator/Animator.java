package com.manateam.glengine3.engine.main.animator;

import static com.manateam.glengine3.utils.Utils.contactArray;
import static com.manateam.glengine3.utils.Utils.millis;
import static com.manateam.glengine3.utils.Utils.popFromArray;

import com.manateam.glengine3.engine.main.engine_object.EnObject;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class Animator {

    public static final int
            SHIFT = 0,
            ROTATION = 1,
            PIVOT_ROTATION = 2,
            LINEAR = 0,
            SIGMOID = 1;
    private static HashMap<EnObject, Animation[]> animQueue;

    public static void initialize() {
        animQueue = new HashMap<>();
    }

    public static void addAnimation(EnObject target, int tfType, float[] args, int vfType, float duration, float vfa, long st, Function<Animation, float[]> customTF, Function<float[], Float> customVF) {
        Function<Animation, float[]> tf = customTF;
        Function<float[], Float> vf = customVF;

        // tfType - defines witch attribute of EnObject is affected by animation (posMatrix = 0; rotMatrix = 1, combined = 2)
        if (customTF == null)
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

        if (customVF == null)
            switch (vfType) {
                case 0:
                    vf = FC::linear;
                    break;
                case 1:
                    vf = FC::sigmoid;
                    break;
            }

        new Animation(target, tfType, tf, args, vf, duration, vfa, st);
    }

    // adds animation without templates, every function has to be specified by hand
    public static void addAnimation() {
    }

    private static void listAnimation(Animation animation, EnObject target) {
        if (!animQueue.containsKey(target)) {
            animQueue.put(target, new Animation[]{animation});
            return;
        }
        Animation[] a = animQueue.get(target);
        if (a == null) animQueue.replace(target, new Animation[]{animation});
        else animQueue.replace(target, contactArray(a, new Animation[]{animation}));
    }

    public static void animate(EnObject target) {
        float[] b = target.getSpaceAttrs();
        for (Animation animation : Objects.requireNonNull(animQueue.get(target))) {
            if (!animation.isDead) {
                animation.setAttrs(b);
                b = animation.getAnimMatrix();
            } else {
                animQueue.replace(target, popFromArray(animQueue.get(target), animation));
            }
        }
        target.setSpaceAttrs(b);
    }

    public static class Animation {
        private boolean isActive;
        private boolean isDead;
        private final Function<Animation, float[]> tf; // transmission function
        private final Function<float[], Float> vf; // velocity function
        private final float[] args; // additional arguments
        private final float duration; // total duration
        // velocity function argument
        private final float vfa; // velocity function argument
        private final long startTiming; // global start timing in millis
        private float dtBuffer, dt;
        private float[] attrs;

        private Animation(EnObject target, int tfType, Function<Animation, float[]> tf, float[] args, Function<float[], Float> vf, float duration, float vfa, long st) {
            long c = millis();
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
            listAnimation(this, target);
            isDead = false;
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

        public float[] getAnimMatrix() {
            if (!isActive) {
                if (startTiming <= millis()) {
                    isActive = true;
                    return getAnimMatrix();
                }
                return attrs;
            }
            float gt = (millis() - startTiming) / duration; // global timing (linear from 0 to 1)
            float t = vf.apply(new float[]{gt, vfa}); // velocity function output for gt
            dt = t - dtBuffer; // difference in current and previous vf output (shift delta)
            dtBuffer = t;
            if (gt >= 1) isDead = true; // completion
            return tf.apply(this);
        }
    }
}
