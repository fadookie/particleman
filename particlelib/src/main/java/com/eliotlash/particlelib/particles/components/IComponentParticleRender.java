package com.eliotlash.particlelib.particles.components;

import com.eliotlash.particlelib.mcwrapper.IBufferBuilder;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;

public interface IComponentParticleRender extends IComponentBase
{
	public void preRender(BedrockEmitter emitter, float partialTicks);

	public void render(BedrockEmitter emitter, BedrockParticle particle, IBufferBuilder builder, float partialTicks);

	public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks, IBufferBuilder builder);

	public void postRender(BedrockEmitter emitter, float partialTicks);
}
