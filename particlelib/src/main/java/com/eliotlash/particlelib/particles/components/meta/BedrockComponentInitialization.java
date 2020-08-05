package com.eliotlash.particlelib.particles.components.meta;

import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.components.IComponentEmitterInitialize;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.eliotlash.particlelib.particles.components.IComponentEmitterUpdate;
import com.eliotlash.molang.MolangException;
import com.eliotlash.molang.MolangParser;
import com.eliotlash.molang.expressions.MolangExpression;

public class BedrockComponentInitialization extends BedrockComponentBase implements IComponentEmitterInitialize, IComponentEmitterUpdate
{
	public MolangExpression creation = MolangParser.ZERO;
	public MolangExpression update = MolangParser.ZERO;

	public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
	{
		if (!elem.isJsonObject()) return super.fromJson(elem, parser);

		JsonObject element = elem.getAsJsonObject();

		if (element.has("creation_expression")) this.creation = parser.parseJson(element.get("creation_expression"));
		if (element.has("per_update_expression")) this.update = parser.parseJson(element.get("per_update_expression"));

		return super.fromJson(element, parser);
	}

	@Override
	public JsonElement toJson()
	{
		JsonObject object = new JsonObject();

		if (!MolangExpression.isZero(this.creation)) object.add("creation_expression", this.creation.toJson());
		if (!MolangExpression.isZero(this.update)) object.add("per_update_expression", this.update.toJson());

		return object;
	}

	@Override
	public void apply(BedrockEmitter emitter)
	{
		this.creation.get();
		emitter.replaceVariables();
	}

	@Override
	public void update(BedrockEmitter emitter)
	{
		this.update.get();
		emitter.replaceVariables();
	}
}
