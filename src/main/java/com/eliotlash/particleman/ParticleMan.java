package com.eliotlash.particleman;

import com.eliotlash.particlelib.mcwrapper.ResourceLocation;
import com.eliotlash.particleman.mcwrapper.ConversionUtils;
import com.eliotlash.particlelib.Settings;
import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particleman.client.RenderingHandler;
import com.eliotlash.particleman.client.particles.RenderableBedrockSchemeJsonAdapter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ParticleMan.MODID, name = ParticleMan.NAME, version = ParticleMan.VERSION)
public class ParticleMan
{
    public static final String MODID = "particleman";
    public static final String NAME = "ParticleMan";
    public static final String VERSION = "0.1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new RenderingHandler()); // For instance event handlers
    }

    static {
        Settings.setBlockLookupImpl(ConversionUtils::blockLookup);
        BedrockScheme.setJsonAdapter(new RenderableBedrockSchemeJsonAdapter());
        BedrockScheme.setDefaultTexture(new ResourceLocation(MODID, "textures/default_particles.png"));
    }
}
