package joshie.harvest.buildings;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.town.TownHelper;
import joshie.harvest.core.util.Direction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage {
    public BuildingImpl building;
    public Direction direction;
    public ConstructionStage stage;
    public int index;
    public BlockPos pos;

    public BuildingStage(){}
    public BuildingStage(BuildingImpl building, BlockPos pos, Mirror mirror, Rotation rotation) {
        this.building = building;
        this.direction = Direction.withMirrorAndRotation(mirror, rotation);
        this.stage = ConstructionStage.BUILD;
        this.index = 0;
        this.pos = pos.add(0, building.getOffsetY(), 0);
    }

    public BlockPos next() {
        return index < building.getFullList().size() ? building.getFullList().get(index).getTransformedPosition(pos, direction): pos;
    }

    public boolean build(World world) {
        if (index >= building.getFullList().size()) {
            if (stage == ConstructionStage.BUILD) {
                stage = ConstructionStage.DECORATE;
                index = 0;
            } else if (stage == ConstructionStage.DECORATE) {
                stage = ConstructionStage.PAINT;
                index = 0;
            } else if (stage == ConstructionStage.PAINT) {
                stage = ConstructionStage.MOVEIN;
                index = 0;
            } else if (stage == ConstructionStage.MOVEIN) {
                stage = ConstructionStage.FINISHED;
                index = 0;

                TownHelper.getClosestTownToBlockPos(world, pos).addBuilding(world, building, direction, pos);
                return true;
            }
        } else {
            while (index < building.getFullList().size()) {
                Placeable block = building.getFullList().get(index);
                if (block.place(world, pos, direction, stage)) {
                    index++;
                    return false;
                }

                index++;
            }
        }

        return false;
    }

    public long getTickTime() {
        return building.getTickTime();
    }

    public boolean isFinished() {
        return stage == ConstructionStage.FINISHED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildingStage that = (BuildingStage) o;
        return building != null ? building.equals(that.building) : that.building == null;

    }

    @Override
    public int hashCode() {
        return building != null ? building.hashCode() : 0;
    }

    public static BuildingStage readFromNBT(NBTTagCompound nbt) {
        BuildingStage stage = new BuildingStage();
        stage.building = BuildingRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("CurrentlyBuilding")));
        stage.direction = Direction.valueOf(nbt.getString("Direction"));
        stage.pos = new BlockPos(nbt.getInteger("BuildingX"), nbt.getInteger("BuildingY"), nbt.getInteger("BuildingZ"));

        if (nbt.hasKey("Stage")) {
            stage.index = nbt.getInteger("Index");
            stage.stage = ConstructionStage.values()[nbt.getInteger("Stage")];
        }

        return stage;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", BuildingRegistry.REGISTRY.getKey(building).toString());
        nbt.setString("Direction", direction.name());
        nbt.setInteger("BuildingX", pos.getX());
        nbt.setInteger("BuildingY", pos.getY());
        nbt.setInteger("BuildingZ", pos.getZ());

        if (stage != null) {
            nbt.setInteger("Stage", stage.ordinal());
            nbt.setInteger("Index", index);
        }
    }
}