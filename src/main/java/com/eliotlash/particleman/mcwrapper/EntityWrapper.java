package com.eliotlash.particleman.mcwrapper;

import com.eliotlash.particlelib.mcwrapper.IEntity;
import com.eliotlash.particlelib.mcwrapper.IWorld;
import net.minecraft.entity.Entity;

public class EntityWrapper<T extends Entity> implements IEntity {
    T entity;
    public EntityWrapper(T entity) {
        this.entity = entity;
    }

    @Override
    public double getPrevPosX() {
        return entity.prevPosX;
    }

    @Override
    public double getPrevPosY() {
        return entity.prevPosY;
    }

    @Override
    public double getPrevPosZ() {
        return entity.prevPosZ;
    }

    @Override
    public double getPosX() {
        return entity.posX;
    }

    @Override
    public double getPosY() {
        return entity.posY;
    }

    @Override
    public double getPosZ() {
        return entity.posZ;
    }

    @Override
    public float getRotationYaw() {
        return entity.rotationYaw;
    }

    @Override
    public float getRotationPitch() {
        return entity.rotationPitch;
    }

    @Override
    public float getPrevRotationYaw() {
        return entity.prevRotationYaw;
    }

    @Override
    public float getPrevRotationPitch() {
        return entity.prevRotationPitch;
    }

    @Override
    public float getWidth() {
        return entity.width;
    }

    @Override
    public float getHeight() {
        return entity.height;
    }

    @Override
    public float getEyeHeight() {
        return entity.getEyeHeight();
    }

    @Override
    public IWorld getWorld() {
        return new WorldWrapper(entity.world);
    }
}
