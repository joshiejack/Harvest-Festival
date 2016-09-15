package joshie.harvest.npc.entity.ai;

import joshie.harvest.api.npc.INPC.Location;
import joshie.harvest.npc.NPCHelper;
import joshie.harvest.npc.entity.EntityNPCShopkeeper;
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
        if(npc.getNPC().getShop() != null && npc.getNPC().getShop().isOpen(npc.worldObj, null)) {
            target = NPCHelper.getCoordinatesForLocation(npc, npc.getNPC().getLocation(Location.WORK));
            return target != null;
        } else return false;
    }

    @Override
    public boolean continueExecuting() {
        return npc.getNPC().getShop().isOpen(npc.worldObj, null);
    }

    @Override
    public void startExecuting() {
        if (moveTimer %20 == 0) {
            if (npc.getDistanceSq(target) >= 9D) {
                npc.setPositionAndUpdate(target.getX() + 0.5D, target.getY() + 1D, target.getZ() + 0.5D);
            }

            if (npc.getDistanceSq(target) >= 8D) {
                npc.getNavigator().tryMoveToXYZ(target.getX() + 0.5D, target.getY() + 1D, target.getZ() + 0.5D, 0.55D);
            } else npc.getNavigator().clearPathEntity();
        }

        moveTimer++;
    }
}
