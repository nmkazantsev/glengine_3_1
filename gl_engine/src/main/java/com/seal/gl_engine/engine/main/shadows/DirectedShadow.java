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

    public DirectedShadow(GamePageInterface gamePageInterface, int shadowWidth, int shadowHeight, DirectedLight lightSource) {
        cameraSettings = new CameraSettings(shadowWidth, shadowHeight);
        projectionMatrixSettings = new ProjectionMatrixSettings(shadowWidth, shadowHeight);
        this.creator = gamePageInterface;
        depthBuffer = createFrameBuffer(shadowWidth, shadowHeight, creator);
        depthShader = new Shader(R.raw.vertex_shader, R.raw.fragment_depth_shader, creator, new MainShaderAdaptor());
        this.directedLight = lightSource;
        this.width = shadowWidth;
        this.height = shadowHeight;
    }

    public void startRenderingDepthPass() {
        cameraSettings.resetFor3d();
        cameraSettings.centerX = cameraSettings.centerY = cameraSettings.centerZ = 0;
        cameraSettings.eyeX = directedLight.direction.x * multCoeff;
        cameraSettings.eyeY = directedLight.direction.y * multCoeff;
        cameraSettings.eyeZ = directedLight.direction.z * multCoeff;

        projectionMatrixSettings.resetFor2d();
        projectionMatrixSettings.left = -10f;
        projectionMatrixSettings.right = 10f;
        projectionMatrixSettings.bottom = -10f;
        projectionMatrixSettings.top = 10f;
        projectionMatrixSettings.near = 0.1f;
        projectionMatrixSettings.far = 20;

        applyShader(depthShader);
        applyCameraSettings(cameraSettings);
        applyProjectionMatrix(projectionMatrixSettings, false); //go to light space view
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
