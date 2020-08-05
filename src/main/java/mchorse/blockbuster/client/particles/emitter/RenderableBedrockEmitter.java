package mchorse.blockbuster.client.particles.emitter;

import com.eliotlash.mclib.mcwrapper.ConversionUtils;
import com.eliotlash.mclib.mcwrapper.EntityWrapper;
import com.eliotlash.mclib.mcwrapper.WorldWrapper;
import com.eliotlash.particlelib.particles.components.IComponentParticleRenderBase;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.client.particles.components.IComponentParticleRender;
import mchorse.blockbuster.client.textures.GifTexture;
import com.eliotlash.mclib.utils.Interpolations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.util.List;

public class RenderableBedrockEmitter extends BedrockEmitter
{
	private World concreteWorld;
//	public boolean lit;
//
//	public boolean added;
//	public int sanityTicks;
//	public boolean running = true;
//	private BedrockParticle guiParticle;
//
//	/* Intermediate values */
//	public Vector3d lastGlobal = new Vector3d();
//	public Matrix3f rotation = new Matrix3f();
//
//	/* Runtime properties */
//	public int age;
//	public int lifetime;
//	public double spawnedParticles;
//	public boolean playing = true;
//
//	public float random1 = (float) Math.random();
//	public float random2 = (float) Math.random();
//	public float random3 = (float) Math.random();
//	public float random4 = (float) Math.random();
//
	private BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
//
	/* Camera properties */
	public int perspective;
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

	public void setTarget(EntityLivingBase target)
	{
		this.target = new EntityWrapper(target);
		this.concreteWorld = target == null ? null : target.world;
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

		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		List<IComponentParticleRender> list = this.scheme.getComponents(IComponentParticleRender.class);

		if (!list.isEmpty())
		{
		    // TODO cache or safe delete
			GifTexture.bindTexture(ConversionUtils.abstractToConcreteRL(this.scheme.texture));

			GlStateManager.enableBlend();
			GlStateManager.disableCull();

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

			GlStateManager.disableBlend();
			GlStateManager.enableCull();
		}
	}

	/**
	 * Render all the particles in this particle emitter
	 */
	public void render(float partialTicks)
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
			if (Blockbuster.particleSorting.get())
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
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

			for (BedrockParticle particle : this.particles)
			{
				this.setEmitterVariables(partialTicks);
				this.setParticleVariables(particle, partialTicks);

				for (IComponentParticleRenderBase component : renders)
				{
					((IComponentParticleRender)component).render(this, particle, builder, partialTicks);
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
			Entity camera = Minecraft.getMinecraft().getRenderViewEntity();

			this.perspective = Minecraft.getMinecraft().gameSettings.thirdPersonView;
			this.cYaw = 180 - Interpolations.lerp(camera.prevRotationYaw, camera.rotationYaw, partialTicks);
			this.cPitch = 180 - Interpolations.lerp(camera.prevRotationPitch, camera.rotationPitch, partialTicks);
			this.cX = Interpolations.lerp(camera.prevPosX, camera.posX, partialTicks);
			this.cY = Interpolations.lerp(camera.prevPosY, camera.posY, partialTicks) + camera.getEyeHeight();
			this.cZ = Interpolations.lerp(camera.prevPosZ, camera.posZ, partialTicks);
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

		return this.concreteWorld.isBlockLoaded(this.blockPos) ? this.concreteWorld.getCombinedLight(this.blockPos, 0) : 0;
	}
}
