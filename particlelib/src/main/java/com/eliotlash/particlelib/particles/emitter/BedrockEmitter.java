package com.eliotlash.particlelib.particles.emitter;

import com.eliotlash.particlelib.Settings;
import com.eliotlash.particlelib.mcwrapper.*;
import com.eliotlash.particlelib.particles.components.IComponentParticleInitialize;
import com.eliotlash.particlelib.particles.components.IComponentParticleUpdate;
import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particlelib.particles.components.IComponentEmitterInitialize;
import com.eliotlash.particlelib.particles.components.IComponentEmitterUpdate;
import com.eliotlash.particlelib.particles.components.IComponentParticleRender;
//import mchorse.blockbuster.client.textures.GifTexture;
import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.math.Variable;
import com.eliotlash.mclib.utils.Interpolations;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.world.World;
//import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BedrockEmitter
{
	public BedrockScheme scheme;
	public List<BedrockParticle> particles = new ArrayList<BedrockParticle>();
	public Map<String, IValue> variables;

	public IEntity target;
	public IWorld world;
	public boolean lit;

	public boolean added;
	public int sanityTicks;
	public boolean running = true;
	private BedrockParticle guiParticle;

	/* Intermediate values */
	public Vector3d lastGlobal = new Vector3d();
	public Matrix3f rotation = new Matrix3f();

	/* Runtime properties */
	public int age;
	public int lifetime;
	public double spawnedParticles;
	public boolean playing = true;

	public float random1 = (float) Math.random();
	public float random2 = (float) Math.random();
	public float random3 = (float) Math.random();
	public float random4 = (float) Math.random();

	private BlockPos blockPos = new BlockPos();

	/* Camera properties */
	public int perspective;
	public float cYaw;
	public float cPitch;

	public double cX;
	public double cY;
	public double cZ;

	/* Cached variable references to avoid hash look ups */
	private Variable varAge;
	private Variable varLifetime;
	private Variable varRandom1;
	private Variable varRandom2;
	private Variable varRandom3;
	private Variable varRandom4;

	private Variable varEmitterAge;
	private Variable varEmitterLifetime;
	private Variable varEmitterRandom1;
	private Variable varEmitterRandom2;
	private Variable varEmitterRandom3;
	private Variable varEmitterRandom4;

	public boolean isFinished()
	{
		return !this.running && this.particles.isEmpty();
	}

	public double getDistanceSq(int perspective, IEntity camera)
	{
		this.setupCameraProperties(0F, perspective, camera);

		double dx = this.cX -  this.lastGlobal.x;
		double dy = this.cY -  this.lastGlobal.y;
		double dz = this.cZ -  this.lastGlobal.z;

		return dx * dx + dy * dy + dz * dz;
	}

	public double getAge()
	{
		return this.getAge(0);
	}

	public double getAge(float partialTicks)
	{
		return (this.age + partialTicks) / 20.0;
	}

	public void setTarget(IEntity target)
	{
		this.target = target;
		this.world = target == null ? null : target.getWorld();
	}

	public void setScheme(BedrockScheme scheme)
	{
		this.scheme = scheme;

		if (this.scheme == null)
		{
			return;
		}

		this.lit = true;
		this.stop();
		this.start();

		this.setupVariables();
		this.setEmitterVariables(0);

		for (IComponentEmitterInitialize component : this.scheme.emitterInitializes)
		{
			component.apply(this);
		}
	}

	/* Variable related code */

	public void setupVariables()
	{
		this.varAge = this.scheme.parser.variables.get("variable.particle_age");
		this.varLifetime = this.scheme.parser.variables.get("variable.particle_lifetime");
		this.varRandom1 = this.scheme.parser.variables.get("variable.particle_random_1");
		this.varRandom2 = this.scheme.parser.variables.get("variable.particle_random_2");
		this.varRandom3 = this.scheme.parser.variables.get("variable.particle_random_3");
		this.varRandom4 = this.scheme.parser.variables.get("variable.particle_random_4");

		this.varEmitterAge = this.scheme.parser.variables.get("variable.emitter_age");
		this.varEmitterLifetime = this.scheme.parser.variables.get("variable.emitter_lifetime");
		this.varEmitterRandom1 = this.scheme.parser.variables.get("variable.emitter_random_1");
		this.varEmitterRandom2 = this.scheme.parser.variables.get("variable.emitter_random_2");
		this.varEmitterRandom3 = this.scheme.parser.variables.get("variable.emitter_random_3");
		this.varEmitterRandom4 = this.scheme.parser.variables.get("variable.emitter_random_4");
	}

	public void setParticleVariables(BedrockParticle particle, float partialTicks)
	{
		if (this.varAge != null) this.varAge.set(particle.getAge(partialTicks));
		if (this.varLifetime != null) this.varLifetime.set(particle.lifetime / 20.0);
		if (this.varRandom1 != null) this.varRandom1.set(particle.random1);
		if (this.varRandom2 != null) this.varRandom2.set(particle.random2);
		if (this.varRandom3 != null) this.varRandom3.set(particle.random3);
		if (this.varRandom4 != null) this.varRandom4.set(particle.random4);

		this.scheme.updateCurves();
	}

	public void setEmitterVariables(float partialTicks)
	{
		if (this.varEmitterAge != null) this.varEmitterAge.set(this.getAge(partialTicks));
		if (this.varEmitterLifetime != null) this.varEmitterLifetime.set(this.lifetime / 20.0);
		if (this.varEmitterRandom1 != null) this.varEmitterRandom1.set(this.random1);
		if (this.varEmitterRandom2 != null) this.varEmitterRandom2.set(this.random2);
		if (this.varEmitterRandom3 != null) this.varEmitterRandom3.set(this.random3);
		if (this.varEmitterRandom4 != null) this.varEmitterRandom4.set(this.random4);

		this.scheme.updateCurves();
	}

	public void parseVariables(Map<String, String> variables)
	{
		this.variables = new HashMap<String, IValue>();

		for (Map.Entry<String, String> entry : variables.entrySet())
		{
			this.parseVariable(entry.getKey(), entry.getValue());
		}
	}

	public void parseVariable(String name, String expression)
	{
		try
		{
			this.variables.put(name, this.scheme.parser.parseNoRegister(expression));
		}
		catch (Exception e)
		{}
	}

	public void replaceVariables()
	{
		if (this.variables == null)
		{
			return;
		}

		for (Map.Entry<String, IValue> entry : this.variables.entrySet())
		{
			Variable var = this.scheme.parser.variables.get(entry.getKey());

			if (var != null)
			{
				var.set(entry.getValue().get());
			}
		}
	}

	public void start()
	{
		if (this.playing)
		{
			return;
		}

		this.age = 0;
		this.spawnedParticles = 0;
		this.playing = true;
	}

	public void stop()
	{
		if (!this.playing)
		{
			return;
		}

		this.spawnedParticles = 0;
		this.playing = false;
	}

	/**
	 * Update this current emitter
	 */
	public void update()
	{
		if (this.scheme == null)
		{
			return;
		}

		this.setEmitterVariables(0);

		for (IComponentEmitterUpdate component : this.scheme.emitterUpdates)
		{
			component.update(this);
		}

		this.setEmitterVariables(0);
		this.updateParticles();

		this.age += 1;
		this.sanityTicks += 1;
	}

	/**
	 * Update all particles
	 */
	private void updateParticles()
	{
		Iterator<BedrockParticle> it = this.particles.iterator();

		while (it.hasNext())
		{
			BedrockParticle particle = it.next();

			this.updateParticle(particle);

			if (particle.dead)
			{
				it.remove();
			}
		}
	}

	/**
	 * Update a single particle
	 */
	private void updateParticle(BedrockParticle particle)
	{
		particle.update(this);

		this.setParticleVariables(particle, 0);

		for (IComponentParticleUpdate component : this.scheme.particleUpdates)
		{
			component.update(this, particle);
		}
	}

	/**
	 * Spawn a particle
	 */
	public void spawnParticle()
	{
		if (!this.running)
		{
			return;
		}

		this.particles.add(this.createParticle(false));
	}

	/**
	 * Create a new particle
	 */
	private BedrockParticle createParticle(boolean forceRelative)
	{
		BedrockParticle particle = new BedrockParticle();

		this.setParticleVariables(particle, 0);
		particle.setupMatrix(this);

		for (IComponentParticleInitialize component : this.scheme.particleInitializes)
		{
			component.apply(this, particle);
		}

		if (particle.relativePosition && !particle.relativeRotation)
		{
			Vector3f vec = new Vector3f(particle.position);

			particle.matrix.transform(vec);
			particle.position.x = vec.x;
			particle.position.y = vec.y;
			particle.position.z = vec.z;
		}

		if (!(particle.relativePosition && particle.relativeRotation))
		{
			particle.position.add(this.lastGlobal);
			particle.initialPosition.add(this.lastGlobal);
		}

		particle.prevPosition.set(particle.position);
		particle.rotation = particle.initialRotation;
		particle.prevRotation = particle.rotation;

		return particle;
	}

	/**
	 * Render the particle on screen
	 */
	public void renderOnScreen(int x, int y, float scale, float partialTicks, IBufferBuilder bufferBuilder)
	{
		if (this.scheme == null)
		{
			return;
		}

//		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		List<IComponentParticleRender> list = this.scheme.getComponents(IComponentParticleRender.class);

		if (!list.isEmpty())
		{
//			GifTexture.bindTexture(this.scheme.texture);

//			GlStateManager.enableBlend();
//			GlStateManager.disableCull();

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
				render.renderOnScreen(this.guiParticle, x, y, scale, partialTicks, bufferBuilder);
			}

//			GlStateManager.disableBlend();
//			GlStateManager.enableCull();
		}
	}

	/**
	 * Render all the particles in this particle emitter
	 */
	public void render(float partialTicks, IBufferBuilder builder, int perspective, IEntity camera)
	{
		if (this.scheme == null)
		{
			return;
		}

		this.setupCameraProperties(partialTicks, perspective, camera);

//		BufferBuilder builder = Tessellator.getInstance().getBuffer();
		List<IComponentParticleRender> renders = this.scheme.particleRender;

		for (IComponentParticleRender component : renders)
		{
			component.preRender(this, partialTicks);
		}

		if (!this.particles.isEmpty())
		{
			if (Settings.getParticleSorting())
			{
				this.particles.sort((a, b) ->
				{
					double ad = a.getDistanceSq(this);
					double bd = b.getDistanceSq(this);

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

//			GifTexture.bindTexture(this.scheme.texture, this.age, partialTicks);
			builder.begin(IBufferBuilder.GL_QUADS, VertexFormats.POSITION_TEX_LMAP_COLOR);

			for (BedrockParticle particle : this.particles)
			{
				this.setEmitterVariables(partialTicks);
				this.setParticleVariables(particle, partialTicks);

				for (IComponentParticleRender component : renders)
				{
					component.render(this, particle, builder, partialTicks);
				}
			}

//			Tessellator.getInstance().draw();
		}

		for (IComponentParticleRender component : renders)
		{
			component.postRender(this, partialTicks);
		}
	}

	public void setupCameraProperties(float partialTicks, int perspective, IEntity camera)
	{
		if (this.world != null)
		{
//			Entity camera = Minecraft.getMinecraft().getRenderViewEntity();

//			this.perspective = Minecraft.getMinecraft().gameSettings.thirdPersonView;
            this.perspective = perspective;
			this.cYaw = 180 - Interpolations.lerp(camera.getPrevRotationYaw(), camera.getRotationYaw(), partialTicks);
			this.cPitch = 180 - Interpolations.lerp(camera.getPrevRotationPitch(), camera.getRotationPitch(), partialTicks);
			this.cX = Interpolations.lerp(camera.getPrevPosX(), camera.getPosX(), partialTicks);
			this.cY = Interpolations.lerp(camera.getPrevPosY(), camera.getPosY(), partialTicks) + camera.getEyeHeight();
			this.cZ = Interpolations.lerp(camera.getPrevPosZ(), camera.getPosZ(), partialTicks);
		}
	}

	/**
	 * Get brightness for the block
	 */
	public int getBrightnessForRender(float partialTicks, double x, double y, double z)
	{
		if (this.lit || this.world == null)
		{
			return 15728880;
		}

		this.blockPos.setPos((int)x, (int)y, (int)z);

		return this.world.isBlockLoaded(this.blockPos) ? this.world.getCombinedLight(this.blockPos, 0) : 0;
	}
}
