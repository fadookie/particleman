package com.eliotlash.mclib.mcwrapper;

import com.eliotlash.particlelib.mcwrapper.*;
import net.minecraft.block.Block;
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
        return world.isBlockLoaded(ConversionUtils.abstractToConcreteBlockPos(pos));
    }

    @Override
    public IBlock getBlockAtPos(BlockPos pos) {
        return new BlockWrapper(world.getBlockState(ConversionUtils.abstractToConcreteBlockPos(pos)).getBlock());
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return world.getCombinedLight(ConversionUtils.abstractToConcreteBlockPos(pos), lightValue);
    }

    @Override
    public List<AxisAlignedBB> getCollisionBoxes(IEntity entityIn, AxisAlignedBB aabb) {
        Entity wrappedEntity = ((EntityWrapper)entityIn).entity;
        return world.getCollisionBoxes(wrappedEntity, ConversionUtils.abstractToConcreteAABB(aabb))
                .stream()
                .map(ConversionUtils::concreteToAbstractAABB)
                .collect(Collectors.toList());
    }
}
