package joshie.harvest.buildings;

import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.Direction;
import joshie.harvest.core.util.interfaces.INBTWriteable;
import joshie.harvest.town.data.TownDataServer;
import joshie.harvest.town.TownHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage implements INBTWriteable {
    public BuildingImpl building;
    public Rotation rotation;
    public ConstructionStage stage;
    private boolean basement;
    private int index;
    public BlockPos pos;

    public BuildingStage(){}
    public BuildingStage(BuildingImpl building, BlockPos pos,  Rotation rotation) {
        this.building = building;
        this.rotation = rotation;
        this.stage = ConstructionStage.BUILD;
        this.index = 0;
        this.basement = true;
        this.pos = pos.add(0, building.getOffsetY(), 0);
        HFBuildings.loadBuilding(building);
    }

    public Placeable next() {
        return index < building.components.length ? building.components[index]: null;
    }

    public Placeable previous() {
        int position = index - 1;
        return position >= 0 && position < building.components.length ? building.components[position]: null;
    }

    public BuildingImpl getBuilding() {
        return building;
    }

    public ConstructionStage getStage() {
        return stage;
    }

    public BlockPos getPos(Placeable placeable) {
        if (placeable == null) return next().getTransformedPosition(pos, rotation);
        return placeable.getTransformedPosition(pos, rotation);
    }

    public double getDistance(Placeable placeable) {
        return placeable.getOffsetPos().getY() <= -building.getOffsetY()? 256D: 96D;
    }

    private boolean increaseIndex(World world) {
        index++;

        if (index >= building.components.length) {
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

                basement = true;
                TownDataServer data = TownHelper.getClosestTownToBlockPos(world, pos);
                data.addBuilding(world, building, rotation, pos);
                data.syncBuildings(world);
                return true;
            }

            TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos).syncBuildings(world);
        }

        //Resync when we change
        if (basement) {
            Placeable placeable = next();
            if (placeable.getOffsetPos().getY() >= -getBuilding().getOffsetY()) {
                basement = false;
                HFTrackers.markDirty(world);
                TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos).syncBuildings(world);
            }
        }

        return true;
    }

    public boolean build(World world) {
        while (index < building.components.length) {
            Placeable block = building.components[index];
            if (block.place(world, pos, rotation, stage, true)) {
                return increaseIndex(world);
            }

            increaseIndex(world);
            if (isFinished()) return true;
        }


        return isFinished() || increaseIndex(world);
    }

    public long getTickTime() {
        return building.getTickTime();
    }

    public boolean isFinished() {
        return stage == ConstructionStage.FINISHED;
    }

    public boolean isWorkingOnBasement() {
        return basement;
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
        //TODO: Remove in 0.7+
        if (nbt.hasKey("Direction")) {
            Direction direction = Direction.valueOf(nbt.getString("Direction"));
            stage.rotation = direction.getRotation();
        } else stage.rotation = Rotation.valueOf(nbt.getString("Rotation"));

        stage.pos = new BlockPos(nbt.getInteger("BuildingX"), nbt.getInteger("BuildingY"), nbt.getInteger("BuildingZ"));
        stage.basement = nbt.getBoolean("Basement");

        if (nbt.hasKey("Stage")) {
            stage.index = nbt.getInteger("Index");
            stage.stage = ConstructionStage.values()[nbt.getInteger("Stage")];
        }

        HFBuildings.loadBuilding(stage.building);
        return stage;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", BuildingRegistry.REGISTRY.getKey(building).toString());
        nbt.setString("Rotation", rotation.name());
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