package com.eliotlash.particleman.example;

import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particleman.ParticleHelper;
import com.eliotlash.particleman.ParticleMan;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.io.IOException;

@Mod.EventBusSubscriber(modid= ParticleMan.MODID)
public class ModEventBus
{
    public static RenderableBedrockEmitter playerEmitter;
    public static RenderableBedrockEmitter pointEmitter;
    public static BedrockScheme scheme;
    public static String debugSchemeName;

    @SubscribeEvent
    public static void doClientStuff(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && event.player == Minecraft.getInstance().player)
        {
            final String testParticleFile = "rainbow";
//            final String testParticleFile = "loading";
//            final String testParticleFile = "helix-ish";
            //final String testParticleFile = "loading";
//            final String testParticleFile = "windspren";
//                final String testParticleFile = "gloryspren-multi";
            if (!testParticleFile.equals(debugSchemeName)) {
                scheme = null;
                if (playerEmitter != null) playerEmitter.stop();
                playerEmitter = null;
                if (pointEmitter != null) pointEmitter.stop();
                pointEmitter = null;
            }

            // Example usage of particle system:
            if (scheme == null)
            {
                debugSchemeName = testParticleFile;
                try {
                    scheme = ParticleHelper.loadScheme(new ResourceLocation("particleman", "particles/" + testParticleFile + ".particle.json"));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }

            if (playerEmitter == null || playerEmitter.isFinished())
            {
                playerEmitter = new RenderableBedrockEmitter();
                playerEmitter.setScheme(scheme);
            }

            if (pointEmitter == null || pointEmitter.isFinished())
            {
                pointEmitter = new RenderableBedrockEmitter();
                pointEmitter.setScheme(scheme);
            }

            // Attach emitter to entity
            ParticleHelper.updateEmitter(playerEmitter, event.player);

            // Attach emitter to point in space
            ParticleHelper.updateEmitter(pointEmitter, new Vector3d(0, 56, 0), event.player.world);
        }
    }
}
