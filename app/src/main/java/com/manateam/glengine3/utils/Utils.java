package com.manateam.glengine3.utils;


import static java.lang.Float.parseFloat;
import static java.lang.Thread.sleep;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.Random;

public class Utils {
    public static Context context;
    public static float kx, ky, x, y;

    public static void background(int r, int b, int g) {
        GLES20.glClearColor(r / 255.0f, g / 255.0f, b / 255.0f, 1);
    }

    public static void showToast(final String text) {
        //Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //лень искать нормальное решение
    public static boolean intInArray(int n, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == n) {
                return true;
            }
        }
        return false;
    }

    public static float findDrot(float rot, float aimRot) {
        aimRot %= 360;
        rot %= 360;
        if (rot < 0) {
            rot = 360 + rot;
        }
        if (aimRot < 0) {
            aimRot = 360 + aimRot;
        }
        float drot = aimRot - rot;
        if (MathUtils.abs(drot) > 180) {
            if (drot > 0) {
                drot = (360 - drot) % 360;
                return -drot;
            } else {
                drot = 360 - MathUtils.abs(drot);
            }
        }
        return drot;
    }

    public static float[] contactArray(float[] a, float[] b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        float[] r = new float[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }


    public static float cutTail(float i, int s) {
        //оставляет s знаков после запятой у числа i
        float p = (int) Math.pow(10, s);
        return parseInt(i * p) / p;
    }

    public static String getString(int p) {
        return context.getString(p);
    }


    public static float random(float a, float b) {
        Random random = new Random();
        a *= 100;
        b *= 100;
        if (a > b) {
            float c = b;
            b = a;
            a = c;
        }
        float dif = b - a;
        return (random.nextInt(parseInt(dif + 1)) + a) / 100.0f;
    }

    public static int parseInt(float i) {
        return ((int) i);
    }


    public static int parseInt(String i) {
        return ((int) parseFloat(i));
    }

    public static int parseInt(boolean i) {
        if (i) {
            return (1);
        }
        return (0);
    }

    public static boolean parseBoolean(String s) {
        return parseInt(s) != 0;
    }

    public static boolean parseBoolean(float s) {
        return s != 0;
    }

    public static int[] parseInt(String[] s) {
        int[] out = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            out[i] = parseInt(s[i]);
        }
        return out;
    }

    public static int[] contactArray(int[] a, int[] b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        int[] r = new int[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static int[][] contactArray(int[][] a, int[][] b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        int[][] r = new int[a.length + b.length][(int) MathUtils.max(a[0].length, b[0].length)];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static int[] append(int[] a, int b) {
        int[] r = new int[a.length + 1];
        System.arraycopy(a, 0, r, 0, a.length);
        r[a.length] = b;
        return r;
    }

    public static float[] append(float[] a, float b) {
        float[] r = new float[a.length + 1];
        System.arraycopy(a, 0, r, 0, a.length);
        r[a.length] = b;
        return r;
    }


    public static float map(float val, float vstart, float vstop, float ostart, float ostop) {
        float dif = vstop - vstart;
        //val -= dif;
        val -= vstart;
        float proc = val / dif;
        float dif2 = ostop - ostart;
        float output = dif2 * proc + ostart;
        return output;
    }

    public static int countSubstrs(String str, String target) {
        return (str.length() - str.replace(target, "").length()) / target.length();
    }

    public static String[] split1(String s, char a) {
        String[] out;
        out = s.split(String.valueOf(a));
        return out;
    }


}
