package com.eliotlash.particlelib.particles.components;

import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;

public interface IComponentParticleInitialize extends IComponentBase
{
	public void apply(BedrockEmitter emitter, BedrockParticle particle);
}
