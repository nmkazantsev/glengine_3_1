package com.seal.gl_engine.engine.main.shadows;

import static com.seal.gl_engine.engine.main.frameBuffers.FrameBufferUtils.connectDefaultFrameBuffer;
import static com.seal.gl_engine.engine.main.frameBuffers.FrameBufferUtils.connectFrameBuffer;
import static com.seal.gl_engine.engine.main.frameBuffers.FrameBufferUtils.createFrameBuffer;
import static com.seal.gl_engine.engine.main.shaders.Shader.applyShader;

import com.example.gl_engine.R;
import com.seal.gl_engine.GamePageInterface;
import com.seal.gl_engine.default_adaptors.MainShaderAdaptor;
import com.seal.gl_engine.engine.main.frameBuffers.FrameBuffer;
import com.seal.gl_engine.engine.main.shaders.Shader;


public class DirectedShadow {
    private final GamePageInterface creator;
    private FrameBuffer depthBuffer;
    private Shader depthShader;

    public DirectedShadow(GamePageInterface gamePageInterface, int shadowWidth, int shadowHeight) {
        this.creator = gamePageInterface;
        depthBuffer = createFrameBuffer(shadowWidth, shadowHeight, creator);
        depthShader = new Shader(R.raw.vertex_shader, R.raw.fragment_shader, creator, new MainShaderAdaptor());
    }

    public void startRenderingDepthPass() {
        applyShader(depthShader);
        connectFrameBuffer(depthBuffer.getFrameBuffer());
    }

    public void stopRenderingDepthPass() {
        connectDefaultFrameBuffer();
    }

    public FrameBuffer getDepthBuffer() {
        return depthBuffer;
    }
}
