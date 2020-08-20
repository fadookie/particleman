package com.eliotlash.particleman.example;

import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;

public class CustomEntity extends EntityPig {
    public static RenderableBedrockEmitter playerEmitter;
    public static BedrockScheme scheme;
    public static String debugSchemeName;

    public CustomEntity(World worldIn) {
        super(worldIn);
    }


}
