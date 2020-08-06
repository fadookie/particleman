package com.eliotlash.particleman.mcwrapper;

import com.eliotlash.particlelib.mcwrapper.IBlock;
import com.eliotlash.particlelib.mcwrapper.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConversionUtils {
    public static BlockPos abstractToConcreteBlockPos(
            com.eliotlash.particlelib.mcwrapper.BlockPos abstractPos) {
        return new BlockPos(abstractPos.getX(), abstractPos.getY(), abstractPos.getZ());
    }

    public static net.minecraft.util.ResourceLocation abstractToConcreteRL(ResourceLocation abstractRL) {
        return new net.minecraft.util.ResourceLocation(abstractRL.namespace, abstractRL.path);
    }

    public static ResourceLocation concreteToAbstractRL(net.minecraft.util.ResourceLocation concreteRL) {
        return new ResourceLocation(concreteRL.getResourceDomain(), concreteRL.getResourcePath());
    }

    public static AxisAlignedBB abstractToConcreteAABB(com.eliotlash.particlelib.mcwrapper.AxisAlignedBB aabb) {
        return new AxisAlignedBB(aabb.getXMin(), aabb.getYMin(), aabb.getZMin(), aabb.getXMax(), aabb.getYMax(), aabb.getZMax());
    }

    public static com.eliotlash.particlelib.mcwrapper.AxisAlignedBB concreteToAbstractAABB(AxisAlignedBB aabb) {
        return new com.eliotlash.particlelib.mcwrapper.AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public static IBlock blockLookup(ResourceLocation resourceLocation) {
        return new BlockWrapper(ForgeRegistries.BLOCKS.getValue(abstractToConcreteRL(resourceLocation)));
    }
}
