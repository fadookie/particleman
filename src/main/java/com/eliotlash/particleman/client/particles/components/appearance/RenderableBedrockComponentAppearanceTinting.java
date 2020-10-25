package com.eliotlash.particleman.client.particles.components.appearance;

import com.eliotlash.particlelib.particles.components.appearance.BedrockComponentAppearanceTinting;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import com.eliotlash.particleman.client.particles.components.IComponentParticleRender;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;

public class RenderableBedrockComponentAppearanceTinting extends BedrockComponentAppearanceTinting implements IComponentParticleRender
{
	/* Interface implementations */
	@Override
	public void render(MatrixStack stack, ActiveRenderInfo info, RenderableBedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks)
	{
		this.renderOnScreen(particle, 0, 0, 0, 0);
	}

	@Override
	public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks)
	{
		if (this.color != null)
		{
			this.color.compute(particle);
		}
		else
		{
			particle.r = particle.g = particle.b = particle.a = 1;
		}
	}
}
