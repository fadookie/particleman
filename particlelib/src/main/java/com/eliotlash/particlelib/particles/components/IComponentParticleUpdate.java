package com.eliotlash.particlelib.particles.components;

import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;

public interface IComponentParticleUpdate extends IComponentBase
{
	public void update(BedrockEmitter emitter, BedrockParticle particle);
}
