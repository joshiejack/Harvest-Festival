package joshie.harvest.mining.gen;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.lib.LootStrings;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.Random;

import static joshie.harvest.core.helpers.EntityHelper.isSpawnable;
import static joshie.harvest.mining.MiningHelper.MAX_FLOORS;
import static joshie.harvest.mining.MiningHelper.getFloor;

@HFEvents
public class MineManager extends WorldSavedData {
    public static final int CHUNK_BOUNDARY = 10;
    private static final TIntObjectMap<TIntObjectMap<IBlockState[][]>> generation = new TIntObjectHashMap<>();
    private static final TIntObjectMap<int[]> coordinates = new TIntObjectHashMap<>();
    private TIntObjectMap<TIntObjectMap<BlockPos>> portalCoordinates = new TIntObjectHashMap<>();
    private TIntSet generated = new TIntHashSet();

    public MineManager(String string) {
        super(string);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {
        portalCoordinates = NBTHelper.readPositionCollection(tag.getTagList("PortalCoordinates", 10));
        if (tag.hasKey("GeneratedMiner")) {
            generated = new TIntHashSet(tag.getIntArray("GeneratedMiner"));
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        tag.setTag("PortalCoordinates", NBTHelper.writePositionCollection(portalCoordinates));
        tag.setIntArray("GeneratedMiner", generated.toArray());
        return tag;
    }

    public static BlockPos modifyNPCPosition(WorldServer dim, BlockPos spawn, Entity entity) {
        IBlockState actual = HFMining.PORTAL.getActualState(dim.getBlockState(spawn), dim, spawn);
        if (actual.getBlock() == HFMining.PORTAL) {
            Random rand = dim.rand;
            for (int i = 0; i < 512; i++) {
                BlockPos pos = spawn.add(rand.nextInt(51) - 25, 0, rand.nextInt(51) - 25);
                if (isSpawnable(dim, pos)) return pos;
            }
        }

        return MiningHelper.modifySpawnAndEntityRotation(dim, spawn, entity);
    }

    void onTeleportToMine(World world, int mineID) {
        if (!generated.contains(mineID)) {
            EntityNPC entity = NPCHelper.getEntityForNPC(world, HFNPCs.MINER);
            BlockPos pos = modifyNPCPosition((WorldServer)world, getSpawnCoordinateForMine(world, mineID, 1), entity);
            entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Blocks.TORCH));
            entity.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.IRON_PICKAXE));
            boolean foundTorch = false;
            boolean foundChest = false;
            boolean foundSign = false;
            for (int i = 0; i < 128; i++) {
                BlockPos torch = pos.add(world.rand.nextInt(7) - 3, 0, world.rand.nextInt(7));
                if (torch.equals(pos)) continue;
                if (!foundTorch && isSpawnable(world, torch)) {
                    world.setBlockState(torch, Blocks.TORCH.getDefaultState(), 3);
                    foundTorch = true;
                } else if (!foundChest && isSpawnable(world, torch)) {
                    EnumFacing facing = EnumFacing.HORIZONTALS[world.rand.nextInt(EnumFacing.HORIZONTALS.length)];
                    if (!world.isAirBlock(torch.east())) facing = EnumFacing.WEST;
                    else if (!world.isAirBlock(torch.west())) facing = EnumFacing.EAST;
                    else if (!world.isAirBlock(torch.north())) facing = EnumFacing.SOUTH;
                    else if (!world.isAirBlock(torch.south())) facing = EnumFacing.NORTH;
                    world.setBlockState(torch, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing));
                    TileEntity chest = world.getTileEntity(torch);
                    if (chest instanceof TileEntityChest) {
                        ((TileEntityChest)chest).setLootTable(LootStrings.MINING_CHEST, world.rand.nextLong());
                    }

                    foundChest = true;
                } else if (!foundSign && isSpawnable(world, torch)) {
                    world.setBlockState(torch, Blocks.STANDING_SIGN.getDefaultState().withProperty(BlockStandingSign.ROTATION, world.rand.nextInt(16)));
                    TileEntity tile = world.getTileEntity(torch);
                    if (tile instanceof TileEntitySign) {
                        TileEntitySign sign = ((TileEntitySign) tile);
                        sign.signText[0] = new TextComponentTranslation("harvestfestival.shop.miner");
                        sign.signText[1] = new TextComponentTranslation("harvestfestival.shop.miner.sign1");
                        sign.signText[2] = new TextComponentTranslation("harvestfestival.shop.miner.sign2");
                        sign.signText[3] = new TextComponentTranslation("harvestfestival.shop.miner.sign3");
                        sign.markDirty();
                        IBlockState state = world.getBlockState(sign.getPos());
                        world.notifyBlockUpdate(sign.getPos(), state, state, 3);
                    }

                    foundSign = true;
                }

                if (foundChest && foundTorch && foundSign) break;
            }

            world.spawnEntityInWorld(entity);
            generated.add(mineID);
        }

        markDirty();
    }

    static boolean areCoordinatesGenerated(World world, int mineID, int floor) {
        return HFTrackers.getMineManager(world).getCoordinateMap(mineID).containsKey(floor);
    }

    BlockPos getSpawnCoordinateForMine(World world, int mineID, int floor) {
        BlockPos ret = getCoordinateMap(mineID).get(floor);
        if (ret == null || getFloor(ret) != floor) {
            int chunkX = (int) (Math.floor(((double)floor - 1) / MAX_FLOORS) * CHUNK_BOUNDARY * 16);
            BlockPos pos = new BlockPos(chunkX, (floor - 1) % MAX_FLOORS == 0 ? 247 : 1, mineID * CHUNK_BOUNDARY * 16);
            for (int x = 0; x < 16 * CHUNK_BOUNDARY; x++) {
                for (int z = 0; z < 16 * CHUNK_BOUNDARY; z++) {
                    BlockPos toCheck = pos.add(x, 0, z);
                    IBlockState state = world.getBlockState(toCheck);
                    if (state.getBlock() == HFMining.PORTAL && state.getActualState(world, toCheck).getValue(HFMining.PORTAL.property).isCentre()) {
                        getCoordinateMap(mineID).put(floor, ret);
                        return toCheck;
                    }
                }
            }

            return new BlockPos(0, 254, mineID * CHUNK_BOUNDARY * 16);
        }

        return ret;
    }

    private TIntObjectMap<BlockPos> getCoordinateMap(int mineID) {
        TIntObjectMap<BlockPos> map = portalCoordinates.get(mineID);
        if (map == null) {
            map = new TIntObjectHashMap<>();
            portalCoordinates.put(mineID, map);
        }

        return map;
    }

    void setSpawnForMine(int mineID, int floor, int x, int y, int z) {
        getCoordinateMap(mineID).putIfAbsent(floor, new BlockPos(x, y, z));
        markDirty();
    }

    static TIntObjectMap<IBlockState[][]> getStateMap(int mapIndex) {
        return MineManager.generation.get(mapIndex);
    }

     static void putStateMap(int mapIndex, TIntObjectMap<IBlockState[][]>  map) {
         MineManager.generation.put(mapIndex, map);
    }

    static boolean containsStateKey(int mapIndex) {
        return MineManager.generation.containsKey(mapIndex);
    }

    static boolean containsCoordinatesKey(int mapIndex) {
        return MineManager.coordinates.containsKey(mapIndex);
    }

    static void putCoordinates(int mapIndex, int[] coordinates) {
        MineManager.coordinates.put(mapIndex, coordinates);
    }

    static int getCoordinates(int mapIndex, int position) {
        return MineManager.coordinates.get(mapIndex)[position];
    }
}
