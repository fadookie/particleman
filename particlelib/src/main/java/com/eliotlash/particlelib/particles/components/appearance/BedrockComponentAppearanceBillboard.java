package com.eliotlash.particlelib.particles.components.appearance;

import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.eliotlash.particlelib.particles.components.IComponentParticleRenderBase;
import com.eliotlash.molang.MolangException;
import com.eliotlash.molang.MolangParser;
import com.eliotlash.molang.expressions.MolangExpression;

public abstract class BedrockComponentAppearanceBillboard extends BedrockComponentBase implements IComponentParticleRenderBase
{
	/* Options */
	public MolangExpression sizeW = MolangParser.ZERO;
	public MolangExpression sizeH = MolangParser.ZERO;
	public CameraFacing facing = CameraFacing.LOOKAT_XYZ;
	public int textureWidth = 128;
	public int textureHeight = 128;
	public MolangExpression uvX = MolangParser.ZERO;
	public MolangExpression uvY = MolangParser.ZERO;
	public MolangExpression uvW = MolangParser.ZERO;
	public MolangExpression uvH = MolangParser.ZERO;

	public boolean flipbook = false;
	public float stepX;
	public float stepY;
	public float fps;
	public MolangExpression maxFrame = MolangParser.ZERO;
	public boolean stretchFPS = false;
	public boolean loop = false;

	/* Runtime properties */
	protected float w;
	protected float h;

	protected float u1;
	protected float v1;
	protected float u2;
	protected float v2;

	public BedrockComponentAppearanceBillboard() {}

	@Override
	public BedrockComponentBase fromJson(JsonElement elem, MolangParser parser) throws MolangException
	{
		if (!elem.isJsonObject()) return super.fromJson(elem, parser);

		JsonObject element = elem.getAsJsonObject();

		if (element.has("size") && element.get("size").isJsonArray())
		{
			JsonArray size = element.getAsJsonArray("size");

			if (size.size() >= 2)
			{
				this.sizeW = parser.parseJson(size.get(0));
				this.sizeH = parser.parseJson(size.get(1));
			}
		}

		if (element.has("facing_camera_mode"))
		{
			this.facing = CameraFacing.fromString(element.get("facing_camera_mode").getAsString());
		}

		if (element.has("uv") && element.get("uv").isJsonObject())
		{
			this.parseUv(element.get("uv").getAsJsonObject(), parser);
		}

		return super.fromJson(element, parser);
	}

	private void parseUv(JsonObject object, MolangParser parser) throws MolangException
	{
		if (object.has("texture_width")) this.textureWidth = object.get("texture_width").getAsInt();
		if (object.has("texture_height")) this.textureHeight = object.get("texture_height").getAsInt();

		if (object.has("uv") && object.get("uv").isJsonArray())
		{
			JsonArray uv = object.getAsJsonArray("uv");

			if (uv.size() >= 2)
			{
				this.uvX = parser.parseJson(uv.get(0));
				this.uvY = parser.parseJson(uv.get(1));
			}
		}

		if (object.has("uv_size") && object.get("uv_size").isJsonArray())
		{
			JsonArray uv = object.getAsJsonArray("uv_size");

			if (uv.size() >= 2)
			{
				this.uvW = parser.parseJson(uv.get(0));
				this.uvH = parser.parseJson(uv.get(1));
			}
		}

		if (object.has("flipbook") && object.get("flipbook").isJsonObject())
		{
			this.flipbook = true;
			this.parseFlipbook(object.get("flipbook").getAsJsonObject(), parser);
		}
	}

	private void parseFlipbook(JsonObject flipbook, MolangParser parser) throws MolangException
	{
		if (flipbook.has("base_UV") && flipbook.get("base_UV").isJsonArray())
		{
			JsonArray uv = flipbook.getAsJsonArray("base_UV");

			if (uv.size() >= 2)
			{
				this.uvX = parser.parseJson(uv.get(0));
				this.uvY = parser.parseJson(uv.get(1));
			}
		}

		if (flipbook.has("size_UV") && flipbook.get("size_UV").isJsonArray())
		{
			JsonArray uv = flipbook.getAsJsonArray("size_UV");

			if (uv.size() >= 2)
			{
				this.uvW = parser.parseJson(uv.get(0));
				this.uvH = parser.parseJson(uv.get(1));
			}
		}

		if (flipbook.has("step_UV") && flipbook.get("step_UV").isJsonArray())
		{
			JsonArray uv = flipbook.getAsJsonArray("step_UV");

			if (uv.size() >= 2)
			{
				this.stepX = uv.get(0).getAsFloat();
				this.stepY = uv.get(1).getAsFloat();
			}
		}

		if (flipbook.has("frames_per_second")) this.fps = flipbook.get("frames_per_second").getAsFloat();
		if (flipbook.has("max_frame")) this.maxFrame = parser.parseJson(flipbook.get("max_frame"));
		if (flipbook.has("stretch_to_lifetime")) this.stretchFPS = flipbook.get("stretch_to_lifetime").getAsBoolean();
		if (flipbook.has("loop")) this.loop = flipbook.get("loop").getAsBoolean();
	}

	@Override
	public JsonElement toJson()
	{
		JsonObject object = new JsonObject();
		JsonArray size = new JsonArray();
		JsonObject uv = new JsonObject();

		size.add(this.sizeW.toJson());
		size.add(this.sizeH.toJson());

		/* Adding "uv" properties */
		uv.addProperty("texture_width", this.textureWidth);
		uv.addProperty("texture_height", this.textureHeight);

		if (!this.flipbook && !MolangExpression.isZero(this.uvX) || !MolangExpression.isZero(this.uvY))
		{
			JsonArray uvs = new JsonArray();
			uvs.add(this.uvX.toJson());
			uvs.add(this.uvY.toJson());

			uv.add("uv", uvs);
		}

		if (!this.flipbook && !MolangExpression.isZero(this.uvW) || !MolangExpression.isZero(this.uvH))
		{
			JsonArray uvs = new JsonArray();
			uvs.add(this.uvW.toJson());
			uvs.add(this.uvH.toJson());

			uv.add("uv_size", uvs);
		}

		/* Adding "flipbook" properties to "uv" */
		if (this.flipbook)
		{
			JsonObject flipbook = new JsonObject();

			if (!MolangExpression.isZero(this.uvX) || !MolangExpression.isZero(this.uvY))
			{
				JsonArray base = new JsonArray();
				base.add(this.uvX.toJson());
				base.add(this.uvY.toJson());

				flipbook.add("base_UV", base);
			}

			if (!MolangExpression.isZero(this.uvW) || !MolangExpression.isZero(this.uvH))
			{
				JsonArray uvSize = new JsonArray();
				uvSize.add(this.uvW.toJson());
				uvSize.add(this.uvH.toJson());

				flipbook.add("size_UV", uvSize);
			}

			if (this.stepX != 0 || this.stepY != 0)
			{
				JsonArray step = new JsonArray();
				step.add(this.stepX);
				step.add(this.stepY);

				flipbook.add("step_UV", step);
			}

			if (this.fps != 0) flipbook.addProperty("frames_per_second", this.fps);
			if (!MolangExpression.isZero(this.maxFrame)) flipbook.add("max_frame", this.maxFrame.toJson());
			if (this.stretchFPS) flipbook.addProperty("stretch_to_lifetime", true);
			if (this.loop) flipbook.addProperty("loop", true);

			uv.add("flipbook", flipbook);
		}

		/* Add main properties */
		object.add("size", size);
		object.addProperty("facing_camera_mode", this.facing.id);
		object.add("uv", uv);

		return object;
	}

	@Override
	public void preRender(BedrockEmitter emitter, float partialTicks)
	{}

	public void calculateUVs(BedrockParticle particle, float partialTicks)
	{
		/* Update particle's UVs and size */
		this.w = (float) this.sizeW.get() * 2.25F;
		this.h = (float) this.sizeH.get() * 2.25F;

		float u = (float) this.uvX.get();
		float v = (float) this.uvY.get();
		float w = (float) this.uvW.get();
		float h = (float) this.uvH.get();

		if (this.flipbook)
		{
			int index = (int) (particle.getAge(partialTicks) * this.fps);
			int max = (int) this.maxFrame.get();

			if (this.stretchFPS)
			{
				float lifetime = particle.lifetime <= 0 ? 0 : (particle.age + partialTicks) / particle.lifetime;

				index = (int) (lifetime * max);
			}

			if (this.loop && max != 0)
			{
				index = index % max;
			}

			if (index > max)
			{
				index = max;
			}

			u += this.stepX * index;
			v += this.stepY * index;
		}

		this.u1 = u;
		this.v1 = v;
		this.u2 = u + w;
		this.v2 = v + h;
	}

	@Override
	public void postRender(BedrockEmitter emitter, float partialTicks)
	{}
}
