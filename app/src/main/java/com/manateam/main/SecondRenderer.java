package com.manateam.main;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static com.seal.gl_engine.OpenGLRenderer.fps;
import static com.seal.gl_engine.OpenGLRenderer.mMatrix;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyCameraSettings;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyMatrix;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyProjectionMatrix;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.resetTranslateMatrix;
import static com.seal.gl_engine.engine.main.shaders.Shader.applyShader;
import static com.seal.gl_engine.utils.Utils.kx;
import static com.seal.gl_engine.utils.Utils.ky;
import static com.seal.gl_engine.utils.Utils.millis;
import static com.seal.gl_engine.utils.Utils.radians;
import static com.seal.gl_engine.utils.Utils.x;
import static com.seal.gl_engine.utils.Utils.y;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.manateam.main.redrawFunctions.MainRedrawFunctions;
import com.seal.gl_engine.GamePageInterface;
import com.seal.gl_engine.OpenGLRenderer;
import com.seal.gl_engine.default_adaptors.LightShaderAdaptor;
import com.seal.gl_engine.default_adaptors.MainShaderAdaptor;
import com.seal.gl_engine.engine.main.camera.CameraSettings;
import com.seal.gl_engine.engine.main.camera.ProjectionMatrixSettings;
import com.seal.gl_engine.engine.main.light.AmbientLight;
import com.seal.gl_engine.engine.main.light.DirectedLight;
import com.seal.gl_engine.engine.main.light.Material;
import com.seal.gl_engine.engine.main.light.SourceLight;
import com.seal.gl_engine.engine.main.shaders.Shader;
import com.seal.gl_engine.engine.main.shadows.DirectedShadow;
import com.seal.gl_engine.engine.main.verticles.Polygon;
import com.seal.gl_engine.engine.main.verticles.Shape;
import com.seal.gl_engine.maths.Point;
import com.seal.gl_engine.maths.Vec3;
import com.seal.gl_engine.utils.Utils;

public class SecondRenderer implements GamePageInterface {
    private final Polygon fpsPolygon;
    private final Shader shader, lightShader;
    private final ProjectionMatrixSettings projectionMatrixSettings;
    private final CameraSettings cameraSettings;
    private final Shape ponchik, p2, cube;
    private SourceLight sourceLight;
    private final AmbientLight ambientLight;
    private DirectedLight directedLight1;
    private Material material;
    private DirectedShadow directedShadow;
    // FrameBuffer fb;


    public SecondRenderer() {
        shader = new Shader(com.example.gl_engine.R.raw.vertex_shader, com.example.gl_engine.R.raw.fragment_shader, this, new MainShaderAdaptor());
        lightShader = new Shader(com.example.gl_engine.R.raw.vertex_shader_light, com.example.gl_engine.R.raw.fragment_shader_light, this, new LightShaderAdaptor());
        fpsPolygon = new Polygon(MainRedrawFunctions::redrawFps, true, 1, this);
        cameraSettings = new CameraSettings(x, y);
        cameraSettings.resetFor3d();
        projectionMatrixSettings = new ProjectionMatrixSettings(x, y);

        ponchik = new Shape("ponchik.obj", "texture.png", this);
        ponchik.addNormalMap("noral_tex.png");
        p2 = new Shape("ponchik.obj", "texture.png", this);
        p2.addNormalMap("noral_tex.png");
        cube = new Shape("cube.obj", "cube.png", this);

        ambientLight = new AmbientLight(this);
        ambientLight.color = new Vec3(0.6f);

        material = new Material(this);
        material.ambient = new Vec3(1);
        material.specular = new Vec3(1);
        material.diffuse = new Vec3(1);
        material.shininess = 1.1f;

        directedLight1 = new DirectedLight(this);
        directedLight1.direction = new Vec3(1);
        directedLight1.color = new Vec3(1);
        directedLight1.diffuse = 0.8f;
        directedLight1.specular = 0.9f;

        directedShadow = new DirectedShadow(this, (int) x, (int) y, directedLight1, shader);
        material = new Material(this);
        material.ambient = new Vec3(1);
        material.diffuse = new Vec3(1);
        material.shininess = 1.1f;
        material.specular = new Vec3(1);

    }

    @Override
    public void draw() {
        //  GLES30.glDisable(GL_BLEND);
        cameraSettings.resetFor3d();
        cameraSettings.eyeZ = 0f;
        cameraSettings.eyeY = 4;
        cameraSettings.eyeX = 2.5f;
        cameraSettings.centerY = 0;
        cameraSettings.centerZ = 0;
        projectionMatrixSettings.resetFor3d();


        glClearColor(1f, 1, 1, 1);

        //  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //  directedShadow.startRenderingDepthPass();
        applyShader(shader);
        material.apply();
        applyProjectionMatrix(projectionMatrixSettings);
        applyCameraSettings(cameraSettings);
        drawScene(1);
        // directedShadow.stopRenderingDepthPass();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        applyShader(lightShader);
        material.apply();
        applyProjectionMatrix(projectionMatrixSettings);
        applyCameraSettings(cameraSettings);

        drawScene(2);

        applyShader(shader);

        cameraSettings.resetFor2d();
        projectionMatrixSettings.resetFor2d();
        applyProjectionMatrix(projectionMatrixSettings, false);
        applyCameraSettings(cameraSettings);
        mMatrix = resetTranslateMatrix(mMatrix);
        applyMatrix(mMatrix);
        fpsPoligon.redrawParams.set(0, String.valueOf(fps));
        fpsPoligon.redrawNow();
        fpsPoligon.prepareAndDraw(new Point(0 * kx, 0, 1), new Point(100 * kx, 0, 1), new Point(0 * kx, 100 * ky, 1));
        directedShadow.getDepthBuffer().drawTexture(new Point(x, y / 2, 1), new Point(x / 2, y / 2, 1), new Point(x, y, 1));
    }

    private void drawScene(int x) {
        Matrix.rotateM(mMatrix, 0, 90, 0, 0, 1);
        Matrix.scaleM(mMatrix, 0, 0.2f, 10, 10);
        applyMatrix(mMatrix);
        // cube.prepareAndDraw();
        mMatrix = resetTranslateMatrix(mMatrix);
        Matrix.translateM(mMatrix, 0, 0, 1.5f + x, 0);
        applyMatrix(mMatrix);
        if (x == 1) {
            ponchik.prepareAndDraw();
        } else {
            ponchik.prepareAndDraw();
        }
        mMatrix = resetTranslateMatrix(mMatrix);
    }

    @Override
    public void touchStarted() {
        Log.e("touch", "statred");
        OpenGLRenderer.startNewPage(new MainRenderer());
    }

    @Override
    public void touchMoved() {

    }

    @Override
    public void touchEnded() {

    }
}
