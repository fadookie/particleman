package com.eliotlash.particleman;

import com.eliotlash.particlelib.Settings;
import com.eliotlash.particlelib.mcwrapper.ResourceLocation;
import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particleman.client.RenderingHandler;
import com.eliotlash.particleman.client.particles.RenderableBedrockSchemeJsonAdapter;
import com.eliotlash.particleman.mcwrapper.ConversionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ParticleMan.MODID)
public class ParticleMan
{
	public static final String MODID = "particleman";
	public static final String NAME = "ParticleMan";
	public static final String VERSION = "0.1.0";

	private static Logger logger = LogManager.getLogger();

	public ParticleMan()
	{
		MinecraftForge.EVENT_BUS.register(new RenderingHandler()); // For instance event handlers
	}

	static
	{
		Settings.setBlockLookupImpl(ConversionUtils::blockLookup);
		BedrockScheme.setJsonAdapter(new RenderableBedrockSchemeJsonAdapter());
		BedrockScheme.setDefaultTexture(new ResourceLocation(MODID, "textures/default_particles.png"));
	}
}
