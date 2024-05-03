package com.manateam.main;

import static android.opengl.GLES20.glClearColor;
import static com.seal.gl_engine.OpenGLRenderer.mMatrix;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyCameraSettings;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyMatrix;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyProjectionMatrix;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.resetTranslateMatrix;
import static com.seal.gl_engine.engine.main.shaders.Shader.applyShader;
import static com.seal.gl_engine.utils.Utils.millis;
import static com.seal.gl_engine.utils.Utils.x;
import static com.seal.gl_engine.utils.Utils.y;

import com.seal.gl_engine.GamePageInterface;
import com.seal.gl_engine.default_adaptors.MainShaderAdaptor;
import com.seal.gl_engine.engine.main.camera.CameraSettings;
import com.seal.gl_engine.engine.main.camera.ProjectionMatrixSettings;
import com.seal.gl_engine.engine.main.shaders.Shader;
import com.seal.gl_engine.engine.main.verticles.Shape;
import com.seal.gl_engine.engine.main.verticles.SkyBox;
import com.seal.gl_engine.utils.SkyBoxShaderAdaptor;
import com.seal.gl_engine.utils.Utils;

public class SkyBoxDemo implements GamePageInterface {
    private final Shader shader, skyBoxShader;
    private final Shape s;
    private final SkyBox skyBox;
    private final ProjectionMatrixSettings projectionMatrixSettings;
    private final CameraSettings cameraSettings;

    public SkyBoxDemo() {
        shader = new Shader(com.example.gl_engine.R.raw.vertex_shader, com.example.gl_engine.R.raw.fragment_shader, this, new MainShaderAdaptor());
        skyBox = new SkyBox("skybox/", "jpg", this);
        skyBoxShader = new Shader(com.example.gl_engine.R.raw.skybox_vertex, com.example.gl_engine.R.raw.skybox_fragment, this, new SkyBoxShaderAdaptor());
        s = new Shape("ponchik.obj", "texture.png", this);
        s.addNormalMap("noral_tex.png");

        cameraSettings = new CameraSettings(x, y);
        cameraSettings.resetFor3d();
        projectionMatrixSettings = new ProjectionMatrixSettings(x, y);
    }

    @Override
    public void draw() {
        cameraSettings.resetFor3d();
        projectionMatrixSettings.resetFor3d();
        cameraSettings.eyeZ = 0f;
        cameraSettings.eyeX = 5f;
        float x1 = 3.5f * Utils.sin(millis() / 1000.0f);
        cameraSettings.centerY = 0;
        cameraSettings.centerZ = x1;

        glClearColor(1f, 1, 1, 1);
        applyShader(skyBoxShader);
        applyProjectionMatrix(projectionMatrixSettings);
        applyCameraSettings(cameraSettings);
        skyBox.prepareAndDraw();

        applyShader(shader);
        mMatrix = resetTranslateMatrix(mMatrix);
        applyMatrix(mMatrix);
        applyProjectionMatrix(projectionMatrixSettings);
        applyCameraSettings(cameraSettings);
        s.prepareAndDraw();
    }

    @Override
    public void touchStarted() {

    }

    @Override
    public void touchMoved() {

    }

    @Override
    public void touchEnded() {

    }
}
