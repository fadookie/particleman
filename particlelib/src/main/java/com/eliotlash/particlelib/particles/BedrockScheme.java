package com.eliotlash.particlelib.particles;

import com.eliotlash.mclib.math.Variable;
import com.eliotlash.particlelib.mcwrapper.ResourceLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.components.IComponentBase;
import com.eliotlash.particlelib.particles.components.IComponentEmitterInitialize;
import com.eliotlash.particlelib.particles.components.IComponentEmitterUpdate;
import com.eliotlash.particlelib.particles.components.IComponentParticleInitialize;
import com.eliotlash.particlelib.particles.components.IComponentParticleRenderBase;
import com.eliotlash.particlelib.particles.components.IComponentParticleUpdate;
import com.eliotlash.particlelib.particles.components.motion.BedrockComponentInitialSpeed;
import com.eliotlash.molang.MolangParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BedrockScheme
{
	static ResourceLocation defaultTexture; //  = new ResourceLocation(Blockbuster.MOD_ID, "textures/default_particles.png");
	public static void setDefaultTexture(ResourceLocation defaultTexture) {
		BedrockScheme.defaultTexture = defaultTexture;
	}

	public static Gson JSON_PARSER;

	/**
	 * MUST be called before trying to parse JSON to register your concrete JSON adapter
	 * @param jsonAdapter Implementing your concrete types for renderable components
	 */
	public static void setJsonAdapter(BedrockSchemeJsonAdapter jsonAdapter) {
		JSON_PARSER = new GsonBuilder()
				.registerTypeAdapter(BedrockScheme.class, jsonAdapter)
				.create();
	}

	/* Particles identifier */
	public String identifier = "";

	/* Particle description */
	public BedrockMaterial material = BedrockMaterial.OPAQUE;
	public ResourceLocation texture = defaultTexture;

	/* Particle's curves */
	public Map<String, BedrockCurve> curves = new HashMap<String, BedrockCurve>();

	/* Particle's components */
	public List<BedrockComponentBase> components = new ArrayList<BedrockComponentBase>();
	public List<IComponentEmitterInitialize> emitterInitializes;
	public List<IComponentEmitterUpdate> emitterUpdates;
	public List<IComponentParticleInitialize> particleInitializes;
	public List<IComponentParticleUpdate> particleUpdates;
	public List<IComponentParticleRenderBase> particleRender;

	private boolean factory;

	/* MoLang integration */
	public MolangParser parser;

	public static BedrockScheme parse(String json)
	{
		return JSON_PARSER.fromJson(json, BedrockScheme.class);
	}

	public static BedrockScheme parse(JsonElement json)
	{
		return JSON_PARSER.fromJson(json, BedrockScheme.class);
	}

	public static JsonElement toJson(BedrockScheme scheme)
	{
		return JSON_PARSER.toJsonTree(scheme);
	}

	/**
	 * Probably it's very expensive, but it's much easier than implementing copy methods
	 * to every component in the particle system...
	 */
	public static BedrockScheme dupe(BedrockScheme scheme)
	{
		return parse(toJson(scheme));
	}

	public BedrockScheme()
	{
		this.parser = new MolangParser();

		/* Default variables */
		this.parser.register(new Variable("variable.particle_age", 0));
		this.parser.register(new Variable("variable.particle_lifetime", 0));
		this.parser.register(new Variable("variable.particle_random_1", 0));
		this.parser.register(new Variable("variable.particle_random_2", 0));
		this.parser.register(new Variable("variable.particle_random_3", 0));
		this.parser.register(new Variable("variable.particle_random_4", 0));
		this.parser.register(new Variable("variable.emitter_age", 0));
		this.parser.register(new Variable("variable.emitter_lifetime", 0));
		this.parser.register(new Variable("variable.emitter_random_1", 0));
		this.parser.register(new Variable("variable.emitter_random_2", 0));
		this.parser.register(new Variable("variable.emitter_random_3", 0));
		this.parser.register(new Variable("variable.emitter_random_4", 0));
	}

	public BedrockScheme factory(boolean factory)
	{
		this.factory = factory;

		return this;
	}

	public boolean isFactory()
	{
		return this.factory;
	}

	public void setup()
	{
		this.getOrCreate(BedrockComponentInitialSpeed.class);

		this.emitterInitializes = this.getComponents(IComponentEmitterInitialize.class);
		this.emitterUpdates = this.getComponents(IComponentEmitterUpdate.class);
		this.particleInitializes = this.getComponents(IComponentParticleInitialize.class);
		this.particleUpdates = this.getComponents(IComponentParticleUpdate.class);
		this.particleRender = this.getComponents(IComponentParticleRenderBase.class);

		/* Link variables with curves */
		for (Map.Entry<String, BedrockCurve> entry : this.curves.entrySet())
		{
			entry.getValue().variable = this.parser.variables.get(entry.getKey());
		}
	}

	public <T extends IComponentBase> List<T> getComponents(Class<T> clazz)
	{
		List<T> list = new ArrayList<T>();

		for (BedrockComponentBase component : this.components)
		{
			if (clazz.isAssignableFrom(component.getClass()))
			{
				list.add((T) component);
			}
		}

		if (list.size() > 1)
		{
			Collections.sort(list, Comparator.comparingInt(IComponentBase::getSortingIndex));
		}

		return list;
	}

	public <T extends BedrockComponentBase> T get(Class<T> clazz)
	{
		for (BedrockComponentBase component : this.components)
		{
			if (clazz.isAssignableFrom(component.getClass()))
			{
				return (T) component;
			}
		}

		return null;
	}

	public <T extends BedrockComponentBase> T add(Class<T> clazz)
	{
		T result = null;

		try
		{
			result = (T) clazz.getConstructor().newInstance();

			this.components.add(result);
			this.setup();
		}
		catch (Exception e)
		{}

		return result;
	}

	public <T extends BedrockComponentBase> T getOrCreate(Class<T> clazz)
	{
		return this.getOrCreate(clazz, clazz);
	}

	public <T extends BedrockComponentBase> T getOrCreate(Class<T> clazz, Class subclass)
	{
		T result = this.get(clazz);

		if (result == null)
		{
			result = (T) this.add(subclass);
		}

		return result;
	}

	public <T extends BedrockComponentBase> T remove(Class<T> clazz)
	{
		Iterator<BedrockComponentBase> it = this.components.iterator();

		while (it.hasNext())
		{
			BedrockComponentBase component = it.next();

			if (clazz.isAssignableFrom(component.getClass()))
			{
				it.remove();

				return (T) component;
			}
		}

		return null;
	}

	public <T extends BedrockComponentBase> T replace(Class<T> clazz, Class subclass)
	{
		this.remove(clazz);

		return (T) this.add(subclass);
	}

	/**
	 * Update curve values
	 */
	public void updateCurves()
	{
		for (BedrockCurve curve : this.curves.values())
		{
			if (curve.variable != null)
			{
				curve.variable.set(curve.compute());
			}
		}
	}
}
