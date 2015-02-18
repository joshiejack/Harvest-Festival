package joshie.harvestmoon.buildings;

import java.util.Random;

import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.Placeable.PlacementStage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage {
    private BuildingGroup building;
    private boolean n1, n2, swap;
    private PlacementStage stage;
    private int subType, index, xCoord, yCoord, zCoord;

    public BuildingStage() {}

    public BuildingStage(BuildingGroup building, int subType, int x, int y, int z, Random rand) {
        this.building = building;
        this.subType = subType;
        this.n1 = rand.nextBoolean();
        this.n2 = rand.nextBoolean();
        this.swap = rand.nextBoolean();
        this.stage = PlacementStage.BLOCKS;
        this.index = 0;
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public BuildingStage build(World world) {
        if (index >= building.getBuilding(subType).getSize()) {
            if (stage == PlacementStage.BLOCKS) {
                stage = PlacementStage.TORCHES;
                index = 0;
            } else if (stage == PlacementStage.TORCHES) {
                stage = PlacementStage.ENTITIES;
                index = 0;
            } else if (stage == PlacementStage.ENTITIES) {
                stage = PlacementStage.NPC;
                index = 0;
            } else if (stage == PlacementStage.NPC) {
                stage = PlacementStage.FINISHED;
                index = 0;
            }
        } else {
            Placeable block = building.getBuilding(subType).get(index);
            block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, stage);
            index++;
        }

        return this;
    }

    public boolean isFinished() {
        return stage == PlacementStage.FINISHED;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = BuildingGroup.getGroup(nbt.getString("CurrentlyBuilding"));
        subType = nbt.getInteger("SubType");
        n1 = nbt.getBoolean("North1");
        n2 = nbt.getBoolean("North2");
        swap = nbt.getBoolean("Swap");
        index = nbt.getInteger("Index");
        xCoord = nbt.getInteger("BuildingX");
        yCoord = nbt.getInteger("BuildingY");
        zCoord = nbt.getInteger("BuildingZ");
        stage = PlacementStage.values()[nbt.getInteger("Stage")];
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", building.getName());
        nbt.setInteger("SubType", subType);
        nbt.setBoolean("North1", n1);
        nbt.setBoolean("North2", n2);
        nbt.setBoolean("Swap", swap);
        nbt.setInteger("Index", index);
        nbt.setInteger("BuildingX", xCoord);
        nbt.setInteger("BuildingY", yCoord);
        nbt.setInteger("BuildingZ", zCoord);
        nbt.setInteger("Stage", stage.ordinal());
    }
}