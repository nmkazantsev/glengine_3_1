package com.seal.gl_engine.engine.main.shadows;

import static android.opengl.GLES20.glViewport;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyCameraSettings;
import static com.seal.gl_engine.engine.config.MainConfigurationFunctions.applyProjectionMatrix;
import static com.seal.gl_engine.engine.main.frameBuffers.FrameBufferUtils.connectDefaultFrameBuffer;
import static com.seal.gl_engine.engine.main.frameBuffers.FrameBufferUtils.connectFrameBuffer;
import static com.seal.gl_engine.engine.main.frameBuffers.FrameBufferUtils.createFrameBuffer;
import static com.seal.gl_engine.engine.main.shaders.Shader.applyShader;
import static com.seal.gl_engine.utils.Utils.x;
import static com.seal.gl_engine.utils.Utils.y;

import android.opengl.GLES30;

import com.example.gl_engine.R;
import com.seal.gl_engine.GamePageInterface;
import com.seal.gl_engine.default_adaptors.MainShaderAdaptor;
import com.seal.gl_engine.engine.main.camera.CameraSettings;
import com.seal.gl_engine.engine.main.camera.ProjectionMatrixSettings;
import com.seal.gl_engine.engine.main.frameBuffers.FrameBuffer;
import com.seal.gl_engine.engine.main.light.DirectedLight;
import com.seal.gl_engine.engine.main.shaders.Shader;


public class DirectedShadow {
    private final GamePageInterface creator;
    private FrameBuffer depthBuffer;
    private Shader depthShader;
    private DirectedLight directedLight;

    private CameraSettings cameraSettings;
    private ProjectionMatrixSettings projectionMatrixSettings;
    private final float multCoeff = 10;
    private int width, height;

    public DirectedShadow(GamePageInterface gamePageInterface, int shadowWidth, int shadowHeight, DirectedLight lightSource, Shader s) {
        cameraSettings = new CameraSettings(shadowWidth, shadowHeight);
        projectionMatrixSettings = new ProjectionMatrixSettings(shadowWidth, shadowHeight);
        this.creator = gamePageInterface;
        depthBuffer = createFrameBuffer(shadowWidth, shadowHeight, creator);
        depthShader = null;//new Shader(R.raw.vertex_shader, R.raw.fragment_shader, creator, new MainShaderAdaptor());
        this.directedLight = lightSource;
        this.width = shadowWidth;
        this.height = shadowHeight;
        this.depthShader = s;
    }

    public void startRenderingDepthPass() {
        cameraSettings.resetFor3d();
        cameraSettings.eyeZ = 0f;
        cameraSettings.eyeY = 4;
        cameraSettings.eyeX = 2.5f;
        cameraSettings.centerY = 0;
        cameraSettings.centerZ = 0;
        projectionMatrixSettings.resetFor3d();


        //applyShader(depthShader);
       // applyCameraSettings(cameraSettings);
        //applyProjectionMatrix(projectionMatrixSettings, true); //go to light space view
        glViewport(0, 0, width, height);
        connectFrameBuffer(depthBuffer.getFrameBuffer());
    }

    public void stopRenderingDepthPass() {
        glViewport(0, 0, (int) x, (int) y);
        connectDefaultFrameBuffer();
    }

    public FrameBuffer getDepthBuffer() {
        return depthBuffer;
    }
}
