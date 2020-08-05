package com.eliotlash.particlelib.mcwrapper;

public interface IBlock {
    ResourceLocation getResourceLocation();

    class Blocks {
        public static final IBlock AIR = new IBlock() {
            @Override
            public ResourceLocation getResourceLocation() {
                return new ResourceLocation("minecraft", "air");
            }
        };
    }
}
