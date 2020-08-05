package com.eliotlash.particlelib.mcwrapper;

public class AxisAlignedBB {
    public final double xMin;
    public final double yMin;
    public final double zMin;
    public final double xMax;
    public final double yMax;
    public final double zMax;

    public double getXMin() {
        return xMin;
    }

    public double getYMin() {
        return yMin;
    }

    public double getZMin() {
        return zMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMax() {
        return yMax;
    }

    public double getZMax() {
        return zMax;
    }

    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.xMin = Math.min(x1, x2);
        this.yMin = Math.min(y1, y2);
        this.zMin = Math.min(z1, z2);
        this.xMax = Math.max(x1, x2);
        this.yMax = Math.max(y1, y2);
        this.zMax = Math.max(z1, z2);
    }

    public AxisAlignedBB expand(double x, double y, double z)
    {
        double d0 = this.xMin;
        double d1 = this.yMin;
        double d2 = this.zMin;
        double d3 = this.xMax;
        double d4 = this.yMax;
        double d5 = this.zMax;

        if (x < 0.0) {
            d0 += x;
        } else if (x > 0.0) {
            d3 += x;
        } if (y < 0.0) {
            d1 += y;
        } else if (y > 0.0) {
            d4 += y;
        } if (z < 0.0) {
            d2 += z;
        } else if (z > 0.0) {
            d5 += z;
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public double calculateXOffset(AxisAlignedBB otherBox, double xOffset) {
        if (otherBox.yMax > this.yMin && otherBox.yMin < this.yMax && otherBox.zMax > this.zMin && otherBox.zMin < this.zMax) {
            if (xOffset > 0.0 && otherBox.xMax <= this.xMin) {
                double d1 = this.xMin - otherBox.xMax;

                if (d1 < xOffset) {
                    xOffset = d1;
                }
            }
            else if (xOffset < 0.0 && otherBox.xMin >= this.xMax) {
                double d0 = this.xMax - otherBox.xMin;

                if (d0 > xOffset) {
                    xOffset = d0;
                }
            }

            return xOffset;
        } else {
            return xOffset;
        }
    }

    public double calculateYOffset(AxisAlignedBB otherBox, double yOffset) {
        if (otherBox.xMax > this.xMin && otherBox.xMin < this.xMax && otherBox.zMax > this.zMin && otherBox.zMin < this.zMax) {
            if (yOffset > 0.0D && otherBox.yMax <= this.yMin) {
                double d1 = this.yMin - otherBox.yMax;

                if (d1 < yOffset) {
                    yOffset = d1;
                }
            }
            else if (yOffset < 0.0D && otherBox.yMin >= this.yMax) {
                double d0 = this.yMax - otherBox.yMin;

                if (d0 > yOffset) {
                    yOffset = d0;
                }
            }

            return yOffset;
        } else {
            return yOffset;
        }
    }

    public double calculateZOffset(AxisAlignedBB otherBox, double zOffset) {
        if (otherBox.xMax > this.xMin && otherBox.xMin < this.xMax && otherBox.yMax > this.yMin && otherBox.yMin < this.yMax) {
            if (zOffset > 0.0D && otherBox.zMax <= this.zMin) {
                double d1 = this.zMin - otherBox.zMax;

                if (d1 < zOffset) {
                    zOffset = d1;
                }
            }
            else if (zOffset < 0.0D && otherBox.zMin >= this.zMax) {
                double d0 = this.zMax - otherBox.zMin;

                if (d0 > zOffset) {
                    zOffset = d0;
                }
            }

            return zOffset;
        } else {
            return zOffset;
        }
    }

    public AxisAlignedBB offset(double x, double y, double z) {
        return new AxisAlignedBB(this.xMin + x, this.yMin + y, this.zMin + z, this.xMax + x, this.yMax + y, this.zMax + z);
    }
}
