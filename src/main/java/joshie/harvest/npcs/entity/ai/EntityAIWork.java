package joshie.harvest.npcs.entity.ai;

import joshie.harvest.api.npc.NPC.Location;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPCShopkeeper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;


public class EntityAIWork extends EntityAIBase {
    private final EntityNPCShopkeeper npc;
    private BlockPos target;
    private int moveTimer;

    public EntityAIWork(EntityNPCShopkeeper npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if(npc.getNPC().getShop(npc.worldObj) != null && NPCHelper.isShopOpen(npc.worldObj, npc, null, npc.getNPC().getShop(npc.worldObj))) {
            target = NPCHelper.getCoordinatesForLocation(npc, npc.getNPC().getLocation(Location.SHOP));
            return target != null;
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return NPCHelper.isShopOpen(npc.worldObj, npc, null, npc.getNPC().getShop(npc.worldObj));
    }

    @Override
    public void startExecuting() {
        if (moveTimer %200 == 0) {
            if (npc.getDistanceSq(target) >= 10D) {
                npc.setPositionAndUpdate(target.getX() + 0.5D, target.getY() + 1D, target.getZ() + 0.5D);
            }

            npc.getNavigator().clearPathEntity();
        }

        moveTimer++;
    }
}
