package joshie.harvest.mining;

import com.google.common.collect.Lists;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.mining.entity.EntityDarkChick;
import joshie.harvest.mining.entity.EntityDarkChicken;
import joshie.harvest.mining.entity.EntityDarkCow;
import joshie.harvest.mining.entity.EntityDarkSheep;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static joshie.harvest.mining.MineManager.CHUNK_BOUNDARY;
import static joshie.harvest.mining.MiningTicker.MAX_LOOP;
import static joshie.harvest.mining.MiningTicker.MAX_Y;

public class MiningChunk implements IChunkGenerator {
    private final Random rand;
    private static final IBlockState WALLS = HFMining.STONE.getDefaultState();
    private static final IBlockState FLOORS = HFMining.DIRT.getDefaultState();
    private static final IBlockState LADDER = HFMining.LADDER.getDefaultState();
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final IBlockState PORTAL = HFMining.PORTAL.getDefaultState();
    private static final IBlockState ORE = HFMining.ORE.getDefaultState();
    protected static final List<Biome.SpawnListEntry> MONSTERS = Lists.newArrayList();
    protected static final List<Block> IRREPLACABLE = Lists.newArrayList();

    static {
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkChick.class, 100, 1, 5));
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkChicken.class, 50, 1, 3));
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkSheep.class, 30, 1, 2));
        MONSTERS.add(new Biome.SpawnListEntry(EntityDarkCow.class, 15, 1, 1));
        IRREPLACABLE.add(ORE.getBlock());
        IRREPLACABLE.add(HFCore.FLOWERS);
        IRREPLACABLE.add(HFMining.PORTAL);
    }
    
    private final World worldObj;
    private Biome[] biomesForGeneration;

    public MiningChunk(World world, long seed) {
        this.worldObj = world;
        this.rand = new Random(seed);
    }

    public void setBlockState(ChunkPrimer primer, int x, int y, int z, IBlockState state, int chunkX, int chunkZ) {
        x = Math.min(15, Math.max(0, x));
        y = Math.min(MAX_Y, Math.max(0, y));
        z = Math.min(15, Math.max(0, z));
        if (state.getBlock() == PORTAL.getBlock()) {
            primer.setBlockState(x, y, z, state);
        } else if (state.getBlock() == ORE.getBlock()) {
            if (primer.getBlockState(x, y, z).getBlock() != Blocks.LADDER && primer.getBlockState(x, y, z).getBlock() != PORTAL.getBlock()) {
                primer.setBlockState(x, y, z, FLOORS);
                if (primer.getBlockState(x, y + 1, z).getBlock() != Blocks.LADDER) {
                    primer.setBlockState(x, y + 1, z, MiningTicker.getBlockState(rand, MiningHelper.getFloor(chunkX, y)));
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
        return floor % MiningTicker.MAX_FLOORS == 1 || floor % MiningTicker.MAX_FLOORS == 0;
    }

    private void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer) {
        //Set the chunk to wall blocks
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < MAX_Y; k++) {
                    setBlockState(primer, i, k, j, WALLS, chunkX, chunkZ);
                }
            }
        }

        if (chunkX >= 0 && chunkZ >= 0) {
            for (int chunkY = 0; chunkY < MAX_LOOP; chunkY += MiningTicker.FLOOR_HEIGHT) {
                IBlockState[][] states = getMineGeneration(chunkX, chunkY, chunkZ);
                rand.setSeed(getIndex(chunkX, chunkY, chunkZ) * worldObj.getSeed());

                //Set the floor blocks
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        if (states[i][j] == FLOORS || states[i][j] == ORE) {
                            setBlockState(primer, i, chunkY, j, states[i][j], chunkX, chunkZ);
                            for (int k = 0; k < 3; k++) {
                                int width = 1 + rand.nextInt(1);
                                int length = 1 + rand.nextInt(1);
                                for (int x4 = -width; x4 <= width; x4++) {
                                    for (int z4 = -length; z4 < length; z4++) {
                                        for (EnumFacing enumFacing: EnumFacing.values()) {
                                            if (enumFacing == EnumFacing.DOWN || enumFacing == EnumFacing.UP) continue;
                                            BlockPos pos = new BlockPos(i + x4, chunkY, j + z4);
                                            BlockPos offset = pos.offset(enumFacing);
                                            if (x4 != z4 && getBlockState(primer, offset.getX(), offset.getY(), offset.getZ()) == states[i][j] && rand.nextBoolean()) {
                                                setBlockState(primer, pos.getX(), chunkY, pos.getZ(), states[i][j], chunkX, chunkZ);
                                                for (int y = 1; y <= MiningTicker.FLOOR_HEIGHT - 2; y++) {
                                                    setBlockState(primer, pos.getX(), chunkY + y, pos.getZ(), AIR, chunkX, chunkZ);
                                                }

                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            for (int y = 1; y <= MiningTicker.FLOOR_HEIGHT - 2; y++) {
                                setBlockState(primer, i, chunkY + y, j, AIR, chunkX, chunkZ);
                            }
                        }
                    }
                }

                //Set the ladders, On the floor below
                int belowY = chunkY - MiningTicker.FLOOR_HEIGHT;
                IBlockState[][] below =  chunkY == 0 ? null : getMineGeneration(chunkX, belowY, chunkZ);
                if (below != null) {
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < 16; j++) {
                            if (below[i][j] == LADDER) {
                                Rotation rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
                                IBlockState theState = HFMining.LADDER.withRotation(LADDER, rotation);
                                if (getBlockState(primer, i, belowY, j).getBlock() != LADDER.getBlock())
                                    setBlockState(primer, i, belowY, j, FLOORS, chunkX, chunkZ);
                                for (int y = 1; y <= MiningTicker.FLOOR_HEIGHT; y++) {
                                    setBlockState(primer, i, belowY + y, j, theState, chunkX, chunkZ);
                                    setBlockState(primer, i, belowY + y + 4, j, AIR, chunkX, chunkZ);
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
                    setBlockState(primer, i, 251, j, WALLS, chunkX, chunkZ);
                }
            }
        }

        //Place the Spawn Portals
        for (int chunkY = 0; chunkY < MAX_LOOP; chunkY += MiningTicker.FLOOR_HEIGHT) {
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
        setBlockState(primer, x, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x - 1, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x + 1, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x - 1, y + 1, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x + 1, y + 1, z, PORTAL, chunkX, chunkZ);
        HFTrackers.getMineManager(worldObj).setSpawnForMine(MiningHelper.getMineID(chunkZ), floor, realX, y, realZ);
    }

    private void setZSpawn(int floor, ChunkPrimer primer, int x, int y, int z, int chunkX, int chunkZ) {
        int realX = (chunkX * 16) + x;
        int realZ = (chunkZ * 16) + z;
        setBlockState(primer, x, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y, z - 1, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y, z + 1, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z - 1, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z + 1, PORTAL, chunkX, chunkZ);
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
        int y = (int) Math.floor(chunkY / MiningTicker.FLOOR_HEIGHT); // Height
        int z = (int) Math.floor(chunkZ / CHUNK_BOUNDARY); //3x3 Chunks
        int result = x;
        result = 31 * result + z;
        result = 31 * result + y;
        return result;
    }

    private IBlockState[][] getMineGeneration(int chunkX, int chunkY, int chunkZ) {
        int mapIndex = getIndex(chunkX, chunkY, chunkZ);
        //Put if absent
        if (!MineManager.containsStateKey(mapIndex)) {
            IBlockState[][] blockStateMap = new IBlockState[CHUNK_BOUNDARY * 16][CHUNK_BOUNDARY * 16];
            boolean first = true;
            rand.setSeed(mapIndex * worldObj.getSeed());
            int startX = 15 + rand.nextInt(85);
            int endX = 15 + rand.nextInt(85);
            int startZ = 15 + rand.nextInt(85);
            int endZ = 15 + rand.nextInt(85);
            int ladderDistance = 5 + rand.nextInt(25);
            int differenceMin = 5 + rand.nextInt(15);
            int endDistance = (differenceMin * 3) - 1;
            int maxLoop = 1 + rand.nextInt(5);
            int endChangeChanceX = 15 + rand.nextInt(15);
            int endChangeChanceZ = 15 + rand.nextInt(15);
            int changeMinX = 10 + rand.nextInt(25);
            int changeMinZ = 10 + rand.nextInt(25);
            int randXChange = 5 + rand.nextInt(30);
            int randZChange = 5 + rand.nextInt(30);
            int randXTime = 7 + rand.nextInt(10);
            int randZTime = 7 + rand.nextInt(10);
            int radius = 1 + rand.nextInt(3);
            int oreChance = rand.nextInt(5) == 0 ? 10 + rand.nextInt(15) : 25 + rand.nextInt(25);
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

                if (chunkY != 0 && MineManager.containsCoordinatesKey(getIndex(chunkX, chunkY - MiningTicker.FLOOR_HEIGHT, chunkZ))) {
                    int below = getIndex(chunkX, chunkY - MiningTicker.FLOOR_HEIGHT, chunkZ);
                    startX = MineManager.getCoordinates(below, 0);
                    startZ = MineManager.getCoordinates(below, 1);
                }


                if (startX == -1 || endX == -1 || startZ == -1 || endZ == -1) {
                } else {
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
    public Chunk provideChunk(int x, int z) {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.setBlocksInChunk(x, z, chunkprimer);
        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {}

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) {
            return MONSTERS;
        } else return this.worldObj.getBiome(pos).getSpawnableList(creatureType);
    }

    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {}
}