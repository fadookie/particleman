package com.eliotlash.particlelib.particles.components.lifetime;

import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;

public class BedrockComponentLifetimeOnce extends BedrockComponentLifetime
{
	@Override
	public void update(BedrockEmitter emitter)
	{
		emitter.lifetime = (int) (this.activeTime.get() * 20);

		if (emitter.getAge() >= emitter.lifetime)
		{
			emitter.stop();
		}
	}
}
