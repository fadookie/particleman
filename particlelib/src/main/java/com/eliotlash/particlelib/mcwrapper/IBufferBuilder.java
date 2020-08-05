package com.eliotlash.particlelib.mcwrapper;

import java.nio.ByteBuffer;

public interface IBufferBuilder {
    int GL_QUADS = 1;

//    void sortVertexData(float p_181674_1_, float p_181674_2_, float p_181674_3_);
//
//    IBufferBuilderState getVertexState();
//
//    void setVertexState(IBufferBuilderState state);
//
//    void reset();

    void begin(int glMode, VertexFormats format);

    IBufferBuilder tex(double u, double v);

    IBufferBuilder lightmap(int p_187314_1_, int p_187314_2_);

//    void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_);
//
//    void putPosition(double x, double y, double z);
//
//    int getColorIndex(int vertexIndex);
//
//    void putColorMultiplier(float red, float green, float blue, int vertexIndex);
//
//    void putColorRGB_F(float red, float green, float blue, int vertexIndex);
//
//    void putColorRGBA(int index, int red, int green, int blue);
//
//    void noColor();

    IBufferBuilder color(float red, float green, float blue, float alpha);

    IBufferBuilder color(int red, int green, int blue, int alpha);

//    void addVertexData(int[] vertexData);

    void endVertex();

    IBufferBuilder pos(double x, double y, double z);

//    void putNormal(float x, float y, float z);

//    IBufferBuilder normal(float x, float y, float z);

//    void setTranslation(double x, double y, double z);
//
//    void finishDrawing();
//
//    ByteBuffer getByteBuffer();

    VertexFormats getVertexFormat();

//    int getVertexCount();

//    int getDrawMode();

//    void putColor4(int argb);

//    void putColorRGB_F4(float red, float green, float blue);

    //For some unknown reason Mojang changed the vanilla function to hardcode alpha as 255.... So lets re-add the parameter -.-
//    void putColorRGBA(int index, int red, int green, int blue, int alpha);
//
//    boolean isColorDisabled();
//
//    void putBulkData(ByteBuffer buffer);
}
