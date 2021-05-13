package uk.joshiejack.harvestcore.world.mine;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.harvestcore.world.mine.dimension.MineData;
import uk.joshiejack.harvestcore.world.mine.dimension.OverworldTeleporter;
import uk.joshiejack.harvestcore.world.mine.tier.Tier;
import uk.joshiejack.harvestcore.world.storage.SavedData;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;

import static uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator.CHUNKS_PER_SECTION;
import static uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator.MAX_XZ_PER_SECTION;

public class MineHelper {
    public static Tier getTierFromEntity(Entity entity) {
        return getTierFromPos(entity.world, new BlockPos(entity));
    }

    public static Tier getTierFromPos(World world, BlockPos pos) {
        return getTierFromChunkZ(world.provider.getDimension(), world.getChunk(pos).z);
    }

    public static Tier getTierFromChunkZ(int dimension, int z) {
        return Mine.BY_ID.get(dimension).getTierFromInt(z / CHUNKS_PER_SECTION);
    }

    public static int getRelativeFloor(int posY) {
        return (40 - (posY + 1) / 6);
    }

    public static int getFloorFromTierNumber(int tierNumber, int posY) {
        return getRelativeFloor(posY) + (tierNumber * 40);
    }

    public static int getFloorFromCoordinates(int chunkZ, int posY) {
        return getFloorFromTierNumber(chunkZ / CHUNKS_PER_SECTION, posY);
    }

    public static int getFloorFromEntity(Entity entity) {
        return getFloorFromCoordinates(entity.world.getChunk(new BlockPos(entity)).z, (int) entity.posY);
    }

    public static void fillChunkWith(IBlockState state, ChunkPrimer primer) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 240; y++) {
                    primer.setBlockState(x, y, z, state);
                }
            }
        }
    }

    public static int getMineIDFromChunk(int chunkX) {
        return chunkX / CHUNKS_PER_SECTION;
    }

    public static int getMineIDFromEntity(Entity entity) {
        return getMineIDFromChunk(entity.world.getChunk(new BlockPos(entity)).x);
    }

    public static void teleportToFloor(Entity entity, Mine mine, int id, int floorTarget, boolean portal) {
        int xStart = id * MAX_XZ_PER_SECTION;
        int floorID = 39 - ((floorTarget - 1) % 40); //40 > 39
        int zStart = ((int) Math.floor((floorTarget - 1) / 40)) * MAX_XZ_PER_SECTION; //0 > 1
        MineData data = SavedData.getMineData(entity.world, entity.dimension);
        Int2ObjectMap<Pair<BlockPos, EnumFacing>> portals = data.getPortalsForID(id);
        Int2ObjectMap<Pair<BlockPos, EnumFacing>> elevators = data.getElevatorsForID(id);
        //160 > 1
        int tierNumber = (zStart / 16) / CHUNKS_PER_SECTION;
        long generateID = new BlockPos(id, 0, tierNumber).toLong();
        Tier tier = mine.getTierFromInt(tierNumber);
        tier.map.remove(generateID);
        tier.build(generateID, elevators, portals);
        Pair<BlockPos, EnumFacing> pair = portal ? portals.get(floorTarget) : elevators.get(floorTarget);
        if (pair != null) {
            BlockPos offset = pair.getKey();
            //Prepare for teleportation
            BlockPos target = new BlockPos(xStart, (floorID * 6), zStart).add(offset);
            if (entity.timeUntilPortal == 0) {
                entity.timeUntilPortal = 10;
                entity.rotationYaw = pair.getValue().getHorizontalAngle();
                if (entity instanceof EntityPlayerMP) {
                    ReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP) entity, true, "invulnerableDimensionChange", "field_184851_cj");
                }

                entity.setPositionAndUpdate(target.getX() + 0.5D, target.getY() + 0.1D, target.getZ() + 0.5D);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void teleportToOverworld(Entity entity, int sub_id) {
        TownServer town = AdventureDataLoader.get(entity.world).getTownByID(0, sub_id);
        entity.changeDimension(0, new OverworldTeleporter(town));
    }
}
