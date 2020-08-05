package com.eliotlash.particlelib.mcwrapper;

import java.util.List;

public interface IWorld {
    boolean isBlockLoaded(BlockPos pos);

    IBlock getBlockAtPos(BlockPos pos);

//    int getCombinedLight(BlockPos pos, int i);

    List<AxisAlignedBB> getCollisionBoxes(IEntity entityIn, AxisAlignedBB aabb);
}
