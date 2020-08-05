package com.eliotlash.particlelib.particles.components.expiration;

import com.eliotlash.particlelib.Settings;
import com.eliotlash.particlelib.mcwrapper.BlockPos;
import com.eliotlash.particlelib.mcwrapper.IBlock;
import com.eliotlash.particlelib.mcwrapper.ResourceLocation;
import com.eliotlash.particlelib.particles.components.BedrockComponentBase;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.eliotlash.molang.MolangException;
import com.eliotlash.molang.MolangParser;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public abstract class BedrockComponentExpireBlocks extends BedrockComponentBase
{
	public List<IBlock> blocks = new ArrayList<IBlock>();

	private BlockPos pos = new BlockPos();

	@Override
	public BedrockComponentBase fromJson(JsonElement element, MolangParser parser) throws MolangException
	{
		if (element.isJsonArray())
		{
			for (JsonElement value : element.getAsJsonArray())
			{
				ResourceLocation location = new ResourceLocation(value.getAsString());
//				Block block = ForgeRegistries.BLOCKS.getValue(location);
                IBlock block = Settings.blockLookup(location);

				if (block != null)
				{
					this.blocks.add(block);
				}
			}
		}

		return super.fromJson(element, parser);
	}

	@Override
	public JsonElement toJson()
	{
		JsonArray array = new JsonArray();

		for (IBlock block : this.blocks)
		{
//			ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(block);
			ResourceLocation rl = block.getResourceLocation();

			if (rl != null)
			{
				array.add(rl.toString());
			}
		}

		return array;
	}

	public IBlock getBlock(BedrockEmitter emitter, BedrockParticle particle)
	{
		if (emitter.world == null)
		{
			return IBlock.Blocks.AIR;
		}

		Vector3d position = particle.getGlobalPosition(emitter);

		this.pos.setPos(position.getX(), position.getY(), position.getZ());

		return emitter.world.getBlockAtPos(this.pos);
	}
}
