package joshie.harvest.buildings.render;

import joshie.harvest.buildings.BuildingImpl;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ObjectUtils;

public class RenderKey {
    private final Rotation rotation;
    private final BuildingImpl building;
    private BlockPos pos;

    public RenderKey(Rotation rotation, BuildingImpl building) {
        this.rotation = rotation;
        this.building = building;
    }

    public static RenderKey of(Rotation rotation, BuildingImpl building, BlockPos pos) {
        return new RenderKey(rotation, building).setPosition(pos);
    }

    public RenderKey setPosition(BlockPos position) {
        this.pos = position;
        return this;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public BuildingImpl getBuilding() {
        return building;
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof RenderKey) {
            final RenderKey other = (RenderKey) obj;
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
