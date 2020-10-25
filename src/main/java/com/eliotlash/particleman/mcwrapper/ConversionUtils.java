package com.eliotlash.particleman.mcwrapper;

import com.eliotlash.particlelib.mcwrapper.IBlock;
import com.eliotlash.particlelib.mcwrapper.ResourceLocation;
import com.eliotlash.particlelib.mcwrapper.Size2f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

public class ConversionUtils
{
	public static BlockPos abstractToConcreteBlockPos(
			com.eliotlash.particlelib.mcwrapper.BlockPos abstractPos)
	{
		return new BlockPos(abstractPos.getX(), abstractPos.getY(), abstractPos.getZ());
	}

	public static net.minecraft.util.ResourceLocation abstractToConcreteRL(ResourceLocation abstractRL)
	{
		return new net.minecraft.util.ResourceLocation(abstractRL.namespace, abstractRL.path);
	}

	public static ResourceLocation concreteToAbstractRL(net.minecraft.util.ResourceLocation concreteRL)
	{
		return new ResourceLocation(concreteRL.getNamespace(), concreteRL.getPath());
	}

	public static AxisAlignedBB abstractToConcreteAABB(com.eliotlash.particlelib.mcwrapper.AxisAlignedBB aabb)
	{
		return new AxisAlignedBB(aabb.getXMin(), aabb.getYMin(), aabb.getZMin(), aabb.getXMax(), aabb.getYMax(), aabb.getZMax());
	}

	public static com.eliotlash.particlelib.mcwrapper.AxisAlignedBB concreteToAbstractAABB(VoxelShape voxelShape)
	{
		AxisAlignedBB aabb = voxelShape.getBoundingBox();
		return new com.eliotlash.particlelib.mcwrapper.AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
	}

	public static Size2f entityToSize(Entity entity)
	{
		return new Size2f(entity.getWidth(), entity.getHeight(), entity);
	}

	public static IBlock blockLookup(ResourceLocation resourceLocation)
	{
		return new BlockWrapper(ForgeRegistries.BLOCKS.getValue(abstractToConcreteRL(resourceLocation)));
	}
}
