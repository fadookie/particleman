package com.eliotlash.particlelib.mcwrapper;

public class BlockPos {
    int x;
    int y;
    int z;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setPos(double x, double y, double z) {
        this.x = (int)x;
        this.y = (int)y;
        this.z = (int)z;
    }
}
