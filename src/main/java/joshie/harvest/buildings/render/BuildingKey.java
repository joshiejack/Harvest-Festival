package joshie.harvest.buildings.render;

import joshie.harvest.api.buildings.Building;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ObjectUtils;

@SuppressWarnings("WeakerAccess")
public class BuildingKey {
    private final Rotation rotation;
    private final Building building;
    private BlockPos pos;

    public BuildingKey(Rotation rotation, Building building) {
        this.rotation = rotation;
        this.building = building;
    }

    public static BuildingKey of(Rotation rotation, Building building, BlockPos pos) {
        return new BuildingKey(rotation, building).setPosition(pos);
    }

    public BuildingKey setPosition(BlockPos position) {
        this.pos = position;
        return this;
    }

    public BlockPos getPos() {
        return pos;
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
            return ObjectUtils.equals(getRotation(), other.getRotation())
                    && ObjectUtils.equals(getBuilding(), other.getBuilding());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (getRotation() == null ? 0 : getRotation().hashCode()) ^
                (getBuilding() == null ? 0 : getBuilding().hashCode());
    }
}
