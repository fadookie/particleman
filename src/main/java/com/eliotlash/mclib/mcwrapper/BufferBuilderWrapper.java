package com.eliotlash.mclib.mcwrapper;

import com.eliotlash.particlelib.mcwrapper.IBufferBuilder;
import com.eliotlash.particlelib.mcwrapper.VertexFormats;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class BufferBuilderWrapper implements IBufferBuilder {
    BufferBuilder builder;

    public BufferBuilderWrapper(BufferBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void begin(int glMode, VertexFormats format) {
        builder.begin(glMode, ConversionUtils.parseVertexEnum(format));
    }

    @Override
    public IBufferBuilder tex(double u, double v) {
        builder.tex(u, v);
        return this;
    }

    @Override
    public IBufferBuilder lightmap(int p_187314_1_, int p_187314_2_) {
        builder.lightmap(p_187314_1_, p_187314_2_);
        return this;
    }

    @Override
    public IBufferBuilder color(float red, float green, float blue, float alpha) {
        builder.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public IBufferBuilder color(int red, int green, int blue, int alpha) {
        builder.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public void endVertex() {
        builder.endVertex();
    }

    @Override
    public IBufferBuilder pos(double x, double y, double z) {
        builder.pos(x, y, z);
        return this;
    }

    @Override
    public VertexFormats getVertexFormat() {
        return ConversionUtils.vertexFormatToEnum(builder.getVertexFormat());
    }
}
