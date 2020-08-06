package com.eliotlash.particleman.mcwrapper;

import com.eliotlash.particlelib.mcwrapper.*;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public class WorldWrapper implements IWorld {
    World world;

    public WorldWrapper(World world) {
        this.world = world;
    }

    @Override
    public boolean isBlockLoaded(BlockPos pos) {
        // TODO This could potentially create a lot of garbage depending on how often this method is called
        return world.isBlockLoaded(ConversionUtils.abstractToConcreteBlockPos(pos));
    }

    @Override
    public IBlock getBlockAtPos(BlockPos pos) {
        // TODO This could potentially create a lot of garbage depending on how often this method is called
        return new BlockWrapper(world.getBlockState(ConversionUtils.abstractToConcreteBlockPos(pos)).getBlock());
    }

    @Override
    public List<AxisAlignedBB> getCollisionBoxes(IEntity entityIn, AxisAlignedBB aabb) {
        Entity wrappedEntity = ((EntityWrapper)entityIn).entity;
        // TODO This could potentially create a lot of garbage depending on how often this method is called - may need
        // to optimize allocations of abstract AABBs using an object pool, or switch to using a mixin interface
        return world.getCollisionBoxes(wrappedEntity, ConversionUtils.abstractToConcreteAABB(aabb))
                .stream()
                .map(ConversionUtils::concreteToAbstractAABB)
                .collect(Collectors.toList());
    }
}
