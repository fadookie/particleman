package com.eliotlash.particleman.client.particles.components.appearance;

import com.eliotlash.mclib.utils.Interpolations;
import com.eliotlash.particlelib.particles.components.appearance.BedrockComponentAppearanceBillboard;
import com.eliotlash.particlelib.particles.components.appearance.CameraFacing;
import com.eliotlash.particlelib.particles.emitter.BedrockParticle;
import com.eliotlash.particleman.client.particles.components.IComponentParticleRender;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class RenderableBedrockComponentAppearanceBillboard extends BedrockComponentAppearanceBillboard implements IComponentParticleRender
{
	private Matrix4f transform = new Matrix4f();
	private Matrix4f rotation = new Matrix4f();
	private Vector4f[] vertices = new Vector4f[] {
		new Vector4f(0, 0, 0, 1),
		new Vector4f(0, 0, 0, 1),
		new Vector4f(0, 0, 0, 1),
		new Vector4f(0, 0, 0, 1)
	};
	private Vector3f vector = new Vector3f();

	public RenderableBedrockComponentAppearanceBillboard() {
		super();
	}

	@Override
	public void render(MatrixStack stack, ActiveRenderInfo info, RenderableBedrockEmitter emitter, BedrockParticle particle, BufferBuilder builder, float partialTicks)
	{
		this.calculateUVs(particle, partialTicks);

		/* Render the particle */
		double px = Interpolations.lerp(particle.prevPosition.x, particle.position.x, partialTicks);
		double py = Interpolations.lerp(particle.prevPosition.y, particle.position.y, partialTicks);
		double pz = Interpolations.lerp(particle.prevPosition.z, particle.position.z, partialTicks);
		float angle = Interpolations.lerp(particle.prevRotation, particle.rotation, partialTicks);

		if (particle.relativePosition && particle.relativeRotation)
		{
			this.vector.set((float) px, (float) py, (float) pz);
			emitter.rotation.transform(this.vector);

			px = this.vector.x;
			py = this.vector.y;
			pz = this.vector.z;

			px += emitter.lastGlobal.x;
			py += emitter.lastGlobal.y;
			pz += emitter.lastGlobal.z;
		}

		/* Calculate yaw and pitch based on the facing mode */
		float entityYaw = emitter.cYaw;
		float entityPitch = emitter.cPitch;
		double entityX = emitter.cX;
		double entityY = emitter.cY;
		double entityZ = emitter.cZ;
		boolean lookAt = this.facing == CameraFacing.LOOKAT_XYZ || this.facing == CameraFacing.LOOKAT_Y;

		/* Flip width when frontal perspective mode */
		if (emitter.perspective == PointOfView.THIRD_PERSON_FRONT)
		{
			this.w = -this.w;
		}
		/* In GUI renderer */
		else if (emitter.perspective == PointOfView.FIRST_PERSON && !lookAt)
		{
			entityYaw = 180 - entityYaw;

			this.w = -this.w;
			this.h = -this.h;
		}

		if (lookAt)
		{
			double dX = entityX - px;
			double dY = entityY - py;
			double dZ = entityZ - pz;
			double horizontalDistance = MathHelper.sqrt(dX * dX + dZ * dZ);

			entityYaw = 180 - (float) (MathHelper.atan2(dZ, dX) * (180D / Math.PI)) - 90.0F;
			entityPitch = (float) (-(MathHelper.atan2(dY, horizontalDistance) * (180D / Math.PI))) + 180;
		}

		/* Calculate the geometry for billboards using cool matrix math */
		int light = emitter.getBrightnessForRender(partialTicks, px, py, pz);

		this.vertices[0].set(-this.w / 2, -this.h / 2, 0, 1);
		this.vertices[1].set(this.w / 2, -this.h / 2, 0, 1);
		this.vertices[2].set(this.w / 2, this.h / 2, 0, 1);
		this.vertices[3].set(-this.w / 2, this.h / 2, 0, 1);
		this.transform.setIdentity();

		if (this.facing == CameraFacing.ROTATE_XYZ || this.facing == CameraFacing.LOOKAT_XYZ)
		{
			this.rotation.rotY(entityYaw / 180 * (float) Math.PI);
			this.transform.mul(this.rotation);
			this.rotation.rotX(entityPitch / 180 * (float) Math.PI);
			this.transform.mul(this.rotation);
		}
		else if (this.facing == CameraFacing.ROTATE_Y || this.facing == CameraFacing.LOOKAT_Y)
		{
			this.rotation.rotY(entityYaw / 180 * (float) Math.PI);
			this.transform.mul(this.rotation);
		}

		this.rotation.rotZ(angle / 180 * (float) Math.PI);
		this.transform.mul(this.rotation);

		for (Vector4f vertex : this.vertices)
		{
			this.transform.transform(vertex);
		}

		float u1 = this.u1 / (float) this.textureWidth;
		float u2 = this.u2 / (float) this.textureWidth;
		float v1 = this.v1 / (float) this.textureHeight;
		float v2 = this.v2 / (float) this.textureHeight;

		stack.push();
		stack.translate(px, py, pz);

		net.minecraft.util.math.vector.Matrix4f matrix4f = stack.getLast().getMatrix();
		net.minecraft.util.math.vector.Vector4f vec0 = new net.minecraft.util.math.vector.Vector4f(this.vertices[0].x, this.vertices[0].y, this.vertices[0].z, 1);
		net.minecraft.util.math.vector.Vector4f vec1 = new net.minecraft.util.math.vector.Vector4f(this.vertices[1].x, this.vertices[1].y, this.vertices[1].z, 1);
		net.minecraft.util.math.vector.Vector4f vec2 = new net.minecraft.util.math.vector.Vector4f(this.vertices[2].x, this.vertices[2].y, this.vertices[2].z, 1);
		net.minecraft.util.math.vector.Vector4f vec3 = new net.minecraft.util.math.vector.Vector4f(this.vertices[3].x, this.vertices[3].y, this.vertices[3].z, 1);

		vec0.transform(matrix4f);
		vec1.transform(matrix4f);
		vec2.transform(matrix4f);
		vec3.transform(matrix4f);

		builder.pos(vec0.getX(), vec0.getY(), vec0.getZ()).tex(u1, v1).color(particle.r, particle.g, particle.b, particle.a).lightmap(light).endVertex();
		builder.pos(vec1.getX(), vec1.getY(), vec1.getZ()).tex(u2, v1).color(particle.r, particle.g, particle.b, particle.a).lightmap(light).endVertex();
		builder.pos(vec2.getX(), vec2.getY(), vec2.getZ()).tex(u2, v2).color(particle.r, particle.g, particle.b, particle.a).lightmap(light).endVertex();
		builder.pos(vec3.getX(), vec3.getY(), vec3.getZ()).tex(u1, v2).color(particle.r, particle.g, particle.b, particle.a).lightmap(light).endVertex();

		stack.pop();
	}

	@Override
	public void renderOnScreen(BedrockParticle particle, int x, int y, float scale, float partialTicks)
	{
		this.calculateUVs(particle, partialTicks);

		this.w = this.h = 0.5F;
		float angle = Interpolations.lerp(particle.prevRotation, particle.rotation, partialTicks);

		/* Calculate the geometry for billboards using cool matrix math */
		this.vertices[0].set(-this.w / 2, -this.h / 2, 0, 1);
		this.vertices[1].set(this.w / 2, -this.h / 2, 0, 1);
		this.vertices[2].set(this.w / 2, this.h / 2, 0, 1);
		this.vertices[3].set(-this.w / 2, this.h / 2, 0, 1);
		this.transform.setIdentity();
		this.transform.setScale(scale * 2.75F);
		this.transform.setTranslation(new Vector3f(x, y - scale / 2, 0));

		this.rotation.rotZ(angle / 180 * (float) Math.PI);
		this.transform.mul(this.rotation);

		for (Vector4f vertex : this.vertices)
		{
			this.transform.transform(vertex);
		}

		float u1 = this.u1 / (float) this.textureWidth;
		float u2 = this.u2 / (float) this.textureWidth;
		float v1 = this.v1 / (float) this.textureHeight;
		float v2 = this.v2 / (float) this.textureHeight;

		BufferBuilder builder = Tessellator.getInstance().getBuffer();

		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		builder.pos(this.vertices[0].x, this.vertices[0].y, this.vertices[0].z).tex(u1, v1).color(particle.r, particle.g, particle.b, particle.a).endVertex();
		builder.pos(this.vertices[1].x, this.vertices[1].y, this.vertices[1].z).tex(u2, v1).color(particle.r, particle.g, particle.b, particle.a).endVertex();
		builder.pos(this.vertices[2].x, this.vertices[2].y, this.vertices[2].z).tex(u2, v2).color(particle.r, particle.g, particle.b, particle.a).endVertex();
		builder.pos(this.vertices[3].x, this.vertices[3].y, this.vertices[3].z).tex(u1, v2).color(particle.r, particle.g, particle.b, particle.a).endVertex();

		Tessellator.getInstance().draw();
	}

}
