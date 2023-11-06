package com.manateam.glengine3.utils;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.manateam.glengine3.MainActivity;
import com.manateam.glengine3.engine.main.images.PImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EngineUtils {
    public static long programStartTime;
    private static long stopTime;

    public static void onPause() {
        stopTime = millis();
    }

    public static void onResume() {
        long dt=millis() - stopTime;
        programStartTime += dt;
    }

    public static void delay(long t) {
        if (t > 0) {
            try {
                sleep(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static PImage scaleImg(PImage img, float w, float h) {
        img.bitmap = Bitmap.createScaledBitmap(img.bitmap, Utils.parseInt(w), Utils.parseInt(h), true);
        img.width = img.bitmap.getWidth();
        img.height = img.bitmap.getHeight();
        return img;
    }

    public static PImage scaleImg(PImage img, float scale) {
        img.width = img.bitmap.getWidth();
        img.height = img.bitmap.getHeight();
        img.bitmap = Bitmap.createScaledBitmap(img.bitmap, Utils.parseInt(img.width * scale), Utils.parseInt(img.height * scale), true);
        img.width = img.bitmap.getWidth();
        img.height = img.bitmap.getHeight();
        return img;
    }

    //todo: delete
    public static PImage deleteBackground(PImage img) {
                /*
                if (img == null) {
                    return (null);
                } else {
                    int colors[] = new int[img.bitmap.getWidth() * img.bitmap.getHeight()];
                    int src[] = new int[img.bitmap.getWidth() * img.bitmap.getHeight()];
                    int width = img.bitmap.getWidth();
                    int height = img.bitmap.getHeight();
                    img.bitmap.getPixels(src, 0, width, 0, 0, width, height);
                    for (int x = 0; x < width * height; x++) {

                        if (src[x] > Color.rgb(170, 170, 170)) {
                            //img.bitmap.setPixel(x,y,0);
                            colors[x] = Color.argb(0, 0, 0, 0);
                        } else {
                            //  colors[x] = src[x];
                            colors[x] = Color.rgb(10, 10, 10);
                        }
                    }
                    img.bitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
                }

                 */
        return (img);


    }

    public static PImage convertTo(PImage img, int red, int green, int blue) {
        if (img != null) {
            int[] colors = new int[img.bitmap.getWidth() * img.bitmap.getHeight()];
            int[] src = new int[img.bitmap.getWidth() * img.bitmap.getHeight()];
            int width = img.bitmap.getWidth();
            int height = img.bitmap.getHeight();
            img.bitmap.getPixels(src, 0, width, 0, 0, width, height);
            int porog = 90;
            for (int x = 0; x < width * height; x++) {
                if (src[x] < Color.rgb(porog, porog, porog)) {
                    //img.bitmap.setPixel(x,y,0);
                    colors[x] = Color.rgb(red, green, blue);
                } else {
                    //colors[x] =src[x];
                }
            }
            img.bitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
            return (img);
        }


        return (null);
    }

    public static PImage loadImage(String name) {
        try {
            PImage img = new PImage(getBitmapFromAssets(name, MainActivity.context));
            img.width = img.bitmap.getWidth();
            img.height = img.bitmap.getHeight();
            if (img == null) {
                Log.e("ERROR LOADING", name);
            }
            img.setLoaded(true);
            return img;
        } catch (Exception e) {
            Log.e("ERROR LOADING", name + String.valueOf(e.getMessage()));
        }
        return null;
    }

    private static Bitmap getBitmapFromAssets(String fileName, Context context) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    public static long millis() {
        return (System.currentTimeMillis() - programStartTime);
    }

    public static String loadFile(String fileName) {
        String content = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(Utils.context.getAssets().open(fileName), StandardCharsets.UTF_8));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                content += mLine + '\n';
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return content;
    }
}
