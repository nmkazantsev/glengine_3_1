package com.manateam.glengine3.engine.main.holder;

import com.manateam.glengine3.engine.main.animator.Animator;
import com.manateam.glengine3.engine.main.engine_object.EnObject;

import java.util.function.Function;

public class Holder {
    private static Holder[] globalHolderList;
    private final EnObject target;
    private boolean active;
    private Animator anim;

    public Holder(EnObject t) {
        target = t;
        listHelder(this);
    }

    public void addAnimation(int tfType, float[] args, int vfType, float duration, float vfa, long st) {
        if (anim == null) anim = new Animator();
        anim.addAnimation(tfType, args, vfType, duration, vfa, st);
    }

    public void addAnimation(Function<Animator.Animation, float[]> tf, float[] args, Function<float[], Float> vf, float duration, float vfa, long st) {
        if (anim == null) anim = new Animator();
        anim.addAnimation(tf, args, vf, duration, vfa, st);
    }

    private void listHelder(Holder h) {
        if (globalHolderList == null) {
            globalHolderList = new Holder[]{h};
            return;
        }
        Holder[] b = new Holder[globalHolderList.length + 1];
        b[globalHolderList.length] = h;
        globalHolderList = b;
    }

    public static void globalProcess() {
        for (Holder h: globalHolderList) h.process();
    }

    private void process() {
        anim.animate(target);
    }
}
