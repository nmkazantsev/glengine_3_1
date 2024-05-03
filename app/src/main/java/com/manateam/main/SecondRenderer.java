package com.manateam.main;

import static android.opengl.GLES20.GL_BLEND;
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
import static com.seal.gl_engine.utils.Utils.x;
import static com.seal.gl_engine.utils.Utils.y;

import android.opengl.GLES30;
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
import com.seal.gl_engine.engine.main.verticles.Poligon;
import com.seal.gl_engine.engine.main.verticles.Shape;
import com.seal.gl_engine.engine.main.verticles.SkyBox;
import com.seal.gl_engine.maths.Point;
import com.seal.gl_engine.maths.Vec3;
import com.seal.gl_engine.utils.SkyBoxShaderAdaptor;
import com.seal.gl_engine.utils.Utils;

public class SecondRenderer implements GamePageInterface {
    private final Poligon fpsPoligon;
    private final Shader shader, lightShader, skyBoxShader;
    private final ProjectionMatrixSettings projectionMatrixSettings;
    private final CameraSettings cameraSettings;
    private final Shape s;
    private SkyBox skyBox;
    private SourceLight sourceLight;
    private final AmbientLight ambientLight;
    private DirectedLight directedLight1;
    private Material material;
    private DirectedShadow directedShadow;
    // FrameBuffer fb;


    public SecondRenderer() {
        shader = new Shader(com.example.gl_engine.R.raw.vertex_shader, com.example.gl_engine.R.raw.fragment_shader, this, new MainShaderAdaptor());
        lightShader = new Shader(com.example.gl_engine.R.raw.vertex_shader_light, com.example.gl_engine.R.raw.fragment_shader_light, this, new LightShaderAdaptor());
        fpsPoligon = new Poligon(MainRedrawFunctions::redrawFps, true, 1, this);
        cameraSettings = new CameraSettings(x, y);
        cameraSettings.resetFor3d();
        projectionMatrixSettings = new ProjectionMatrixSettings(x, y);
        s = new Shape("ponchik.obj", "texture.png", this);
        s.addNormalMap("noral_tex.png");

        ambientLight = new AmbientLight(this);
        ambientLight.color = new Vec3(0.6f);

        material = new Material(this);
        material.ambient = new Vec3(1);
        material.specular = new Vec3(1);
        material.diffuse = new Vec3(1);
        material.shininess = 1.1f;

        skyBox = new SkyBox("skybox/", "jpg", this);
        skyBoxShader = new Shader(com.example.gl_engine.R.raw.skybox_vertex, com.example.gl_engine.R.raw.skybox_fragment, this, new SkyBoxShaderAdaptor());
        directedShadow = new DirectedShadow(this, (int)x, (int)y);
        // fb = createFrameBuffer((int) x, (int) y, this);
    }

    @Override
    public void draw() {
        GLES30.glDisable(GL_BLEND);
        cameraSettings.resetFor3d();
        projectionMatrixSettings.resetFor3d();
        cameraSettings.eyeZ = 0f;
        cameraSettings.eyeX = 5f;
        float x1 = 3.5f * Utils.sin(millis() / 1000.0f);
        cameraSettings.centerY = 0;
        cameraSettings.centerZ = x1;
       /* applyShader(skyBoxShader);
        applyProjectionMatrix(projectionMatrixSettings);
        applyCameraSettings(cameraSettings);
        skyBox.prepareAndDraw();*/

        //applyShader(shader);
        glClearColor(1f, 1, 1, 1);
        material.apply();

        directedShadow.startRenderingDepthPass();
        applyCameraSettings(cameraSettings);
        applyProjectionMatrix(projectionMatrixSettings);
        mMatrix = resetTranslateMatrix(mMatrix);
        //Matrix.rotateM(mMatrix, 0, map(millis() % 10000, 0, 10000, 0, 360), 1, 0.5f, 0);
        // Matrix.translateM(mMatrix, 0, 0, -0f, 0);
        // Matrix.scaleM(mMatrix, 0, 1.5f, 1.5f, 1.5f);
        applyMatrix(mMatrix);
        //connectFrameBuffer(fb.getFrameBuffer());
        s.prepareAndDraw();
        //connectDefaultFrameBuffer();
        directedShadow.stopRenderingDepthPass();
        applyShader(shader);
        fpsPoligon.setRedrawNeeded(true);
        cameraSettings.resetFor2d();
        projectionMatrixSettings.resetFor2d();
        applyProjectionMatrix(projectionMatrixSettings, false);
        applyCameraSettings(cameraSettings);
        mMatrix = resetTranslateMatrix(mMatrix);
        applyMatrix(mMatrix);
        fpsPoligon.redrawParams.set(0, String.valueOf(fps));
        fpsPoligon.redrawNow();
        fpsPoligon.prepareAndDraw(new Point(0 * kx, 0, 1), new Point(100 * kx, 0, 1), new Point(0 * kx, 100 * ky, 1));
        directedShadow.getDepthBuffer().drawTexture(new Point(x, y, 1), new Point(0, y, 1), new Point(x, 0, 1));
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
