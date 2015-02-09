package joshie.harvestmoon.entities;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.BuildingStage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityNPCBuilder extends EntityNPC {
    private BuildingStage building;

    public EntityNPCBuilder(EntityNPCBuilder entity) {
        super(entity);
        building = entity.building;
    }

    public EntityNPCBuilder(World world) {
        super(world);
    }

    public EntityNPCBuilder(World world, NPC npc) {
        super(world, npc);
    }

    @Override
    protected void updateAITick() {
        if (building == null) {
            super.updateAITick();
        } else {
            if (!worldObj.isRemote) {
                building = building.build(worldObj);
                if (building.isFinished()) {
                    building = null;
                }
            }
        }
    }

    public boolean startBuilding(Building building, int x, int y, int z) {
        if (!worldObj.isRemote) {
            this.building = new BuildingStage(building, x, y, z, worldObj.rand);
        }

        return false;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("CurrentlyBuilding")) {
            building = new BuildingStage();
            building.readFromNBT(nbt);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (building != null) {
            building.writeToNBT(nbt);
        }
    }
}
