package mchorse.blockbuster.client;

import com.eliotlash.mclib.mcwrapper.BufferBuilderWrapper;
import com.eliotlash.mclib.mcwrapper.EntityWrapper;
import mchorse.blockbuster.Blockbuster;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Rendering handler
 *
 * This handler is another handler in this mod that responsible for rendering.
 * Currently this handler only renders recording overlay
 */
@SideOnly(Side.CLIENT)
public class RenderingHandler
{
    /**
     * Bedrock particle emitters
     */
    private static final List<BedrockEmitter> emitters = new ArrayList<>();

    /**
     * Render lit particles (call by ASM, but not used for anything yet...
     * I might use it for morph based Snowstorm system)...
     */
    public static void renderLitParticles(float partialTicks)
    {}

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Post event) {
//        System.out.println("renderPlayer e:" + event);
        GlStateManager.disableLighting();
        renderParticles(event.getPartialRenderTick());
        GlStateManager.enableLighting();
    }

    /**
     * Render particle emitters (called by ASM)
     */
    public static void renderParticles(float partialTicks)
    {
        if (!emitters.isEmpty())
        {
            Entity camera = Minecraft.getMinecraft().getRenderViewEntity();
            EntityWrapper cameraWrapper = new EntityWrapper(camera);
            int perspective = Minecraft.getMinecraft().gameSettings.thirdPersonView;
            double playerX = camera.prevPosX + (camera.posX - camera.prevPosX) * (double) partialTicks;
            double playerY = camera.prevPosY + (camera.posY - camera.prevPosY) * (double) partialTicks;
            double playerZ = camera.prevPosZ + (camera.posZ - camera.prevPosZ) * (double) partialTicks;

            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(516, 0.003921569F);

            BufferBuilder builder = Tessellator.getInstance().getBuffer();
            BufferBuilderWrapper builderWrapper = new BufferBuilderWrapper(builder);

            GlStateManager.disableTexture2D();

            builder.setTranslation(-playerX, -playerY, -playerZ);

            GlStateManager.disableCull();
            GlStateManager.enableTexture2D();

            if (Blockbuster.particleSorting.get())
            {
                emitters.sort((a, b) ->
                {
                    double ad = a.getDistanceSq(perspective, cameraWrapper);
                    double bd = b.getDistanceSq(perspective, cameraWrapper);

                    if (ad < bd)
                    {
                        return 1;
                    }
                    else if (ad > bd)
                    {
                        return -1;
                    }

                    return 0;
                });
            }

            for (BedrockEmitter emitter : emitters)
            {
                emitter.render(partialTicks, builderWrapper, perspective, cameraWrapper);
                emitter.running = emitter.sanityTicks < 2;
            }

            builder.setTranslation(0, 0, 0);

            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
        }
    }

    public static void addEmitter(BedrockEmitter emitter, EntityLivingBase target)
    {
        if (!emitter.added)
        {
            emitters.add(emitter);

            emitter.added = true;
            emitter.setTarget(new EntityWrapper(target));
        }
    }

    public static void updateEmitters()
    {
        Iterator<BedrockEmitter> it = emitters.iterator();

        while (it.hasNext())
        {
            BedrockEmitter emitter = it.next();

            emitter.update();

            if (emitter.isFinished())
            {
                it.remove();
                emitter.added = false;
            }
        }
    }

    public RenderingHandler(/*GuiRecordingOverlay overlay*/)
    {
//        this.overlay = overlay;
    }
}
