package joshie.harvest.npc.entity;

import joshie.harvest.api.npc.INPC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityNPCShopkeeper extends EntityNPC<EntityNPCShopkeeper> {
    private boolean isWorking;

    public EntityNPCShopkeeper(EntityNPCShopkeeper entity) {
        super(entity);
        isWorking = entity.isWorking;
    }

    @Override
    public EntityNPCShopkeeper getNewEntity(EntityNPC entity) {
        return new EntityNPCShopkeeper((EntityNPCShopkeeper)entity);
    }

    public EntityNPCShopkeeper(World world) {
        super(world);
    }

    public EntityNPCShopkeeper(World world, INPC npc) {
        super(world, npc);
    }

    @Override
    protected void updateTasks() {
        if (npc.getShop() != null) {
            if (!isWorking) {
                if (npc.getShop().isOpen(worldObj, null)) {
                    isWorking = true;
                }
            } else if (worldObj.getWorldTime() % 200 == 0 && !npc.getShop().isOpen(worldObj, null)) {
                isWorking = false;
            }
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