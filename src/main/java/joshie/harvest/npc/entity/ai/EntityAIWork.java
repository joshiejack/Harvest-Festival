package joshie.harvest.npc.entity.ai;

import joshie.harvest.npc.entity.EntityNPCShopkeeper;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIWork extends EntityAIBase {
    private EntityNPCShopkeeper npc;

    public EntityAIWork(EntityNPCShopkeeper npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return npc.getNPC().getShop() != null && npc.getNPC().getShop().isOpen(npc.worldObj, null);
    }

    @Override
    public boolean continueExecuting() {
        return npc.getNPC().getShop().isOpen(npc.worldObj, null);
    }

    @Override
    public void startExecuting() {
        npc.getNavigator().clearPathEntity();
    }
}
