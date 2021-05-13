package uk.joshiejack.harvestcore.world.mine.tier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.block.BlockElevator;
import uk.joshiejack.harvestcore.block.HCBlocks;
import uk.joshiejack.harvestcore.database.MineLoader;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.harvestcore.world.mine.dimension.decorators.DecoratorLoot;
import uk.joshiejack.harvestcore.world.mine.dimension.floors.RoomGenerator;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.DecorateWrapperBlockStateMap;
import uk.joshiejack.penguinlib.util.BlockStates;
import uk.joshiejack.penguinlib.util.loot.LootRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator.MAX_XZ_PER_SECTION;

public abstract class Tier extends LootRegistry<DecoratorLoot> {
    private final List<Biome.SpawnListEntry> spawnableMonsterList = Lists.newArrayList();
    private final List<Biome.SpawnListEntry> spawnableCreatureList = Lists.newArrayList();
    private final List<Biome.SpawnListEntry> spawnableWaterCreatureList = Lists.newArrayList();
    private final List<Biome.SpawnListEntry> spawnableCaveCreatureList = Lists.newArrayList();
    private final Map<EnumCreatureType, List<Biome.SpawnListEntry>> modSpawnableLists = Maps.newHashMap();
    private final List<RoomGenerator> generators = Lists.newArrayList();
    private final List<RoomGenerator> simpleGenerators = Lists.newArrayList();
    public final Long2ObjectMap<Int2ObjectMap<IBlockState[][][]>> map = new Long2ObjectOpenHashMap<>();
    private final Random rand = new Random();
    protected ResourceLocation hud;
    protected IBlockState floor;
    protected IBlockState wall;
    protected IBlockState ladder;
    protected IBlockState upper_portal;
    protected IBlockState lower_portal;

    public Tier() {
        hud = new ResourceLocation(HarvestCore.MODID, "textures/gui/mine.png");
    }

    public void init() {}

    public abstract Biome getBiome();
    public ResourceLocation getHUD() {
        return hud;
    }

    protected DecoratorLoot addLootedDecorator(IBlockState state, int min, int max, double weight) {
        DecoratorLoot loot = new DecoratorLoot(state, min, max);
        add(loot, weight);
        return loot;
    }

    protected void addGenerator(RoomGenerator generator) {
        if (generator.isSimple()) {
            simpleGenerators.add(generator);
        }

        generators.add(generator);
    }

    public boolean canSpawn(EntityLiving entity, int floor) {
        return false;
    }

    @Nullable
    public DecoratorLoot getLoot(Random rand, int floor, int actualFloor) {
        return get(rand);
    }

    public void build(long generationID, Int2ObjectMap<Pair<BlockPos, EnumFacing>> elevators, Int2ObjectMap<Pair<BlockPos, EnumFacing>> portals) {
        if (!map.containsKey(generationID)) {
            rand.setSeed(generationID);
            map.put(generationID, build(rand, elevators, portals, generationID));
        }
    }

    public void setBlocksInChunk(ChunkPrimer primer, long generationID, int sectionX, int sectionZ, Int2ObjectMap<Pair<BlockPos, EnumFacing>> elevators, Int2ObjectMap<Pair<BlockPos, EnumFacing>> portals) {
        build(generationID, elevators, portals);

        Int2ObjectMap<IBlockState[][][]> states = map.get(generationID);
        //Set the blocks for these sections
        for (int floor = 0; floor < 40; floor++) {
            IBlockState[][][] blockStates = states.get(floor);
            for (BlockPos pos: BlockPos.getAllInBox(0, 0, 0, 15, 5, 15)) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                IBlockState state = blockStates[(sectionX * 16) + x][(sectionZ * 16) + z][y];
                if (state == BlockStates.BEDROCK) {
                    primer.setBlockState(x, (floor * 6) + y + 1, z, BlockStates.AIR);
                } else primer.setBlockState(x, (floor * 6) + y + 1, z, state);
                primer.setBlockState(x, 0, z, getWall(40));
            }
        }
    }

    public IBlockState getFloor(int floor) {
        return this.floor;
    }

    public IBlockState getWall(int floor) {
        return wall;
    }

    public IBlockState getPortal(int floor) {
        return floor == 1 ? upper_portal : lower_portal;
    }

    public IBlockState getLadder() {
        return ladder;
    }

    public RoomGenerator getValidGeneratorFromList(List<RoomGenerator> generators, Random rand, BlockPos target) {
        RoomGenerator generator = generators.get(rand.nextInt(generators.size()));
        int attempts = 0;
        while (!generator.canGenerate(target)) {
            generator = generators.get(rand.nextInt(generators.size()));
            attempts++;

            if (attempts >= 10) {
                return MineLoader.CIRCLE;
            }
        }

        return generator;
    }

    @SuppressWarnings("ConstantConditions")
    public Int2ObjectMap<IBlockState[][][]> build(Random rand, Int2ObjectMap<Pair<BlockPos, EnumFacing>> elevators, Int2ObjectMap<Pair<BlockPos, EnumFacing>> portals, long generationID) {
        int chunkZ = BlockPos.fromLong(generationID).getZ();
        Int2ObjectMap<IBlockState[][][]> states = new Int2ObjectOpenHashMap<>();
        int floorCounter = 0;
        BlockPos start = new BlockPos(MAX_XZ_PER_SECTION / 2, 0, MAX_XZ_PER_SECTION / 2);
        BlockPos prevLadder = null;
        IBlockState prevLadderState = getLadder();
        while(floorCounter < 40) {
            IBlockState[][][] blockStateMap = new IBlockState[MAX_XZ_PER_SECTION][MAX_XZ_PER_SECTION][6];
            int floor = 40 - floorCounter;
            int actualFloor = MineHelper.getFloorFromTierNumber(chunkZ, (floorCounter * 6));
            //Fill the chunk with the default blocks
            for (BlockPos pos: BlockPos.getAllInBox(0, 0, 0, blockStateMap.length - 1,  5,  blockStateMap.length - 1)) {
                blockStateMap[pos.getX()][pos.getZ()][pos.getY()] = getWall(floor);
            }

            AbstractDecoratorWrapper wrapper = new DecorateWrapperBlockStateMap(blockStateMap, this, rand, floor);
            BlockPos pos = prevLadder == null ? start : prevLadder;
            RoomGenerator generator = floor == 1 ? MineLoader.CIRCLE : getValidGeneratorFromList(generators, rand, pos);
            BlockPos result = generator.generate(wrapper, pos);
            //Rooms generated, now add the portals and elevators

            //Add the portal locations
            if (floor == 1 || floor == 40) {
                placePortal:
                for (int x = 2; x < MAX_XZ_PER_SECTION - 3; x++) {
                    for (int z = 2; z < MAX_XZ_PER_SECTION - 3; z++) {
                        BlockPos check = new BlockPos(x, 1, z);
                        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
                            if (areWallBlocks(wrapper, check, check.offset(facing), check.offset(facing, 2))) {
                                for (EnumFacing front: EnumFacing.HORIZONTALS) {
                                    if (areAirBlocks(wrapper, check.offset(front), check.offset(facing).offset(front), check.offset(facing, 2).offset(front)) &&
                                            areWallBlocks(wrapper, check.offset(front.getOpposite()), check.offset(facing).offset(front.getOpposite()), check.offset(facing, 2).offset(front.getOpposite()))) {
                                        IBlockState portal = getPortal(floor);
                                        for (int o = 0; o <= 2; o++) {
                                            for (int y = 0; y <= 1; y++) {
                                                wrapper.setBlockState(check.offset(facing, o).up(y), portal);
                                                //Now that we know this, set the blocks in front to bedrock
                                                for (int i = 1; i <= 2; i++) {
                                                    wrapper.setBlockState(check.offset(facing, o).up(y).offset(front, i), BlockStates.BEDROCK);
                                                }
                                            }
                                        }

                                        BlockPos lowerCentre = check.offset(facing).offset(front).up();
                                        for (EnumFacing direction: EnumFacing.HORIZONTALS) {
                                            if (wrapper.getBlockState(lowerCentre.offset(direction)) == portal) {
                                                portals.put(actualFloor, Pair.of(lowerCentre, direction.getOpposite()));
                                                break placePortal;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (floor %5 == 1) {
                placeElevator:
                for (BlockPos check: BlockPos.getAllInBox(1, 1, 1, MAX_XZ_PER_SECTION - 2, 1, MAX_XZ_PER_SECTION - 2)) {
                    if (wrapper.getBlockState(check) == getWall(floor) && wrapper.getBlockState(check.up()) == getWall(floor)) {
                        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
                            if (wrapper.isAirBlock(check.offset(facing)) && wrapper.isAirBlock(check.offset(facing).up())) {
                                IBlockState elevator = HCBlocks.ELEVATOR.getStateFromEnum(BlockElevator.Elevator.BASIC);
                                wrapper.setBlockState(check, HCBlocks.ELEVATOR.withFacing(elevator, facing));
                                wrapper.setBlockState(check.up(), HCBlocks.ELEVATOR.getDefaultState().withProperty(BlockElevator.TOP, true));
                                wrapper.setBlockState(check.offset(facing), BlockStates.BEDROCK);
                                wrapper.setBlockState(check.offset(facing).up(), BlockStates.BEDROCK);
                                elevators.put(actualFloor, Pair.of(new BlockPos(check.getX(), 2, check.getZ()), facing));
                                break placeElevator;
                            }
                        }
                    }
                }
            }

            //Decorate the floors
            for (int x = 0; x < blockStateMap.length; x += 16) {
                for (int z = 0; z < blockStateMap[x].length; z += 16) {
                    decorate(wrapper, new BlockPos(x, 1, z)); //Start at the level above the ground
                }
            }

            Rotation rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
            IBlockState ladder = getLadder().withRotation(rotation);
            //Set the ladder blocks after generating the room
            if (floor != 40 && prevLadder != null) {
                wrapper.setBlockState(prevLadder, prevLadderState);
                for (int i = 1; i <= 2; i++) {
                    if (wrapper.getBlockState(prevLadder.up(i)) != ladder) {
                        wrapper.setBlockState(prevLadder.up(i), BlockStates.AIR);
                    }
                }
            }

            if (floor != 1) {
                for (int y = 1; y < 6; y++) {
                    wrapper.setBlockState(result.up(y), ladder);
                }
            }

            prevLadder = result;
            prevLadderState = ladder;

            states.put(floorCounter, blockStateMap);

            floorCounter++;
        }

        return states;
    }

    private boolean areWallBlocks(AbstractDecoratorWrapper world, BlockPos... positions) {
        for (BlockPos pos: positions) {
            if (!world.isWallBlock(pos) || !world.isWallBlock(pos.up())) {
                return false;
            }
        }

        return true;
    }

    private boolean areAirBlocks(AbstractDecoratorWrapper world, BlockPos... positions) {
        for (BlockPos pos: positions) {
            if (!world.isAirBlock(pos) || !world.isAirBlock(pos.up())) {
                return false;
            }
        }

        return true;
    }

    protected abstract void decorate(AbstractDecoratorWrapper world, BlockPos pos);

    public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
        switch (creatureType) {
            case MONSTER:
                return spawnableMonsterList;
            case CREATURE:
                return spawnableCreatureList;
            case WATER_CREATURE:
                return spawnableWaterCreatureList;
            case AMBIENT:
                return spawnableCaveCreatureList;
            default:
                if (!modSpawnableLists.containsKey(creatureType)) modSpawnableLists.put(creatureType, Lists.newArrayList());
                return modSpawnableLists.get(creatureType);
        }
    }

    public List<RoomGenerator> getSimpleGenerators() {
        return simpleGenerators;
    }
}
