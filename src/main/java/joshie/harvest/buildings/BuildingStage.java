package joshie.harvest.buildings;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Direction;
import joshie.harvest.town.TownDataServer;
import joshie.harvest.town.TownHelper;
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
    public boolean basement;
    public int index;
    public BlockPos pos;

    public BuildingStage(){}
    public BuildingStage(BuildingImpl building, BlockPos pos, Mirror mirror, Rotation rotation) {
        this.building = building;
        this.direction = Direction.withMirrorAndRotation(mirror, rotation);
        this.stage = ConstructionStage.BUILD;
        this.index = 0;
        this.basement = true;
        this.pos = pos.add(0, building.getOffsetY(), 0);
    }

    public Placeable next() {
        return index < building.getFullList().size() ? building.getFullList().get(index): null;
    }

    public BuildingImpl getBuilding() {
        return building;
    }

    public ConstructionStage getStage() {
        return stage;
    }

    public BlockPos getPos(Placeable placeable) {
        return placeable.getTransformedPosition(pos, direction);
    }

    public double getDistance(Placeable placeable) {
        return placeable.getOffsetPos().getY() <= -building.getOffsetY()? 256D: 96D;
    }

    private boolean increaseIndex(World world) {
        index++;

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

            basement = true; //Reset the basement
            TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos).syncBuildings(world);
        }

        //Resync when we change
        if (basement) {
            Placeable placeable = next();
            if (placeable.getOffsetPos().getY() > -getBuilding().getOffsetY()) {
                basement = false;
                HFTrackers.markDirty(world);
                TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos).syncBuildings(world);
            }
        }

        return false;
    }

    public boolean build(World world) {
        while (index < building.getFullList().size()) {
            Placeable block = building.getFullList().get(index);
            //if (block.isBlocked(world, block.getTransformedPosition(pos.up(), direction))) return false; //Don't do anything while it's blocked
            if (block.place(world, pos, direction, stage, true)) {
                return increaseIndex(world);
            }

            if (increaseIndex(world)) return true;
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
        stage.basement = nbt.getBoolean("Basement");

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
        nbt.setBoolean("Basement", basement);

        if (stage != null) {
            nbt.setInteger("Stage", stage.ordinal());
            nbt.setInteger("Index", index);
        }
    }
}