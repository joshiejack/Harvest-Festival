package joshie.harvest.town;

import joshie.harvest.core.HFTracker;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public abstract class TownTracker extends HFTracker {
    public static final TownData NULL_TOWN = new TownData().setUUID(UUID.fromString("5b529b64-62dc-35df-416c-05e0210f6ab0"));

    public TownData getClosestTownToBlockPos(BlockPos pos) {
        return NULL_TOWN;
    }

    public abstract TownData createNewTown(BlockPos blockPos);

    public void syncToPlayer(EntityPlayer player) {}

    public EntityNPCBuilder getBuilderOrCreate(TownData town, EntityLivingBase player) {
        return new EntityNPCBuilder(player.worldObj);
    }

    public TownData getTownByID(UUID townID) {
        return NULL_TOWN;
    }

    public void newDay() {}

    public void addBuilder(EntityNPCBuilder npc) {}
}
