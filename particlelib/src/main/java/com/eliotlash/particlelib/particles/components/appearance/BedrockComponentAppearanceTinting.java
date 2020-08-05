package com.eliotlash.particlelib.particles.components.appearance;

import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.eliotlash.particlelib.particles.BedrockSchemeJsonAdapter;
import com.eliotlash.particlelib.particles.components.IComponentParticleRenderBase;
import com.eliotlash.molang.MolangException;
import com.eliotlash.molang.MolangParser;
//import net.minecraft.client.renderer.BufferBuilder;

public abstract class BedrockComponentAppearanceTinting extends BedrockComponentBase implements IComponentParticleRenderBase
{
	public Tint color = new Tint.Solid(MolangParser.ONE, MolangParser.ONE, MolangParser.ONE, MolangParser.ONE);

	@Override
	public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
	{
		if (!elem.isJsonObject()) return super.fromJson(elem, parser);

		JsonObject element = elem.getAsJsonObject();

		if (element.has("color"))
		{
			JsonElement color = element.get("color");

			if (color.isJsonArray() || color.isJsonPrimitive())
			{
				this.color = Tint.parseColor(color, parser);
			}
			else if (color.isJsonObject())
			{
				this.color = Tint.parseGradient(color.getAsJsonObject(), parser);
			}
		}

		return super.fromJson(element, parser);
	}

	@Override
	public JsonElement toJson()
	{
		JsonObject object = new JsonObject();
		JsonElement element = this.color.toJson();

		if (!BedrockSchemeJsonAdapter.isEmpty(element))
		{
			object.add("color", element);
		}

		return object;
	}

	/* Interface implementations */

	@Override
	public void preRender(BedrockEmitter emitter, float partialTicks)
	{}

	@Override
	public void postRender(BedrockEmitter emitter, float partialTicks)
	{}

	@Override
	public int getSortingIndex()
	{
		return -10;
	}
}
