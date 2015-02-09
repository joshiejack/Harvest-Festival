package joshie.harvestmoon.buildings;

import java.util.Random;

import joshie.harvestmoon.buildings.placeable.Placeable;
import joshie.harvestmoon.buildings.placeable.Placeable.PlacementStage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/** This data is used by the BuilderNPC, 
 * to know their current progress through a building project **/
public class BuildingStage {
    private Building building;
    private boolean n1, n2, swap;
    private PlacementStage stage;
    private int index, xCoord, yCoord, zCoord;

    public BuildingStage() {}
    public BuildingStage(Building building, int x, int y, int z, Random rand) {
        this.building = building;
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
        Placeable block = building.list.get(index);        
        block.place(world, xCoord, yCoord, zCoord, n1, n2, swap, stage);
        index++;

        if (index >= building.list.size()) {
            if (stage == PlacementStage.BLOCKS) {
                stage = PlacementStage.TORCHES;
                index = 0;
            } else if (stage == PlacementStage.TORCHES) {
                stage = PlacementStage.ENTITIES;
                index = 0;
            } else if (stage == PlacementStage.ENTITIES) {
                stage = PlacementStage.FINISHED;
                index = 0;
            }
        }

        return this;
    }

    public boolean isFinished() {
        return stage == PlacementStage.FINISHED;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        building = Building.getBuilding(nbt.getString("CurrentlyBuilding"));
        n1 = nbt.getBoolean("North1");
        n2 = nbt.getBoolean("North2");
        swap = nbt.getBoolean("Swap");
        index = nbt.getInteger("Index");
        xCoord = nbt.getInteger("BuildingX");
        yCoord = nbt.getInteger("BuildingY");
        zCoord = nbt.getInteger("BuildingZ");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("CurrentlyBuilding", building.getName());
        nbt.setBoolean("North1", n1);
        nbt.setBoolean("North2", n2);
        nbt.setBoolean("Swap", swap);
        nbt.setInteger("Index", index);
        nbt.setInteger("BuildingX", xCoord);
        nbt.setInteger("BuildingY", yCoord);
        nbt.setInteger("BuildingZ", zCoord);
    }
}