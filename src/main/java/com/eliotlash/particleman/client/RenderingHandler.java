package com.eliotlash.particleman.client;

import com.eliotlash.particlelib.Settings;
import com.eliotlash.particlelib.mcwrapper.Size2f;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Rendering handler
 *
 * This handler is another handler in this mod that responsible for rendering.
 * Currently this handler only renders recording overlay
 */
@OnlyIn(Dist.CLIENT)
public class RenderingHandler
{
    /**
     * Bedrock particle emitters
     */
    private static final List<RenderableBedrockEmitter> emitters = new ArrayList<>();

    /**
     * Render lit particles (call by ASM, but not used for anything yet...
     * I might use it for morph based Snowstorm system)...
     */
    public static void renderLitParticles(float partialTicks)
    {}

    /**
     * Render particle emitters (called by ASM)
     */
    public static void renderParticles(MatrixStack stack, ActiveRenderInfo info, float partialTicks, TextureManager renderer)
    {
        RenderSystem.multMatrix(stack.getLast().getMatrix());

        if (!emitters.isEmpty())
        {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableCull();

            stack = new MatrixStack();
            stack.translate(-info.getProjectedView().getX(), -info.getProjectedView().getY(), -info.getProjectedView().getZ());

            RenderSystem.enableTexture();

            if (Settings.getParticleSorting())
            {
                emitters.sort((a, b) ->
                {
                    double ad = a.getDistanceSq();
                    double bd = b.getDistanceSq();

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

            for (RenderableBedrockEmitter emitter : emitters)
            {
                emitter.render(stack, info, partialTicks, renderer);
                emitter.running = emitter.sanityTicks < 2;
            }

            RenderSystem.alphaFunc(516, 0.1F);
            RenderSystem.enableCull();
        }
    }

    public static void addEmitterGuard(RenderableBedrockEmitter emitter, Runnable targetSetter) {
        if (!emitter.added)
        {
            emitters.add(emitter);

            emitter.added = true;
            targetSetter.run();
        }
    }

    public static void addEmitter(RenderableBedrockEmitter emitter, Entity target)
    {
        addEmitterGuard(emitter, () -> emitter.setTarget(target));
    }

    public static void addEmitter(RenderableBedrockEmitter emitter, Size2f size, World world)
    {
        addEmitterGuard(emitter, () -> emitter.setTarget(size, world));
    }

    public static void addEmitter(RenderableBedrockEmitter emitter, World world)
    {
        addEmitterGuard(emitter, () -> emitter.setWorld(world));
    }

    public static void updateEmitters()
    {
        Iterator<RenderableBedrockEmitter> it = emitters.iterator();

        while (it.hasNext())
        {
            RenderableBedrockEmitter emitter = it.next();

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
