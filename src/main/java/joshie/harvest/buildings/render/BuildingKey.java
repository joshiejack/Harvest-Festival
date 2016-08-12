package joshie.harvest.buildings.render;

import joshie.harvest.buildings.Building;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ObjectUtils;

public class BuildingKey {
    private final BlockPos pos;
    private final Mirror mirror;
    private final Rotation rotation;
    private final Building building;

    public BuildingKey(BlockPos pos, Mirror mirror, Rotation rotation, Building building) {
        this.pos = pos;
        this.mirror = mirror;
        this.rotation = rotation;
        this.building = building;
    }

    public static BuildingKey of(BlockPos pos, Mirror mirror, Rotation rotation, Building building) {
        return new BuildingKey(pos, mirror, rotation, building);
    }

    public BlockPos getPos() {
        return pos;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Building getBuilding() {
        return building;
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof BuildingKey) {
            final BuildingKey other = (BuildingKey) obj;
            return ObjectUtils.equals(getPos(), other.getPos())
                    && ObjectUtils.equals(getMirror(), other.getMirror())
                    && ObjectUtils.equals(getRotation(), other.getRotation())
                    && ObjectUtils.equals(getBuilding(), other.getBuilding());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (getPos() == null ? 0 : getPos().hashCode()) ^
                (getMirror() == null ? 0 : getMirror().hashCode()) ^
                (getRotation() == null ? 0 : getRotation().hashCode()) ^
                (getBuilding() == null ? 0 : getBuilding().hashCode());
    }
}
