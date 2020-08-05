package com.eliotlash.particlelib.particles.components.expiration;

import com.eliotlash.particlelib.mcwrapper.IBlock;
import com.eliotlash.particlelib.particles.components.IComponentParticleUpdate;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;

public class BedrockComponentExpireInBlocks extends BedrockComponentExpireBlocks implements IComponentParticleUpdate
{
	@Override
	public void update(BedrockEmitter emitter, BedrockParticle particle)
	{
		if (particle.dead || emitter.world == null)
		{
			return;
		}

		IBlock current = this.getBlock(emitter, particle);

		for (IBlock block : this.blocks)
		{
			if (block == current)
			{
				particle.dead = true;

				return;
			}
		}
	}
}
