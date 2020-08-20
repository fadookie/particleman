package com.eliotlash.particleman.example;

import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particleman.ParticleHelper;
import com.eliotlash.particleman.ParticleMan;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@Mod.EventBusSubscriber(modid= ParticleMan.MODID)
public class ModEventBus
{
    public static RenderableBedrockEmitter playerEmitter;
    public static RenderableBedrockEmitter pointEmitter;
    public static BedrockScheme scheme;
    public static String debugSchemeName;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void doClientStuff(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && event.player == Minecraft.getMinecraft().player)
        {
//            final String testParticleFile = "rainbow";
//            final String testParticleFile = "loading";
//            final String testParticleFile = "helix-ish";
            final String testParticleFile = "loading";
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
            ParticleHelper.updateEmitter(pointEmitter, new Vec3d(0, 56, 0), event.player.world);
        }
    }
}
