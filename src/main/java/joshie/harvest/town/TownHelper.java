package joshie.harvest.town;

import joshie.harvest.api.town.TownManager;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.UUID;

import static joshie.harvest.core.HFTrackers.getTownTracker;

@HFApiImplementation
public class TownHelper implements TownManager {
    @SuppressWarnings("unchecked")
    public static <T extends TownData> T getClosestTownToBlockPos(World world, BlockPos pos) {
        return (T) getTownTracker(world).getClosestTownToBlockPos(pos);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TownData> T getClosestTownToEntity(Entity entity) {
        return (T) getTownTracker(entity.worldObj).getClosestTownToBlockPos(new BlockPos(entity));
    }

    public static TownData getTownByID(World world, UUID townID) {
        return getTownTracker(world).getTownByID(townID);
    }

    public static void ensureBuilderExists(World world, BlockPos pos, TownData original) {
        if (!world.isRemote) {
            TownDataServer data = (TownDataServer) original;
            if (data.getBuilder((WorldServer) world) == null) {
                data.createNewBuilder(world, pos);
            }
        }
    }

    @Override
    public boolean doesClosestTownHaveBuildings(Entity entity, ResourceLocation... buildings) {
        return getClosestTownToEntity(entity).hasBuildings(buildings);
    }
}
