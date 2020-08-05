package com.eliotlash.particlelib.mcwrapper;

public interface IEntity {
    double getPrevPosX();
//    void setPrevPosX(double prevPosX);
    double getPrevPosY();
//    void setPrevPosY(double prevPosY);
    double getPrevPosZ();
//    void setPrevPosZ(double prevPosZ);
    double getPosX();
//    void setPosX(double posX);
    double getPosY();
//    void setPosY(double posY);
    double getPosZ();
//    void setPosZ(double posZ);
    float getRotationYaw();
//    void setRotationYaw(float yaw);
    float getRotationPitch();
//    void setRotationPitch(float pitch);
    float getPrevRotationYaw();
    float getPrevRotationPitch();
    float getWidth();
    float getHeight();
    float getEyeHeight();
    IWorld getWorld();

    /*
    int getEntityId();

    void setEntityId(int id);

    Set<String> getTags();

    boolean addTag(String tag);

    boolean removeTag(String tag);

    void onKillCommand();

//    EntityDataManager getDataManager();

    boolean equals(Object p_equals_1_);

    int hashCode();

    void setDead();

    void setDropItemsWhenDead(boolean dropWhenDead);

    void setPosition(double x, double y, double z);

    void turn(float yaw, float pitch);

    void onUpdate();

    void onEntityUpdate();

    int getMaxInPortalTime();

    void setFire(int seconds);

    void extinguish();

    boolean isOffsetPositionInLiquid(double x, double y, double z);

//    void move(MoverType type, double x, double y, double z);

    void resetPositionToBB();

//    void playSound(SoundEvent soundIn, float volume, float pitch);

    boolean isSilent();

    void setSilent(boolean isSilent);

    boolean hasNoGravity();

    void setNoGravity(boolean noGravity);

//    AxisAlignedBB getCollisionBoundingBox();

    boolean isImmuneToFire();

    void fall(float distance, float damageMultiplier);

    boolean isWet();

    boolean isInWater();

    boolean isOverWater();

    boolean handleWaterMovement();

    void spawnRunningParticles();

//    boolean isInsideOfMaterial(Material materialIn);

    boolean isInLava();

    void moveRelative(float strafe, float up, float forward, float friction);

    int getBrightnessForRender();

    float getBrightness();

//    void setWorld(World worldIn);

    void setPositionAndRotation(double x, double y, double z, float yaw, float pitch);

//    void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn);

    void setLocationAndAngles(double x, double y, double z, float yaw, float pitch);

//    float getDistance(Entity entityIn);

    double getDistanceSq(double x, double y, double z);
//
//    double getDistanceSq(BlockPos pos);
//
//    double getDistanceSqToCenter(BlockPos pos);

    double getDistance(double x, double y, double z);

//    double getDistanceSq(Entity entityIn);
//
//    void onCollideWithPlayer(EntityPlayer entityIn);
//
//    void applyEntityCollision(Entity entityIn);
//
//    void addVelocity(double x, double y, double z);
//
//    boolean attackEntityFrom(DamageSource source, float amount);

    Vec3d getLook(float partialTicks);

    Vec3d getPositionEyes(float partialTicks);

//    RayTraceResult rayTrace(double blockReachDistance, float partialTicks);

    boolean canBeCollidedWith();

    boolean canBePushed();

//    void awardKillScore(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_);

//    boolean isInRangeToRender3d(double x, double y, double z);

    boolean isInRangeToRenderDist(double distance);

//    boolean writeToNBTAtomically(NBTTagCompound compound);
//
//    boolean writeToNBTOptional(NBTTagCompound compound);
//
//    NBTTagCompound writeToNBT(NBTTagCompound compound);
//
//    void readFromNBT(NBTTagCompound compound);
//
//    @Nullable
//    EntityItem dropItem(Item itemIn, int size);
//
//    @Nullable
//    EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY);
//
//    @Nullable
//    EntityItem entityDropItem(ItemStack stack, float offsetY);

    boolean isEntityAlive();

    boolean isEntityInsideOpaqueBlock();

//    boolean processInitialInteract(EntityPlayer player, EnumHand hand);
//
//    @Nullable
//    AxisAlignedBB getCollisionBox(Entity entityIn);

    void updateRidden();

//    void updatePassenger(Entity passenger);

//    @SideOnly(Side.CLIENT)
//    void applyOrientationToEntity(Entity entityToUpdate);

    double getYOffset();

    double getMountedYOffset();

//    boolean startRiding(Entity entityIn);
//
//    boolean startRiding(Entity entityIn, boolean force);

    void removePassengers();

    void dismountRidingEntity();

//    void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport);

    float getCollisionBorderSize();

    Vec3d getLookVec();

//    Vec2f getPitchYaw();

//    Vec3d getForward();

//    void setPortal(BlockPos pos);

    int getPortalCooldown();

    void setVelocity(double x, double y, double z);

    void handleStatusUpdate(byte id);

    void performHurtAnimation();

//    Iterable<ItemStack> getHeldEquipment();
//
//    Iterable<ItemStack> getArmorInventoryList();

//    Iterable<ItemStack> getEquipmentAndArmor();

//    void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack);

    boolean isBurning();

    boolean isRiding();

    boolean isBeingRidden();

    boolean isSneaking();

    void setSneaking(boolean sneaking);

    boolean isSprinting();

    void setSprinting(boolean sprinting);

    boolean isGlowing();

    void setGlowing(boolean glowingIn);

    boolean isInvisible();

//    boolean isInvisibleToPlayer(EntityPlayer player);

//    Team getTeam();

//    boolean isOnSameTeam(Entity entityIn);

//    boolean isOnScoreboardTeam(Team teamIn);

    void setInvisible(boolean invisible);

    int getAir();

    void setAir(int air);

//    void onStruckByLightning(EntityLightningBolt lightningBolt);

//    void onKillEntity(EntityLivingBase entityLivingIn);

    void setInWeb();

    String getName();

//    Entity[] getParts();

//    boolean isEntityEqual(Entity entityIn);

    float getRotationYawHead();

    void setRotationYawHead(float rotation);

    void setRenderYawOffset(float offset);

    boolean canBeAttackedWithItem();

//    boolean hitByEntity(Entity entityIn);

    String toString();

//    boolean isEntityInvulnerable(DamageSource source);

    boolean getIsInvulnerable();

    void setEntityInvulnerable(boolean isInvulnerable);

//    void copyLocationAndAnglesFrom(Entity entityIn);

//    Entity changeDimension(int dimensionIn);

        // Forge: Entities that require custom handling should override this method, not the other
//    Entity changeDimension(int dimensionIn, net.minecraftforge.common.util.ITeleporter teleporter);

    boolean isNonBoss();

//    float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn);

//    boolean canExplosionDestroyBlock(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_);

    int getMaxFallHeight();

    Vec3d getLastPortalVec();

//    EnumFacing getTeleportDirection();

    boolean doesEntityNotTriggerPressurePlate();

//    void addEntityCrashInfo(CrashReportCategory category);

    void setUniqueId(UUID uniqueIdIn);

//    boolean canRenderOnFire();

    UUID getUniqueID();

    String getCachedUniqueIdString();

    boolean isPushedByWater();

//    ITextComponent getDisplayName();

    void setCustomNameTag(String name);

    String getCustomNameTag();

    boolean hasCustomName();

    void setAlwaysRenderNameTag(boolean alwaysRenderNameTag);

    boolean getAlwaysRenderNameTag();

    void setPositionAndUpdate(double x, double y, double z);

//    void notifyDataManagerChange(DataParameter<?> key);

    boolean getAlwaysRenderNameTagForRender();

//    EnumFacing getHorizontalFacing();

//    EnumFacing getAdjustedHorizontalFacing();

//    boolean isSpectatedByPlayer(EntityPlayerMP player);

//    AxisAlignedBB getEntityBoundingBox();

//    AxisAlignedBB getRenderBoundingBox();

//    void setEntityBoundingBox(AxisAlignedBB bb);

    boolean isOutsideBorder();

    void setOutsideBorder(boolean outsideBorder);

//    boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn);

//    void sendMessage(ITextComponent component);

    boolean canUseCommand(int permLevel, String commandName);

//    BlockPos getPosition();

    Vec3d getPositionVector();

//    World getEntityWorld();

//    Entity getCommandSenderEntity();

    boolean sendCommandFeedback();

//    void setCommandStat(CommandResultStats.Type type, int amount);

//    MinecraftServer getServer();

//    CommandResultStats getCommandStats();

//    void setCommandStats(Entity entityIn);

//    EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand);

    boolean isImmuneToExplosions();

    boolean isAddedToWorld();

    void onAddedToWorld();

    void onRemovedFromWorld();

//    NBTTagCompound getEntityData();

    boolean shouldRiderSit();

//    ItemStack getPickedResult(RayTraceResult target);

    UUID getPersistentID();

    @Deprecated
        // TODO: remove (1.13?)
    void resetEntityId();

    boolean shouldRenderInPass(int pass);

//    boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount);

    boolean canRiderInteract();

//    boolean shouldDismountInWater(Entity rider);

//    boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing);

//    <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing);

//    void deserializeNBT(NBTTagCompound nbt);

//    NBTTagCompound serializeNBT();

//    boolean canTrample(World world, Block block, BlockPos pos, float fallDistance);

//    void addTrackingPlayer(EntityPlayerMP player);

//    void removeTrackingPlayer(EntityPlayerMP player);

//    float getRotatedYaw(Rotation transformRotation);

    float getMirroredYaw(Mirror transformMirror);

    boolean ignoreItemEntityData();

    boolean setPositionNonDirty();

//    Entity getControllingPassenger();

//    List<Entity> getPassengers();

//    boolean isPassenger(Entity entityIn);

//    Collection<Entity> getRecursivePassengers();

//    <T extends Entity> Collection<T> getRecursivePassengersByType(Class<T> entityClass);

//    Entity getLowestRidingEntity();

//    boolean isRidingSameEntity(Entity entityIn);

//    boolean isRidingOrBeingRiddenBy(Entity entityIn);

    boolean canPassengerSteer();

//    Entity getRidingEntity();

//    EnumPushReaction getPushReaction();

//    SoundCategory getSoundCategory();
*/
}
