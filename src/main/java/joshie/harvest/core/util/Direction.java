package joshie.harvest.core.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Locale;

public enum Direction implements IStringSerializable {
    MN_R0(Mirror.NONE, Rotation.NONE),
    MN_R90(Mirror.NONE, Rotation.CLOCKWISE_90),
    MN_R180(Mirror.NONE, Rotation.CLOCKWISE_180),
    MN_R270(Mirror.NONE, Rotation.COUNTERCLOCKWISE_90),
    MLR_R0(Mirror.LEFT_RIGHT, Rotation.NONE),
    MLR_R90(Mirror.LEFT_RIGHT, Rotation.CLOCKWISE_90),
    MLR_R180(Mirror.LEFT_RIGHT, Rotation.CLOCKWISE_180),
    MLR_R270(Mirror.LEFT_RIGHT, Rotation.COUNTERCLOCKWISE_90),
    MFB_R0(Mirror.FRONT_BACK, Rotation.NONE),
    MFB_R90(Mirror.FRONT_BACK, Rotation.CLOCKWISE_90),
    MFB_R180(Mirror.FRONT_BACK, Rotation.CLOCKWISE_180),
    MFB_R270(Mirror.FRONT_BACK, Rotation.COUNTERCLOCKWISE_90);

    public static final HashMap<Pair<Mirror, Rotation>, Direction> map = new HashMap<Pair<Mirror, Rotation>, Direction>();
    private final Mirror mirror;
    private final Rotation rotation;

    Direction(Mirror mirror, Rotation rotation) {
        this.mirror = mirror;
        this.rotation = rotation;
    }

    public Mirror getMirror() {
        return this.mirror;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public IBlockState withDirection(IBlockState state) {
        return state.withMirror(mirror).withRotation(rotation);
    }

    public static Direction withMirrorAndRotation(Mirror mirror, Rotation rotation) {
        return map.get(Pair.of(mirror, rotation));
    }

    @Override
    public String getName() {
        return toString().toLowerCase(Locale.ENGLISH);
    }

    static {
        for (Direction direction: Direction.values()) {
            Direction.map.put(Pair.of(direction.getMirror(), direction.getRotation()), direction);
        }
    }
}
