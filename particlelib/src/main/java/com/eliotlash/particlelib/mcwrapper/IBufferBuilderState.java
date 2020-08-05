package com.eliotlash.particlelib.mcwrapper;

import javafx.scene.shape.VertexFormat;

public interface IBufferBuilderState {
    int[] getRawBuffer();

    int getVertexCount();

    VertexFormat getVertexFormat();
}
