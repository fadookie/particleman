package mchorse.blockbuster.client.particles.components;

import com.eliotlash.particlelib.particles.components.IComponentParticleRenderBase;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import mchorse.blockbuster.client.particles.emitter.RenderableBedrockEmitter;
import net.minecraft.client.renderer.BufferBuilder;

public interface IComponentParticleRender extends IComponentParticleRenderBase
{
	public void render(RenderableBedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks);

	public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks);
}
