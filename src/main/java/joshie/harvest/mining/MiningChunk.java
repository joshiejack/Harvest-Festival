package joshie.harvest.mining;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.HFApi;
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

import static joshie.harvest.mining.HFMining.ORE;
import static joshie.harvest.mining.MineManager.CHUNK_BOUNDARY;

public class MiningChunk implements IChunkGenerator {
    private MineManager manager;
    private Random rand;
    private static final IBlockState WALLS = HFMining.STONE.getDefaultState();
    private static final IBlockState FLOORS = HFMining.DIRT.getDefaultState();
    private static final IBlockState LADDER = HFMining.LADDER.getDefaultState();
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final IBlockState PORTAL = HFMining.PORTAL.getDefaultState();
    static final int FLOOR_HEIGHT = 6;
    
    private final World worldObj;
    private Biome[] biomesForGeneration;

    public MiningChunk(World world, long seed, MineManager manager) {
        this.worldObj = world;
        this.rand = new Random(seed);
        this.manager = manager;
    }

    public void setBlockState(ChunkPrimer primer, int x, int y, int z, IBlockState state, int chunkX, int chunkZ) {
        x = Math.min(15, Math.max(0, x));
        y = Math.min(255, Math.max(0, y));
        z = Math.min(15, Math.max(0, z));
        if (primer.getBlockState(x, y, z).getBlock() != ORE && primer.getBlockState(x, y, z).getBlock() != Blocks.GOLD_BLOCK) {
            primer.setBlockState(x, y, z, state);

            if (state.getBlock() == FLOORS.getBlock()) {
                int chance = 4 + rand.nextInt(46);
                if (rand.nextInt(chance) == 0) {
                    int realX = (chunkX * 16) + x;
                    int realZ = (chunkZ * 16) + z;

                    HFApi.tickable.addTickable(worldObj, new BlockPos(realX, y, realZ), HFApi.tickable.getTickableFromBlock(FLOORS.getBlock()));
                    IBlockState theState = MiningTicker.getBlockState(rand, MiningTicker.getFloor(chunkX, y));
                    primer.setBlockState(x, y + 1, z, theState);
                }
            }
        }
    }

    public IBlockState getBlockState(ChunkPrimer primer, int x, int y, int z) {
        x = Math.min(15, Math.max(0, x));
        y = Math.min(255, Math.max(0, y));
        z = Math.min(15, Math.max(0, z));
        return primer.getBlockState(x, y, z);
    }

    private void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer) {
        //Set the chunk to wall blocks
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 252; k++) {
                    setBlockState(primer, i, k, j, WALLS, chunkX, chunkZ);
                }
            }
        }

        if (chunkX >= 0 && chunkZ >= 0) {
            for (int chunkY = 0; chunkY < 250; chunkY += FLOOR_HEIGHT) {
                IBlockState[][] states = getMineGeneration(chunkX, chunkY, chunkZ);
                rand.setSeed(getIndex(chunkX, chunkY, chunkZ) * worldObj.getSeed());

                //Set the floor blocks
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        if (states[i][j] == FLOORS) {
                            setBlockState(primer, i, chunkY, j, FLOORS, chunkX, chunkZ);
                            for (int k = 0; k < 3; k++) {
                                int width = 1 + rand.nextInt(1);
                                int length = 1 + rand.nextInt(1);
                                for (int x4 = -width; x4 <= width; x4++) {
                                    for (int z4 = -length; z4 < length; z4++) {
                                        for (EnumFacing enumFacing: EnumFacing.values()) {
                                            if (enumFacing == EnumFacing.DOWN || enumFacing == EnumFacing.UP) continue;
                                            BlockPos pos = new BlockPos(i + x4, chunkY, j + z4);
                                            BlockPos offset = pos.offset(enumFacing);
                                            if (x4 != z4 && getBlockState(primer, offset.getX(), offset.getY(), offset.getZ()) == FLOORS && rand.nextBoolean()) {
                                                setBlockState(primer, pos.getX(), chunkY, pos.getZ(), FLOORS, chunkX, chunkZ);
                                                for (int y = 1; y <= FLOOR_HEIGHT - 2; y++) {
                                                    setBlockState(primer, pos.getX(), chunkY + y, pos.getZ(), AIR, chunkX, chunkZ);
                                                }

                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            for (int y = 1; y <= FLOOR_HEIGHT - 2; y++) {
                                setBlockState(primer, i, chunkY + y, j, AIR, chunkX, chunkZ);
                            }
                        }
                    }
                }

                //Set the ladders, On the floor below
                int belowY = chunkY - FLOOR_HEIGHT;
                IBlockState[][] below =  chunkY == 0 ? null : getMineGeneration(chunkX, belowY, chunkZ);
                if (below != null) {
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < 16; j++) {
                            if (below[i][j] == LADDER) {
                                Rotation rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
                                IBlockState theState = HFMining.LADDER.withRotation(LADDER, rotation);
                                if (getBlockState(primer, i, belowY, j).getBlock() != LADDER.getBlock())
                                    setBlockState(primer, i, belowY, j, FLOORS, chunkX, chunkZ);
                                for (int y = 1; y <= FLOOR_HEIGHT; y++) {
                                    setBlockState(primer, i, belowY + y, j, theState, chunkX, chunkZ);
                                    setBlockState(primer, i, belowY + y + 4 + 1, j, AIR, chunkX, chunkZ);
                                }
                            }
                        }
                    }
                }

                int mineID = MiningHelper.getMineID(chunkZ);
                if (MiningTicker.getFloor(chunkX, chunkY) == 1 && !manager.areCoordinatesGenerated(mineID)) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            if (isXLineWall(primer, x, chunkY + 1, z) && isXLineAir(primer, x, chunkY + 1, z)) {
                                setXSpawn(primer, x, chunkY + 1, z, chunkX, chunkZ);
                                return;
                            } else if (isZLineWall(primer, x, chunkY + 1, z) && isZLineAir(primer, x, chunkY + 1, z)) {
                                setZSpawn(primer, x, chunkY + 1, z, chunkX, chunkZ);
                                return;
                            }
                        }
                    }
                }
            }
        }

        //Fix the ceiling
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 252; k++) {
                    setBlockState(primer, i, 251, j, WALLS, chunkX, chunkZ);
                }
            }
        }
    }

    private void setXSpawn(ChunkPrimer primer, int x, int y, int z, int chunkX, int chunkZ) {
        int realX = (chunkX * 16) + x;
        int realZ = (chunkZ * 16) + z;
        setBlockState(primer, x, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x - 1, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x + 1, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x - 1, y + 1, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x + 1, y + 1, z, PORTAL, chunkX, chunkZ);
        manager.setSpawnForMine(MiningHelper.getMineID(chunkZ), realX, y, realZ);
    }

    private void setZSpawn(ChunkPrimer primer, int x, int y, int z, int chunkX, int chunkZ) {
        int realX = (chunkX * 16) + x;
        int realZ = (chunkZ * 16) + z;
        setBlockState(primer, x, y, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y, z - 1, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y, z + 1, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z - 1, PORTAL, chunkX, chunkZ);
        setBlockState(primer, x, y + 1, z + 1, PORTAL, chunkX, chunkZ);
        manager.setSpawnForMine(MiningHelper.getMineID(chunkZ), realX, y, realZ);
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
        int y = (int) Math.floor(chunkY / MiningChunk.FLOOR_HEIGHT); // Height
        int z = (int) Math.floor(chunkZ / CHUNK_BOUNDARY); //3x3 Chunks
        int result = x;
        result = 31 * result + z;
        result = 31 * result + y;
        return result;
    }

    private IBlockState[][] getMineGeneration(int chunkX, int chunkY, int chunkZ) {
        int mapIndex = getIndex(chunkX, chunkY, chunkZ);
        //Put if absent
        if (!manager.containsStateKey(mapIndex)) {
            IBlockState[][] blockStateMap = new IBlockState[CHUNK_BOUNDARY * 16][CHUNK_BOUNDARY * 16];
            boolean first = true;
            rand.setSeed(mapIndex * worldObj.getSeed());
            int startX = 50 + rand.nextInt(110);
            int endX = 50 + rand.nextInt(110);
            int startZ = 50 + rand.nextInt(110);
            int endZ = 50 + rand.nextInt(110);
            int ladderDistance = 10 + rand.nextInt(40);
            int differenceMin = 5 + rand.nextInt(27);
            int endDistance = (differenceMin * 4) - 1;
            int maxLoop = 1 + rand.nextInt(7);
            int endChangeChanceX = 30 + rand.nextInt(21);
            int endChangeChanceZ = 30 + rand.nextInt(21);
            int changeMinX = 25 + rand.nextInt(56);
            int changeMinZ = 25 + rand.nextInt(56);
            int randXChange = 10 + rand.nextInt(71);
            int randZChange = 10 + rand.nextInt(71);
            int randXTime = 5 + rand.nextInt(16);
            int randZTime = 5 + rand.nextInt(16);
            int radius = 2 + rand.nextInt(6);
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

                if (chunkY != 0 && manager.containsCoordinatesKey(getIndex(chunkX, chunkY - FLOOR_HEIGHT, chunkZ))) {
                    int below = getIndex(chunkX, chunkY - FLOOR_HEIGHT, chunkZ);
                    startX = manager.getCoordinates(below, 0);
                    startZ = manager.getCoordinates(below, 1);
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
                             blockStateMap[x2][z2] = FLOORS; //Mark as floor
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
                            if (blockStateMap[x3][z3] == FLOORS) {
                                int chance = 2;
                                if (rand.nextInt(150) == 0) {
                                    chance = 5;
                                } else if (rand.nextInt(100) == 0)  {
                                    chance = 4;
                                } if (rand.nextInt(3) >= 1) {
                                    chance = 3;
                                }

                                int wStart = rand.nextInt(chance);
                                int wEnd = rand.nextInt(chance);
                                int lStart = rand.nextInt(chance);
                                int lEnd = rand.nextInt(chance);
                                for (int x4 = -wStart; x4 <= wEnd; x4++) {
                                    for (int z4 = -lStart; z4 <= lEnd; z4++) {
                                        if (x4 != z4 && blockStateMap[clamp(x2 + x4)][clamp(z2 + z4)] != LADDER) {
                                            blockStateMap[clamp(x2 + x4)][clamp(z2 + z4)] = FLOORS;
                                            differenceX = startX > x ? startX - x : x - startX;
                                            differenceZ = startZ > z ? startZ - z : z - startZ;
                                            if ((differenceX >= ladderDistance || differenceZ >= ladderDistance) && !manager.containsCoordinatesKey(mapIndex)) {
                                                blockStateMap[clamp(x2 + x4)][clamp(z2 + z4)] = LADDER;
                                                manager.putCoordinates(mapIndex, new int[]{clamp(x2 + x4), clamp(z2 + z4)});
                                            }
                                        }
                                    }
                                }

                                break;
                            }
                        }
                    }

                    if (!manager.containsCoordinatesKey(mapIndex) && k == maxLoop - 1) {
                        blockStateMap[startX][startZ] = LADDER;
                        manager.putCoordinates(mapIndex, new int[]{ startX, startZ });
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

            manager.putStateMap(mapIndex, stateMap);
        }

        TIntObjectMap<IBlockState[][]> map = manager.getStateMap(mapIndex);
        int chunkXIndex = chunkX % CHUNK_BOUNDARY;
        int chunkZIndex = chunkZ % CHUNK_BOUNDARY;
        int checkIndex = getChunkIndexFromCoordinates(chunkXIndex, chunkZIndex);
        IBlockState[][] result = map.get(checkIndex);
        return result;
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
        return this.worldObj.getBiome(pos).getSpawnableList(creatureType);
    }

    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {}
}