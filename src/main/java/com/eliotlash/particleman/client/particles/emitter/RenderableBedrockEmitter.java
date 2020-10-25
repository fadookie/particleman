package com.eliotlash.particleman.client.particles.emitter;

import com.eliotlash.particlelib.mcwrapper.Size2f;
import com.eliotlash.particleman.mcwrapper.ConversionUtils;
import com.eliotlash.particleman.mcwrapper.WorldWrapper;
import com.eliotlash.mclib.utils.Interpolations;
import com.eliotlash.particlelib.Settings;
import com.eliotlash.particlelib.particles.components.IComponentParticleRenderBase;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import com.eliotlash.particleman.client.textures.GifTexture;
import com.eliotlash.particleman.client.particles.components.IComponentParticleRender;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.util.List;

public class RenderableBedrockEmitter extends BedrockEmitter
{
	private World concreteWorld;

	private BlockPos.Mutable blockPos = new BlockPos.Mutable();

	/* Camera properties */
	public PointOfView perspective;
	public float cYaw;
	public float cPitch;

	public double cX;
	public double cY;
	public double cZ;

	public double getDistanceSq()
	{
		this.setupCameraProperties(0F);

		double dx = this.cX - this.lastGlobal.x;
		double dy = this.cY - this.lastGlobal.y;
		double dz = this.cZ - this.lastGlobal.z;

		return dx * dx + dy * dy + dz * dz;
	}


	public double getDistanceSq(BedrockParticle particle)
	{
		Vector3d pos = particle.getGlobalPosition(this);

		double dx = cX - pos.x;
		double dy = cY - pos.y;
		double dz = cZ - pos.z;

		return dx * dx + dy * dy + dz * dz;
	}

	public void setTarget(Entity target)
	{
		this.target = ConversionUtils.entityToSize(target);
		setWorld(target == null ? null : target.world);
	}

	public void setTarget(Size2f size, World world)
	{
		target = size;
		setWorld(world);
	}

	public void setWorld(World world)
	{
		concreteWorld = world;
		this.world = concreteWorld == null ? null : new WorldWrapper(concreteWorld);
	}


	/**
	 * Render the particle on screen
	 */
	public void renderOnScreen(int x, int y, float scale)
	{
		if (this.scheme == null)
		{
			return;
		}

		float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
		List<IComponentParticleRender> list = this.scheme.getComponents(IComponentParticleRender.class);

		if (!list.isEmpty())
		{
		    // TODO cache or safe delete
			GifTexture.bindTexture(ConversionUtils.abstractToConcreteRL(this.scheme.texture));

			RenderSystem.enableBlend();
			RenderSystem.disableCull();

			if (this.guiParticle == null || this.guiParticle.dead)
			{
				this.guiParticle = this.createParticle(true);
			}

			this.rotation.setIdentity();
			this.guiParticle.update(this);
			this.setEmitterVariables(partialTicks);
			this.setParticleVariables(this.guiParticle, partialTicks);

			for (IComponentParticleRender render : list)
			{
				render.renderOnScreen(this.guiParticle, x, y, scale, partialTicks);
			}

			RenderSystem.disableBlend();
			RenderSystem.enableCull();
		}
	}

	@Override
	public void render(float partialTicks)
	{

	}

	/**
	 * Render all the particles in this particle emitter
	 */
	public void render(MatrixStack stack, float partialTicks, TextureManager renderer)
	{
		if (this.scheme == null)
		{
			return;
		}

		this.setupCameraProperties(partialTicks);

		BufferBuilder builder = Tessellator.getInstance().getBuffer();
		List<IComponentParticleRenderBase> renders = this.scheme.particleRender;

		for (IComponentParticleRenderBase component : renders)
		{
			component.preRender(this, partialTicks);
		}

		if (!this.particles.isEmpty())
		{
			if (Settings.getParticleSorting())
			{
				this.particles.sort((a, b) ->
				{
					double ad = this.getDistanceSq(a);
					double bd = this.getDistanceSq(b);

					if (ad < bd)
					{
						return 1;
					}
					else if (ad > bd)
					{
						return -1;
					}

					return 0;
				});
			}

			// TODO cache or delete
			GifTexture.bindTexture(ConversionUtils.abstractToConcreteRL(this.scheme.texture), this.age, partialTicks);
			renderer.bindTexture(ConversionUtils.abstractToConcreteRL(this.scheme.texture));
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

			for (BedrockParticle particle : this.particles)
			{
				this.setEmitterVariables(partialTicks);
				this.setParticleVariables(particle, partialTicks);

				for (IComponentParticleRenderBase component : renders)
				{
					if(component instanceof IComponentParticleRender) {
						((IComponentParticleRender) component).render(stack, this, particle, builder, partialTicks);
					}
				}
			}

			Tessellator.getInstance().draw();
		}

		for (IComponentParticleRenderBase component : renders)
		{
			component.postRender(this, partialTicks);
		}
	}

	public void setupCameraProperties(float partialTicks)
	{
		if (this.concreteWorld != null)
		{
			Entity camera = Minecraft.getInstance().getRenderViewEntity();


			boolean thirdPersonReverse = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().isThirdPerson();
			this.perspective = Minecraft.getInstance().gameSettings.func_243230_g();
			this.cYaw = 180 - Interpolations.lerp(camera.prevRotationYaw, camera.rotationYaw, partialTicks);
			this.cPitch = 180 - Interpolations.lerp(camera.prevRotationPitch, camera.rotationPitch, partialTicks);
			this.cX = Interpolations.lerp(camera.prevPosX, camera.getPosX(), partialTicks);
			this.cY = Interpolations.lerp(camera.prevPosY, camera.getPosY(), partialTicks) + camera.getEyeHeight();
			this.cZ = Interpolations.lerp(camera.prevPosZ, camera.getPosZ(), partialTicks);
		}
	}

	/**
	 * Get brightness for the block
	 */
	public int getBrightnessForRender(float partialTicks, double x, double y, double z)
	{
		if (this.lit || this.concreteWorld == null)
		{
			return 15728880;
		}

		this.blockPos.setPos(x, y, z);

		return this.concreteWorld.isBlockLoaded(this.blockPos) ? this.concreteWorld.getLightSubtracted(this.blockPos, 0) : 0;
	}
}
