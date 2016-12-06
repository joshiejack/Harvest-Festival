package joshie.harvest.mining.gen;

import com.google.common.collect.Lists;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.MiningRegistry;
import joshie.harvest.mining.block.BlockStone.Type;
import joshie.harvest.mining.entity.EntityDarkChick;
import joshie.harvest.mining.entity.EntityDarkChicken;
import joshie.harvest.mining.entity.EntityDarkCow;
import joshie.harvest.mining.entity.EntityDarkSheep;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static joshie.harvest.mining.MiningHelper.*;
import static joshie.harvest.mining.gen.MineManager.CHUNK_BOUNDARY;

@SuppressWarnings("synthetic-access")
public class MiningChunk implements IChunkGenerator {
    private final Random rand;
    private static final IBlockState WALLS = HFMining.STONE.getDefaultState();
    private static final IBlockState FLOORS = HFMining.DIRT.getDefaultState();
    private static final IBlockState LADDER = HFMining.LADDER.getDefaultState();
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final IBlockState PORTAL = HFMining.PORTAL.getDefaultState();
    private static final IBlockState ORE = HFMining.ORE.getDefaultState();
    private static final IBlockState LADDER_HOLE = HFMining.STONE.getStateFromEnum(Type.LADDER_HOLE);
    private static final List<Biome.SpawnListEntry> MONSTERS = Lists.newArrayList();
    private static final List<Block> IRREPLACABLE = Lists.newArrayList();

    static {
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkChick.class, 70, 1, 2));
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkChicken.class, 50, 1, 1));
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkSheep.class, 30, 1, 1));
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkCow.class, 15, 1, 1));
        MONSTERS.add(new Biome.SpawnListEntry(EntityBat.class, 1, 1, 1));
        IRREPLACABLE.add(ORE.getBlock());
        IRREPLACABLE.add(HFCore.FLOWERS);
        IRREPLACABLE.add(HFMining.PORTAL);
    }
    
    private final World worldObj;
    private Biome[] biomesForGeneration;
    private Season season;

    public MiningChunk(World world, long seed) {
        this.worldObj = world;
        this.rand = new Random(seed);
    }

    public void setBlockState(ChunkPrimer primer, int x, int y, int z, IBlockState state, int chunkX) {
        x = Math.min(15, Math.max(0, x));
        y = Math.min(MAX_Y, Math.max(0, y));
        z = Math.min(15, Math.max(0, z));
        if (state.getBlock() == PORTAL.getBlock()) {
            primer.setBlockState(x, y, z, state);
        } else if (state.getBlock() == ORE.getBlock()) {
            if (primer.getBlockState(x, y, z).getBlock() != Blocks.LADDER && primer.getBlockState(x, y, z).getBlock() != PORTAL.getBlock()) {
                primer.setBlockState(x, y, z, FLOORS);
                if (primer.getBlockState(x, y + 1, z).getBlock() != Blocks.LADDER) {
                    IBlockState theState = MiningRegistry.INSTANCE.getRandomStateForSeason(worldObj, MiningHelper.getFloor(chunkX, y), season);
                    if (theState != null) {
                        primer.setBlockState(x, y + 1, z, theState);
                    }
                }
            }
        } else {
            Block block = primer.getBlockState(x, y, z).getBlock();
            if ((!IRREPLACABLE.contains(block) && state == AIR) || state != AIR) {
                primer.setBlockState(x, y, z, state);
            }
        }
    }

    public IBlockState getBlockState(ChunkPrimer primer, int x, int y, int z) {
        x = Math.min(15, Math.max(0, x));
        y = Math.min(MAX_Y, Math.max(0, y));
        z = Math.min(15, Math.max(0, z));
        return primer.getBlockState(x, y, z);
    }

    private boolean isFloorWithPortal(int floor) {
        return floor % MiningHelper.MAX_FLOORS == 1 || floor % MiningHelper.MAX_FLOORS == 0;
    }

    private void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer) {
        //Set the chunk to wall blocks
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < MAX_Y; k++) {
                    setBlockState(primer, i, k, j, WALLS, chunkX);
                }
            }
        }

        if (chunkX >= 0 && chunkZ >= 0) {
            boolean up = true;
            int been = 0;
            for (int chunkY = 0; chunkY < MAX_LOOP; chunkY += MiningHelper.FLOOR_HEIGHT) {
                IBlockState[][] states = getMineGeneration(chunkX, chunkY, chunkZ);
                rand.setSeed(getIndex(chunkX, chunkY, chunkZ) * worldObj.getSeed());

                //Set the floor blocks
                int height = rand.nextInt(3);
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        if (states[i][j] == FLOORS || states[i][j] == ORE) {
                            setBlockState(primer, i, chunkY, j, states[i][j], chunkX);
                            if (been >= 10 + rand.nextInt(16) && rand.nextInt(8) == 0) {
                                if (up) height++;
                                else height--;
                                if (height > 2) {
                                    height = 2;
                                    up = false;
                                } else if (height < 0) {
                                    height = 0;
                                    up = true;
                                }

                                been = 0;
                            } else been++;

                            for (int k = 0; k < 3; k++) {
                                int width = 1 + rand.nextInt(1);
                                int length = 1 + rand.nextInt(1);
                                for (int x4 = -width; x4 <= width; x4++) {
                                    for (int z4 = -length; z4 < length; z4++) {
                                        for (EnumFacing enumFacing: EnumFacing.HORIZONTALS) {
                                            if (enumFacing == EnumFacing.DOWN || enumFacing == EnumFacing.UP) continue;
                                            BlockPos pos = new BlockPos(i + x4, chunkY, j + z4);
                                            BlockPos offset = pos.offset(enumFacing);
                                            if (x4 != z4 && getBlockState(primer, offset.getX(), offset.getY(), offset.getZ()) == states[i][j] && rand.nextBoolean()) {
                                                setBlockState(primer, pos.getX(), chunkY, pos.getZ(), states[i][j], chunkX);
                                                for (int y = 1; y <= MiningHelper.FLOOR_HEIGHT - 4; y++) {
                                                    setBlockState(primer, pos.getX(), chunkY + y, pos.getZ(), AIR, chunkX);
                                                }

                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            for (int y = 1; y <= MiningHelper.FLOOR_HEIGHT - 1 - height; y++) {
                                setBlockState(primer, i, chunkY + y, j, AIR, chunkX);
                            }
                        }
                    }
                }

                //Set the ladders, On the floor below
                int belowY = chunkY - MiningHelper.FLOOR_HEIGHT;
                IBlockState[][] below =  chunkY == 0 ? null : getMineGeneration(chunkX, belowY, chunkZ);
                if (below != null) {
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < 16; j++) {
                            if (below[i][j] == LADDER) {
                                Rotation rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
                                IBlockState theState = HFMining.LADDER.withRotation(LADDER, rotation);
                                if (getBlockState(primer, i, belowY, j).getBlock() != LADDER.getBlock() && getBlockState(primer, i, belowY, j) != LADDER_HOLE)
                                    setBlockState(primer, i, belowY, j, FLOORS, chunkX);
                                for (int y = 1; y <= MiningHelper.FLOOR_HEIGHT; y++) {
                                    setBlockState(primer, i, belowY + y, j, theState, chunkX);
                                    setBlockState(primer, i, belowY + y + 4, j, AIR, chunkX);
                                }

                                int floor = MiningHelper.getFloor(chunkX, belowY + MiningHelper.FLOOR_HEIGHT);
                                if (MiningHelper.HOLE_FLOORS.contains(floor) || (floor > MYSTRIL_FLOOR && rand.nextInt(4) == 0)) {
                                    setBlockState(primer, i, belowY + MiningHelper.FLOOR_HEIGHT, j, LADDER_HOLE, chunkX);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Fix the ceiling
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < MAX_Y; k++) {
                    setBlockState(primer, i, 251, j, WALLS, chunkX);
                }
            }
        }

        //Place the Spawn Portals
        for (int chunkY = 0; chunkY < MAX_LOOP; chunkY += MiningHelper.FLOOR_HEIGHT) {
            int mineID = MiningHelper.getMineID(chunkZ);
            int floor = MiningHelper.getFloor(chunkX, chunkY);
            if (floor != 0 && isFloorWithPortal(floor) && !MineManager.areCoordinatesGenerated(worldObj, mineID, floor)) {
                placePortals(primer, mineID, floor, chunkX, chunkY, chunkZ);
            }
        }
    }

    private void placePortals(ChunkPrimer primer, int mineID, int floor, int chunkX, int chunkY, int chunkZ) {
        if (isFloorWithPortal(floor) && !MineManager.areCoordinatesGenerated(worldObj, mineID, floor)) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if (isXLineWall(primer, x, chunkY + 1, z) && isXLineAir(primer, x, chunkY + 1, z)) {
                        setXSpawn(floor, primer, x, chunkY + 1, z, chunkX, chunkZ);
                        return;
                    } else if (isZLineWall(primer, x, chunkY + 1, z) && isZLineAir(primer, x, chunkY + 1, z)) {
                        setZSpawn(floor, primer, x, chunkY + 1, z, chunkX, chunkZ);
                        return;
                    }
                }
            }
        }
    }

    private void setXSpawn(int floor, ChunkPrimer primer, int x, int y, int z, int chunkX, int chunkZ) {
        int realX = (chunkX * 16) + x;
        int realZ = (chunkZ * 16) + z;
        setBlockState(primer, x, y, z, PORTAL, chunkX);
        setBlockState(primer, x - 1, y, z, PORTAL, chunkX);
        setBlockState(primer, x + 1, y, z, PORTAL, chunkX);
        setBlockState(primer, x, y + 1, z, PORTAL, chunkX);
        setBlockState(primer, x - 1, y + 1, z, PORTAL, chunkX);
        setBlockState(primer, x + 1, y + 1, z, PORTAL, chunkX);
        /** Torches
        for (int r = -2; r <= 2; r++) {
            for (int s = -2; s <= 2; s++) {
                for (int t = 0; t <= 2; t++) {
                    if (getBlockState(primer, x + r, y + t, z + s).getBlock() == Blocks.TORCH) {
                        setBlockState(primer, x + r, y + t, z + s, AIR, chunkX);
                    }
                }
            }
        } **/

        HFTrackers.getMineManager(worldObj).setSpawnForMine(MiningHelper.getMineID(chunkZ), floor, realX, y, realZ);
    }

    private void setZSpawn(int floor, ChunkPrimer primer, int x, int y, int z, int chunkX, int chunkZ) {
        int realX = (chunkX * 16) + x;
        int realZ = (chunkZ * 16) + z;
        setBlockState(primer, x, y, z, PORTAL, chunkX);
        setBlockState(primer, x, y, z - 1, PORTAL, chunkX);
        setBlockState(primer, x, y, z + 1, PORTAL, chunkX);
        setBlockState(primer, x, y + 1, z, PORTAL, chunkX);
        setBlockState(primer, x, y + 1, z - 1, PORTAL, chunkX);
        setBlockState(primer, x, y + 1, z + 1, PORTAL, chunkX);
        /** Torches
        for (int r = -2; r <= 2; r++) {
            for (int s = -2; s <= 2; s++) {
                for (int t = 0; t <= 2; t++) {
                    if (getBlockState(primer, x + r, y + t, z + s).getBlock() == Blocks.TORCH) {
                        setBlockState(primer, x + r, y + t, z + s, AIR, chunkX);
                    }
                }
            }
        } */

        HFTrackers.getMineManager(worldObj).setSpawnForMine(MiningHelper.getMineID(chunkZ), floor, realX, y, realZ);
    }

    private boolean isXLineWall(ChunkPrimer primer, int x, int y, int z) {
        return x + 1 < 16 && x - 1 >= 0 && getBlockState(primer, x, y, z) == WALLS && getBlockState(primer, x + 1, y, z) == WALLS && getBlockState(primer, x - 1, y, z) == WALLS;
    }

    private boolean isXLineAir(ChunkPrimer primer, int x, int y, int z) {
        return (z + 1 < 16 && getBlockState(primer, x, y, z + 1) == AIR && getBlockState(primer, x + 1, y, z + 1) == AIR && getBlockState(primer, x - 1, y, z + 1) == AIR)
                || (z - 1 >= 0 && getBlockState(primer, x, y, z - 1) == AIR && getBlockState(primer, x + 1, y, z - 1) == AIR && getBlockState(primer, x - 1, y, z - 1) == AIR);
    }

    private boolean isZLineWall(ChunkPrimer primer, int x, int y, int z) {
        return z + 1 < 16 && z - 1 >= 0 && getBlockState(primer, x, y, z) == WALLS && getBlockState(primer, x, y, z + 1) == WALLS && getBlockState(primer, x, y, z - 1) == WALLS;
    }

    private boolean isZLineAir(ChunkPrimer primer, int x, int y, int z) {
        return (x + 1 < 16 && getBlockState(primer, x + 1, y, z) == AIR && getBlockState(primer, x + 1, y, z + 1) == AIR && getBlockState(primer, x + 1, y, z - 1) == AIR)
                || (x - 1 >= 16 && getBlockState(primer, x - 1, y, z) == AIR && getBlockState(primer, x - 1, y, z + 1) == AIR && getBlockState(primer, x - 1, y, z - 1) == AIR);
    }

    private int clamp(int number) {
        return Math.max(0, Math.min((CHUNK_BOUNDARY * 16) - 1, number));
    }

    private int getChunkIndexFromCoordinates(int chunkXIndex, int chunkZIndex) {
        return chunkXIndex + (chunkZIndex * CHUNK_BOUNDARY);
    }

    private IBlockState[][] getBooleanFromMap(TIntObjectMap<IBlockState[][]> map, int index) {
        map.putIfAbsent(index, new IBlockState[16][16]);
        return map.get(index);
    }

    private int getIndex(int chunkX, int chunkY, int chunkZ) {
        int x = (int) Math.floor(chunkX / CHUNK_BOUNDARY); //3x3 Chunks
        int y = (int) Math.floor(chunkY / MiningHelper.FLOOR_HEIGHT); // Height
        int z = (int) Math.floor(chunkZ / CHUNK_BOUNDARY); //3x3 Chunks
        int result = x;
        result = 31 * result + z;
        result = 31 * result + y;
        return result;
    }

    @SuppressWarnings("complexity")
    @Nonnull
    private IBlockState[][] getMineGeneration(int chunkX, int chunkY, int chunkZ) {
        int mapIndex = getIndex(chunkX, chunkY, chunkZ);
        //Put if absent
        if (!MineManager.containsStateKey(mapIndex)) {
            IBlockState[][] blockStateMap = new IBlockState[CHUNK_BOUNDARY * 16][CHUNK_BOUNDARY * 16];
            boolean first = true;
            rand.setSeed(mapIndex * worldObj.getSeed());
            int startX = 15 + rand.nextInt(75);
            int endX = 15 + rand.nextInt(75);
            int startZ = 15 + rand.nextInt(75);
            int endZ = 15 + rand.nextInt(75);
            int ladderDistance = 5 + rand.nextInt(20);
            int differenceMin = 5 + rand.nextInt(15);
            int endDistance = (differenceMin * 3) - 1;
            int maxLoop = 1 + rand.nextInt(5);
            int endChangeChanceX = 10 + rand.nextInt(15);
            int endChangeChanceZ = 10 + rand.nextInt(15);
            int changeMinX = 10 + rand.nextInt(25);
            int changeMinZ = 10 + rand.nextInt(25);
            int randXChange = 5 + rand.nextInt(30);
            int randZChange = 5 + rand.nextInt(30);
            int randXTime = 7 + rand.nextInt(10);
            int randZTime = 7 + rand.nextInt(10);
            int radius = 1 + rand.nextInt(3);
            int oreChance = MiningHelper.getOreChance(season, MiningHelper.getFloor(chunkX, chunkY), rand);
            for (int k = 0; k < maxLoop; k++) {
                if (first) {
                    first = false;
                } else {
                    startX = endX;
                    startZ = endZ;
                }

                int differenceX = startX > endX ? startX - endX : endX - startX;
                int differenceZ = startZ > endZ ? startZ - endZ : endZ - startZ;
                while (endX == endZ || differenceX < differenceMin || differenceZ < differenceMin) {
                    endX = rand.nextInt(endDistance);
                    endZ = rand.nextInt(endDistance);
                    differenceX = startX > endX ? startX - endX : endX - startX;
                    differenceZ = startZ > endZ ? startZ - endZ : endZ - startZ;
                }

                if (chunkY != 0 && MineManager.containsCoordinatesKey(getIndex(chunkX, chunkY - MiningHelper.FLOOR_HEIGHT, chunkZ))) {
                    int below = getIndex(chunkX, chunkY - MiningHelper.FLOOR_HEIGHT, chunkZ);
                    startX = MineManager.getCoordinates(below, 0);
                    startZ = MineManager.getCoordinates(below, 1);
                }


                if (!(startX == -1 || endX == -1 || startZ == -1 || endZ == -1)) {
                    //Fill in the starting position with a true boolean
                    blockStateMap[startX][startZ] = FLOORS; //Mark as floor
                    //Mark a random circle radius around
                    for (int i = -radius; i <= radius; i++) {
                         for (int l = -radius; l <= radius; l++) {
                            if (i * i + l * l >= (radius + 0.50f) * (radius + 0.50f)) {
                                continue;
                            }

                             int x = startX + i;
                             int z = startZ + l;
                             int x2 = clamp(x);
                             int z2 = clamp(z);
                             IBlockState state = rand.nextInt(oreChance) == 0 ? ORE: FLOORS;
                             blockStateMap[x2][z2] = state; //Force it in
                        }
                    }

                    int x = startX;
                    int z = startZ;
                    //Let's try this 20 times
                    int sameXTime = 0;
                    int sameZTime = 0;
                    while (x != endX || z != endZ) {
                        int option = rand.nextInt(4);
                        if (option == 0) {
                            if (x < endX) x++;
                        } else if (option == 1) {
                            if (x > endX) x--;
                        } else if (option == 2) {
                            if (z < endZ) z++;
                        } else if (option == 3) {
                            if (z > endZ) z--;
                        }

                        if (blockStateMap[clamp(x)][clamp(z)] != LADDER) {
                            IBlockState state = rand.nextInt(oreChance) == 0 ? ORE: FLOORS;
                            blockStateMap[clamp(x)][clamp(z)] = state; //Force it in
                        }

                        option = rand.nextInt(16);
                        if (option == 0) {
                            if (x > endX) x++;
                        } else if (option == 1) {
                            if (x < endX) x--;
                        } else if (option == 2) {
                            if (z > endZ) z++;
                        } else if (option == 3) {
                            if (z < endZ) z--;
                        }

                        if (blockStateMap[clamp(x)][clamp(z)] != LADDER) {
                            IBlockState state = rand.nextInt(oreChance) == 0 ? ORE: FLOORS;
                            blockStateMap[clamp(x)][clamp(z)] = state; //Force it in
                        }

                        boolean regenEndX = false;
                        boolean regenEndZ = false;
                        if (x == endX) {
                            sameXTime++;
                            if (sameXTime >= randXTime) {
                                sameXTime = 0;
                                regenEndX = true;
                            }
                        }

                        if (z == endZ) {
                            sameZTime++;
                            if (sameZTime >= randZTime) {
                                sameZTime = 0;
                                regenEndZ = true;
                            }
                        }

                        if (regenEndX || rand.nextInt(endChangeChanceX) == 0) {
                            endX = changeMinX + rand.nextInt(randXChange);
                        }

                        if (regenEndZ || rand.nextInt(endChangeChanceZ) == 0) {
                            endZ = changeMinZ + rand.nextInt(randZChange);
                        }

                        int x2 = clamp(x);
                        int z2 = clamp(z);

                        for (EnumFacing enumfacing : EnumFacing.values()) {
                            if (enumfacing == EnumFacing.DOWN || enumfacing == EnumFacing.UP) continue;
                            BlockPos pos = new BlockPos(x, chunkY, z).offset(enumfacing);
                            int x3 = clamp(pos.getX());
                            int z3 = clamp(pos.getZ());

                            if (pos.getX() < 0 || pos.getX() >= blockStateMap.length) continue;
                            if (pos.getZ() < 0 || pos.getZ() >= blockStateMap[pos.getX()].length) continue;
                            if (blockStateMap[x3][z3] == FLOORS || blockStateMap[x3][z3] == ORE) {
                                int wStart = rand.nextInt(2);
                                int wEnd = rand.nextInt(2);
                                int lStart = rand.nextInt(2);
                                int lEnd = rand.nextInt(2);
                                for (int x4 = -wStart; x4 <= wEnd; x4++) {
                                    for (int z4 = -lStart; z4 <= lEnd; z4++) {
                                        if (x4 != z4 && blockStateMap[clamp(x2 + x4)][clamp(z2 + z4)] != LADDER) {
                                            IBlockState state = rand.nextInt(oreChance) == 0 ? ORE: FLOORS;
                                            blockStateMap[clamp(x2 + x4)][clamp(z2 + z4)] = state;
                                            differenceX = startX > x ? startX - x : x - startX;
                                            differenceZ = startZ > z ? startZ - z : z - startZ;
                                            if ((differenceX >= ladderDistance || differenceZ >= ladderDistance) && !MineManager.containsCoordinatesKey(mapIndex)) {
                                                int clampedX = clamp(x2 + x4);
                                                int clampedZ = clamp(z2 + z4);
                                                blockStateMap[clampedX][clampedZ] = LADDER;
                                                for (int x5 = -1; x5 <= 1; x5++) {
                                                    for (int z5 = -1; z5<= 1; z5++) {
                                                        int clampedX5 = clamp(clampedX + x5);
                                                        int clampedZ5 = clamp(clampedZ + z5);
                                                        if (blockStateMap[clampedX5][clampedZ5] != LADDER) {
                                                            blockStateMap[clampedX5][clampedZ5] = FLOORS;
                                                        }
                                                    }
                                                }

                                                MineManager.putCoordinates(mapIndex, new int[]{ clampedX, clampedZ });
                                            }
                                        }
                                    }
                                }

                                break;
                            }
                        }
                    }

                    if (!MineManager.containsCoordinatesKey(mapIndex) && k == maxLoop - 1) {
                        blockStateMap[startX][startZ] = LADDER;
                        MineManager.putCoordinates(mapIndex, new int[]{ startX, startZ });
                    }
                }
            }

            //Convert the full map to individual maps
            TIntObjectMap<IBlockState[][]> stateMap = new TIntObjectHashMap<>();
            for (int cX = 0; cX < blockStateMap.length; cX++) {
                for (int cZ = 0; cZ < blockStateMap[cX].length; cZ++) {
                    int chunkIndexX = (int) Math.floor(cX / 16);
                    int chunkIndexZ = (int) Math.floor(cZ / 16);
                    int chunkIndex = getChunkIndexFromCoordinates(chunkIndexX, chunkIndexZ);
                    IBlockState[][] air = getBooleanFromMap(stateMap, chunkIndex);
                    air[cX % 16][cZ % 16] = blockStateMap[cX][cZ];
                    stateMap.put(chunkIndex, air);
                }
            }

            MineManager.putStateMap(mapIndex, stateMap);
        }

        TIntObjectMap<IBlockState[][]> map = MineManager.getStateMap(mapIndex);
        int chunkXIndex = chunkX % CHUNK_BOUNDARY;
        int chunkZIndex = chunkZ % CHUNK_BOUNDARY;
        int checkIndex = getChunkIndexFromCoordinates(chunkXIndex, chunkZIndex);
        return map.get(checkIndex);
    }

    @Override
    @Nonnull
    public Chunk provideChunk(int x, int z) {
        rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        biomesForGeneration = this.worldObj.getBiomeProvider().getBiomes(biomesForGeneration, x * 16, z * 16, 16, 16);
        season = HFApi.calendar.getDate(worldObj).getSeason();
        if (season == null) season = Season.SPRING;
        setBlocksInChunk(x, z, chunkprimer);
        Chunk chunk = new Chunk(worldObj, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte) Biome.getIdForBiome(biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {}

    @Override
    public boolean generateStructures(@Nonnull Chunk chunkIn, int x, int z) {
        return true;
    }

    @Override
    @Nonnull
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType creatureType, @Nonnull BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) {
            return MONSTERS;
        } else return this.worldObj.getBiome(pos).getSpawnableList(creatureType);
    }

    @Nullable
    public BlockPos getStrongholdGen(@Nonnull World worldIn, @Nonnull String structureName, @Nonnull BlockPos position) {
        return null;
    }

    @Override
    public void recreateStructures(@Nonnull Chunk chunkIn, int x, int z) {}
}