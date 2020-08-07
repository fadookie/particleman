package com.eliotlash.particleman.mixin;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingScreenRenderer.class)
public class ExampleMixin {
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/Minecraft;)V")
    private void init(Minecraft minecraft, CallbackInfo info) {
        System.out.println("@@@MIXIN This line is printed by an example mod mixin!");
    }
}
