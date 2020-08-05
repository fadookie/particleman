package com.eliotlash.particlelib.particles.components.appearance;

import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.components.IComponentEmitterInitialize;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;

public class BedrockComponentAppearanceLighting extends BedrockComponentBase implements IComponentEmitterInitialize
{
	@Override
	public void apply(BedrockEmitter emitter)
	{
		emitter.lit = false;
	}

	@Override
	public boolean canBeEmpty()
	{
		return true;
	}
}
