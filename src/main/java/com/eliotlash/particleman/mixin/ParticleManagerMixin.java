package com.eliotlash.particleman.mixin;

import com.eliotlash.particleman.client.RenderingHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin
{
	@Shadow
	@Final
	private TextureManager renderer;

	@Inject(at = @At("RETURN"), method = "renderParticles(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer$Impl;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/renderer/ActiveRenderInfo;F)V")
	public void renderParticles(MatrixStack stack, IRenderTypeBuffer.Impl bufferIn, LightTexture lightTextureIn, ActiveRenderInfo renderInfo, float partialTicks, CallbackInfo ci)
	{
		RenderSystem.pushMatrix();

		Vector3d vector3d = renderInfo.getProjectedView();
		//stack.translate(vector3d.x, vector3d.y, vector3d.z);
		RenderingHandler.renderLitParticles(partialTicks);
		RenderingHandler.renderParticles(stack, renderInfo, partialTicks, renderer);
		RenderSystem.popMatrix();
	}
}
