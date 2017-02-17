package joshie.harvest.npcs.entity.ai;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPCShopkeeper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;


public class EntityAIWork extends EntityAIBase {
    private final EntityNPCShopkeeper npc;
    private BlockPos target;

    public EntityAIWork(EntityNPCShopkeeper npc) {
        this.npc = npc;
        this.setMutexBits(1);
    }

    private BuildingLocation getBuildingTarget(CalendarDate date) {
        return npc.getNPC().getScheduler().getTarget(npc.worldObj, npc, npc.getNPC(), date.getSeason(), HFApi.calendar.getWeekday(npc.worldObj), CalendarHelper.getTime(npc.worldObj));
    }

    @Override
    public boolean shouldExecute() {
        BlockPos pos = new BlockPos(npc);
        if(npc.getNPC().getShop(npc.worldObj, pos, null) != null && NPCHelper.isShopOpen(npc.worldObj, npc, null, npc.getNPC().getShop(npc.worldObj, pos, null))) {
            target = NPCHelper.getCoordinatesForLocation(npc, getBuildingTarget(HFApi.calendar.getDate(npc.worldObj)));
            return target != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean continueExecuting() {
        return NPCHelper.isShopOpen(npc.worldObj, npc, null, npc.getNPC().getShop(npc.worldObj, new BlockPos(npc), null));
    }

    @Override
    public void startExecuting() {
        if (npc.getDistanceSq(target) >= 1.5D) {
            npc.setPositionAndUpdate(target.getX() + 0.5D, target.getY() + 1D, target.getZ() + 0.5D);
        }

        npc.getNavigator().clearPathEntity();
    }
}
