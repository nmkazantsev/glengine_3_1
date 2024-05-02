package com.seal.gl_engine.engine.main.shadows;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glVertexAttribPointer;

import android.opengl.GLES30;

import com.seal.seal_engine.engine.main.shaders.Adaptor;
import com.seal.seal_engine.engine.main.vertex_bueffer.VertexBuffer;
import com.seal.seal_engine.engine.main.verticles.Face;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class DepthShaderAdaptor extends Adaptor {
    private int aPositionLocation;
    private int projectionMatrixLoation;
    private int viewMatrixLocation;
    private int modelMtrixLocation;

    private final static int POSITION_COUNT = 3;
    private static final int STRIDE = (POSITION_COUNT) * 4;

    @Override
    public int bindData(Face[] faces) {
        float[] vertices = new float[faces.length * faces[0].verticesNumber()];
        int vertexesNumber = 0;
        for (int i = 0; i < faces.length; i++) {
            //copy first 3 values - coordinates
            System.arraycopy(faces[i].getArrayRepresentation(), 0, vertices, i * faces[i].verticesNumber(), 3);
            vertexesNumber++;
        }
        FloatBuffer vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);//4 байта на флоат
        // координаты вершин
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
        return vertexesNumber;
    }

    private void loadDataToBuffer(float[] vertices, int bufferIndex, VertexBuffer vertexBuffer) {
        FloatBuffer vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);//4 байта на флоат
        vertexBuffer.bindVbo(bufferIndex);//vertex coords
        vertexData.position(0);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * 4, vertexData, GL_STATIC_DRAW);
    }

    @Override
    public int bindData(Face[] faces, VertexBuffer vertexBuffer) {
        //set up positions
        float[] vertices = new float[3 * 3];//3 because 3angle
        for (int i = 0; i < faces.length; i++) {
            vertices[i * 3] = faces[i].vertices[0].x;
            vertices[i * 3 + 1] = faces[i].vertices[0].y;
            vertices[i * 3 + 2] = faces[i].vertices[0].z;
        }
        loadDataToBuffer(vertices, 0, vertexBuffer);

        vertexBuffer.bindVao();
        glEnableVertexAttribArray(aPositionLocation);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer.getVboAdress(0));
        glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 0, 0);
        vertexBuffer.bindDefaultVbo();//vertex coords
        vertexBuffer.bindDefaultVao();
        return 0;
    }

    @Override
    public void updateLocations() {
        aPositionLocation = glGetAttribLocation(programId, "aPos");
        projectionMatrixLoation = GLES30.glGetUniformLocation(programId, "projection");
        viewMatrixLocation = GLES30.glGetUniformLocation(programId, "view");
        modelMtrixLocation = GLES30.glGetUniformLocation(programId, "model");
    }

    @Override
    public int getTransformMatrixLocation() {
        return modelMtrixLocation;
    }

    @Override
    public int getCameraLocation() {
        return viewMatrixLocation;
    }

    @Override
    public int getProjectionLocation() {
        return projectionMatrixLoation;
    }

    @Override
    public int getTextureLocation() {
        return -1;
    }

    @Override
    public int getNormalTextureLocation() {
        return -1;
    }

    @Override
    public int getCameraPosLlocation() {
        return -1;
    }
}
