package joshie.harvest.mining;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.mining.block.BlockPortal.Portal;
import joshie.harvest.mining.gen.MiningProvider;
import joshie.harvest.town.tracker.TownTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;
import java.util.Random;

import static joshie.harvest.api.calendar.Season.WINTER;
import static joshie.harvest.mining.HFMining.MINING_ID;
import static joshie.harvest.mining.gen.MineManager.CHUNK_BOUNDARY;

public class MiningHelper {
    public static final int MYSTRIL_FLOOR = 163;
    public static final int GEM_FLOOR = 123;
    public static final int GOLD_FLOOR = 83;
    public static final int SILVER_FLOOR = 43;
    public static final int COPPER_FLOOR = 10;
    public static final double WORLD_HEIGHT = 256D;
    public static final int MAX_Y = (int) WORLD_HEIGHT - 1;
    public static final int FLOOR_HEIGHT = 6;
    public static final int MAX_FLOORS = (int) Math.floor(WORLD_HEIGHT / FLOOR_HEIGHT);
    public static final int MAX_LOOP = (int) WORLD_HEIGHT - FLOOR_HEIGHT;
    public static final int CHICK_FLOORS = 7;
    public static final int CHICKEN_FLOORS = 11;
    public static final int SHEEP_FLOORS = 13;
    public static final int COW_FLOORS = 17;
    public static final TIntSet HOLE_FLOORS = new TIntHashSet();
    static {
        //1k to copper (1)
        //2k to silver (2)
        //4k to gold (4)
        //8k to gem(8)
        //15k to mystril (15)
        addFloors(9); //Copper unlock, 1k
        addFloors(27, 41);//Silver unlock, 2k
        addFloors(51, 61, 71, 81); //Gold unlock, 4k
        addFloors(83, 88, 93, 98, 103, 108, 113, 118); //Gem unlock, 8k
        addFloors(124, 128, 129, 132, 133, 140, 143, 145, 149, 150, 152, 156, 157, 160, 161); //Mystril unlock, 15 floors
    }

    private static void addFloors(int... ints) {
        HOLE_FLOORS.addAll(ints);
    }

    public static List<ItemStack> getLoot(ResourceLocation loot, World world, EntityPlayer player, float luck) {
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
        lootcontext$builder.withLuck(player.getLuck() + luck);
        lootcontext$builder.withPlayer(player);
        return world.getLootTableManager().getLootTableFromLocation(loot).generateLootForPools(world.rand, lootcontext$builder.build());
    }

    public static int getMineID(int chunkZ) {
        return (int)Math.floor(chunkZ / CHUNK_BOUNDARY);
    }

    private static void preloadChunks(WorldServer worldServer, int mineID, int floor) {
        MiningProvider provider = ((MiningProvider)worldServer.provider);
        if (!provider.areCoordinatesGenerated(mineID, floor)) {
            int xStart = (int) Math.floor((floor - 1) / MAX_FLOORS);
            for (int x = xStart * CHUNK_BOUNDARY; x < (xStart * CHUNK_BOUNDARY) + CHUNK_BOUNDARY; x++) {
                for (int z = mineID * CHUNK_BOUNDARY; z < (mineID * CHUNK_BOUNDARY) + CHUNK_BOUNDARY; z++) {
                    worldServer.getChunkProvider().provideChunk(x, z);
                }
            }

            //Force a save
            if (worldServer instanceof WorldServerMulti) {
                ((WorldServerMulti) worldServer).saveAdditionalData();
            }
        }
    }

    private static boolean isSpawnable(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isPassable(world, pos);
    }

    private static BlockPos modifySpawnAndPlayerRotation(WorldServer dim, BlockPos spawn, Entity entity) {
        IBlockState actual = HFMining.PORTAL.getActualState(dim.getBlockState(spawn), dim, spawn);
        if (actual.getBlock() == HFMining.PORTAL) {
            Portal portal = HFMining.PORTAL.getEnumFromState(actual);
            for (int distance = 2; distance < 9; distance++) {
                if (portal.isEW()) {
                    if (isSpawnable(dim, spawn.north(distance))) {
                        entity.rotationYaw = 180F;
                        return spawn.north(distance);
                    } else if (isSpawnable(dim, spawn.south(distance))) {
                        entity.rotationYaw = 0F;
                        return spawn.south(distance);
                    }
                } else {
                    if (isSpawnable(dim, spawn.east(distance))) {
                        entity.rotationYaw = 270F;
                        return spawn.east(distance);
                    } else if (isSpawnable(dim, spawn.west(distance))) {
                        entity.rotationYaw = 90F;
                        return spawn.west(distance);
                    }
                }
            }
        }

        return spawn;
    }

    public static boolean teleportToMine(Entity entity) {
        int id = HFTrackers.getTownTracker(entity.worldObj).getMineIDFromCoordinates(new BlockPos(entity));
        return id != -1 && teleportToMine(entity, id);
    }

    public static boolean teleportToMine(Entity entity, int mineID) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer newWorld = server.worldServerForDimension(MINING_ID);
        preloadChunks(newWorld, mineID, 1);
        BlockPos spawn = modifySpawnAndPlayerRotation(newWorld, ((MiningProvider)newWorld.provider).getSpawnCoordinateForMine(mineID, 1), entity);
        return EntityHelper.teleport(entity, MINING_ID, spawn);
    }

    public static boolean teleportToOverworld(Entity entity) {
        int mineID = getMineID((int)entity.posZ >> 4);
        TownTracker tracker = HFTrackers.getTownTracker(DimensionManager.getWorld(0));
        BlockPos spawn = tracker.getCoordinatesForOverworldMine(entity, mineID);
        Rotation rotation = tracker.getMineOrientation(mineID);
        if (spawn == null) spawn = entity.worldObj.getSpawnPoint();
        if (rotation == Rotation.NONE) {
            entity.rotationYaw = 90F;
        } else if (rotation == Rotation.CLOCKWISE_90) {
            entity.rotationYaw = 180F;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            entity.rotationYaw = 270F;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
            entity.rotationYaw = 0F;
        }

        return EntityHelper.teleport(entity, 0, spawn);
    }

    public static boolean teleportBetweenMine(Entity entity) {
        int mineID = MiningHelper.getMineID((int)entity.posZ >> 4); //Current Mine
        int floor = getFloor((int)entity.posX >> 4, (int) entity.posY); //Current Floor
        boolean top = floor % MAX_FLOORS == 1;
        int newFloor = top ? floor - 1 : floor + 1;
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer newWorld = server.worldServerForDimension(MINING_ID);
        preloadChunks(newWorld, mineID, newFloor);
        BlockPos spawn = modifySpawnAndPlayerRotation(newWorld, ((MiningProvider)newWorld.provider).getSpawnCoordinateForMine(mineID, newFloor), entity);
        if (entity.timeUntilPortal == 0) {
            entity.timeUntilPortal = 100;
            if (entity instanceof EntityPlayerMP) {
                ReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP) entity, true, "invulnerableDimensionChange", "field_184851_cj");
            }

            entity.setPositionAndUpdate(spawn.getX() + 0.5D, spawn.getY() + 0.1D, spawn.getZ() + 0.5D);
        }

        //Teleport complete
        return true;
    }

    public static int getFloor(int xPosition, int posY) {
        int chunkIndex = (int) Math.floor(((double)xPosition) / CHUNK_BOUNDARY);
        int floorIndex = (int) (MAX_FLOORS - Math.floor(((double)posY) / FLOOR_HEIGHT));
        return (chunkIndex * MAX_FLOORS) + floorIndex; //Floor
    }

    public static int getOreChance(Season season, int floor, Random rand) {
        int lowerLimit = season == WINTER ? 8: 10;
        int upperLimit = season == WINTER ? 16: 20;

        int chance = season == WINTER ? 7 + rand.nextInt(9) : 10 + rand.nextInt(11);
        if (floor %COW_FLOORS == 0) {
            chance -= 5;
        } else if (floor %SHEEP_FLOORS == 0) {
            chance -= 3;
        } else if (floor %CHICKEN_FLOORS == 0) {
            chance -= 2;
        } else if (floor %CHICK_FLOORS == 0) {
            chance--;
        }

        //7 in winter < lowest, 15 max winter
        //10 in spring < lowest, 20 max spring
        if (chance >= upperLimit) chance = upperLimit;
        if (chance <= lowerLimit) chance  = lowerLimit;
        return chance;
    }
}
