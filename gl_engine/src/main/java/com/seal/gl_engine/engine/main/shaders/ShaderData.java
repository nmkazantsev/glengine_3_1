package com.seal.gl_engine.engine.main.shaders;

import com.seal.gl_engine.GamePageInterface;

public abstract class ShaderData {
    private final GamePageInterface gamePageInterface;

    protected ShaderData(GamePageInterface gamePageInterface) {
        this.gamePageInterface = gamePageInterface;
        Shader.getActiveShader().getAdaptor().addLightAdaptor(this);
    }

    protected String getCreatorClassName() {
        if (gamePageInterface != null) {
            return gamePageInterface.getClass().getName();
        }
        return null;
    }

    protected abstract void getLocations(int programId);

    protected abstract void forwardData();

    protected abstract void delete();
}
