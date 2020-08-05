package com.eliotlash.particlelib.particles;

import com.eliotlash.particlelib.mcwrapper.ResourceLocation;
import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.components.appearance.BedrockComponentAppearanceBillboard;
import com.eliotlash.particlelib.particles.components.appearance.BedrockComponentAppearanceTinting;
import com.eliotlash.particlelib.particles.components.expiration.BedrockComponentExpireInBlocks;
import com.eliotlash.particlelib.particles.components.expiration.BedrockComponentExpireNotInBlocks;
import com.eliotlash.particlelib.particles.components.expiration.BedrockComponentKillPlane;
import com.eliotlash.particlelib.particles.components.expiration.BedrockComponentParticleLifetime;
import com.eliotlash.particlelib.particles.components.motion.BedrockComponentMotionParametric;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.eliotlash.particlelib.particles.components.appearance.BedrockComponentAppearanceLighting;
import com.eliotlash.particlelib.particles.components.lifetime.BedrockComponentLifetimeExpression;
import com.eliotlash.particlelib.particles.components.lifetime.BedrockComponentLifetimeLooping;
import com.eliotlash.particlelib.particles.components.lifetime.BedrockComponentLifetimeOnce;
import com.eliotlash.particlelib.particles.components.meta.BedrockComponentInitialization;
import com.eliotlash.particlelib.particles.components.meta.BedrockComponentLocalSpace;
import com.eliotlash.particlelib.particles.components.motion.BedrockComponentInitialSpeed;
import com.eliotlash.particlelib.particles.components.motion.BedrockComponentInitialSpin;
import com.eliotlash.particlelib.particles.components.motion.BedrockComponentMotionCollision;
import com.eliotlash.particlelib.particles.components.motion.BedrockComponentMotionDynamic;
import com.eliotlash.particlelib.particles.components.rate.BedrockComponentRateInstant;
import com.eliotlash.particlelib.particles.components.rate.BedrockComponentRateSteady;
import com.eliotlash.particlelib.particles.components.shape.BedrockComponentShapeBox;
import com.eliotlash.particlelib.particles.components.shape.BedrockComponentShapeDisc;
import com.eliotlash.particlelib.particles.components.shape.BedrockComponentShapeEntityAABB;
import com.eliotlash.particlelib.particles.components.shape.BedrockComponentShapePoint;
import com.eliotlash.particlelib.particles.components.shape.BedrockComponentShapeSphere;
import com.eliotlash.molang.MolangException;
import com.eliotlash.mclib.math.Operation;
//import com.eliotlash.mclib.utils.resources.RLUtils;

import java.lang.reflect.Type;
import java.util.Map;

public class BedrockSchemeJsonAdapter implements JsonDeserializer<BedrockScheme>, JsonSerializer<BedrockScheme>
{
	public BiMap<String, Class<? extends BedrockComponentBase>> components = HashBiMap.create();

	public static boolean isEmpty(JsonElement element)
	{
		if (element.isJsonArray())
		{
			return element.getAsJsonArray().size() == 0;
		}
		else if (element.isJsonObject())
		{
			return element.getAsJsonObject().size() == 0;
		}
		else if (element.isJsonPrimitive())
		{
			JsonPrimitive primitive = element.getAsJsonPrimitive();

			if (primitive.isString())
			{
				return primitive.getAsString().isEmpty();
			}
			else if (primitive.isNumber())
			{
				return Operation.equals(primitive.getAsDouble(), 0);
			}
		}

		return element.isJsonNull();
	}

	public BedrockSchemeJsonAdapter()
	{
		/* Meta components */
		this.components.put("minecraft:emitter_local_space", BedrockComponentLocalSpace.class);
		this.components.put("minecraft:emitter_initialization", BedrockComponentInitialization.class);

		/* Rate */
		this.components.put("minecraft:emitter_rate_instant", BedrockComponentRateInstant.class);
		this.components.put("minecraft:emitter_rate_steady", BedrockComponentRateSteady.class);

		/* Lifetime emitter */
		this.components.put("minecraft:emitter_lifetime_looping", BedrockComponentLifetimeLooping.class);
		this.components.put("minecraft:emitter_lifetime_once", BedrockComponentLifetimeOnce.class);
		this.components.put("minecraft:emitter_lifetime_expression", BedrockComponentLifetimeExpression.class);

		/* Shapes */
		this.components.put("minecraft:emitter_shape_disc", BedrockComponentShapeDisc.class);
		this.components.put("minecraft:emitter_shape_box", BedrockComponentShapeBox.class);
		this.components.put("minecraft:emitter_shape_entity_aabb", BedrockComponentShapeEntityAABB.class);
		this.components.put("minecraft:emitter_shape_point", BedrockComponentShapePoint.class);
		this.components.put("minecraft:emitter_shape_sphere", BedrockComponentShapeSphere.class);

		/* Lifetime particle */
		this.components.put("minecraft:particle_lifetime_expression", BedrockComponentParticleLifetime.class);
		this.components.put("minecraft:particle_expire_if_in_blocks", BedrockComponentExpireInBlocks.class);
		this.components.put("minecraft:particle_expire_if_not_in_blocks", BedrockComponentExpireNotInBlocks.class);
		this.components.put("minecraft:particle_kill_plane", BedrockComponentKillPlane.class);

		/* Appearance */
		this.components.put("minecraft:particle_appearance_billboard", BedrockComponentAppearanceBillboard.class);
		this.components.put("minecraft:particle_appearance_lighting", BedrockComponentAppearanceLighting.class);
		this.components.put("minecraft:particle_appearance_tinting", BedrockComponentAppearanceTinting.class);

		/* Motion & Rotation */
		this.components.put("minecraft:particle_initial_speed", BedrockComponentInitialSpeed.class);
		this.components.put("minecraft:particle_initial_spin", BedrockComponentInitialSpin.class);
		this.components.put("minecraft:particle_motion_collision", BedrockComponentMotionCollision.class);
		this.components.put("minecraft:particle_motion_dynamic", BedrockComponentMotionDynamic.class);
		this.components.put("minecraft:particle_motion_parametric", BedrockComponentMotionParametric.class);
	}

	@Override
	public BedrockScheme deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		BedrockScheme scheme = new BedrockScheme();

		if (!json.isJsonObject())
		{
			throw new JsonParseException("The root element of Bedrock particle should be an object!");
		}

		/* Skip format_version check to avoid breaking semi-compatible particles */
		JsonObject root = json.getAsJsonObject();

		try
		{
			this.parseEffect(scheme, this.getObject(root, "particle_effect", "No particle_effect was found..."));
		}
		catch (MolangException e)
		{
			throw new JsonParseException("Couldn't parse some MoLang expression!", e);
		}

		scheme.setup();

		return scheme;
	}

	private void parseEffect(BedrockScheme scheme, JsonObject effect) throws JsonParseException, MolangException
	{
		this.parseDescription(scheme, this.getObject(effect, "description", "No particle_effect.description was found..."));

		if (effect.has("curves"))
		{
			JsonElement curves = effect.get("curves");

			if (curves.isJsonObject())
			{
				this.parseCurves(scheme, curves.getAsJsonObject());
			}
		}

		this.parseComponents(scheme, this.getObject(effect, "components", "No particle_effect.components was found..."));
	}

	/**
	 * Parse description object (which contains ID of the particle, material type and texture)
	 */
	private void parseDescription(BedrockScheme scheme, JsonObject description) throws JsonParseException
	{
		if (description.has("identifier"))
		{
			scheme.identifier = description.get("identifier").getAsString();
		}

		JsonObject parameters = this.getObject(description, "basic_render_parameters", "No particle_effect.basic_render_parameters was found...");

		if (parameters.has("material"))
		{
			scheme.material = BedrockMaterial.fromString(parameters.get("material").getAsString());
		}

		if (parameters.has("texture"))
		{
			String texture = parameters.get("texture").getAsString();

			if (!texture.equals("textures/particle/particles"))
			{
//				scheme.texture = RLUtils.create(texture);
                scheme.texture = new ResourceLocation(texture);
			}
		}
	}

	/**
	 * Parse curves object
	 */
	private void parseCurves(BedrockScheme scheme, JsonObject curves) throws MolangException
	{
		for (Map.Entry<String, JsonElement> entry : curves.entrySet())
		{
			JsonElement element = entry.getValue();

			if (element.isJsonObject())
			{
				BedrockCurve curve = new BedrockCurve();

				curve.fromJson(element.getAsJsonObject(), scheme.parser);
				scheme.curves.put(entry.getKey(), curve);
			}
		}
	}

	private void parseComponents(BedrockScheme scheme, JsonObject components) throws MolangException
	{
		for (Map.Entry<String, JsonElement> entry : components.entrySet())
		{
			String key = entry.getKey();

			if (this.components.containsKey(key))
			{
				BedrockComponentBase component = null;

				try
				{
					component = this.components.get(key).getConstructor().newInstance();
				}
				catch (Exception e)
				{}

				if (component != null)
				{
					component.fromJson(entry.getValue(), scheme.parser);
					scheme.components.add(component);
				}
				else
				{
					System.out.println("Failed to parse given component " + key + " in " + scheme.identifier + "!");
				}
			}
		}
	}

	private JsonObject getObject(JsonObject object, String key, String message) throws JsonParseException
	{
		/* Skip format_version check to avoid breaking semi-compatible particles */
		if (!object.has(key) && !object.get(key).isJsonObject())
		{
			throw new JsonParseException(message);
		}

		return object.get(key).getAsJsonObject();
	}

	/**
	 * Turn given bedrock scheme into JSON
	 */
	@Override
	public JsonElement serialize(BedrockScheme src, Type typeOfSrc, JsonSerializationContext context)
	{
		JsonObject object = new JsonObject();
		JsonObject effect = new JsonObject();

		object.addProperty("format_version", "1.10.0");
		object.add("particle_effect", effect);

		this.addDescription(effect, src);
		this.addCurves(effect, src);
		this.addComponents(effect, src);

		return object;
	}

	private void addDescription(JsonObject effect, BedrockScheme scheme)
	{
		JsonObject desc = new JsonObject();
		JsonObject render = new JsonObject();

		effect.add("description", desc);

		desc.addProperty("identifier", scheme.identifier);
		desc.add("basic_render_parameters", render);

		render.addProperty("material", scheme.material.id);
		render.addProperty("texture", "textures/particle/particles");

		if (scheme.texture != null && !scheme.texture.equals(BedrockScheme.defaultTexture))
		{
			render.addProperty("texture", scheme.texture.toString());
		}
	}

	private void addCurves(JsonObject effect, BedrockScheme scheme)
	{
		JsonObject curves = new JsonObject();

		effect.add("curves", curves);

		for (Map.Entry<String, BedrockCurve> entry : scheme.curves.entrySet())
		{
			curves.add(entry.getKey(), entry.getValue().toJson());
		}
	}

	private void addComponents(JsonObject effect, BedrockScheme scheme)
	{
		JsonObject components = new JsonObject();

		effect.add("components", components);

		for (BedrockComponentBase component : scheme.components)
		{
			JsonElement element = component.toJson();

			if (this.isEmpty(element) && !component.canBeEmpty())
			{
				continue;
			}

			components.add(this.components.inverse().get(component.getClass()), element);
		}
	}
}
