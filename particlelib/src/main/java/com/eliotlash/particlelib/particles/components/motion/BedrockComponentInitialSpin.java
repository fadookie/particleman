package com.eliotlash.particlelib.particles.components.motion;

import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.components.IComponentParticleInitialize;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.eliotlash.molang.MolangException;
import com.eliotlash.molang.MolangParser;
import com.eliotlash.molang.expressions.MolangExpression;

public class BedrockComponentInitialSpin extends BedrockComponentBase implements IComponentParticleInitialize
{
	public MolangExpression rotation = MolangParser.ZERO;
	public MolangExpression rate = MolangParser.ZERO;

	@Override
	public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
	{
		if (!elem.isJsonObject()) return super.fromJson(elem, parser);

		JsonObject element = elem.getAsJsonObject();

		if (element.has("rotation")) this.rotation = parser.parseJson(element.get("rotation"));
		if (element.has("rotation_rate")) this.rate = parser.parseJson(element.get("rotation_rate"));

		return super.fromJson(element, parser);
	}

	@Override
	public JsonElement toJson()
	{
		JsonObject object = new JsonObject();

		if (!MolangExpression.isZero(this.rotation)) object.add("rotation", this.rotation.toJson());
		if (!MolangExpression.isZero(this.rate)) object.add("rotation_rate", this.rate.toJson());

		return object;
	}

	@Override
	public void apply(BedrockEmitter emitter, BedrockParticle particle)
	{
		particle.initialRotation = (float) this.rotation.get();
		particle.rotationVelocity = (float) this.rate.get() / 20;
	}
}
