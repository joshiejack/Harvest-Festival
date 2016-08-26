package joshie.harvest.npc.entity;

import joshie.harvest.npc.NPC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityNPCShopkeeper extends EntityNPCHuman<EntityNPCShopkeeper> {
    private boolean isWorking;

    public EntityNPCShopkeeper(World world) {
        super(world);
    }

    public EntityNPCShopkeeper(World world, NPC npc) {
        super(world, npc);
    }

    public EntityNPCShopkeeper(EntityNPCShopkeeper entity) {
        super(entity);
        isWorking = entity.isWorking;
    }

    @Override
    public EntityNPCShopkeeper getNewEntity(EntityNPCShopkeeper entity) {
        return new EntityNPCShopkeeper(entity);
    }

    @Override
    protected void updateAITasks() {
        if (npc.getShop() != null) {
            if (!isWorking) {
                if (npc.getShop().isOpen(worldObj, null)) {
                    isWorking = true;
                }
            } else if (worldObj.getWorldTime() % 200 == 0 && !npc.getShop().isOpen(worldObj, null)) {
                isWorking = false;
            }
        }

        if (!isWorking) {
            super.updateAITasks();
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        isWorking = nbt.getBoolean("IsWorking");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("IsWorking", isWorking);
    }
}