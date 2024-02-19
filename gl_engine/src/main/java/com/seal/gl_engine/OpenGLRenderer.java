package com.seal.gl_engine;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.resetTranslateMatrix;
import static com.seal.gl_engine.utils.Utils.parseInt;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.seal.gl_engine.engine.main.frameBuffers.FrameBuffer;
import com.seal.gl_engine.engine.config.MainConfigurationFunctions;
import com.seal.gl_engine.engine.main.frameBuffers.FrameBufferUtils;
import com.seal.gl_engine.engine.main.shaders.Shader;
import com.seal.gl_engine.engine.main.textures.Texture;
import com.seal.gl_engine.engine.main.verticles.VectriesShapesManager;
import com.seal.gl_engine.utils.Utils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements Renderer {

    public static float fps;
    private long prevFps;
    private int cadrs;
    public static float[] mMatrix = new float[16];
    private static GamePageInterface gamePage;
    private boolean firstStart = true;
    private static long prevPageChangeTime = 0;


    public OpenGLRenderer(Context context, float width, float height) {
        Utils.x = width;
        Utils.y = height;
        Utils.ky = Utils.y / 1280.0f;
        Utils.kx = Utils.x / 720.0f;
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        gaphicsSetup();
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);
        mMatrix = MainConfigurationFunctions.resetTranslateMatrix(mMatrix);
        if (firstStart) {
            Utils.programStartTime = System.currentTimeMillis();
            setup();
            firstStart = false;
        }
        Log.e("gl_error_in_setup", String.valueOf(GLES20.glGetError()));
        if (Utils.millis() > 60 * 60 * 1000) {
            //smth went wrong...
            Utils.programStartTime = System.currentTimeMillis();
            prevPageChangeTime = Utils.millis();
        }
        VectriesShapesManager.redrawAllSetup();
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        Log.e("surface changed", String.valueOf(Utils.x));
    }

    private void gaphicsSetup() {
        Shader.updateAllLocations();
        Texture.reloadAll();
        VectriesShapesManager.onRedrawSetup();
        FrameBuffer.onRedraw();
    }

    private void setup() {
        prevPageChangeTime = Utils.millis();
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        //calculate fps:
        if (Utils.millis() - prevFps > 100) {
            fps = 1000 / (int) ((Utils.millis() - prevFps) / (float) cadrs);
            prevFps = Utils.millis();
            cadrs = 0;
        }
        cadrs++;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw();
        VectriesShapesManager.redrawAll();
        touchEvent();
    }

    private void draw() {
        if(gamePage==null){
            startNewPage(Engine.getStartPage.apply(null));
        }
        gamePage.draw();
    }

    public void touchEvent() {
        for (int i = 0; i < Utils.min(Engine.touchEventsNumb, 100); i++) {
            for (int j = 0; j < 10; j++) {
                Engine.touches[j].x = Engine.touchEvents[i][j * 2];
                Engine.touches[j].y = Engine.touchEvents[i][j * 2 + 1];
            }
            float y = Engine.touchEvents[i][20];
            Engine.pointsNumber = Utils.parseInt(Engine.touchEvents[i][21]);
            if (y == 0) {
                touchStarted();
                Engine.touchEvent = "";
            }
            if (y == 1) {
                touchMoved();
                Engine.touchEvent = "";
            }
            if (y == 2) {
                touchEnded();
                Engine.touchEvent = "";
            }
        }
        Engine.touchEvents = new float[100][22];
        Engine.touchEventsNumb = 0;
    }

    public static void startNewPage(GamePageInterface newPage) {
        prevPageChangeTime = Utils.millis();
        gamePage = null;
        System.gc();
        gamePage = newPage;
        Texture.onPageChanged();
        FrameBufferUtils.onPageChanged();
        Shader.onPageChange();
    }

    public static long pageMillis() {
        return Utils.millis() - prevPageChangeTime;
    }

    public static String getPageClassName() {
        return gamePage.getClass().getName();
    }


    static void touchStarted() {
        gamePage.touchStarted();
    }

    static void touchMoved() {
        gamePage.touchMoved();
    }

    static void touchEnded() {
        gamePage.touchEnded();
    }
}