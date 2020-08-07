package com.eliotlash.particleman.mixin;

import com.eliotlash.particleman.client.RenderingHandler;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(at = @At("RETURN"), method = "renderLitParticles(Lnet/minecraft/entity/Entity;F)V")
    public void renderLitParticles(Entity entityIn, float partialTicks, CallbackInfo callbackInfo) {
        RenderingHandler.renderLitParticles(partialTicks);
    }

    @Inject(at = @At("RETURN"), method = "renderParticles(Lnet/minecraft/entity/Entity;F)V")
    public void renderParticles(Entity entityIn, float partialTicks, CallbackInfo callbackInfo) {
        RenderingHandler.renderParticles(partialTicks);
    }
}
