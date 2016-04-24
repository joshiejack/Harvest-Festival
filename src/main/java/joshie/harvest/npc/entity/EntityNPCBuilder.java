package joshie.harvest.npc.entity;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.buildings.Building;
import joshie.harvest.buildings.BuildingStage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityNPCBuilder extends EntityNPCShopkeeper {
    private BuildingStage building;
    private int tick;

    public EntityNPCBuilder(UUID owning_player, EntityNPCBuilder entity) {
        super(owning_player, entity);
        building = entity.building;
    }

    public EntityNPCBuilder(World world) {
        super(world);
    }

    public EntityNPCBuilder(UUID owning_player, World world, INPC npc) {
        super(owning_player, world, npc);
    }

    public boolean isBuilding() {
        return building != null;
    }

    @Override
    protected void updateAITick() {
        if (building == null) {
            super.updateAITick();
        } else {
            if (!worldObj.isRemote) {
                if (tick % building.getTickTime() == 0) {
                    building = building.build(worldObj);
                    if (building.isFinished()) {
                        building = null;
                    }
                }
                
                tick++;
            }
        }
    }

    public boolean startBuilding(Building building, int x, int y, int z, boolean n1, boolean n2, boolean swap, UUID uuid) {
        if (!worldObj.isRemote) {
            this.building = new BuildingStage(uuid, building, x, y, z, n1, n2, swap);
        }

        return false;
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns()) {
            EntityNPCBuilder clone = new EntityNPCBuilder(owning_player, this);
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
