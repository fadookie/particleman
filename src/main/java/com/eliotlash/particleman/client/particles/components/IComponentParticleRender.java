package com.eliotlash.particleman.client.particles.components;

import com.eliotlash.particlelib.particles.components.IComponentParticleRenderBase;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;

public interface IComponentParticleRender extends IComponentParticleRenderBase
{
	public void render(MatrixStack stack, ActiveRenderInfo info, RenderableBedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks);

	public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks);
}
