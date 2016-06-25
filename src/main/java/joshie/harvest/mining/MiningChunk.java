package joshie.harvest.mining;

import joshie.harvest.blocks.HFBlocks;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MiningChunk implements IChunkGenerator {
    private MineManager manager;
    private Random rand;
    private static final IBlockState WALLS = HFBlocks.STONE.getDefaultState();
    private static final IBlockState FLOORS = HFBlocks.DIRT.getDefaultState();
    protected static IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final int FLOOR_HEIGHT = 6;
    protected static final int CEILING = FLOOR_HEIGHT * 42;
    private final World worldObj;
    private Biome[] biomesForGeneration;

    public MiningChunk(World world, long seed, MineManager manager) {
        this.worldObj = world;
        this.rand = new Random(seed);
        this.manager = manager;
    }

    public void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer) {
        if (chunkX >= 0 && chunkZ >= 0) {
            for (int y = CEILING; y >= 0; y--) {
                int width = manager.getWidth(chunkX, chunkZ, y);
                int length = manager.getLength(chunkX, chunkZ, y);
                int startX  = manager.getStartX(chunkX, y, chunkZ, width);
                int startZ  = manager.getStartZ(chunkX, y, chunkZ, length);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        primer.setBlockState(x, y, z, WALLS);
                        if (x >= startX && x < width + startX && z >= startZ && z < length + startZ) {
                            if (y % FLOOR_HEIGHT == 0) {
                                primer.setBlockState(x, y, z, FLOORS);
                                for (int j = 1; j <= FLOOR_HEIGHT - 2; j++) {
                                    primer.setBlockState(x, y + j, z, Blocks.AIR.getDefaultState());
                                }
                            }
                        }

                        //Some extra spacing
                        if (y % FLOOR_HEIGHT == 0) {
                            for (int i = 0; i < 3; ++i) {
                                int x2 = Math.min(Math.max(0, x + (worldObj.rand.nextInt(8) - worldObj.rand.nextInt(8))), 15);
                                int z2 = Math.min(Math.max(0, z + (worldObj.rand.nextInt(8) - worldObj.rand.nextInt(8))), 15);
                                if (primer.getBlockState(x2, y, z2) == WALLS) {
                                    for (EnumFacing enumfacing : EnumFacing.values()) {
                                        BlockPos pos = new BlockPos(x2, y, z2).offset(enumfacing);
                                        int x3 = Math.min(Math.max(0, pos.getX()), 15);
                                        int z3 = Math.min(Math.max(0, pos.getZ()), 15);

                                        if (primer.getBlockState(x3, y, z3) == FLOORS) {
                                            primer.setBlockState(x2, y, z2, FLOORS);
                                            for (int k = 1; k <= FLOOR_HEIGHT - 2; k++) {
                                                primer.setBlockState(x2, y + k, z2, AIR);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int y = CEILING; y < 255; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        primer.setBlockState(x, y, z, WALLS);
                    }
                }
            }
        } else {
            for (int y = 0; y < 255; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        primer.setBlockState(x, y, z, WALLS);
                    }
                }
            }
        }
    }

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

    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;
        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.worldObj, this.rand, x, z, false);
        BlockPos blockpos = new BlockPos(x * 16, 0, z * 16);
        this.worldObj.getBiome(blockpos.add(16, 0, 16)).decorate(this.worldObj, this.worldObj.rand, blockpos);
        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.worldObj, this.rand, x, z, false);
        BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.worldObj.getBiome(pos).getSpawnableList(creatureType);
    }

    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    public void recreateStructures(Chunk chunkIn, int x, int z) {}
}