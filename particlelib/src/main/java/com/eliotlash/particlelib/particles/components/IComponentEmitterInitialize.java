package com.eliotlash.particlelib.particles.components;

import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;

public interface IComponentEmitterInitialize extends IComponentBase
{
	public void apply(BedrockEmitter emitter);
}
