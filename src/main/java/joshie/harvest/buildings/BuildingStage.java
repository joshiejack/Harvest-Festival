package joshie.harvest.buildings;

import joshie.harvest.api.buildings.Building;
import joshie.harvest.buildings.placeable.Placeable;
import joshie.harvest.buildings.placeable.Placeable.ConstructionStage;
import joshie.harvest.core.util.Direction;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.interfaces.INBTWriteable;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage implements INBTWriteable {
    public Building building;
    private HFTemplate template;
    public Rotation rotation;
    public ConstructionStage stage;
    private int index;
    public BlockPos pos;

    public BuildingStage(){}
    public BuildingStage(Building building, BlockPos pos, Rotation rotation) {
        this.building = building;
        this.template = BuildingRegistry.INSTANCE.getTemplateForBuilding(building);
        this.rotation = rotation;
        this.stage = ConstructionStage.BUILD;
        this.index = 0;
        this.pos = pos.add(0, building.getOffsetY(), 0);
        //HFBuildings.loadBuilding(building);
    }

    public Placeable next() {
        return index < template.getComponents().length ? template.getComponents()[index]: null;
    }

    public Placeable previous() {
        int position = index - 1;
        return position >= 0 && position < template.getComponents().length ? template.getComponents()[position]: null;
    }

    public Building getBuilding() {
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

        if (index >= template.getComponents().length) {
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

                TownDataServer data = TownHelper.getClosestTownToBlockPos(world, pos, false);
                data.addBuilding(world, building, rotation, pos);
                data.syncBuildings(world);
                return true;
            }

            TownHelper.<TownDataServer>getClosestTownToBlockPos(world, pos, false).syncBuildings(world);
        }

        return true;
    }

    public boolean build(World world) {
        while (index < template.getComponents().length) {
            Placeable block = template.getComponents()[index];
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

    @SuppressWarnings("deprecation")
    public static BuildingStage readFromNBT(NBTTagCompound nbt) {
        BuildingStage stage = new BuildingStage();
        stage.building = Building.REGISTRY.get(new ResourceLocation(nbt.getString("CurrentlyBuilding")));
        stage.template = BuildingRegistry.INSTANCE.getTemplateForBuilding(stage.building);
        //TODO: Remove in 0.7+
        if (nbt.hasKey("Direction")) {
            Direction direction = Direction.valueOf(nbt.getString("Direction"));
            stage.rotation = direction.getRotation();
        } else stage.rotation = Rotation.valueOf(nbt.getString("Rotation"));

        stage.pos = new BlockPos(nbt.getInteger("BuildingX"), nbt.getInteger("BuildingY"), nbt.getInteger("BuildingZ"));

        if (nbt.hasKey("Stage")) {
            stage.index = nbt.getInteger("Index");
            stage.stage = ConstructionStage.values()[nbt.getInteger("Stage")];
        }

        return stage;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", building.getResource().toString());
        nbt.setString("Rotation", rotation.name());
        nbt.setInteger("BuildingX", pos.getX());
        nbt.setInteger("BuildingY", pos.getY());
        nbt.setInteger("BuildingZ", pos.getZ());

        if (stage != null) {
            nbt.setInteger("Stage", stage.ordinal());
            nbt.setInteger("Index", index);
        }
    }
}