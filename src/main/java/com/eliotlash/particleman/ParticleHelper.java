package com.eliotlash.particleman;

import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particlelib.particles.emitter.BedrockEmitter;
import com.eliotlash.particleman.client.RenderingHandler;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import javax.swing.text.html.parser.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class ParticleHelper {
    public static BedrockScheme loadScheme(ResourceLocation location) throws IOException {
        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        try(
            SimpleResource resource = (SimpleResource) resourceManager.getResource(location);
            InputStreamReader stream = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            Reader reader = new BufferedReader(stream)
        ) {
            Gson GSON = new Gson();
            JsonObject jsonobject = JsonUtils.fromJson(GSON, reader, JsonObject.class);
            return BedrockScheme.parse(jsonobject);
        }
    }

    public static void updateEmitter(RenderableBedrockEmitter emitter, LivingEntity target) {
        emitter.update();
        /* Screw interpolation for now */
        emitter.lastGlobal.set(target.getPosX(), target.getPosY(), target.getPosZ());
        /* Sanity ticks is a stupid workaround to see if the emitter's controller
         * is still in the world, when the sanity ticks reaches 2, it stops itself */
        emitter.sanityTicks = 0;

        RenderingHandler.addEmitter(emitter, target);
    }

    public static void updateEmitter(RenderableBedrockEmitter emitter, Vector3d target, World world) {
        emitter.update();
        /* Screw interpolation for now */
        emitter.lastGlobal.set(target.x, target.y, target.z);
        /* Sanity ticks is a stupid workaround to see if the emitter's controller
         * is still in the world, when the sanity ticks reaches 2, it stops itself */
        emitter.sanityTicks = 0;

        RenderingHandler.addEmitter(emitter, world);
    }
}
