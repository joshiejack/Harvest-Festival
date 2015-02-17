package joshie.harvestmoon.npc;

import joshie.harvestmoon.buildings.Building;
import joshie.harvestmoon.buildings.BuildingStage;
import joshie.harvestmoon.init.HMBuildings;
import net.minecraft.entity.player.EntityPlayer;
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

    @Override
    public boolean interact(EntityPlayer player) {
        startBuilding(HMBuildings.townhall, (int) player.posX, (int) player.posY, (int) player.posZ);
        return true;
    }

    public boolean startBuilding(Building building, int x, int y, int z) {
        if (!worldObj.isRemote) {
            this.building = new BuildingStage(building, building.random(worldObj.rand), x, y, z, worldObj.rand);
        }

        return false;
    }
    
    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns()) {
            EntityNPCBuilder clone = new EntityNPCBuilder(this);
            worldObj.spawnEntityInWorld(clone);
        }

        isDead = true;
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
