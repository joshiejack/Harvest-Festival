package joshie.harvest.npcs.entity;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityNPCTrader extends EntityNPCShopkeeper {
    public EntityNPCTrader(World world, NPC npc) {
        super(world, npc);
    }

    @SuppressWarnings("unused")
    public EntityNPCTrader(World world) {
        super(world, HFNPCs.TRADER);
    }

    private EntityNPCTrader(EntityNPCTrader entity) {
        super(entity);
    }

    @Override
    public EntityNPCTrader getNewEntity(EntityNPCShopkeeper entity) {
        return new EntityNPCTrader((EntityNPCTrader) entity);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!worldObj.isRemote && !isTalking() && worldObj.getTotalWorldTime() %300 == 0 && (CalendarHelper.isBetween(worldObj, 18000, 24000) || CalendarHelper.isBetween(worldObj, 0, 5500))) {
            this.setDead();
            this.<TownDataServer>getHomeTown().markNPCDead(getNPC().getRegistryName(), new BlockPos(this));
            HFTrackers.markTownsDirty(); //Mark this npc as dead, ready for tomorrow to be reborn
        }
    }
}