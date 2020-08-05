package com.eliotlash.particlelib.particles.components;

import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;

public interface IComponentEmitterUpdate extends IComponentBase
{
	public void update(BedrockEmitter emitter);
}
