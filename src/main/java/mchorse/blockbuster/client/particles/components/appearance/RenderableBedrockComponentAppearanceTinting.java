package mchorse.blockbuster.client.particles.components.appearance;

import com.eliotlash.particlelib.particles.components.appearance.BedrockComponentAppearanceTinting;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import mchorse.blockbuster.client.particles.components.IComponentParticleRender;
import mchorse.blockbuster.client.particles.emitter.RenderableBedrockEmitter;
import net.minecraft.client.renderer.BufferBuilder;

public class RenderableBedrockComponentAppearanceTinting extends BedrockComponentAppearanceTinting implements IComponentParticleRender
{
	/* Interface implementations */
	@Override
	public void render(RenderableBedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks)
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
