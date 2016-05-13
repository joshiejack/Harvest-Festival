package joshie.harvest.npc.town;

import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class TownTracker {
    public static final TownData NULL_TOWN = new TownData();

    public TownData getClosestTownToBlockPos(int dimension, BlockPos pos) {
        System.out.println("Returning the null town for some reason");
        return NULL_TOWN;
    }

    public abstract TownData createNewTown(int dimension, BlockPos blockPos);

    public void syncToPlayer(EntityPlayer player) {}

    public EntityNPCBuilder getBuilderOrCreate(TownData town, EntityLivingBase player) {
        return new EntityNPCBuilder(player.worldObj);
    }

    public TownData getTownByID(UUID townID) {
        return NULL_TOWN;
    }

    public void newDay(World world) {}
}
