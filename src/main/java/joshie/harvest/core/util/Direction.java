package joshie.harvest.core.util;

import net.minecraft.util.Rotation;

@Deprecated //TODO: Remove in 0.7+
public enum Direction {
    MN_R0(Rotation.NONE),
    MN_R90(Rotation.CLOCKWISE_90),
    MN_R180(Rotation.CLOCKWISE_180),
    MN_R270(Rotation.COUNTERCLOCKWISE_90),
    MLR_R0(Rotation.NONE),
    MLR_R90(Rotation.CLOCKWISE_90),
    MLR_R180(Rotation.CLOCKWISE_180),
    MLR_R270(Rotation.COUNTERCLOCKWISE_90),
    MFB_R0(Rotation.NONE),
    MFB_R90(Rotation.CLOCKWISE_90),
    MFB_R180(Rotation.CLOCKWISE_180),
    MFB_R270(Rotation.COUNTERCLOCKWISE_90);

    private final Rotation rotation;

    Direction(Rotation rotation) {
        this.rotation = rotation;
    }

    public Rotation getRotation() {
        return this.rotation;
    }
}
